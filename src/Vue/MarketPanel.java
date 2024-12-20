package Vue;
import java.awt.*;
import javax.swing.*;

import Modele.Player;

import Modele.Machine.Machine;
import Modele.Resource.*;

import Modele.GameStates;
import Modele.Market;

import java.util.List;

public class MarketPanel extends JPanel implements UpdateInterface{

    private Market market;
    private Player player;  
    private JLabel moneyLabel;  
    private JPanel sellPanel; 

   

    public MarketPanel(Market market, Player player) {
        this.market = market;
        this.player = player;
    
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);
        this.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
    
        JPanel moneyPanel = new JPanel(new BorderLayout());
        moneyPanel.setBackground(Color.DARK_GRAY);
        moneyPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        moneyLabel = new JLabel("Argent: " + player.getMoney() + " coins");
        moneyLabel.setForeground(Color.WHITE);
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moneyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        moneyPanel.add(moneyLabel, BorderLayout.CENTER);
        this.add(moneyPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(Color.DARK_GRAY);
    
        JPanel buyPanel = new JPanel();
        buyPanel.setLayout(new BoxLayout(buyPanel, BoxLayout.Y_AXIS));
        buyPanel.setBackground(Color.DARK_GRAY);
        buyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Buy from Market", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));
    
        sellPanel = new JPanel();
        sellPanel.setLayout(new BoxLayout(sellPanel, BoxLayout.Y_AXIS));
        sellPanel.setBackground(Color.DARK_GRAY);
        sellPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Sell from Inventory", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        updateInventoryPanel(sellPanel);

        for (Resource resource : market.getResourcesForSale()){
            addItemToMarket(buyPanel, "assets/"+ resource.getName()+".png", resource.getName(), resource, sellPanel);
        }
        for (Machine machine : market.getMachinesForSale()){
            addMachineToMarket(buyPanel, "assets/"+ machine.getName()+".png", machine, sellPanel);
        }
    
        JScrollPane buyScrollPane = new JScrollPane(buyPanel);
        buyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        buyScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        buyScrollPane.setBorder(BorderFactory.createEmptyBorder());
    
        JScrollPane sellScrollPane = new JScrollPane(sellPanel);
        sellScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sellScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sellScrollPane.setBorder(BorderFactory.createEmptyBorder());

        centerPanel.add(buyScrollPane);
        centerPanel.add(sellScrollPane);

        this.add(centerPanel, BorderLayout.CENTER);

        JPanel closeButtonPanel = new JPanel(new BorderLayout());
        closeButtonPanel.setBackground(Color.DARK_GRAY);
        closeButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Bouton Fermer
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(Color.LIGHT_GRAY);
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> {
            GameStates.setMoveOn(true);
            this.setVisible(false);
        });

        closeButtonPanel.add(closeButton, BorderLayout.CENTER);
        this.add(closeButtonPanel, BorderLayout.SOUTH);
    }

    public void setPlayer(Player player){
        this.player = player;
        updateMoneyLabel();
        updateInventoryPanel(sellPanel);}
    public void setMarket(Market market){this.market = market;}

    private void addItemToMarket(JPanel panel, String imagePath, String name, Resource resource, JPanel inventoryPanel) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        itemPanel.setBackground(Color.DARK_GRAY);
    
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
    
        JLabel imageLabel = new JLabel(scaledIcon);
        itemPanel.add(imageLabel);
    
        JLabel nameLabel = new JLabel(name);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        itemPanel.add(nameLabel);

        int price = resource.getPrix();
        JButton buyButton = new JButton(price + " coins");
        buyButton.addActionListener(e -> {
            market.buyResource(player, resource);
            updateMoneyLabel();
            updateInventoryPanel(inventoryPanel);
        });
        itemPanel.add(buyButton);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(itemPanel);
    }

    private void addMachineToMarket(JPanel panel, String imagePath, Machine machine, JPanel inventoryPanel) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        itemPanel.setBackground(Color.DARK_GRAY);
    
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledIcon);
        itemPanel.add(imageLabel);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.DARK_GRAY);
    
        JLabel nameLabel = new JLabel(machine.getName());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        textPanel.add(nameLabel);
    
        JLabel optionLabel = new JLabel(machine.getOption());
        optionLabel.setForeground(Color.LIGHT_GRAY);
        optionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        textPanel.add(optionLabel);
        itemPanel.add(textPanel);

        int price = machine.getPrix();
        JButton buyButton = new JButton(price + " coins");
        buyButton.addActionListener(e -> {
            market.buyMachine(player, machine);
            updateMoneyLabel();
            updateInventoryPanel(inventoryPanel);
        });
        itemPanel.add(buyButton);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(itemPanel);
    }
    
    private void updateInventoryPanel(JPanel panel) {
        panel.removeAll();
        List<Resource> inventory = player.getInv().getResources();
        
        for (Resource resource : inventory) {
            if (resource.getQuantity() > 0){
                JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
                itemPanel.setBackground(Color.DARK_GRAY);

                ImageIcon scaledIcon = new ImageIcon(new ImageIcon("assets/" + resource.getName() + ".png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(scaledIcon);
                itemPanel.add(imageLabel);
        
                JLabel nameLabel = new JLabel(resource.getName() + " x" + resource.getQuantity());
                nameLabel.setForeground(Color.WHITE);
                nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                itemPanel.add(nameLabel);

                int sellPrice = resource.getPrix();
                JButton sellButton = new JButton(sellPrice + " coins");
                sellButton.addActionListener(e -> {
                    if (resource.getQuantity() > 0) {
                        player.removeResource(resource);
                        player.setMoney(player.getMoney() + sellPrice);
                        updateMoneyLabel();
                        updateInventoryPanel(panel);
                    }
                });
                itemPanel.add(sellButton);
        
                itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                panel.add(itemPanel);
            }
        }
        panel.revalidate();
        panel.repaint();
    }
    
    private void updateMoneyLabel() {
        moneyLabel.setText("Money: " + player.getMoney() + " coins");
    }

    @Override
    public void notificationUpdate(){
        updateInventoryPanel(sellPanel);
    }
}
