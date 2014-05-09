package com.image.diff.core;

import java.io.File;

public class Defaults {

    private static final String TITLE_VALUE = "";
    private static final File RESULT_IMAGE_VALUE = new File("output/result_image.png");
    private static final File RESULT_SOURCE_IMAGE_VALUE = new File("output/result_source_image.png");
    private static final double MATCH_SIMILARITY_VALUE = 0.8;
    private static final TemplateMatchMethod MATCH_METHOD_VALUE = TemplateMatchMethod.CV_TM_CCOEFF_NORMED;
    private static final int LIMIT_VALUE = 100;

    public MatchContext getFindDiffRoiSampleContext() {
        MatchContext matchContext = new MatchContext();

        String image1FilePath = "samples/image1.png";
        String image2FilePath = "samples/image2.png";

        matchContext.setDiffSpecified(true);
        matchContext.setTitle("[CV_TM_CCOEFF_NORMED] Find differences between images images with equal sizes and ROIs");
        matchContext.setMatchSimilarity(0.95D);
        matchContext.setShowResult(true);
        matchContext.setImage1(new File(image1FilePath));
        matchContext.setImage2(new File(image2FilePath));
        matchContext.setResultImage(Defaults.RESULT_IMAGE_VALUE);
        matchContext.setResultSourceImage(Defaults.RESULT_SOURCE_IMAGE_VALUE);
        matchContext.setMatchMethod(TemplateMatchMethod.CV_TM_CCOEFF_NORMED);
        matchContext.setLimit(100);
        matchContext.addRoi(new Roi(247, 137, 325, 35));

        return matchContext;
    }

    public MatchContext getFindDiffSampleContext() {
        MatchContext matchContext = new MatchContext();

        String image1FilePath = "samples/image1.png";
        String image2FilePath = "samples/image2.png";

        matchContext.setDiffSpecified(true);
        matchContext.setTitle("[CV_TM_CCOEFF_NORMED] Find differences between images with equal sizes");
        matchContext.setMatchSimilarity(0.95D);
        matchContext.setShowResult(true);
        matchContext.setImage1(new File(image1FilePath));
        matchContext.setImage2(new File(image2FilePath));
        matchContext.setResultImage(Defaults.RESULT_IMAGE_VALUE);
        matchContext.setResultSourceImage(Defaults.RESULT_SOURCE_IMAGE_VALUE);
        matchContext.setMatchMethod(TemplateMatchMethod.CV_TM_CCOEFF_NORMED);
        matchContext.setLimit(100);

        return matchContext;
    }

    public MatchContext getFindTemplateInSourceImageRoiSampleContext() {
        MatchContext matchContext = new MatchContext();

        String sourceImageFilePath = "samples/sourceImage.jpg";
        String templateImageFilePath = "samples/templateImage.jpg";

        matchContext.setFindSpecified(true);
        matchContext.setTitle("[CV_TM_CCOEFF_NORMED] Find template in source image inside of the ROIs");
        matchContext.setMatchSimilarity(0.95D);
        matchContext.setShowResult(true);
        matchContext.setImage1(new File(sourceImageFilePath));
        matchContext.setImage2(new File(templateImageFilePath));
        matchContext.setResultImage(Defaults.RESULT_IMAGE_VALUE);
        matchContext.setResultSourceImage(Defaults.RESULT_SOURCE_IMAGE_VALUE);
        matchContext.setMatchMethod(TemplateMatchMethod.CV_TM_CCOEFF_NORMED);
        matchContext.setLimit(100);
        matchContext.addRoi(new Roi(230, 140, 185, 155));

        return matchContext;
    }

    public MatchContext getFindTemplateInSourceImageSampleContext() {
        MatchContext matchContext = new MatchContext();

        String sourceImageFilePath = "samples/sourceImage.jpg";
        String templateImageFilePath = "samples/templateImage.jpg";

        matchContext.setFindSpecified(true);
        matchContext.setTitle("[CV_TM_CCOEFF_NORMED] Find template in source image");
        matchContext.setMatchSimilarity(0.95D);
        matchContext.setShowResult(true);
        matchContext.setImage1(new File(sourceImageFilePath));
        matchContext.setImage2(new File(templateImageFilePath));
        matchContext.setResultImage(Defaults.RESULT_IMAGE_VALUE);
        matchContext.setResultSourceImage(Defaults.RESULT_SOURCE_IMAGE_VALUE);
        matchContext.setMatchMethod(TemplateMatchMethod.CV_TM_CCOEFF_NORMED);
        matchContext.setLimit(100);

        return matchContext;
    }

    public String getTitleValue() {
        return Defaults.TITLE_VALUE;
    }

    public File getResultImageValue() {
        return Defaults.RESULT_IMAGE_VALUE;
    }

    public File getResultSourceImageValue() {
        return Defaults.RESULT_SOURCE_IMAGE_VALUE;
    }

    public double getMatchSimilarityValue() {
        return Defaults.MATCH_SIMILARITY_VALUE;
    }

    public TemplateMatchMethod getMatchMethodValue() {
        return Defaults.MATCH_METHOD_VALUE;
    }

    public int getLimitValue() {
        return Defaults.LIMIT_VALUE;
    }
}
