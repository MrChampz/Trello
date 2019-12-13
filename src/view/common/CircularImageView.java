package view.common;

import model.bean.Foto;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CircularImageView implements MouseListener {

    private JLabel lbImage;

    private int diameter;
    private BufferedImage image;
    private Icon badge;
    private Foto source;

    private ClickListener listener;

    public CircularImageView(String filepath, int diameter) throws IOException {
        this.diameter = diameter;
        setupImageFromFile(filepath);
        setupImageView();
    }

    public CircularImageView(byte[] source, int diameter) throws IOException {
        this.diameter = diameter;
        setupImageFromMemory(source);
        setupImageView();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (listener != null) {
            listener.onClick();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public Foto getSource() {
        return source;
    }

    public void setImage(String filepath) throws IOException {
        setupImageFromFile(filepath);
        lbImage.setIcon(new ImageIcon(image));
    }

    public void setImage(File file) throws IOException {
        setupImageFromFile(file);
        lbImage.setIcon(new ImageIcon(image));
    }

    public void setImage(byte[] source) throws IOException {
        setupImageFromMemory(source);
        lbImage.setIcon(new ImageIcon(image));
    }

    public void setImage(BufferedImage image) {
        setupImage(image);
        lbImage.setIcon(new ImageIcon(image));
    }

    public void setBadge(Icon img) {
        badge = img;
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public void setCursor(Cursor cursor) {
        lbImage.setCursor(cursor);
    }

    public void add(Component component) {
        lbImage.add(component);
    }

    public Component get() {
        return lbImage;
    }

    private void setupImageFromFile(String filepath) throws IOException {
        File file = new File(filepath);
        setupImageFromFile(file);
    }

    private void setupImageFromFile(File file) throws IOException {
        // Abre e redimensiona a imagem
        BufferedImage original = ImageIO.read(file);
        Image scaled = original.getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH);

        // Salva a fonte
        String filepath = file.getPath();
        String format = filepath.substring(filepath.lastIndexOf('.') + 1);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(original, format, stream);

        stream.flush();
        source = new Foto(stream.toByteArray());
        stream.close();

        // Configura o ImageView
        setupImage(scaled);
    }

    private void setupImageFromMemory(byte[] source) throws IOException {
        // Salva a fonte
        this.source = new Foto(source);

        // Inicializa e redimensiona a imagem com os dados
        Image original = ImageIO.read(new ByteArrayInputStream(source));
        original = original.getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH);

        // Configura o ImageView
        setupImage(original);
    }

    private void setupImage(Image original) {
        BufferedImage mask = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mask.createGraphics();
        applyQualityRenderingHints(g2d);
        g2d.fillOval(0, 0, diameter - 1, diameter - 1);
        g2d.dispose();

        image = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        applyQualityRenderingHints(g2d);

        g2d.drawImage(original, 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);

        g2d.dispose();
    }

    private void setupImageView() {
        lbImage = new JLabel(new ImageIcon(image)) {
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

        lbImage.addMouseListener(this);
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
