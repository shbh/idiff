package com.image.diff.search;

import com.image.diff.core.Match;
import com.image.diff.core.MatchContext;
import com.image.diff.core.MatchResult;
import com.image.diff.core.Roi;
import com.image.diff.helper.ImageHelper;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import com.image.diff.helper.HighlightHelper;
import com.image.diff.ui.DiffResultWindow;
import com.image.diff.visual.HighlightElement;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiffSearchStrategy implements SearchStrategy {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private MatchContext context;
    private ImageHelper imageHelper;
    private HighlightHelper highlightHelper;

    private DiffSearchStrategy() {
    }

    @Override
    public List<Match> find() {
        final List<Match> matchResults = new ArrayList<Match>();
        IplImage firstImage = imageHelper.createGrayImageFrom(context.getBufferedImage1());
        IplImage secondImage = imageHelper.createGrayImageFrom(context.getBufferedImage2());
        IplImage diffImage = IplImage.createCompatible(firstImage);

        // compute difference
        cvAbsDiff(firstImage, secondImage, diffImage);

        // do some threshold for wipe away useless details
        cvThreshold(diffImage, diffImage, 0, 255, CV_THRESH_BINARY);

        List<CvRect> foundRects = imageHelper.findBoundingRectangles(diffImage);

        for (CvRect foundRect : foundRects) {
            // keep only rectangles with at least certain size
            if (foundRect.width() < 5 && foundRect.height() < 5) {
                continue;
            }
            if (matchResults.size() >= context.getLimit()) {
                break;
            }

            // count the number of diff pixels in the rectangle
            cvSetImageROI(diffImage, foundRect);
            int numberNonZeroPixels = cvCountNonZero(diffImage);
            cvResetImageROI(diffImage);

            // compute the score as the ratio of diff pixels to area
            double score = 1.0 * numberNonZeroPixels / (foundRect.width() * foundRect.height());

            if (Double.compare(score, context.getMatchSimilarity()) < 0) {
                boolean matched;
                if (context.getRois().isEmpty()) {
                    matched = true;
                } else {
                    // only if difference located inside of ROI
                    matched = intersect(new Roi(foundRect.x(), foundRect.y(), foundRect.width(), foundRect.height()), context.getRois());
                }

                if (matched) {
                    matchResults.add(new MatchResult(score, foundRect.x(), foundRect.y(), foundRect.width(), foundRect.height()));
                }
            }
        }

        // Release all resources
        cvReleaseImage(firstImage);
        cvReleaseImage(secondImage);
        cvReleaseImage(diffImage);

        return Collections.unmodifiableList(matchResults);
    }

    private boolean intersect(Roi roi, List<Roi> rois) {
        if (rois.isEmpty()) {
            return true;
        }

        boolean isIntersect = false;
        for (Roi r : rois) {
            Roi intersection = roi.intersection(r);

            if (intersection.getWidth() > 0 && intersection.getHeight() > 0) {
                isIntersect = true;
                break;
            }
        }

        return isIntersect;
    }

    @Override
    public void showResult(final List<Match> matchResults) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DiffResultWindow resultWindow = new DiffResultWindow(context, matchResults);
                resultWindow.show();
            }
        });
    }

    @Override
    public void highlightRois() {
        for (Roi r : context.getRois()) {
            try {
                highlightElementOnSourceImage(new HighlightElement.Builder().
                    x(r.getX()).
                    y(r.getY()).
                    width(r.getWidth()).
                    height(r.getHeight()).
                    borderColor(Color.GREEN).
                    areaColor(new Color(245, 255, 255, 255 * 50 / 100)).
                    build());

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
                highlightElementOnSourceImage(new HighlightElement.Builder().
                    x(x).
                    y(y).
                    width(width).
                    height(height).
                    borderColor(Color.GREEN).
                    areaColor(new Color(0, 255, 0, 255 * 50 / 100)).
                    build()
                );

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

    private void highlightElementOnSourceImage(HighlightElement element) throws IOException {
        highlightHelper.highlightElement(context.getResultSourceImage(), element);
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

        public DiffSearchStrategy build() {
            // post init
            postInitContext();

            DiffSearchStrategy instance = new DiffSearchStrategy();

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
                FileUtils.copyFile(context.getImage2(), context.getResultImage());
                FileUtils.copyFile(context.getImage1(), context.getResultSourceImage());
            } catch (IOException ex) {
                throw new IllegalStateException("Could not initialize result image file: " + context.getResultImage().getAbsolutePath(), ex);
            }
        }
    }
}
