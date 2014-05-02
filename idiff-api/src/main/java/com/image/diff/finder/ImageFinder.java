package com.image.diff.finder;

import com.image.diff.core.Match;
import com.image.diff.core.MatchContext;
import java.util.List;

public interface ImageFinder {

    List<Match> find(MatchContext context);
}
