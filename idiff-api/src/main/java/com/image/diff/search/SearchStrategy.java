package com.image.diff.search;

import com.image.diff.core.Match;
import java.util.List;

public interface SearchStrategy {

    List<Match> find();

    void showResult(List<Match> matchResults);

    void highlightRois();

    void highlightMatchedElements(List<Match> matchResults);
}
