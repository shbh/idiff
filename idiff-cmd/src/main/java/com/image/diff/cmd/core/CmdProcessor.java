package com.image.diff.cmd.core;

import com.image.diff.core.MatchContext;
import com.image.diff.core.Roi;
import com.image.diff.core.TemplateMatchMethod;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdProcessor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final String[] cmdArgs;
    private CmdCreator cmdCreator;
    private Defaults defaults;

    private CmdProcessor(String[] cmdArgs) {
        this.cmdArgs = cmdArgs;
    }

    MatchContext createContext(String runArg) {
        try {
            Options options = cmdCreator.getOptions();
            CommandLine commandLine = cmdCreator.getCommandLine(options, cmdArgs);
            boolean noArgumentsSpecified = cmdArgs == null || cmdArgs.length == 0;
            if (isHelpOptionSpecified(commandLine)) {
                printHelp(runArg, options);
                // just exit, that's all we need to do in this case ;-)
                System.exit(0);
            }
            if (noArgumentsSpecified) {
                logger.warn("No arguments specified");
                printHelp(runArg, options);
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
        return commandLine.hasOption(defaults.getHelpOptionName());
    }

    private boolean isShowResultOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(defaults.getShowResultOptionName());
    }

    private boolean isFindOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(defaults.getFindOptionName());
    }

    private boolean isDiffOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(defaults.getDiffOptionName());
    }

    private String getTitleOption(CommandLine commandLine) {
        String value = defaults.getTitleValue();
        final Object parsedOptionValue = commandLine.getOptionValue(defaults.getTitleOptionName());
        if (parsedOptionValue != null) {
            value = String.valueOf(parsedOptionValue);
        }

        return value;
    }

    private String getImage1Option(CommandLine commandLine) {
        String value = "";
        final Object parsedOptionValue = commandLine.getOptionValue(defaults.getImage1OptionName());
        if (parsedOptionValue != null) {
            value = String.valueOf(parsedOptionValue);
        }

        return value;
    }

    private String getImage2Option(CommandLine commandLine) {
        String value = "";
        final Object parsedOptionValue = commandLine.getOptionValue(defaults.getImage2OptionName());
        if (parsedOptionValue != null) {
            value = String.valueOf(parsedOptionValue);
        }

        return value;
    }

    private String getResultImageOption(CommandLine commandLine) {
        String value = defaults.getResultImageValue().getAbsolutePath();
        final Object parsedOptionValue = commandLine.getOptionValue(defaults.getResultImageOptionName());
        if (parsedOptionValue != null) {
            value = String.valueOf(parsedOptionValue);
        }

        return value;
    }

    private String getResultSourceImageOption(CommandLine commandLine) {
        String value = defaults.getResultImageValue().getAbsolutePath();
        final Object parsedOptionValue = commandLine.getOptionValue(defaults.getResultSourceImageOptionName());
        if (parsedOptionValue != null) {
            value = String.valueOf(parsedOptionValue);
        }

        return value;
    }

    private double getMatchSimilarityOption(CommandLine commandLine) {
        Double value = defaults.getMatchSimilarityValue();
        final Object parsedOptionValue = commandLine.getOptionValue(defaults.getMatchSimilarityOptionName());
        if (parsedOptionValue != null) {
            String valueString = String.valueOf(parsedOptionValue);
            value = Double.valueOf(valueString);
        }

        return value;
    }

    private int getLimitOption(CommandLine commandLine) {
        Integer value = defaults.getLimitValue();
        final Object parsedOptionValue = commandLine.getOptionValue(defaults.getLimitOptionName());
        if (parsedOptionValue != null) {
            String valueString = String.valueOf(parsedOptionValue);
            value = Integer.valueOf(valueString);
        }

        return value;
    }

    private List<Roi> getRoisOption(CommandLine commandLine) {
        List<Roi> values = new ArrayList<Roi>();
        final Object parsedOptionValue = commandLine.getOptionValue(defaults.getRoisOptionName());
        if (parsedOptionValue != null) {
            String valuesString = String.valueOf(parsedOptionValue);
            String[] roiStrings = valuesString.split(defaults.getRoiDelimiter());
            if (roiStrings.length < 1) {
                return values;
            }

            for (String roiString : roiStrings) {
                String[] roiParams = roiString.split(defaults.getRoiParamsDelimiter());
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
        return commandLine.hasOption(defaults.getFindDiffSampleOptionName());
    }

    private boolean isFindDiffRoiSampleOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(defaults.getFindDiffRoiSampleOptionName());
    }

    private boolean isFindTemplateInSourceImageSampleOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(defaults.getFindTemplateInSourceImageSampleOptionName());
    }

    private boolean isFindTemplateInSourceImageRoiSampleOptionSpecified(CommandLine commandLine) {
        return commandLine.hasOption(defaults.getFindTemplateInSourceImageRoiSampleOptionName());
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

    private void printHelp(final String applicationName, final Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(applicationName, options);
    }

    public static class Builder {

        private Logger logger = LoggerFactory.getLogger(getClass());

        private final String[] cmdArgs;
        private CmdCreator cmdCreator;
        private Defaults defaults;

        public Builder(String[] cmdArgs) {
            this.cmdArgs = cmdArgs;
        }

        public Builder cmdCreator(CmdCreator cmdCreator) {
            this.cmdCreator = cmdCreator;
            return this;
        }

        public Builder defaults(Defaults defaults) {
            this.defaults = defaults;

            return this;
        }

        public CmdProcessor build() {
            CmdProcessor processor = new CmdProcessor(cmdArgs);

            if (defaults == null) {
                logger.debug("Default instance will be used for defaults values");
                defaults = new Defaults();
            }
            if (cmdCreator == null) {
                logger.debug("Default instance will be used for cmd creator");
                cmdCreator = new CmdCreator.Builder().defaults(defaults).build();
            }
            processor.defaults = defaults;
            processor.cmdCreator = cmdCreator;

            return processor;
        }
    }
}
