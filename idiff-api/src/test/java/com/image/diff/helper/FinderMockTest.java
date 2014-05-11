package com.image.diff.helper;

import com.image.diff.ImagesContainer;
import com.image.diff.core.Match;
import com.image.diff.core.MatchContext;
import com.image.diff.core.MatchResult;
import com.image.diff.core.TemplateMatchMethod;
import com.image.diff.search.SearchStrategy;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class FinderMockTest {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static ImagesContainer imagesContainer;

    @Mock
    SearchStrategy searchStrategy;

    @BeforeClass
    public static void beforeTests() {
        imagesContainer = new ImagesContainer();
    }

    @Test
    public void testFinderToSearchForDifferencesWithOneDifferenceExistedAndShowResult() {
        // given
        MatchContext context = new MatchContext.Builder().
            diff().
            image1(imagesContainer.getDiffImage1File()).
            image2(imagesContainer.getDiffImage2File()).
            resultImage(imagesContainer.getResultImageFile()).
            resultSourceImage(imagesContainer.getResultSourceImageFile()).
            matchSimilarity(0.5D).
            matchMethod(TemplateMatchMethod.CV_TM_CCORR_NORMED).
            limit(100).
            showResult().
            build();

        Finder finder = new Finder.Builder(context).
            searchStrategy(searchStrategy).
            build();

        Match assertMatch = new MatchResult(0.9D, 0, 0, 10, 10);
        List<Match> assertMatches = Collections.singletonList(assertMatch);

        when(searchStrategy.find()).thenReturn(assertMatches);

        // when
        List<Match> matches = finder.find();

        // then
        assertThat(matches, Matchers.notNullValue());
        assertThat(matches.size(), Matchers.equalTo(1));
        assertThat(matches.iterator().next(), Matchers.equalTo(assertMatch));

        verify(searchStrategy, times(1)).find();
        verify(searchStrategy, times(1)).highlightRois();
        verify(searchStrategy, times(1)).highlightMatchedElements(assertMatches);
        verify(searchStrategy, times(1)).showResult(matches);
    }

    @Test
    public void testFinderToSearchForDifferencesWithOneDifferenceExistedAndDoNotShowResult() {
        // given
        MatchContext context = new MatchContext.Builder().
            diff().
            image1(imagesContainer.getDiffImage1File()).
            image2(imagesContainer.getDiffImage2File()).
            resultImage(imagesContainer.getResultImageFile()).
            resultSourceImage(imagesContainer.getResultSourceImageFile()).
            matchSimilarity(0.5D).
            matchMethod(TemplateMatchMethod.CV_TM_CCORR_NORMED).
            limit(100).
            build();

        Finder finder = new Finder.Builder(context).
            searchStrategy(searchStrategy).
            build();

        Match assertMatch = new MatchResult(0.9D, 0, 0, 10, 10);
        List<Match> assertMatches = Collections.singletonList(assertMatch);

        when(searchStrategy.find()).thenReturn(assertMatches);

        // when
        List<Match> matches = finder.find();

        // then
        assertThat(matches, Matchers.notNullValue());
        assertThat(matches.size(), Matchers.equalTo(1));
        assertThat(matches.iterator().next(), Matchers.equalTo(assertMatch));

        verify(searchStrategy, times(1)).find();
        verify(searchStrategy, times(1)).highlightRois();
        verify(searchStrategy, times(1)).highlightMatchedElements(assertMatches);
        verify(searchStrategy, times(0)).showResult(matches);
    }
}
