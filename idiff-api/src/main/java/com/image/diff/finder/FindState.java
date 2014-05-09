package com.image.diff.finder;

import com.image.diff.core.Match;
import com.image.diff.core.MatchContainer;
import com.image.diff.core.MatchContext;
import com.image.diff.core.TemplateMatchMethod;
import com.image.diff.core.MatchResult;
import com.image.diff.helper.ImageHelper;
import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.cvMinMaxLoc;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRealScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindState implements State {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final ImageHelper imageHelper = new ImageHelper();

    private MatchResult fetchNextBestMatch(final MatchContainer resultContainer) {
        MatchResult result;
        double minValue[] = new double[2];
        double maxValue[] = new double[2];
        CvPoint minPoint = new CvPoint();
        CvPoint maxPoint = new CvPoint();

        IplImage sourceImage = resultContainer.getSourceImage();
        IplImage templateImage = resultContainer.getTemplateImage();
        IplImage resultImage = resultContainer.getResultImage();
        TemplateMatchMethod matchMethod = resultContainer.getMatchMethod();
        CvPoint minMaxMatchLocation;
        double minMaxScore;

        cvMinMaxLoc(resultImage, minValue, maxValue, minPoint, maxPoint, null);

        if (matchMethod == TemplateMatchMethod.CV_TM_SQDIFF_NORMED) {
            minMaxMatchLocation = minPoint;
            minMaxScore = 1 - minValue[0];
        } else {
            minMaxMatchLocation = maxPoint;
            minMaxScore = maxValue[0];
        }

        int x = minMaxMatchLocation.x();
        int y = minMaxMatchLocation.y();
        int width = templateImage.width();
        int height = templateImage.height();

        // Suppress returned match
        int xmargin = width / 3;
        int ymargin = height / 3;

        int x0 = Math.max(x - xmargin, 0);
        int y0 = Math.max(y - ymargin, 0);
        // no need to blank right and bottom
        int x1 = Math.min(x + xmargin, resultImage.width());
        int y1 = Math.min(y + ymargin, resultImage.height());

        cvRectangle(resultImage, cvPoint(x0, y0), cvPoint(x1 - 1, y1 - 1),
            cvRealScalar(0.0), CV_FILLED, 8, 0);

        result = new MatchResult(minMaxScore, x, y, width, height);

        return result;
    }

    public List<Match> find(MatchContext context) {
        BufferedImage sourceBufferedImage = context.getBufferedImage1();
        BufferedImage templateBufferedImage = context.getBufferedImage2();

        if (sourceBufferedImage.getWidth() < templateBufferedImage.getWidth() || sourceBufferedImage.getHeight() < templateBufferedImage.getHeight()) {
            // if source image is smaller than the target, no target can be found
            logger.warn("Source image is smaller than the target, no target can be found.");
            return Collections.EMPTY_LIST;
        }

        final List<Match> matchResults = new ArrayList<Match>();
        IplImage sourceImage = imageHelper.createGrayImageFrom(sourceBufferedImage);
        IplImage templateImage = imageHelper.createGrayImageFrom(templateBufferedImage);

        MatchContainer container;
        if (context.getRois().isEmpty()) {
            container = imageHelper.computeResultImage(sourceImage, templateImage, context.getMatchMethod());
        } else {
            container = imageHelper.computeResultImage(sourceImage, templateImage, context.getMatchMethod(), context.getRois());
        }

        MatchResult prev = null;
        double score = 1.0D;
        while (Double.compare(score, context.getMatchSimilarity()) > 0) {
            MatchResult result = fetchNextBestMatch(container);
            if (prev != null) {
                // avoid looping
                if (result.equals(prev)) {
                    break;
                }
            }
            if (matchResults.size() >= context.getLimit()) {
                break;
            }

            score = result.getScore();
            if (Double.compare(score, context.getMatchSimilarity()) > 0) {
                matchResults.add(result);
            }
            prev = result;
        }

        // Release all resources
        cvReleaseImage(container.getSourceImage());
        cvReleaseImage(container.getTemplateImage());
        cvReleaseImage(container.getResultImage());
        return matchResults;
    }

    @Override
    public List<Match> find() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void highlightRegions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void highlightMatchedElements() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
