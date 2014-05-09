package com.image.diff.helper;

import com.image.diff.core.Match;
import com.image.diff.core.MatchContext;
import com.image.diff.finder.DiffStrategy;
import com.image.diff.finder.FindStrategy;
import com.image.diff.finder.SearchStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Finder {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final MatchContext context;
    private SearchStrategy searchStrategy;

    private Finder(MatchContext context) {
        this.context = context;
    }

    public List<Match> find() {
        List<Match> matchResults = searchStrategy.find();
        context.addMatches(matchResults);

        searchStrategy.highlightRegions();
        searchStrategy.highlightMatchedElements();

        if (context.isShowResult()) {
            searchStrategy.showResult();
        }

        return matchResults;
    }

    public static final class Builder {

        private final MatchContext context;
        private ImageHelper imageHelper;
        private HighlightHelper highlightHelper;
        private SearchStrategy searchStrategy;

        public Builder(MatchContext context) {
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

        public Builder searchStrategy(SearchStrategy imageFinder) {
            this.searchStrategy = imageFinder;
            return this;
        }

        public Finder build() {
            Validate.notNull(context, "Context must not be null");
            Validate.notNull(context.getImage1(), "Please set first image.");
            Validate.notNull(context.getImage2(), "Please set second image.");
            Validate.notNull(context.getResultImage(), "Please set result image.");
            Validate.notNull(context.getResultSourceImage(), "Please set result source image.");
            Validate.isTrue(context.getMatchSimilarity() > 0 && context.getMatchSimilarity() < 1, "Expected matching similarity should be from 0..1 (both exclusive).");
            Validate.notNull(context.getMatchMethod(), "Please set match method.");
            Validate.isTrue(context.getLimit() > 0, "Expected limit of results should be > 0.");
            Validate.notNull(context.getRois(), "List of region of interests should not be null.");

            Validate.isTrue(context.getImage1().exists(), "First image should exists.");
            Validate.isTrue(context.getImage2().exists(), "Second image should exists.");

            // init images in context
            initImages();

            if (searchStrategy == null) {
                // initialize it based on context values
                if (context.isDiffSpecified()) {
                    searchStrategy = new DiffStrategy.Builder(context).
                        highlightHelper(highlightHelper).
                        imageHelper(imageHelper).build();
                } else if (context.isFindSpecified()) {
                    searchStrategy = new FindStrategy.Builder(context).
                        highlightHelper(highlightHelper).
                        imageHelper(imageHelper).build();
                } else {
                    throw new IllegalStateException("Search type undefined");
                }
            }

            Finder finder = new Finder(context);
            finder.searchStrategy = searchStrategy;

            return finder;
        }

        private void initImages() {
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
