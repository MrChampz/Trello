package view;

import javax.swing.*;
import java.awt.*;

public class Card {

    private JPanel pnCard;

    public Card(int width, int height, int round, Color cardColor, Color shadowColor) {
        setupCard(width, height, round, cardColor, shadowColor);
    }

    public Card(int round, Color cardColor, Color shadowColor) {
        setupCard(0, 0, round, cardColor, shadowColor);
    }

    public Component get() {
        return pnCard;
    }

    public void add(Component component) {
        pnCard.add(component);
    }

    public void add(Component component, Object constr) {
        pnCard.add(component, constr);
    }

    public void add(Component component, Object constr, int index) {
        pnCard.add(component, constr, index);
    }

    public void remove(int index) {
        pnCard.remove(index);
    }

    public void remove(Component component) {
        pnCard.remove(component);
    }

    public int getWidth() {
        return pnCard.getPreferredSize().width;
    }

    public void setWidth(int width) {
        Dimension size = pnCard.getPreferredSize();
        size.width = width;
        pnCard.setPreferredSize(size);
    }

    public int getHeight() {
        return pnCard.getPreferredSize().height;
    }

    public void setHeight(int height) {
        Dimension size = pnCard.getPreferredSize();
        size.height = height;
        pnCard.setPreferredSize(size);
    }

    public void setMinimumWidth(int width) {
        Dimension size = pnCard.getMinimumSize();
        size.width = width;
        pnCard.setMinimumSize(size);
    }

    public void setMinimumHeight(int height) {
        Dimension size = pnCard.getMinimumSize();
        size.height = height;
        pnCard.setMinimumSize(size);
    }

    public Container getParent() {
        return pnCard.getParent();
    }

    public void recalculate() {
        pnCard.setPreferredSize(null);
        pnCard.revalidate();
    }

    private void setupCard(int width, int height, int round, Color cardColor, Color shadowColor) {
        // Inicializa o panel como uma classe anônima
        pnCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D graphics = (Graphics2D)g;
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                Color fgColor = pnCard.getForeground();
                if (fgColor != null) {
                    // Renderiza a sombra
                    graphics.setColor(fgColor);
                    graphics.drawRoundRect(0, 1, w - 2, h - 3, round, round);
                }

                // Renderiza o cartão
                graphics.setColor(pnCard.getBackground());
                graphics.fillRoundRect(0, 0, w - 1, h - 2, round, round);
            }
        };

        if (width != 0 && height != 0) {
            pnCard.setPreferredSize(new Dimension(width, height));
        }

        pnCard.setLayout(new GridBagLayout());
        pnCard.setForeground(shadowColor);
        pnCard.setBackground(cardColor);
        pnCard.setOpaque(false);
    }
}
