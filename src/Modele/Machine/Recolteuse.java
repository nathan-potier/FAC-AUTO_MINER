package Modele.Machine;

import Modele.Resource.Resource;
import Vue.UpdateInterface;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import Modele.*;

public class Recolteuse extends Machine {
    private String typeRessource;
    private boolean isCollecting = false;
    private ArrayList<String> ressourceCollectable = new ArrayList<>();
    private Timer timer;  
    private Carte map;

    public Recolteuse(String option) {
        super("Recolteuse", new Position(), option, new Inventory());
        this.timer = new Timer();
    }

    public Recolteuse(Position position, String option,  Inventory stock) {
        super("Recolteuse", position, option, stock);
        this.timer = new Timer();
    }

    public void setMap(Carte map){
        this.map = map;
        ArrayList<Position> positions = getPosition().posAutour4();
        for(Position pos : positions){
            Cell<?> content = map.getGrid()[pos.getY()][pos.getX()];
            if (content != null && content.getContent() instanceof Resource ){
                String nomResource = ((Resource)map.getGrid()[pos.getY()][pos.getX()].getContent()).getName();
                if(!ressourceCollectable.contains(nomResource)){
                    ressourceCollectable.add(nomResource);
                }

            }else if (content != null && content.getContent() instanceof Attente ){
                String nomResource = ((Attente)map.getGrid()[pos.getY()][pos.getX()].getContent()).getResource().getName();
                if(!ressourceCollectable.contains(nomResource)){
                    ressourceCollectable.add(nomResource);
                }
            }

        }
        
    }

    public ArrayList<String> getRessourceCollectable() {
        return ressourceCollectable;
    }

    public boolean isCollecting() {
        return isCollecting;
    }
    public void setTypeRessource(String typeRessource) {
        this.typeRessource = typeRessource;

        for (Resource resource : getStock().getResources()){
            if (resource.getName().equals(typeRessource) &&
                getLimiteStock(getOption()) <= resource.getQuantity()){return;}
        }
            startCollecting();
    }

    public void startCollecting() {
        if (isCollecting) {
            return; // Si la collecte est déjà en cours, on ne fait rien
        }

        // Réinitialiser le Timer
        if (timer != null) {
            timer.cancel();  // Annuler l'ancien Timer s'il existe
            timer = new Timer(); // Créer une nouvelle instance du Timer
        }

        isCollecting = true;
        int tempCollectMs = getTempFabriq(getOption()) * 1000;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isCollecting) {
                    collectResourcesAround();
                    Random random = new Random();
                    int randomNumber = random.nextInt(5); 
                    if (!getFonctionneBien() && randomNumber == 0){
                        stopWorking();

                    }
                } else {
                    stopWorking(); // Arrête le Timer si la collecte est terminée
                }
            }
        }, tempCollectMs, tempCollectMs);
    }
    
    @Override
    public void stopWorking() {
        isCollecting = false;
        timer.cancel();
        UpdateInterface update = getInterfaceUpdate();
        if (update != null){
            update.notificationUpdate();
        }
    }
    

    private void collectResourcesAround() {
        // Simuler la collecte des ressources dans la zone autour de la position
        ArrayList<Cell<?>> cells = map.regarderAutour(getPosition());
        ArrayList<Resource> resources = new ArrayList<>();

        for (Cell<?> cell : cells) {
            if (cell != null && cell.getContent() instanceof Resource) {
                Resource resource = (Resource) cell.getContent();

                if (!resources.contains(resource)) {
                    
                    resources.add(resource);
                }
            }
        }
 
        for (Resource resource : resources) {
            

            if (resource.getName().equals(typeRessource)) {
                ajouterAustock(resource);
                map.collectRessource(null, resource.getPosition()); // Enlève la ressource de la carte

                if (getStock().getResources().stream().anyMatch(r -> r.getName().equals(resource.getName()) && r.getQuantity() >= getLimiteStock(getOption()))) {
                    stopWorking(); // Arrêter si la limite de stock est atteinte
                }
            }
        }
    }

    public void recuperer() {
        if (getStock().getResources().isEmpty()) {
            System.out.println("Il n'y a rien à récolter dans cette Recolteuse");
        } else {
            for (int i = 0; i < getStock().getResources().size(); i++) {
                Resource ressource = getStock().getResources().get(i);
                for (int j = 0; j < getStock().getResources().get(i).getQuantity(); j++){
                    
                    if(Game.getPlayer().collectQuantityResource(ressource)){// Ajoute la ressource à l'inventaire
                        startCollecting();
                        getStock().removeResource(ressource, 1);
                        if(ressource.getQuantity() == 0){break;}
                    } 
                }
            }
        }
    }
    public boolean typeRessourceIsSet(){
        return typeRessource != null;
    }
    @Override
    public String getResourcesSTR() {
        String res = "";
        for (Resource r : getStock().getResources()) {
            res += "(" + r.getName() + ";" + r.getQuantity() + "),";
        }
        return res;
    }

    @Override
    public String getNomRecetteEnCours() {
        if (typeRessource != null){
            return typeRessource;
        }
        return "";
    }

    @Override
    public Recolteuse getClone() {
        return new Recolteuse(getOption());
    }

    @Override
    public void produire() {
        startCollecting();
    }
}
