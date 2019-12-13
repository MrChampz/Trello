package view.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class TextArea extends JPanel {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 50;
    private static final int ROUND = 4;

    private JTextArea textArea;
    private JScrollPane scroll;

    private int round;
    private String hint;
    private Color hintColor;

    public TextArea(String hint) {
        this(hint, "");
    }

    public TextArea(String hint, String text) {
        this.round = ROUND;
        this.hint = hint;
        setupPanel();
        setupTextArea(text);
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
        setViewportBackground(bg);
    }

    public void setTextFont(Font font) {
        textArea.setFont(font);
    }

    public Color getTextColor() {
        return textArea.getForeground();
    }

    public void setTextColor(Color color) {
        textArea.setForeground(color);
    }

    public Color getHintColor() {
        return hintColor;
    }

    public void setHintColor(Color hintColor) {
        this.hintColor = hintColor;
    }

    public Color getSelectionColor() {
        return textArea.getSelectionColor();
    }

    public void setSelectionColor(Color selectionColor) {
        textArea.setSelectionColor(selectionColor);
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
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    private void setupPanel() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.white);
        setOpaque(false);
    }

    private void setupTextArea(String text) {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(12, 18, 6, 6);
        constr.fill = GridBagConstraints.BOTH;
        constr.weightx = 1.0; constr.weighty = 1.0;

        textArea = new JTextArea(text) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                if (getText().isEmpty()) {
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);

                    Insets insets = getInsets();
                    FontMetrics fm = g.getFontMetrics();

                    g.setColor(hintColor);
                    g.drawString(hint, insets.left, insets.top + fm.getAscent());
                }
            }
        };

        textArea.setOpaque(false);
        textArea.setBorder(new EmptyBorder(0, 0, 0, 6));
        textArea.setAlignmentY(CENTER_ALIGNMENT);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

        setTextFont(new Font("Helvetica", Font.PLAIN, 20));
        setTextColor(Color.black);
        setHintColor(new Color(117, 117, 117));
        setSelectionColor(new Color(0, 0, 0, 51));

        setupScrollPane();

        add(scroll, constr);
    }

    private void setupScrollPane() {
        scroll = new JScrollPane(textArea);
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBar(new RoundedScrollBar());
        scroll.setBorder(null);

        // Altera o background da viewport
        setViewportBackground(getBackground());
    }

    private void setViewportBackground(Color bg) {
        if (scroll != null) {
            final JViewport viewport = scroll.getViewport();
            viewport.setBackground(bg);
            scroll.setViewport(viewport);
        }
    }
}
