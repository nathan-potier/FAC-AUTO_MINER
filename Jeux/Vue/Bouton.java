package Vue;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Bouton {

    private double x, y;
    private double width, height;
    private ImageIcon image;
    private Color defaultColor = Color.YELLOW;
    private Color hoverColor = Color.RED;
    private boolean isHover = false;

    public Bouton(double x, double y, double width, double height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = loadImage(imagePath);
        this.width = image.getIconWidth();
        this.height = image.getIconHeight();
    }

    private ImageIcon loadImage(String imagePath) {
        // Charger l'image depuis le chemin spécifié
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            return new ImageIcon(imagePath);  // Retourne un ImageIcon chargé avec le chemin
        } else {
            System.out.println("L'image n'a pas pu être trouvée : " + imagePath);
            return new ImageIcon(); // Si l'image n'est pas trouvée, retourner une image vide
        }
    }

    public double getX() { return x; }

    public double getY() { return y; }

    public double getWidth() { return width; }

    public double getHeight() { return height; }

    public void setImage(ImageIcon newImage) {
        this.image = newImage;
        this.width = image.getIconWidth();
        this.height = image.getIconHeight();
    }

    public ImageIcon getImage() { return image; }

    public Color getDefaultColor() { return defaultColor; }

    public Color getHoverColor() { return hoverColor; }

    public void setHover(boolean hover) { this.isHover = hover; }

    public void afficher(Graphics g) {
        if (isHover) {
            g.setColor(hoverColor);
        } else {
            g.setColor(defaultColor);
        }

        g.drawRect((int) x, (int) y, (int) width, (int) height);
        image.paintIcon(null, g, (int) x, (int) y);
    }

    public boolean estClique(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public boolean isHover(double mouseX, double mouseY) {
        return estClique(mouseX, mouseY);
    }
}
