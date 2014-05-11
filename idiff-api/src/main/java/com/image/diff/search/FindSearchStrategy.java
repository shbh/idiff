package com.image.diff.search;

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
import com.image.diff.core.Roi;
import com.image.diff.helper.HighlightHelper;
import com.image.diff.ui.FindResultWindow;
import com.image.diff.visual.HighlightElement;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindSearchStrategy implements SearchStrategy {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private MatchContext context;
    private ImageHelper imageHelper;
    private HighlightHelper highlightHelper;

    private FindSearchStrategy() {
    }

    @Override
    public List<Match> find() {
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

        return Collections.unmodifiableList(matchResults);
    }

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

    @Override
    public void showResult(final List<Match> matchResults) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FindResultWindow resultWindow = new FindResultWindow(context, matchResults);
                resultWindow.show();
            }
        });
    }

    @Override
    public void highlightRois() {
        for (Roi r : context.getRois()) {
            try {
                highlightElementOnResultImage(new HighlightElement.Builder().
                    x(r.getX()).
                    y(r.getY()).
                    width(r.getWidth()).
                    height(r.getHeight()).
                    areaColor(new Color(245, 255, 255, 255 * 50 / 100)).
                    build());
            } catch (IOException ex) {
                logger.error("Can't highlight area", ex);
            }
        }
    }

    @Override
    public void highlightMatchedElements(List<Match> matchResults) {
        if (matchResults == null || matchResults.isEmpty()) {
            logger.warn("No matchings to highlight");
            return;
        }

        for (Match match : matchResults) {
            int x = match.getX();
            int y = match.getY();
            int width = match.getWidth();
            int height = match.getHeight();
            double score = match.getScore();
            String text = highlightHelper.getHighlightElementText(match);

            try {
                highlightElementOnResultImage(new HighlightElement.Builder().
                    x(x).
                    y(y).
                    width(width).
                    height(height).
                    text(text).
                    build()
                );
            } catch (IOException ex) {
                logger.error("Can't highlight element", ex);
            }
        }
    }

    private void highlightElementOnResultImage(HighlightElement element) throws IOException {
        highlightHelper.highlightElement(context.getResultImage(), element);
    }

    public static class Builder {

        private Logger logger = LoggerFactory.getLogger(getClass());

        private final MatchContext context;
        private ImageHelper imageHelper;
        private HighlightHelper highlightHelper;

        public Builder(MatchContext context) {
            Validate.notNull(context, "Context must not be null");
            this.context = context;
        }

        public Builder imageHelper(ImageHelper imageHelper) {
            this.imageHelper = imageHelper;
            return this;
        }

        public Builder highlightHelper(HighlightHelper highlightHelper) {
            this.highlightHelper = highlightHelper;
            return this;
        }

        public FindSearchStrategy build() {
            // post init
            postInitContext();

            FindSearchStrategy instance = new FindSearchStrategy();

            if (imageHelper == null) {
                logger.debug("Default instance will be used as image helper");
                imageHelper = new ImageHelper();
            }
            if (highlightHelper == null) {
                logger.debug("Default instance will be used as highlight helper");
                highlightHelper = new HighlightHelper();
            }
            instance.context = context;
            instance.imageHelper = imageHelper;
            instance.highlightHelper = highlightHelper;

            return instance;
        }

        private void postInitContext() {
            // copy content from images to result images to show it in final UI frame, if needed
            try {
                FileUtils.copyFile(context.getImage1(), context.getResultImage());
            } catch (IOException ex) {
                throw new IllegalStateException("Could not initialize result image file: " + context.getResultImage().getAbsolutePath(), ex);
            }
        }
    }
}
