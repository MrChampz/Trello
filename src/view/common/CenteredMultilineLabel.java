package view.common;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

import static java.awt.Component.CENTER_ALIGNMENT;
import static java.awt.Font.PLAIN;

public class CenteredMultilineLabel {

    private JTextPane tpLabel;

    public CenteredMultilineLabel(String text) {
        setupLabel(text);
    }

    public void setPreferredSize(Dimension size) {
        tpLabel.setPreferredSize(size);
    }

    public int getWidth() {
        return tpLabel.getWidth();
    }

    public void setWidth(int width) {
        Dimension size = tpLabel.getPreferredSize();
        size.width = width;
        tpLabel.setPreferredSize(size);
    }

    public int getHeight() {
        return tpLabel.getHeight();
    }

    public void setHeight(int height) {
        Dimension size = tpLabel.getPreferredSize();
        size.height = height;
        tpLabel.setPreferredSize(size);
    }

    public void setText(String text) {
        tpLabel.setText(text);
    }

    public void setTextColor(Color color) {
        tpLabel.setForeground(color);
    }

    public void setTextSize(float size) {
        Font font = tpLabel.getFont().deriveFont(size);
        tpLabel.setFont(font);
    }

    public void setTextStyle(int style) {
        Font font = tpLabel.getFont().deriveFont(style);
        tpLabel.setFont(font);
    }

    public void setFont(Font font, Color color) {
        tpLabel.setFont(font);
        tpLabel.setForeground(color);
    }

    public void setBorder(Border border) {
        tpLabel.setBorder(border);
    }

    public Component get() {
        return tpLabel;
    }

    private void setupLabel(String text) {
        tpLabel = new JTextPane();
        tpLabel.setText(text);
        tpLabel.setEditable(false);
        tpLabel.setCursor(null);
        tpLabel.setOpaque(false);
        tpLabel.setFocusable(false);
        tpLabel.setAlignmentY(CENTER_ALIGNMENT);
        tpLabel.setFont(new Font("Helvetica", PLAIN, 14));

        var doc = tpLabel.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        tpLabel.setStyledDocument(doc);
    }
}
