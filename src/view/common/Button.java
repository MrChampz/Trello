package view.common;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Button implements MouseListener {

    private static final int ROUND = 4;

    private enum State { NORMAL, HOVERED, CLICKED }

    private JPanel pnRoot;
    private JLabel lbText;
    private JLabel lbIcon;

    private State state;
    private Color color;
    private Color hoverColor;
    private Color clickColor;
    private int round;

    private ClickListener listener;

    public Button(String text) {
        this(text, null);
    }

    public Button(String text, ClickListener listener) {
        this.listener = listener;
        this.round = ROUND;
        setupButton();
        setupText(text);
    }

    public Button(Icon icon) {
        this(icon, null);
    }

    public Button(Icon icon,  ClickListener listener) {
        this.listener = listener;
        this.round = ROUND;
        setupButton();
        setupIcon(icon);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        state = State.CLICKED;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        state = State.NORMAL;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        state = State.HOVERED;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        state = State.NORMAL;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (listener != null) {
            listener.onClick();
        }
    }

    public void setIcon(Icon icon) {
        if (lbIcon != null) {
            lbIcon.setIcon(icon);
        }
    }

    public void setText(String text) {
        if (lbText != null) {
            lbText.setText(text);
        }
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public int getWidth() {
        return pnRoot.getWidth();
    }

    public void setWidth(int width) {
        Dimension size = pnRoot.getPreferredSize();
        size.width = width;
        setPreferredSize(size);
    }

    public int getHeight() {
        return pnRoot.getHeight();
    }

    public void setHeight(int height) {
        Dimension size = pnRoot.getPreferredSize();
        size.height = height;
        pnRoot.setPreferredSize(size);
    }

    public void setPreferredSize(Dimension dim) {
        pnRoot.setPreferredSize(dim);
    }

    public void setBorder(Border border) {
        pnRoot.setBorder(border);
    }

    public void setFont(Font font) {
        lbText.setFont(font);
    }

    public void setTextColor(Color color) {
        lbText.setForeground(color);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setHoverColor(Color color) {
        this.hoverColor = color;
    }

    public void setClickColor(Color color) {
        this.clickColor = color;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Component get() {
        return pnRoot;
    }

    private void setupButton() {
        pnRoot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D graphics = (Graphics2D)g;
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                switch (state) {
                    case NORMAL: graphics.setColor(color); break;
                    case HOVERED: graphics.setColor(hoverColor); break;
                    case CLICKED: graphics.setColor(clickColor); break;
                }

                // Renderiza o cartão
                graphics.fillRoundRect(0, 0, w - 1, h - 1, round, round);
            }
        };

        pnRoot.setLayout(new GridBagLayout());
        pnRoot.setOpaque(false);
        pnRoot.addMouseListener(this);
        pnRoot.setCursor(new Cursor(Cursor.HAND_CURSOR));

        state = State.NORMAL;
        color = new Color(255, 255, 255, 102);
        hoverColor = new Color(255, 255, 255, 76);
        clickColor = new Color(255, 255, 255, 51);
    }

    private void setupText(String text) {
        lbText = new JLabel(text, JLabel.CENTER);
        lbText.setBorder(new EmptyBorder(2, 2, 4, 4));
        lbText.setFont(new Font("Helvetica", Font.BOLD, 14));
        lbText.setForeground(Color.WHITE);

        pnRoot.add(lbText);
    }

    private void setupIcon(Icon icon) {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;

        lbIcon = new JLabel(icon, SwingConstants.CENTER);
        pnRoot.add(lbIcon, constr);
    }

    private void repaint() {
        // Por conta de um bug quando usando o canal alfa,
        // o repaint do component com alfa re-desenha sobre as camadas anteriores.
        // Por isso, pedimos para que seja re-desenhado toda a parte em alfa.

        Container parent = SwingUtilities.getWindowAncestor(pnRoot);
        parent.repaint();
    }
}
