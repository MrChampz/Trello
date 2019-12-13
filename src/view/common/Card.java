package view.common;

import javax.swing.*;
import java.awt.*;

public class Card extends JPanel {

    private int round;
    private Color shadowColor;

    public Card(int round, Color cardColor) {
        this(0, 0, round, cardColor, null);
    }

    public Card(int round, Color cardColor, Color shadowColor) {
        this(0, 0, round, cardColor, shadowColor);
    }

    public Card(int width, int height, int round, Color cardColor) {
        this(width, height, round, cardColor, null);
    }

    public Card(int width, int height, int round, Color cardColor, Color shadowColor) {
        this.round = round;
        this.shadowColor = shadowColor;
        setupCard(width, height, cardColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D)g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (shadowColor != null) {
            // Renderiza a sombra
            graphics.setColor(shadowColor);
            graphics.drawRoundRect(0, 1, w - 2, h - 3, round, round);
        }

        // Renderiza o cartão
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, w - 1, h - 2, round, round);
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
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

    private void setupCard(int width, int height, Color cardColor) {
        if (width != 0 && height != 0) {
            setPreferredSize(new Dimension(width, height));
        }

        setLayout(new GridBagLayout());
        setBackground(cardColor);
        setOpaque(false);
    }
}
