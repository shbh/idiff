package com.image.diff.helper;

import com.image.diff.visual.HighlightElement;
import com.image.diff.core.Match;
import com.image.diff.core.MatchContext;
import com.image.diff.core.MatchType;
import com.image.diff.ui.FindResultWindow;
import com.image.diff.core.Roi;
import com.image.diff.ui.DiffResultWindow;
import com.image.diff.finder.DiffImageFinder;
import com.image.diff.finder.ImageFinder;
import com.image.diff.finder.TemplateImageFinder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Finder {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final MatchContext context;
    private ImageFinder imageFinder;

    private Finder(MatchContext context) {
        this.context = context;
    }

    public List<Match> find() {
        List<Match> matchResults = imageFinder.find(context);
        context.addMatches(matchResults);

        highlightRegions();
        highlightMatchedElements(matchResults);

        if (context.isShowResult()) {
            showResult();
        }

        return matchResults;
    }

    protected void showResult() {
        if (MatchType.DIFFERENCES == context.getMatchType()) {
            showDifferencesResultWindow();
        } else {
            showResultWindow();
        }
    }

    protected void showDifferencesResultWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DiffResultWindow resultWindow = new DiffResultWindow(context);
                resultWindow.show();
            }
        });
    }

    protected void showResultWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FindResultWindow resultWindow = new FindResultWindow(context);
                resultWindow.show();
            }
        });
    }

    protected void highlightRegions() {
        for (Roi r : context.getRois()) {
            try {
                if (context.getMatchType() == MatchType.DIFFERENCES) {
                    highlightElementOnSourceImage(new HighlightElement.Builder().
                        x(r.getX()).
                        y(r.getY()).
                        width(r.getWidth()).
                        height(r.getHeight()).
                        borderColor(Color.GREEN).
                        areaColor(new Color(245, 255, 255, 255 * 50 / 100)).
                        build());
                }

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

    protected void highlightMatchedElements(List<Match> matchResults) {
        for (Match match : matchResults) {
            int x = match.getX();
            int y = match.getY();
            int width = match.getWidth();
            int height = match.getHeight();
            double score = match.getScore();
            String text = getHighlightElementText(match);

            try {
                if (context.getMatchType() == MatchType.DIFFERENCES) {
                    highlightElementOnSourceImage(new HighlightElement.Builder().
                        x(x).
                        y(y).
                        width(width).
                        height(height).
                        borderColor(Color.GREEN).
                        areaColor(new Color(0, 255, 0, 255 * 50 / 100)).
                        build()
                    );
                }

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

    protected String getHighlightElementText(Match match) {
        double score = match.getScore();
        int roundedScore = (int) (score * 100);

        return roundedScore + "%";
    }

    protected void highlightElementOnSourceImage(HighlightElement element) throws IOException {
        highlightElement(context.getResultSourceImage(), element);
    }

    protected void highlightElementOnResultImage(HighlightElement element) throws IOException {
        highlightElement(context.getResultImage(), element);
    }

    protected void highlightElement(File image, HighlightElement element) throws IOException {
        BufferedImage canvas = ImageIO.read(image);

        Graphics2D g2 = canvas.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        // border
        g2.setColor(element.getBorderColor());
        g2.drawRect(element.getX(), element.getY(), element.getWidth(), element.getHeight());

        // area
        g2.setColor(element.getAreaColor());
        g2.fillRect(element.getX(), element.getY(), element.getWidth(), element.getHeight());

        // text
        if (element.getText() != null && !element.getText().isEmpty()) {
            g2.setColor(element.getFontColor());
            int centerX = element.getX() + 1;
            int centerY = element.getY() + element.getHeight() / 2;
            g2.setFont(element.getFont());
            g2.drawString(element.getText(), centerX, centerY);
        }

        g2.dispose();

        // update result image with highlights
        ImageIO.write(canvas, "PNG", image);
    }

    public static final class Builder {

        private final MatchContext context;
        private ImageFinder imageFinder;
        private FinderValidator finderValidator;

        public Builder(MatchContext context) {
            this.context = context;
        }

        public Builder imageFinder(ImageFinder imageFinder) {
            this.imageFinder = imageFinder;
            return this;
        }

        public Builder finderValidator(FinderValidator finderValidator) {
            this.finderValidator = finderValidator;
            return this;
        }

        public Finder build() {
            Validate.notNull(context, "Context must not be null");
            Validate.notNull(context.getImage1(), "Please set first image.");
            Validate.notNull(context.getImage2(), "Please set second image.");
            Validate.notNull(context.getResultImage(), "Please set result image.");
            Validate.notNull(context.getResultSourceImage(), "Please set result source image.");

            if (finderValidator == null) {
                // use default if no specified
                finderValidator = new FinderValidator();
            }

            postInitProcessContext(context);
            finderValidator.validate(context);

            if (imageFinder == null) {
                // initialize it based on context values
                switch (context.getMatchType()) {
                    case DIFFERENCES:
                        imageFinder = new DiffImageFinder();
                        break;
                    case TEMPLATES:
                        imageFinder = new TemplateImageFinder();
                        break;
                    default:
                        throw new IllegalStateException("Can't find handler for type: " + context.getMatchType());
                }
            }

            Finder finder = new Finder(context);
            finder.imageFinder = imageFinder;

            return finder;
        }

        private void postInitProcessContext(MatchContext context) {
            // copy content from images to result images to show it in final UI frame, if needed
            try {
                if (context.getMatchType() == MatchType.DIFFERENCES) {
                    FileUtils.copyFile(context.getImage2(), context.getResultImage());
                    FileUtils.copyFile(context.getImage1(), context.getResultSourceImage());
                } else {
                    FileUtils.copyFile(context.getImage1(), context.getResultImage());
                }
            } catch (IOException ex) {
                throw new IllegalStateException("Could not initialize result image file: " + context.getResultImage().getAbsolutePath(), ex);
            }

            BufferedImage bufferedImage1;
            try {
                bufferedImage1 = ImageIO.read(context.getImage1());
            } catch (IOException ex) {
                throw new IllegalStateException("Could not initialize first image: " + context.getImage1().getAbsolutePath(), ex);
            }
            context.setBufferedImage1(bufferedImage1);

            BufferedImage bufferedImage2;
            try {
                bufferedImage2 = ImageIO.read(context.getImage2());
            } catch (IOException ex) {
                throw new IllegalStateException("Could not initialize second image: " + context.getImage2().getAbsolutePath(), ex);
            }
            context.setBufferedImage2(bufferedImage2);
        }
    }
}
