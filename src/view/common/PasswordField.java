package view.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;

public class PasswordField extends JPanel {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 50;
    private static final int ROUND = 4;

    private JPasswordField passField;

    private int round;
    private String hint;
    private Color hintColor;

    public PasswordField(String hint) {
        this(hint, "");
    }

    public PasswordField(String hint, String text) {
        this.round = ROUND;
        this.hint = hint;
        setupPanel();
        setupPassField(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D)g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        graphics.setColor(getBackground());

        // Renderiza o cartão
        graphics.fillRoundRect(0, 0, w - 1, h - 1, round, round);
    }

    public void setTextFont(Font font) {
        passField.setFont(font);
    }

    public Color getTextColor() {
        return passField.getForeground();
    }

    public void setTextColor(Color color) {
        passField.setForeground(color);
    }

    public Color getHintColor() {
        return hintColor;
    }

    public void setHintColor(Color hintColor) {
        this.hintColor = hintColor;
    }

    public Color getSelectionColor() {
        return passField.getSelectionColor();
    }

    public void setSelectionColor(Color selectionColor) {
        passField.setSelectionColor(selectionColor);
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getText() {
        return passField.getText();
    }

    public void setText(String text) {
        passField.setText(text);
    }

    private void setupPanel() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.white);
        setOpaque(false);
    }

    private void setupPassField(String text) {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(6, 18, 6, 6);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;

        passField = new JPasswordField(text, JLabel.CENTER) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                if (getPassword().length == 0) {
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);

                    int h = getHeight();
                    Insets insets = getInsets();
                    FontMetrics fm = g.getFontMetrics();

                    g.setColor(hintColor);
                    g.drawString(hint, insets.left, (h / 2) + (fm.getAscent() / 2) - 2);
                }
            }
        };

        passField.setBorder(new EmptyBorder(0, 0, 0, 0));
        setTextFont(new Font("Helvetica", Font.PLAIN, 20));
        setTextColor(Color.black);
        setHintColor(new Color(117, 117, 117));
        setSelectionColor(new Color(0, 0, 0, 51));

        add(passField, constr);
    }
}
