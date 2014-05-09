package com.image.diff;

import com.image.diff.core.Defaults;
import com.image.diff.core.Match;
import com.image.diff.core.MatchContext;
import com.image.diff.helper.CommandLineHelper;
import com.image.diff.helper.Finder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunApp {

    private static Logger logger = LoggerFactory.getLogger(RunApp.class);

    public static void main(String[] args) throws Exception {
        final String runArg = "java -jar idiff-1.0.jar";

//        args = new String[]{"--image1", "samples/sourceImage.jpg", "--image2", "samples/templateImage.jpg"};
        new RunApp().run(args, runArg);
    }

    public void run(String[] commandLineArguments, String runArg) throws Exception {
        CommandLineHelper commandLineHelper = new CommandLineHelper(commandLineArguments, new Defaults());
        MatchContext matchContext = commandLineHelper.createContext(runArg);
        Finder finder = new Finder.Builder(matchContext).build();
        List<Match> matches = finder.find();
        logger.info("Found: {}", matches);
    }
}
