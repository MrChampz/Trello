package util;

import model.bean.Color;

import java.util.Random;

public class ColorUtils {

    public static Color fromAwtColor(java.awt.Color c) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        int a = c.getAlpha();
        return new Color(r, g, b, a);
    }

    public static java.awt.Color toAwtColor(Color c) {
        int r = c.getR();
        int g = c.getG();
        int b = c.getB();
        int a = c.getA();
        return new java.awt.Color(r, g, b, a);
    }

    public static Color getRandomColor() {
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        return new Color(r, g, b);
    }
}
