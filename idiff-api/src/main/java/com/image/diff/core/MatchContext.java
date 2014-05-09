package com.image.diff.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MatchContext {

    private boolean showResult;
    private String title;
    private File image1;
    private File image2;
    private BufferedImage bufferedImage1;
    private BufferedImage bufferedImage2;
    private File resultImage;
    private File resultSourceImage;
    private double matchSimilarity;
    private TemplateMatchMethod matchMethod;
    private int limit;
    private final List<Roi> rois = new ArrayList<Roi>();
    private boolean findSpecified;
    private boolean diffSpecified;
    private final List<Match> matches = new ArrayList<Match>();

    public boolean isShowResult() {
        return showResult;
    }

    public void setShowResult(boolean showResult) {
        this.showResult = showResult;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getImage1() {
        return image1;
    }

    public void setImage1(File image1) {
        this.image1 = image1;
    }

    public File getImage2() {
        return image2;
    }

    public void setImage2(File image2) {
        this.image2 = image2;
    }

    public BufferedImage getBufferedImage1() {
        return bufferedImage1;
    }

    public void setBufferedImage1(BufferedImage bufferedImage1) {
        this.bufferedImage1 = bufferedImage1;
    }

    public BufferedImage getBufferedImage2() {
        return bufferedImage2;
    }

    public void setBufferedImage2(BufferedImage bufferedImage2) {
        this.bufferedImage2 = bufferedImage2;
    }

    public File getResultImage() {
        return resultImage;
    }

    public void setResultImage(File resultImage) {
        this.resultImage = resultImage;
    }

    public File getResultSourceImage() {
        return resultSourceImage;
    }

    public void setResultSourceImage(File resultSourceImage) {
        this.resultSourceImage = resultSourceImage;
    }

    public double getMatchSimilarity() {
        return matchSimilarity;
    }

    public void setMatchSimilarity(double matchSimilarity) {
        this.matchSimilarity = matchSimilarity;
    }

    public TemplateMatchMethod getMatchMethod() {
        return matchMethod;
    }

    public void setMatchMethod(TemplateMatchMethod matchMethod) {
        this.matchMethod = matchMethod;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Roi> getRois() {
        return rois;
    }

    public void addRois(List<Roi> rois) {
        this.rois.addAll(rois);
    }

    public void addRoi(Roi roi) {
        this.rois.add(roi);
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void addMatches(List<Match> matches) {
        this.matches.addAll(matches);
    }

    public void addMatch(Match match) {
        this.matches.add(match);
    }

    public boolean isFindSpecified() {
        return findSpecified;
    }

    public void setFindSpecified(boolean findSpecified) {
        this.findSpecified = findSpecified;
    }

    public boolean isDiffSpecified() {
        return diffSpecified;
    }

    public void setDiffSpecified(boolean diffSpecified) {
        this.diffSpecified = diffSpecified;
    }

    @Override
    public String toString() {
        return "MatchContext{" + "showResult=" + showResult + ", title=" + title + ", image1=" + image1 + ", image2=" + image2 + ", bufferedImage1=" + bufferedImage1 + ", bufferedImage2=" + bufferedImage2 + ", resultImage=" + resultImage + ", resultSourceImage=" + resultSourceImage + ", matchSimilarity=" + matchSimilarity + ", matchMethod=" + matchMethod + ", limit=" + limit + ", rois=" + rois + ", findSpecified=" + findSpecified + ", diffSpecified=" + diffSpecified + ", matches=" + matches + '}';
    }
}
