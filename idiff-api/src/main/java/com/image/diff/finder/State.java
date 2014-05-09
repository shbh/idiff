package com.image.diff.finder;

import com.image.diff.core.Match;
import java.util.List;

public interface State {

    List<Match> find();

    void showResult();

    void highlightRegions();

    void highlightMatchedElements();
}
