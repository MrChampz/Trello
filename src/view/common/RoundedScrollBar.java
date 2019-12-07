package view.common;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class RoundedScrollBar extends JScrollBar {

    private static int ROUND = 8;

    private class ScrollBarUI extends BasicScrollBarUI {

        @Override
        protected JButton createIncreaseButton(int orientation) {
            JButton bt = new JButton();
            bt.setPreferredSize(new Dimension(0, 0));
            return bt;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            JButton bt = new JButton();
            bt.setPreferredSize(new Dimension(0, 0));
            return bt;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;

            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(9,30, 66, 33));
            g2d.fillRoundRect(
                thumbBounds.x, thumbBounds.y,
                thumbBounds.width, thumbBounds.height,
                ROUND, ROUND
            );
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(9,30, 66, 20));
            g2d.fillRoundRect(
                trackBounds.x, trackBounds.y,
                trackBounds.width, trackBounds.height,
                ROUND, ROUND
            );
        }
    }

    public RoundedScrollBar() {
        setUnitIncrement(20);
        setPreferredSize(new Dimension(8, 96));
        setMinimumSize(new Dimension(8, 8));
        setMaximumSize(new Dimension(8, 32767));
        setUI(new ScrollBarUI());
    }
}
