package view.main;

import javax.swing.*;
import java.awt.*;

public class Indicator {

    private JLabel lbIndicator;

    public Indicator() {
        // Instancia a label que servirá como indicador
        lbIndicator = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension arcs = new Dimension(9, 9);

                Graphics2D graphics = (Graphics2D)g;
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Renderiza o indicador
                graphics.setColor(lbIndicator.getBackground());
                graphics.fillRoundRect(0, 0, w - 1, h - 1, arcs.width, arcs.height);
            }
        };

        // Define o tamanho padrão
        lbIndicator.setPreferredSize(new Dimension(40, 9));

        // E opaque = false, para podermos renderizar
        lbIndicator.setOpaque(false);
    }

    public void setColor(Color color) {
        lbIndicator.setBackground(color);
    }

    public int getWidth() {
        return lbIndicator.getPreferredSize().width;
    }

    public int getHeight() {
        return lbIndicator.getPreferredSize().height;
    }

    public Component get() {
        return lbIndicator;
    }
}
