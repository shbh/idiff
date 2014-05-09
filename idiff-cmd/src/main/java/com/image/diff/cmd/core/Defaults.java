package com.image.diff.cmd.core;

import com.image.diff.core.MatchContext;
import com.image.diff.core.Roi;
import com.image.diff.core.TemplateMatchMethod;
import java.io.File;

public class Defaults {

    public static final String HELP_OPTION_NAME = "help";
    public static final String SHOW_RESULT_OPTION_NAME = "show-result";
    public static final String TITLE_OPTION_NAME = "title";
    public static final String IMAGE1_OPTION_NAME = "image1";
    public static final String IMAGE2_OPTION_NAME = "image2";
    public static final String RESULT_IMAGE_OPTION_NAME = "result-image";
    public static final String RESULT_SOURCE_IMAGE_OPTION_NAME = "result-source-image";
    public static final String MATCH_SIMILARITY_OPTION_NAME = "match-similarity";
    public static final String LIMIT_OPTION_NAME = "limit";
    public static final String ROIS_OPTION_NAME = "rois";

    public static final String FIND_OPTION_NAME = "find";
    public static final String DIFF_OPTION_NAME = "diff";

    public static final String FIND_DIFF_SAMPLE_OPTION_NAME = "find-diff-sample";
    public static final String FIND_DIFF_ROI_SAMPLE_OPTION_NAME = "find-diff-roi-sample";
    public static final String FIND_TEMPLATE_IN_SOURCE_IMAGE_SAMPLE_OPTION_NAME = "find-template-in-source-image-sample";
    public static final String FIND_TEMPLATE_IN_SOURCE_IMAGE_ROI_SAMPLE_OPTION_NAME = "find-template-in-source-image-roi-sample";

    public static final String ROI_DELIMITER = "\\s*;\\s*";
    public static final String ROI_PARAMS_DELIMITER = "\\s*,\\s*";

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

    public String getHelpOptionName() {
        return Defaults.HELP_OPTION_NAME;
    }

    public String getShowResultOptionName() {
        return Defaults.SHOW_RESULT_OPTION_NAME;
    }

    public String getTitleOptionName() {
        return Defaults.TITLE_OPTION_NAME;
    }

    public String getImage1OptionName() {
        return Defaults.IMAGE1_OPTION_NAME;
    }

    public String getImage2OptionName() {
        return Defaults.IMAGE2_OPTION_NAME;
    }

    public String getResultImageOptionName() {
        return Defaults.RESULT_IMAGE_OPTION_NAME;
    }

    public String getResultSourceImageOptionName() {
        return Defaults.RESULT_SOURCE_IMAGE_OPTION_NAME;
    }

    public String getMatchSimilarityOptionName() {
        return Defaults.MATCH_SIMILARITY_OPTION_NAME;
    }

    public String getLimitOptionName() {
        return Defaults.LIMIT_OPTION_NAME;
    }

    public String getRoisOptionName() {
        return Defaults.ROIS_OPTION_NAME;
    }

    public String getFindOptionName() {
        return Defaults.FIND_OPTION_NAME;
    }

    public String getDiffOptionName() {
        return Defaults.DIFF_OPTION_NAME;
    }

    public String getFindDiffSampleOptionName() {
        return Defaults.FIND_DIFF_SAMPLE_OPTION_NAME;
    }

    public String getFindDiffRoiSampleOptionName() {
        return Defaults.FIND_DIFF_ROI_SAMPLE_OPTION_NAME;
    }

    public String getFindTemplateInSourceImageSampleOptionName() {
        return Defaults.FIND_TEMPLATE_IN_SOURCE_IMAGE_SAMPLE_OPTION_NAME;
    }

    public String getFindTemplateInSourceImageRoiSampleOptionName() {
        return Defaults.FIND_TEMPLATE_IN_SOURCE_IMAGE_ROI_SAMPLE_OPTION_NAME;
    }

    public String getRoiDelimiter() {
        return Defaults.ROI_DELIMITER;
    }

    public String getRoiParamsDelimiter() {
        return Defaults.ROI_PARAMS_DELIMITER;
    }
}
