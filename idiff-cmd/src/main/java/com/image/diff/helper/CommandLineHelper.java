package com.image.diff.helper;

import com.image.diff.core.Defaults;
import com.image.diff.core.MatchContext;
import com.image.diff.core.Roi;
import com.image.diff.core.TemplateMatchMethod;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandLineHelper {

    private Logger logger = LoggerFactory.getLogger(getClass());
    public static final String HELP_OPTION_NAME = "help";
    public static final String SHOW_RESULT_OPTION_NAME = "show-result";
    public static final String TITLE_OPTION_NAME = "title";
    public static final String IMAGE1_OPTION_NAME = "image1";
    public static final String IMAGE2_OPTION_NAME = "image2";
    public static final String RESULT_IMAGE_OPTION_NAME = "result-image";
    public static final String RESULT_SOURCE_IMAGE_OPTION_NAME = "result-source-image";
    public static final String MATCH_SIMILARITY_OPTION_NAME = "match-similarity";
    public static final String LIMIT_OPTION_NAME = "limit";
    public static final String ROIS_OPTION_NAME = "rois";

    public static final String FIND_OPTION_NAME = "find";
    public static final String DIFF_OPTION_NAME = "diff";

    public static final String FIND_DIFF_SAMPLE_OPTION_NAME = "find-diff-sample";
    public static final String FIND_DIFF_ROI_SAMPLE_OPTION_NAME = "find-diff-roi-sample";
    public static final String FIND_TEMPLATE_IN_SOURCE_IMAGE_SAMPLE_OPTION_NAME = "find-template-in-source-image-sample";
    public static final String FIND_TEMPLATE_IN_SOURCE_IMAGE_ROI_SAMPLE_OPTION_NAME = "find-template-in-source-image-roi-sample";

    public static final String ROI_DELIMITER = "\\s*;\\s*";
    public static final String ROI_PARAMS_DELIMITER = "\\s*,\\s*";

    private final String[] commandLineArguments;
    private final Defaults defaults;

    public CommandLineHelper(String[] commandLineArguments, Defaults defaults) {
        this.commandLineArguments = commandLineArguments;
        this.defaults = defaults;
    }

    public MatchContext createContext(String runArg) {
        final CommandLineParser cmdLinePosixParser = new PosixParser();
        final Options posixOptions = buildPosixOptions();
        CommandLine commandLine;
        try {
            commandLine = cmdLinePosixParser.parse(posixOptions, commandLineArguments);
            boolean noArgumentsSpecified = commandLineArguments == null || commandLineArguments.length == 0;
            if (isHelpOptionSpecified(commandLine)) {
                printHelp(runArg, posixOptions);
                // just exit, that's all we need to do in this case ;-)
                System.exit(0);
            }
            if (noArgumentsSpecified) {
                logger.warn("No arguments specified");
                printHelp(runArg, posixOptions);
                // just exit, that's all we need to do in this case ;-)
                System.exit(0);
            }

            if (isFindDiffRoiSampleOptionSpecified(commandLine)) {
                MatchContext matchContext = createFindDiffRoiSampleContext();
                return matchContext;
            }
            if (isFindDiffSampleOptionSpecified(commandLine)) {
                MatchContext matchContext = createFindDiffSampleContext();
                return matchContext;
            }
            if (isFindTemplateInSourceImageRoiSampleOptionSpecified(commandLine)) {
                MatchContext matchContext = createFindTemplateInSourceImageRoiSampleContext();
                return matchContext;
            }
            if (isFindTemplateInSourceImageSampleOptionSpecified(commandLine)) {
                MatchContext matchContext = createFindTemplateInSourceImageSampleContext();
                return matchContext;
            }
            MatchContext matchContext = createContextFromArgs(commandLine);
            return matchContext;
        } catch (ParseException e) {
            logger.error("Encountered exception while parsing using PosixParser", e);
            throw new RuntimeException("Can't initialize context", e);
        }
    }

    private boolean isHelpOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(HELP_OPTION_NAME);
    }

    private boolean isShowResultOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(SHOW_RESULT_OPTION_NAME);
    }

    private boolean isFindOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(FIND_OPTION_NAME);
    }

    private boolean isDiffOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(DIFF_OPTION_NAME);
    }

    private String getTitleOption(CommandLine commandLine) {
        String value = defaults.getTitleValue();
        final Object parsedOptionValue = commandLine.getOptionValue(TITLE_OPTION_NAME);
        if (parsedOptionValue != null) {
            value = String.valueOf(parsedOptionValue);
        }

        return value;
    }

    private String getImage1Option(CommandLine commandLine) {
        String value = "";
        final Object parsedOptionValue = commandLine.getOptionValue(IMAGE1_OPTION_NAME);
        if (parsedOptionValue != null) {
            value = String.valueOf(parsedOptionValue);
        }

        return value;
    }

    private String getImage2Option(CommandLine commandLine) {
        String value = "";
        final Object parsedOptionValue = commandLine.getOptionValue(IMAGE2_OPTION_NAME);
        if (parsedOptionValue != null) {
            value = String.valueOf(parsedOptionValue);
        }

        return value;
    }

    private String getResultImageOption(CommandLine commandLine) {
        String value = defaults.getResultImageValue().getAbsolutePath();
        final Object parsedOptionValue = commandLine.getOptionValue(RESULT_IMAGE_OPTION_NAME);
        if (parsedOptionValue != null) {
            value = String.valueOf(parsedOptionValue);
        }

        return value;
    }

    private String getResultSourceImageOption(CommandLine commandLine) {
        String value = defaults.getResultImageValue().getAbsolutePath();
        final Object parsedOptionValue = commandLine.getOptionValue(RESULT_SOURCE_IMAGE_OPTION_NAME);
        if (parsedOptionValue != null) {
            value = String.valueOf(parsedOptionValue);
        }

        return value;
    }

    private double getMatchSimilarityOption(CommandLine commandLine) {
        Double value = defaults.getMatchSimilarityValue();
        final Object parsedOptionValue = commandLine.getOptionValue(MATCH_SIMILARITY_OPTION_NAME);
        if (parsedOptionValue != null) {
            String valueString = String.valueOf(parsedOptionValue);
            value = Double.valueOf(valueString);
        }

        return value;
    }

    private int getLimitOption(CommandLine commandLine) {
        Integer value = defaults.getLimitValue();
        final Object parsedOptionValue = commandLine.getOptionValue(LIMIT_OPTION_NAME);
        if (parsedOptionValue != null) {
            String valueString = String.valueOf(parsedOptionValue);
            value = Integer.valueOf(valueString);
        }

        return value;
    }

    private List<Roi> getRoisOption(CommandLine commandLine) {
        List<Roi> values = new ArrayList<Roi>();
        final Object parsedOptionValue = commandLine.getOptionValue(ROIS_OPTION_NAME);
        if (parsedOptionValue != null) {
            String valuesString = String.valueOf(parsedOptionValue);
            String[] roiStrings = valuesString.split(ROI_DELIMITER);
            if (roiStrings.length < 1) {
                return values;
            }

            for (String roiString : roiStrings) {
                String[] roiParams = roiString.split(ROI_PARAMS_DELIMITER);
                if (roiParams.length != 4) {
                    logger.warn("Wrong ROI params in: " + roiString + ". Expects 4 params. Actual: " + roiParams.length);
                    continue;
                }

                try {
                    int x = Integer.valueOf(roiParams[0]);
                    int y = Integer.valueOf(roiParams[1]);
                    int width = Integer.valueOf(roiParams[2]);
                    int height = Integer.valueOf(roiParams[3]);

                    Roi roi = new Roi(x, y, width, height);
                    logger.debug("Roi created: {}", roi);
                    values.add(roi);
                } catch (NumberFormatException e) {
                    logger.warn("Can't parse ROI param", e);
                }
            }
        }

        return values;
    }

    private TemplateMatchMethod getMatchMethodOption(CommandLine commandLine) {
        TemplateMatchMethod value = defaults.getMatchMethodValue();

        return value;
    }

    private boolean isFindDiffSampleOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(FIND_DIFF_SAMPLE_OPTION_NAME);
    }

    private boolean isFindDiffRoiSampleOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(FIND_DIFF_ROI_SAMPLE_OPTION_NAME);
    }

    private boolean isFindTemplateInSourceImageSampleOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(FIND_TEMPLATE_IN_SOURCE_IMAGE_SAMPLE_OPTION_NAME);
    }

    private boolean isFindTemplateInSourceImageRoiSampleOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(FIND_TEMPLATE_IN_SOURCE_IMAGE_ROI_SAMPLE_OPTION_NAME);
    }

    private MatchContext createFindDiffRoiSampleContext() {
        MatchContext matchContext = defaults.getFindDiffRoiSampleContext();
        return matchContext;
    }

    private MatchContext createFindDiffSampleContext() {
        MatchContext matchContext = defaults.getFindDiffSampleContext();
        return matchContext;
    }

    private MatchContext createFindTemplateInSourceImageRoiSampleContext() {
        MatchContext matchContext = defaults.getFindTemplateInSourceImageRoiSampleContext();
        return matchContext;
    }

    private MatchContext createFindTemplateInSourceImageSampleContext() {
        MatchContext matchContext = defaults.getFindTemplateInSourceImageSampleContext();
        return matchContext;
    }

    private MatchContext createContextFromArgs(CommandLine commandLine) {
        String image1Option = getImage1Option(commandLine);
        String image2Option = getImage2Option(commandLine);
        int limitOption = getLimitOption(commandLine);
        double matchSimilarityOption = getMatchSimilarityOption(commandLine);

        boolean findSpecified = isFindOptionSpecified(commandLine);
        boolean diffSpecified = isDiffOptionSpecified(commandLine);

        String resultImageOption = getResultImageOption(commandLine);
        String resultSourceImageOption = getResultSourceImageOption(commandLine);
        List<Roi> roisOption = getRoisOption(commandLine);
        String titleOption = getTitleOption(commandLine);
        TemplateMatchMethod matchMethodOption = getMatchMethodOption(commandLine);
        boolean showResultOptionSpecified = isShowResultOptionSpecified(commandLine);

        MatchContext matchContext = new MatchContext();
        matchContext.setImage1(new File(image1Option));
        matchContext.setImage2(new File(image2Option));
        matchContext.setLimit(limitOption);
        matchContext.setMatchSimilarity(matchSimilarityOption);
        matchContext.setDiffSpecified(diffSpecified);
        matchContext.setFindSpecified(findSpecified);
        matchContext.setResultImage(new File(resultImageOption));
        matchContext.setResultSourceImage(new File(resultSourceImageOption));
        if (!roisOption.isEmpty()) {
            matchContext.addRois(roisOption);
        }
        matchContext.setTitle(titleOption);
        matchContext.setMatchMethod(matchMethodOption);
        matchContext.setShowResult(showResultOptionSpecified);

        return matchContext;
    }

    private Options buildPosixOptions() {
        final Options posixOptions = new Options();
        posixOptions.addOption(OptionBuilder.withLongOpt(HELP_OPTION_NAME).
            withDescription("print this message").
            create());
        posixOptions.addOption(OptionBuilder.withLongOpt(SHOW_RESULT_OPTION_NAME).
            withDescription("Show result in UI.").
            withType(Boolean.class).
            create());
        posixOptions.addOption(OptionBuilder.withLongOpt(FIND_OPTION_NAME).
            withDescription("Find the template (second image argument) inside of the image (first image argument).").
            withType(Boolean.class).
            create());
        posixOptions.addOption(OptionBuilder.withLongOpt(DIFF_OPTION_NAME).
            withDescription("Find all differences between two images. Images should have the same size.").
            withType(Boolean.class).
            create());
        posixOptions.addOption(OptionBuilder.withLongOpt(TITLE_OPTION_NAME)
            .withDescription("Set title into GUI frame. Default: " + defaults.getTitleValue())
            .hasArg()
            .withArgName(TITLE_OPTION_NAME)
            .withType(String.class)
            .create());
        posixOptions.addOption(OptionBuilder.withLongOpt(IMAGE1_OPTION_NAME)
            .withDescription("The first image. In case of looking for template this argument used as source image.")
            .hasArg()
            .withArgName(IMAGE1_OPTION_NAME)
            .withType(String.class)
            .create());
        posixOptions.addOption(OptionBuilder.withLongOpt(IMAGE2_OPTION_NAME)
            .withDescription("The second image. In case of looking for template this argument used as template image.")
            .hasArg()
            .withArgName(IMAGE2_OPTION_NAME)
            .withType(String.class)
            .create());
        posixOptions.addOption(OptionBuilder.withLongOpt(RESULT_IMAGE_OPTION_NAME)
            .withDescription("Path to save result of image comparison. Default location: " + defaults.getResultImageValue().getAbsolutePath())
            .hasArg()
            .withArgName(RESULT_IMAGE_OPTION_NAME)
            .withType(String.class)
            .create());
        posixOptions.addOption(OptionBuilder.withLongOpt(RESULT_SOURCE_IMAGE_OPTION_NAME)
            .withDescription("Path to save result of source image comparison. Default location: " + defaults.getResultSourceImageValue().getAbsolutePath())
            .hasArg()
            .withArgName(RESULT_SOURCE_IMAGE_OPTION_NAME)
            .withType(String.class)
            .create());
        posixOptions.addOption(OptionBuilder.withLongOpt(MATCH_SIMILARITY_OPTION_NAME)
            .withDescription("Use match similarity to search for. Default value: " + defaults.getMatchSimilarityValue())
            .hasArg()
            .withArgName(MATCH_SIMILARITY_OPTION_NAME)
            .withType(Number.class)
            .create());
        posixOptions.addOption(OptionBuilder.withLongOpt(LIMIT_OPTION_NAME)
            .withDescription("Use limit to limit number of found blocks. Default value: " + defaults.getLimitValue())
            .hasArg()
            .withArgName(LIMIT_OPTION_NAME)
            .withType(Number.class)
            .create());
        posixOptions.addOption(OptionBuilder.withLongOpt(ROIS_OPTION_NAME)
            .withDescription("Use region of interests as x1,y1,w1,h1;x2,y2,w2,h2; to search for any differences/templates. "
                + "For example: 247,137,325,35;1,111,155,155 - describes x,y,width,height for two regions. "
                + "First - x:247,y:137,width:325,height:35; Second - x:1,y:111,width:155,height:155")
            .hasArg()
            .withArgName(ROIS_OPTION_NAME)
            .withType(String.class)
            .create());
        posixOptions.addOption(OptionBuilder.withLongOpt(FIND_DIFF_SAMPLE_OPTION_NAME).
            withDescription("Find difference with the same bounds sample.").
            create());
        posixOptions.addOption(OptionBuilder.withLongOpt(FIND_DIFF_ROI_SAMPLE_OPTION_NAME).
            withDescription("Find difference with the same bounds inside of predefined region of interests sample.").
            create());
        posixOptions.addOption(OptionBuilder.withLongOpt(FIND_TEMPLATE_IN_SOURCE_IMAGE_SAMPLE_OPTION_NAME).
            withDescription("Find the template in the source image sample.").
            create());
        posixOptions.addOption(OptionBuilder.withLongOpt(FIND_TEMPLATE_IN_SOURCE_IMAGE_ROI_SAMPLE_OPTION_NAME).
            withDescription("Find the template in the source image inside of predefined region of interests sample.").
            create());

        return posixOptions;
    }

    private void printHelp(final String applicationName, final Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(applicationName, options);
    }
}
