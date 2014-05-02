package com.image.diff.core;

import java.awt.Rectangle;

public class MatchResult implements Match {

    private final double score;
    private final Rectangle rectangle;

    public MatchResult(double score, int x, int y, int width, int height) {
        this.score = score;
        this.rectangle = new Rectangle(x, y, width, height);
    }

    @Override
    public double getScore() {
        return score;
    }

    @Override
    public int getX() {
        return rectangle.x;
    }

    @Override
    public int getY() {
        return rectangle.y;
    }

    @Override
    public int getWidth() {
        return rectangle.width;
    }

    @Override
    public int getHeight() {
        return rectangle.height;
    }

    @Override
    public boolean contains(int x, int y) {
        return rectangle.contains(x, y);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.rectangle != null ? this.rectangle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MatchResult other = (MatchResult) obj;
        if (this.rectangle != other.rectangle && (this.rectangle == null || !this.rectangle.equals(other.rectangle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        int roundedScore = (int) (score * 100);

        return "score=" + roundedScore + "%, [x=" + rectangle.x + ", y=" + rectangle.y + ", width=" + rectangle.width + ", height=" + rectangle.height + "]";
    }
}
