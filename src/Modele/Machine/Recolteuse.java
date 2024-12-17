package Modele.Machine;

import Modele.Resource.Resource;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Modele.*;

public class Recolteuse extends Machine {
    private String typeRessource;
    private boolean isCollecting;
    private ArrayList<Resource> ressourceCollectable = new ArrayList<>();

    private Carte map;

    public Recolteuse(String option, String typeRessource, Player player) {
        super("Recolteuse", new Position(), option, new Inventory(), player);
        this.typeRessource = typeRessource;
        this.isCollecting = false;
    }

    public Recolteuse(Position position, String option,  Inventory stock, Player player) {
        super("Recolteuse", position, option, stock, player);
        this.isCollecting = false;
    }

    public void setMap(Carte map){
        this.map = map;
        ArrayList<Position> positions = map.posAutour4(getPosition());
        for(Position pos : positions){
            Cell<?> content = map.getGrid()[pos.getY()][pos.getX()];
            if (content != null && content.getContent() instanceof Resource){
                ressourceCollectable.add((Resource)map.getGrid()[pos.getY()][pos.getX()].getContent());
            }

        }
        System.out.println("bonjour");
    }
    public String getTypeRessource() {
        return typeRessource;
    }
    public ArrayList<Resource> getRessourceCollectable() {
        return ressourceCollectable;
    }

    public boolean isCollecting() {
        return isCollecting;
    }
    public void setTypeRessource(String typeRessource) {
        this.typeRessource = typeRessource;
    }

    public void startCollecting() {
        if (isCollecting) {
            return; // Déjà en cours de collecte
        }

        isCollecting = true;
        int tempCollectMs = getTempFabriq(getOption()) * 1000;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isCollecting) {
                    collectResourcesAround();
                } else {
                    cancel(); // Arrête le Timer si la collecte est terminée
                }
            }
        }, tempCollectMs, tempCollectMs);
    }

    public void stopCollecting() {
        isCollecting = false;
    }

    private void collectResourcesAround() {
        // Simuler la collecte des ressources dans la zone autour de la position
        ArrayList<Cell<?>> cells = map.regarderAutour(getPosition());
        ArrayList<Resource> resources = new ArrayList<>();
        for (Cell<?> cell : cells) {
            if (cell != null && cell.getContent() instanceof Resource) {
                Resource resource = (Resource) cell.getContent();
                
                // Ajouter la ressource à ressourceCollectable si elle n'y est pas déjà
                if (!ressourceCollectable.contains(resource)) {
                    ressourceCollectable.add(resource);
                }
            }
        }
        for (Resource resource : resources) {
            if (resource.getName().equals(typeRessource)) {
                ajouterAustock(resource);
                map.collectRessource(null, resource.getPosition()); // Enlève la ressource de la carte
                System.out.println("Récolte réussie : " + resource.getName());
                if (getStock().getResources().stream().anyMatch(r -> r.getName().equals(resource.getName()) && r.getQuantity() >= getLimiteStock(getOption()))) {
                    stopCollecting(); // Arrêter si la limite de stock est atteinte
                }
            }
        }
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
        return new Recolteuse(getOption(), typeRessource, getPlayer());
    }

    @Override
    public void produire() {
        startCollecting();
    }
}
