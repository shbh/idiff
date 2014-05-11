package com.image.diff.visual;

import java.awt.Color;
import java.awt.Font;
import org.apache.commons.lang3.Validate;

public class HighlightElement {

    private int x;
    private int y;
    private int width;
    private int height;
    private Color borderColor;
    private Color areaColor;
    private Color fontColor;
    private Font font;
    private String text;

    private HighlightElement() {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Color getAreaColor() {
        return areaColor;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public Font getFont() {
        return font;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", borderColor=" + borderColor + ", areaColor=" + areaColor + ", fontColor=" + fontColor + ", font=" + font + ", text=" + text;
    }

    public static class Builder {

        private int x;
        private int y;
        private int width;
        private int height;
        private Color borderColor = Color.YELLOW;
        private Color areaColor = new Color(255, 255, 0, 255 * 50 / 100);
        private Color fontColor = Color.RED;
        private Font font = new Font("Serif", Font.PLAIN, 14);
        private String text = "";

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder borderColor(Color borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Builder areaColor(Color areaColor) {
            this.areaColor = areaColor;
            return this;
        }

        public Builder font(Font font) {
            this.font = font;
            return this;
        }

        public Builder fontColor(Color fontColor) {
            this.fontColor = fontColor;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public HighlightElement build() {
            Validate.isTrue(x >= 0, "X should be any positive number.");
            Validate.isTrue(y >= 0, "Y should be any positive number.");
            Validate.isTrue(width > 0, "Width should be any positive number.");
            Validate.isTrue(height > 0, "Height should be any positive number.");
            Validate.notNull(borderColor, "Please set border color to mark differences.");
            Validate.notNull(areaColor, "Please set area color.");
            Validate.notNull(fontColor, "Please set font color.");
            Validate.notNull(font, "Please set font.");
            Validate.notNull(text, "Text should be not null.");

            HighlightElement element = new HighlightElement();
            element.x = x;
            element.y = y;
            element.width = width;
            element.height = height;
            element.borderColor = borderColor;
            element.areaColor = areaColor;
            element.fontColor = fontColor;
            element.font = font;
            element.text = text;

            return element;
        }
    }
}
