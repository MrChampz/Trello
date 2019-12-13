package view.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.*;

import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;

public class ComboBox extends JPanel {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 50;
    private static final int ROUND = 4;

    private JComboBox comboBox;

    private int round;
    private String hint;
    private Color hintColor;

    public ComboBox(String hint) {
        this(hint, new DefaultComboBoxModel());
    }

    public ComboBox(String hint, ComboBoxModel model) {
        this.round = ROUND;
        this.hint = hint;
        setupPanel();
        setupComboBox(model);
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

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        setComboBoxBackground(bg);
    }

    public void setTextFont(Font font) {
        comboBox.setFont(font);
    }

    public Color getTextColor() {
        return comboBox.getForeground();
    }

    public void setTextColor(Color color) {
        comboBox.setForeground(color);
    }

    public Color getHintColor() {
        return hintColor;
    }

    public void setHintColor(Color hintColor) {
        this.hintColor = hintColor;
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

    public Object getSelectedItem() {
        return comboBox.getSelectedItem();
    }

    public void setSelectedItem(Object item) {
        comboBox.setSelectedItem(item);
    }

    private void setupPanel() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.white);
        setOpaque(false);
    }

    private void setupComboBox(ComboBoxModel model) {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(6, 18, 6, 6);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;

        comboBox = new JComboBox(model) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                if (getSelectedItem() == null || getSelectedIndex() == -1) {
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

        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                Icon icon = new ImageIcon("res/ic_dropdown.png");
                JButton arrowButton = new JButton(icon);
                arrowButton.setBackground(new Color(255, 255, 255, 0));
                arrowButton.setBorder(new EmptyBorder(0, 0, 0, 0));
                return arrowButton;
            }
        });

        comboBox.setOpaque(false);
        comboBox.setBorder(new EmptyBorder(0, 0, 0, 0));
        comboBox.setSelectedItem(null);
        setComboBoxBackground(getBackground());

        setTextFont(new Font("Helvetica", Font.PLAIN, 20));
        setTextColor(Color.black);
        setHintColor(new Color(117, 117, 117));

        add(comboBox, constr);
    }

    private void setComboBoxBackground(Color bg) {
        if (comboBox != null) {
            comboBox.setBackground(bg);
        }
    }
}
