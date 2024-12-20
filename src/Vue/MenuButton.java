package Vue;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuButton {
    private int xPos, yPos;
    private BufferedImage[] imgs;
    private int width;
    private int height;
    private boolean mouseOver;
    private Rectangle bounds;

    public MenuButton(int width, int height, int xPos, int yPos, String defaultImagePath, String hoverImagePath) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        loadImgs(defaultImagePath, hoverImagePath);
        initBounds();
    }

    private void loadImgs(String defaultImagePath, String hoverImagePath) {
        imgs = new BufferedImage[2]; // Deux états : normal et survolé

        try {
            BufferedImage original1 = ImageIO.read(getClass().getResource(defaultImagePath));
            BufferedImage original2 = ImageIO.read(getClass().getResource(hoverImagePath));
            imgs[0] = resizeImage(original1, width, height);
            imgs[1] = resizeImage(original2, width, height);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des images.");
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

    public void setMouseOver(boolean mouseOver) {this.mouseOver = mouseOver;}

    private void initBounds(){
        bounds = new Rectangle(xPos - width / 2, yPos - height / 2, width, height);
    }

    public Rectangle getBounds(){return bounds;}
}
