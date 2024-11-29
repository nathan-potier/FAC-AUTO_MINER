package Vue;
import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bouton2 extends JButton {

    private ImageIcon imageIcon;
    private String imagePath;

    // Constructeur avec ActionListener personnalisé
    public Bouton2(String imagePath, int x, int y, int width, int height, ActionListener actionListener) {
        this.imagePath = imagePath;
        
        // Charge l'image depuis le chemin spécifié
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image img = originalIcon.getImage(); // Obtenir l'image de l'icône
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); // Redimensionner l'image
        imageIcon = new ImageIcon(resizedImg); // Créer une nouvelle ImageIcon avec l'image redimensionnée

        // Appliquer l'image comme l'icône du bouton
        setIcon(imageIcon);
        setBounds(x, y, width, height); // Définir la position et la taille du bouton

        // Spécifier l'apparence du bouton
        setContentAreaFilled(false); // Empêche le bouton de remplir son espace avec une couleur
        setBorderPainted(false); // Empêche l'affichage du bord du bouton
        setFocusable(false); // Empêche le bouton de recevoir le focus

        // Ajouter l'action au bouton
        addActionListener(actionListener);
    }

    // Méthode pour changer l'image à un moment donné
    public void setImage(String newImagePath, int width, int height) {
        ImageIcon newIcon = new ImageIcon(newImagePath);
        Image img = newIcon.getImage(); // Obtenir l'image de l'icône
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); // Redimensionner l'image
        setIcon(new ImageIcon(resizedImg)); // Appliquer la nouvelle image redimensionnée
    }

    // Méthode pour obtenir l'image actuelle du bouton
    public ImageIcon getImageIcon() {
        return imageIcon;
    }
}
