package view.common;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static java.awt.Component.CENTER_ALIGNMENT;
import static java.awt.Font.PLAIN;

public class MultilineLabel {

    private JTextArea taLabel;

    public MultilineLabel(String text) {
        setupLabel(text);
    }

    public int getWidth() {
        return taLabel.getWidth();
    }

    public void setWidth(int width) {
        Dimension size = taLabel.getPreferredSize();
        size.width = width;
        taLabel.setPreferredSize(size);
    }

    public int getHeight() {
        return taLabel.getHeight();
    }

    public void setHeight(int height) {
        Dimension size = taLabel.getPreferredSize();
        size.height = height;
        taLabel.setPreferredSize(size);
    }

    public String getText() {
        return taLabel.getText();
    }

    public void setText(String text) {
        taLabel.setText(text);
    }

    public void setTextColor(Color color) {
        taLabel.setForeground(color);
    }

    public void setTextSize(float size) {
        Font font = taLabel.getFont().deriveFont(size);
        taLabel.setFont(font);
    }

    public void setTextStyle(int style) {
        Font font = taLabel.getFont().deriveFont(style);
        taLabel.setFont(font);
    }

    public void setFont(Font font, Color color) {
        taLabel.setFont(font);
        taLabel.setForeground(color);
    }

    public void setBorder(Border border) {
        taLabel.setBorder(border);
    }

    public Component get() {
        return taLabel;
    }

    private void setupLabel(String text) {
        taLabel = new JTextArea(text);
        taLabel.setEditable(false);
        taLabel.setCursor(null);
        taLabel.setOpaque(false);
        taLabel.setFocusable(false);
        taLabel.setWrapStyleWord(true);
        taLabel.setLineWrap(true);
        taLabel.setAlignmentY(CENTER_ALIGNMENT);
        taLabel.setFont(new Font("Helvetica", PLAIN, 14));
    }
}
