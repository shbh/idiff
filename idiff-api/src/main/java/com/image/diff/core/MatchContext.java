package com.image.diff.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.Validate;

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

    public boolean isShowResult() {
        return showResult;
    }

    public String getTitle() {
        return title;
    }

    public File getImage1() {
        return image1;
    }

    public File getImage2() {
        return image2;
    }

    public BufferedImage getBufferedImage1() {
        return bufferedImage1;
    }

    public BufferedImage getBufferedImage2() {
        return bufferedImage2;
    }

    public File getResultImage() {
        return resultImage;
    }

    public File getResultSourceImage() {
        return resultSourceImage;
    }

    public double getMatchSimilarity() {
        return matchSimilarity;
    }

    public TemplateMatchMethod getMatchMethod() {
        return matchMethod;
    }

    public int getLimit() {
        return limit;
    }

    public List<Roi> getRois() {
        return Collections.unmodifiableList(rois);
    }

    private void addRois(List<Roi> rois) {
        this.rois.addAll(rois);
    }

    private void addRoi(Roi roi) {
        this.rois.add(roi);
    }

    public boolean isFindSpecified() {
        return findSpecified;
    }

    public boolean isDiffSpecified() {
        return diffSpecified;
    }

    @Override
    public String toString() {
        return "MatchContext{" + "showResult=" + showResult + ", title=" + title + ", image1=" + image1 + ", image2=" + image2 + ", bufferedImage1=" + bufferedImage1 + ", bufferedImage2=" + bufferedImage2 + ", resultImage=" + resultImage + ", resultSourceImage=" + resultSourceImage + ", matchSimilarity=" + matchSimilarity + ", matchMethod=" + matchMethod + ", limit=" + limit + ", rois=" + rois + ", findSpecified=" + findSpecified + ", diffSpecified=" + diffSpecified + '}';
    }

    public static class Builder {

        private boolean showResult;
        private String title;
        private File image1;
        private File image2;
        private File resultImage;
        private File resultSourceImage;
        private double matchSimilarity;
        private TemplateMatchMethod matchMethod;
        private int limit;
        private final List<Roi> rois = new ArrayList<Roi>();
        private boolean findSpecified;
        private boolean diffSpecified;

        public Builder showResult() {
            this.showResult = true;
            return this;
        }

        public Builder showResult(boolean showResult) {
            this.showResult = showResult;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder image1(File image1) {
            this.image1 = image1;
            return this;
        }

        public Builder image2(File image2) {
            this.image2 = image2;
            return this;
        }

        public Builder resultImage(File resultImage) {
            this.resultImage = resultImage;
            return this;
        }

        public Builder resultSourceImage(File resultSourceImage) {
            this.resultSourceImage = resultSourceImage;
            return this;
        }

        public Builder matchSimilarity(double matchSimilarity) {
            this.matchSimilarity = matchSimilarity;
            return this;
        }

        public Builder matchMethod(TemplateMatchMethod matchMethod) {
            this.matchMethod = matchMethod;
            return this;
        }

        public Builder rois(List<Roi> rois) {
            this.rois.addAll(rois);
            return this;
        }

        public Builder roi(Roi roi) {
            this.rois.add(roi);
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder find() {
            this.findSpecified = true;
            return this;
        }

        public Builder diff() {
            this.diffSpecified = true;
            return this;
        }

        public Builder find(boolean findSpecified) {
            this.findSpecified = findSpecified;
            return this;
        }

        public Builder diff(boolean diffSpecified) {
            this.diffSpecified = diffSpecified;
            return this;
        }

        public MatchContext build() {
            Validate.notNull(image1, "First image must not be null");
            Validate.notNull(image2, "Second image must not be null");

            BufferedImage bufferedImage1;
            try {
                bufferedImage1 = ImageIO.read(image1);
            } catch (IOException ex) {
                throw new IllegalStateException("Could not read first image: " + image1.getAbsolutePath(), ex);
            }

            BufferedImage bufferedImage2;
            try {
                bufferedImage2 = ImageIO.read(image2);
            } catch (IOException ex) {
                throw new IllegalStateException("Could not read second image: " + image2.getAbsolutePath(), ex);
            }

            MatchContext context = new MatchContext();
            context.showResult = showResult;
            context.title = title;
            context.image1 = image1;
            context.image2 = image2;
            context.bufferedImage1 = bufferedImage1;
            context.bufferedImage2 = bufferedImage2;
            context.resultImage = resultImage;
            context.resultSourceImage = resultSourceImage;
            context.matchSimilarity = matchSimilarity;
            context.matchMethod = matchMethod;
            context.limit = limit;
            context.findSpecified = findSpecified;
            context.diffSpecified = diffSpecified;

            if (!rois.isEmpty()) {
                context.addRois(rois);
            }

            return context;
        }
    }
}
