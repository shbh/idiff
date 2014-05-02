package com.image.diff.core;

public enum TemplateMatchMethod {

    CV_TM_SQDIFF_NORMED(com.googlecode.javacv.cpp.opencv_imgproc.CV_TM_SQDIFF_NORMED), CV_TM_CCORR_NORMED(com.googlecode.javacv.cpp.opencv_imgproc.CV_TM_CCORR_NORMED), CV_TM_CCOEFF_NORMED(com.googlecode.javacv.cpp.opencv_imgproc.CV_TM_CCOEFF_NORMED);
    private final int matchMethod;

    private TemplateMatchMethod(final int matchMethod) {
        this.matchMethod = matchMethod;
    }

    public int value() {
        return matchMethod;
    }

    public static TemplateMatchMethod valueOf(int matchMethod) {
        TemplateMatchMethod[] methods = TemplateMatchMethod.values();
        for (TemplateMatchMethod method : methods) {
            if (method.value() == matchMethod) {
                return method;
            }
        }

        throw new IllegalArgumentException("Match Method not supported: " + matchMethod);
    }
}
