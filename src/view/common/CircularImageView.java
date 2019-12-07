package view.common;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class CircularImageView {

    private JLabel lbImage;

    private Icon badge;

    public CircularImageView(String filepath, int diameter) throws IOException {
        setupImageFromFile(filepath, diameter);
    }

    public CircularImageView(byte[] source, int diameter) throws IOException {
        setupImageFromMemory(source, diameter);
    }

    public void setBadge(Icon img) {
        badge = img;
    }

    public void setBounds(int x, int y, int width, int height) {
        lbImage.setBounds(x, y, width, height);
    }

    public void add(Component component) {
        lbImage.add(component);
    }

    public Component get() {
        return lbImage;
    }

    private void setupImageFromFile(String filepath, int diameter) throws IOException {
        Image original = ImageIO.read(new File(filepath));
        original = original.getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH);
        setupImage(original, diameter);
    }

    private void setupImageFromMemory(byte[] source, int diameter) throws IOException {
        Image original = ImageIO.read(new ByteArrayInputStream(source));
        original = original.getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH);
        setupImage(original, diameter);
    }

    private void setupImage(Image original, int diameter) {
        BufferedImage mask = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mask.createGraphics();
        applyQualityRenderingHints(g2d);
        g2d.fillOval(0, 0, diameter - 1, diameter - 1);
        g2d.dispose();

        BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        g2d = masked.createGraphics();
        applyQualityRenderingHints(g2d);

        g2d.drawImage(original, 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);

        g2d.dispose();

        lbImage = new JLabel(new ImageIcon(masked)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (badge != null) {
                    Graphics2D graphics = (Graphics2D)g.create();

                    int x = lbImage.getWidth() - badge.getIconWidth();
                    int y = lbImage.getHeight() - badge.getIconHeight();

                    badge.paintIcon(lbImage, graphics, x, y);
                    graphics.dispose();
                }
            }
        };
    }

    private static void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
}
