import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuButton {
    private int xPos, yPos;
    private BufferedImage[] imgs;
    private int width = 160;
    private int height = 85;
    private int centreX = width / 2;
    private boolean mouseOver;
    private Rectangle bounds;

    // Constantes pour les chemins des images
    private static final String DEFAULT_IMAGE_PATH = "assets/play_button.png";
    private static final String HOVER_IMAGE_PATH = "assets/Hover.png";

    public MenuButton(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        loadImgs();
        initBounds();
    }

    private void loadImgs() {
        imgs = new BufferedImage[2]; // Deux états : normal et survolé

        try {
            // Charger les images depuis les ressources
            BufferedImage original1 = ImageIO.read(getClass().getResource(DEFAULT_IMAGE_PATH));
            BufferedImage original2 = ImageIO.read(getClass().getResource(HOVER_IMAGE_PATH));

            // Redimensionner les images à la taille prédéfinie
            imgs[0] = resizeImage(original1, width, height);
            imgs[1] = resizeImage(original2, width, height);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement des images.");

            // Ajouter des images par défaut si le chargement échoue
            imgs[0] = createPlaceholderImage(Color.RED);
            imgs[1] = createPlaceholderImage(Color.ORANGE);
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    private BufferedImage createPlaceholderImage(Color color) {
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return placeholder;
    }

    public void draw(Graphics g) {
        int index = mouseOver ? 1 : 0;
        g.drawImage(imgs[index], xPos - width / 2, yPos - height / 2, width, height, null);
    }
    

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    private void initBounds(){
        bounds = new Rectangle(xPos - width / 2, yPos - height / 2, width, height);
    }
    

    public Rectangle getBounds() {
        return bounds;
    }
}