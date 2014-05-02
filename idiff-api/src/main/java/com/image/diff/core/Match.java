package com.image.diff.core;

public interface Match {

    double getScore();

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    boolean contains(int x, int y);
}
