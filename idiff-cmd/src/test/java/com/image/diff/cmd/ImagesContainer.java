package com.image.diff.cmd;

import java.io.File;

public class ImagesContainer {

    private static final String DIFF_IMAGE_1_PATH = "src/test/resources/samples/image1.png";
    private static final String DIFF_IMAGE_2_PATH = "src/test/resources/samples/image2.png";
    private static final String FIND_SOURCE_IMAGE_PATH = "src/test/resources/samples/sourceImage.jpg";
    private static final String FIND_TEMPLATE_IMAGE_PATH = "src/test/resources/samples/templateImage.jpg";
    private static final String RESULT_IMAGE_PATH = "target/resultImage.png";
    private static final String RESULT_SOURCE_IMAGE_PATH = "target/resultSourceImage.png";

    public File getDiffImage1File() {
        return new File(DIFF_IMAGE_1_PATH);
    }

    public File getDiffImage2File() {
        return new File(DIFF_IMAGE_2_PATH);
    }

    public File getSourceImageFile() {
        return new File(FIND_SOURCE_IMAGE_PATH);
    }

    public File getTemplateImageFile() {
        return new File(FIND_TEMPLATE_IMAGE_PATH);
    }

    public File getResultImageFile() {
        return new File(RESULT_IMAGE_PATH);
    }

    public File getResultSourceImageFile() {
        return new File(RESULT_SOURCE_IMAGE_PATH);
    }
}
