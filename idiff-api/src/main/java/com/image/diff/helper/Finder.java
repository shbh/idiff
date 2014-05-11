package com.image.diff.helper;

import com.image.diff.core.Match;
import com.image.diff.core.MatchContext;
import com.image.diff.search.DiffSearchStrategy;
import com.image.diff.search.FindSearchStrategy;
import com.image.diff.search.SearchStrategy;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Finder {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private MatchContext context;
    private SearchStrategy searchStrategy;

    private Finder() {
    }

    public List<Match> find() {
        List<Match> matchResults = searchStrategy.find();

        searchStrategy.highlightRois();
        searchStrategy.highlightMatchedElements(matchResults);

        if (context.isShowResult()) {
            searchStrategy.showResult(matchResults);
        }

        return matchResults;
    }

    public static class Builder {

        private final MatchContext context;
        private ImageHelper imageHelper;
        private HighlightHelper highlightHelper;
        private SearchStrategy searchStrategy;

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
            Validate.isTrue(context.getMatchSimilarity() > 0 && context.getMatchSimilarity() < 1,
                "Expected matching similarity should be from 0..1 (both exclusive). Passed: " + context.getMatchSimilarity());
            Validate.notNull(context.getMatchMethod(), "Please set match method.");
            Validate.isTrue(context.getLimit() > 0,
                "Expected limit of results should be greater than 0. Passed: " + context.getLimit());
            Validate.notNull(context.getRois(), "List of region of interests should not be null.");

            Validate.isTrue(context.getImage1().exists(), "First image should exists.");
            Validate.isTrue(context.getImage2().exists(), "Second image should exists.");

            boolean oneOptionChosen = !((!context.isFindSpecified() && !context.isDiffSpecified()) || (context.isFindSpecified() && context.isDiffSpecified()));
            Validate.isTrue(oneOptionChosen, "You should choose mode before start: diff or find.");

            if (searchStrategy == null) {
                // initialize it based on context values
                if (context.isDiffSpecified()) {
                    searchStrategy = new DiffSearchStrategy.Builder(context).
                        highlightHelper(highlightHelper).
                        imageHelper(imageHelper).build();
                } else if (context.isFindSpecified()) {
                    searchStrategy = new FindSearchStrategy.Builder(context).
                        highlightHelper(highlightHelper).
                        imageHelper(imageHelper).build();
                } else {
                    throw new IllegalStateException("Search type undefined");
                }
            }

            Finder finder = new Finder();
            finder.context = context;
            finder.searchStrategy = searchStrategy;

            return finder;
        }
    }
}
