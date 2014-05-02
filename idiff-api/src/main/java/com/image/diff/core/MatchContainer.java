package com.image.diff.core;

import static com.googlecode.javacv.cpp.opencv_core.*;

public class MatchContainer {

    private final IplImage sourceImage;
    private final IplImage templateImage;
    private final IplImage resultImage;
    private final TemplateMatchMethod matchMethod;

    public MatchContainer(IplImage sourceImage, IplImage templateImage, IplImage resultImage, TemplateMatchMethod matchMethod) {
        this.sourceImage = sourceImage;
        this.templateImage = templateImage;
        this.resultImage = resultImage;
        this.matchMethod = matchMethod;
    }

    public IplImage getSourceImage() {
        return sourceImage;
    }

    public IplImage getTemplateImage() {
        return templateImage;
    }

    public IplImage getResultImage() {
        return resultImage;
    }

    public TemplateMatchMethod getMatchMethod() {
        return matchMethod;
    }
}
