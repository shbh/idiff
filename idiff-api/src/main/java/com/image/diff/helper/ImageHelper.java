package com.image.diff.helper;

import com.image.diff.core.MatchContainer;
import com.image.diff.core.Roi;
import com.image.diff.core.TemplateMatchMethod;
import com.googlecode.javacpp.Loader;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;

public class ImageHelper {

    private void validateInputImage(IplImage sourceImage) {
        if (sourceImage == null || sourceImage.isNull()) {
            throw new IllegalArgumentException("Image must not be null");
        }

        if (sourceImage.width() <= 0 || sourceImage.height() <= 0) {
            throw new IllegalArgumentException("Invalid image dimensions");
        }
    }

    public MatchContainer computeResultImage(IplImage sourceImage, IplImage templateImage, TemplateMatchMethod matchMethod) {
        validateInputImage(sourceImage);
        validateInputImage(templateImage);

        cvResetImageROI(sourceImage);
        int resultWidth = sourceImage.width() - templateImage.width() + 1;
        int resultHeight = sourceImage.height() - templateImage.height() + 1;

        IplImage resultImage = IplImage.create(cvSize(resultWidth, resultHeight), IPL_DEPTH_32F, 1);
        cvSet(resultImage, cvScalarAll(0));
        cvMatchTemplate(sourceImage, templateImage, resultImage, matchMethod.value());

        return new MatchContainer(sourceImage, templateImage, resultImage, matchMethod);
    }

    public MatchContainer computeResultImage(IplImage sourceImage, IplImage templateImage, TemplateMatchMethod matchMethod, List<Roi> rois) {
        validateInputImage(sourceImage);
        validateInputImage(templateImage);

        cvResetImageROI(sourceImage);
        int resultWidth = sourceImage.width() - templateImage.width() + 1;
        int resultHeight = sourceImage.height() - templateImage.height() + 1;

        IplImage resultImage = IplImage.create(cvSize(resultWidth, resultHeight), IPL_DEPTH_32F, 1);
        cvSet(resultImage, cvScalarAll(0));

        for (Roi unboundedROI : rois) {
            if (unboundedROI.getWidth() < templateImage.width()) {
                // width too narrow to fit the target
                continue;
            }
            if (unboundedROI.getHeight() < templateImage.height()) {
                // height too short to fit the target
                continue;
            }

            // ROI should intersect with image bounds
            Roi roi = unboundedROI.intersection(new Roi(0, 0, sourceImage.width(), sourceImage.height()));

            cvSetImageROI(sourceImage, cvRect(roi.getX(), roi.getY(), roi.getWidth(), roi.getHeight()));

            int w = roi.getWidth() - templateImage.width() + 1;
            int h = roi.getHeight() - templateImage.height() + 1;
            cvSetImageROI(resultImage, cvRect(roi.getX(), roi.getY(), w, h));

            cvMatchTemplate(sourceImage, templateImage, resultImage, matchMethod.value());

            cvResetImageROI(resultImage);
            cvResetImageROI(sourceImage);
        }

        return new MatchContainer(sourceImage, templateImage, resultImage, matchMethod);
    }

    public List<CvRect> findBoundingRectangles(IplImage inputImage) {
        validateInputImage(inputImage);

        IplImage clonedInput = inputImage.clone();
        // Find the contours of the difference in images
        CvMemStorage memoryStorage = CvMemStorage.create();
        CvSeq contours = new CvSeq(null);
        // rectangle to use to put around the difference area
        cvFindContours(clonedInput, memoryStorage, contours, Loader.sizeof(CvContour.class), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);

        List<CvRect> boundingRectangles = new ArrayList<CvRect>();

        for (; contours != null && !contours.isNull(); contours = contours.h_next()) {
            if (contours.elem_size() > 0) {
                // get a bounding rectangle around the difference in image
                CvRect boundingRectangle = cvBoundingRect(contours, 0);
                boundingRectangles.add(boundingRectangle);
            }
        }

        // clear memory and contours
        cvClearMemStorage(memoryStorage);
        if (contours != null) {
            contours.setNull();
        }
        cvReleaseImage(clonedInput);

        return boundingRectangles;
    }

    public IplImage createGrayImageFrom(BufferedImage inputBufferedImage) {
        Validate.notNull(inputBufferedImage, "Input buffered image must not be null");

        IplImage inputImage = IplImage.createFrom(inputBufferedImage);
        IplImage result = createGrayImageFrom(inputImage);
        cvReleaseImage(inputImage);

        return result;
    }

    public IplImage createGrayImageFrom(IplImage inputImage) {
        validateInputImage(inputImage);

        IplImage result;
        if (inputImage.nChannels() > 1) {
            result = IplImage.create(cvGetSize(inputImage), inputImage.depth(), 1);
            cvCvtColor(inputImage, result, CV_BGR2GRAY);
        } else {
            result = inputImage;
        }

        return result;
    }
}
