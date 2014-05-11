package com.image.diff.cmd.core;

import com.image.diff.cmd.ImagesContainer;
import com.image.diff.core.MatchContext;
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
public class CmdContextCreatorMockTest {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static ImagesContainer imagesContainer;

    @Mock
    private CmdProcessor cmdProcessor;

    @BeforeClass
    public static void beforeTests() {
        imagesContainer = new ImagesContainer();
    }

    @Test
    public void testCreateContextForDiffCase() {
        // given
        MatchContext assertContext = new MatchContext.Builder().
            image1(imagesContainer.getDiffImage1File()).
            image2(imagesContainer.getDiffImage2File()).
            build();
        String runArg = "test";
        String[] cmdArgs = new String[]{};
        CmdContextCreator creator = new CmdContextCreator.Builder(runArg, cmdArgs).cmdProcessor(cmdProcessor).build();
        when(creator.create()).thenReturn(assertContext);

        // when
        MatchContext context = creator.create();

        // then
        assertThat(context, Matchers.equalTo(assertContext));
        verify(cmdProcessor, times(1)).createContext(runArg);
    }
}
