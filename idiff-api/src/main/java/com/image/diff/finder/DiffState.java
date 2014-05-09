package com.image.diff.finder;

import com.image.diff.core.Match;
import com.image.diff.core.MatchContext;
import com.image.diff.core.MatchResult;
import com.image.diff.core.Roi;
import com.image.diff.helper.ImageHelper;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiffState implements State {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final ImageHelper imageHelper;
    private final MatchContext context;

    public DiffState(MatchContext context, ImageHelper imageHelper) {
        this.context = context;
        this.imageHelper = imageHelper;
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
        return matchResults;
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
