package com.image.diff.cmd;

import com.image.diff.cmd.core.CmdContextCreator;
import com.image.diff.core.Match;
import com.image.diff.core.MatchContext;
import com.image.diff.helper.Finder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunApp {

    private static Logger logger = LoggerFactory.getLogger(RunApp.class);

    public static void main(String[] args) throws Exception {
        final String runArg = "java -jar idiff-1.0.jar";

//        args = new String[]{"--image1", "samples/sourceImage.jpg", "--image2", "samples/templateImage.jpg", "--help"};
        new RunApp().run(runArg, args);
    }

    public void run(String runArg, String[] cmdArgs) throws Exception {
        CmdContextCreator creator = new CmdContextCreator.Builder(runArg, cmdArgs).build();
        MatchContext matchContext = creator.create();
        Finder finder = new Finder.Builder(matchContext).build();
        List<Match> matches = finder.find();
        logger.info("Found: {}", matches);
    }
}
