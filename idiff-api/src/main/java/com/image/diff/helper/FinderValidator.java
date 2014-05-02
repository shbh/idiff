package com.image.diff.helper;

import com.image.diff.core.MatchContext;
import org.apache.commons.lang3.Validate;

public class FinderValidator {

    public void validate(MatchContext context) {
        Validate.isTrue(context.getMatchSimilarity() > 0 && context.getMatchSimilarity() < 1, "Expected matching similarity should be from 0..1 (both exclusive).");
        Validate.notNull(context.getMatchMethod(), "Please set match method.");
        Validate.isTrue(context.getLimit() > 0, "Expected limit of results should be > 0.");
        Validate.notNull(context.getRois(), "List of region of interests should not be null.");
        Validate.notNull(context.getMatchType(), "Match type should not be null.");

        Validate.isTrue(context.getImage1().exists(), "First image should exists.");
        Validate.isTrue(context.getImage2().exists(), "Second image should exists.");

        Validate.notNull(context.getBufferedImage1(), "Buffered image1 should initialized at this step.");
        Validate.notNull(context.getBufferedImage2(), "Buffered image2 should initialized at this step.");
    }
}
