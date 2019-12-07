package view.common;

import javax.swing.*;
import java.awt.*;

public class Card extends JPanel {

    private int round;

    public Card(int round, Color cardColor, Color shadowColor) {
        this(0, 0, round, cardColor, shadowColor);
    }

    public Card(int width, int height, int round, Color cardColor, Color shadowColor) {
        this.round = round;
        setupCard(width, height, cardColor, shadowColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D)g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        Color fgColor = getForeground();
        if (fgColor != null) {
            // Renderiza a sombra
            graphics.setColor(fgColor);
            graphics.drawRoundRect(0, 1, w - 2, h - 3, round, round);
        }

        // Renderiza o cartão
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, w - 1, h - 2, round, round);
    }

    public int getPreferredWidth() {
        return getPreferredSize().width;
    }

    public void setPreferredWidth(int width) {
        Dimension size = getPreferredSize();
        size.width = width;
        setPreferredSize(size);
    }

    public int getPreferredHeight() {
        return getPreferredSize().height;
    }

    public void setPreferredHeight(int height) {
        Dimension size = getPreferredSize();
        size.height = height;
        setPreferredSize(size);
    }

    public int getMinimumWidth() {
        return getMinimumSize().width;
    }

    public void setMinimumWidth(int width) {
        Dimension size = getMinimumSize();
        size.width = width;
        setMinimumSize(size);
    }

    public int getMinimumHeight() {
        return getMinimumSize().height;
    }

    public void setMinimumHeight(int height) {
        Dimension size = getMinimumSize();
        size.height = height;
        setMinimumSize(size);
    }

    public void recalculate() {
        setPreferredSize(null);
        revalidate();
    }

    private void setupCard(int width, int height, Color cardColor, Color shadowColor) {
        if (width != 0 && height != 0) {
            setPreferredSize(new Dimension(width, height));
        }

        setLayout(new GridBagLayout());
        setForeground(shadowColor);
        setBackground(cardColor);
        setOpaque(false);
    }
}
