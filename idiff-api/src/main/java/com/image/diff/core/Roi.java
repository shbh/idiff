package com.image.diff.core;

import java.awt.Rectangle;

public class Roi {

    private final Rectangle rectangle;

    public Roi(int x, int y, int width, int height) {
        this.rectangle = new Rectangle(x, y, width, height);
    }

    public Roi intersection(Roi roi) {
        Rectangle intersection = this.rectangle.intersection(roi.rectangle);
        Roi result = new Roi(intersection.x, intersection.y, intersection.width, intersection.height);
        return result;
    }

    public int getX() {
        return rectangle.x;
    }

    public int getY() {
        return rectangle.y;
    }

    public int getWidth() {
        return rectangle.width;
    }

    public int getHeight() {
        return rectangle.height;
    }

    @Override
    public String toString() {
        return "rectangle=" + rectangle;
    }
}
