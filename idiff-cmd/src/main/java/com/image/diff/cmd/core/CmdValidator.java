package com.image.diff.cmd.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdValidator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final String[] cmdArgs;
    private CmdCreator cmdCreator;
    private Defaults defaults;

    private CmdValidator(String[] cmdArgs) {
        this.cmdArgs = cmdArgs;
    }

    List<ErrorMessage> validate() {
        final List<ErrorMessage> errors = new ArrayList<ErrorMessage>();

        boolean hasAnyArgument = (cmdArgs != null && cmdArgs.length > 0);
        if (!hasAnyArgument) {
            // nothing to validate, just show a help
            return errors;
        }

        CommandLine commandLine;
        try {
            Options options = cmdCreator.getOptions();
            commandLine = cmdCreator.getCommandLine(options, cmdArgs);
        } catch (ParseException ex) {
            String message = "Error happened during parsing of a command-line arguments";
            logger.debug(message, ex);
            errors.add(new ErrorMessage.Builder().message(message).build());
            return errors;
        }

        boolean helpArgument = isHelpArgument(commandLine);
        if (helpArgument) {
            // nothing to validate, just show a help
            return errors;
        }

        boolean sampleArgument = isSampleArgument(commandLine);
        if (sampleArgument) {
            // nothing to validate, presume that samples configured properly
            return errors;
        }

        boolean findModeUsed = commandLine.hasOption(defaults.getFindOptionName());
        boolean diffModeUsed = commandLine.hasOption(defaults.getDiffOptionName());

        boolean notOneOptionChosen = (!findModeUsed && !diffModeUsed) || (findModeUsed && diffModeUsed);

        if (notOneOptionChosen) {
            errors.add(new ErrorMessage.Builder().message("Find or Diff mode should be used").build());
            return errors;
        }

        boolean matchSimilarityUsed = commandLine.hasOption(defaults.getMatchSimilarityOptionName());
        if (matchSimilarityUsed) {
            String matchSimilarityString = commandLine.getOptionValue(defaults.getMatchSimilarityOptionName());
            try {
                double matchSimilarity = Double.valueOf(matchSimilarityString);

                if (matchSimilarity <= 0 && matchSimilarity >= 1) {
                    errors.add(new ErrorMessage.Builder().message("Expected matching similarity should be from 0..1 (both exclusive). Passed: " + matchSimilarity).build());
                }
            } catch (NumberFormatException ex) {
                String message = "Match similarity value does not have the appropriate format: " + matchSimilarityString;
                logger.debug(message, ex);
                errors.add(new ErrorMessage.Builder().message(message).build());
            }
        }

        boolean limitUsed = commandLine.hasOption(defaults.getLimitOptionName());
        if (limitUsed) {
            String limitString = commandLine.getOptionValue(defaults.getLimitOptionName());
            try {
                int limit = Integer.valueOf(limitString);

                if (limit <= 0) {
                    errors.add(new ErrorMessage.Builder().message("Expected limit should be greater than 0. Passed: " + limit).build());
                }
            } catch (NumberFormatException ex) {
                String message = "Limit value does not have the appropriate format: " + limitString;
                logger.debug(message, ex);
                errors.add(new ErrorMessage.Builder().message(message).build());
            }
        }

        final ValidationContext context = new ValidationContext();
        context.setCommandLine(commandLine);
        if (!errors.isEmpty()) {
            context.addErrors(errors);
        }

        if (findModeUsed) {
            validateFindMode(context);
        } else if (diffModeUsed) {
            validateDiffMode(context);
        }

        return context.getErrors();
    }

    private void validateFindMode(ValidationContext context) {
        CommandLine commandLine = context.getCommandLine();

        boolean image1Passed = commandLine.hasOption(defaults.getImage1OptionName());
        if (!image1Passed) {
            context.addError(new ErrorMessage.Builder().message("First image (the source) argument expected").build());
        }

        validateFirstImage(image1Passed, context);
        BufferedImage bufferedImage1 = context.getBufferedImage1();

        boolean image2Passed = commandLine.hasOption(defaults.getImage2OptionName());
        if (!image2Passed) {
            context.addError(new ErrorMessage.Builder().message("Second image (the template) argument expected").build());
        }

        validateSecondImage(image2Passed, context);
        BufferedImage bufferedImage2 = context.getBufferedImage2();

        if (bufferedImage1 != null && bufferedImage2 != null) {
            if (bufferedImage1.getHeight() < bufferedImage2.getHeight()
                || bufferedImage1.getWidth() < bufferedImage2.getWidth()) {
                context.addError(new ErrorMessage.Builder().message("First image (the source) should be greater than second image (the template)").build());
            }
        }
    }

    private void validateDiffMode(ValidationContext context) {
        CommandLine commandLine = context.getCommandLine();

        boolean image1Passed = commandLine.hasOption(defaults.getImage1OptionName());
        if (!image1Passed) {
            context.addError(new ErrorMessage.Builder().message("First image argument expected").build());
        }

        validateFirstImage(image1Passed, context);
        BufferedImage bufferedImage1 = context.getBufferedImage1();

        boolean image2Passed = commandLine.hasOption(defaults.getImage2OptionName());
        if (!image2Passed) {
            context.addError(new ErrorMessage.Builder().message("Second image argument expected").build());
        }

        validateSecondImage(image2Passed, context);
        BufferedImage bufferedImage2 = context.getBufferedImage2();

        if (bufferedImage1 != null && bufferedImage2 != null) {
            if (bufferedImage1.getHeight() != bufferedImage2.getHeight()
                || bufferedImage1.getWidth() != bufferedImage2.getWidth()) {
                context.addError(new ErrorMessage.Builder().message("First image size should be equals to second image size").build());
            }
        }
    }

    private void validateFirstImage(boolean image1Passed, ValidationContext context) {
        CommandLine commandLine = context.getCommandLine();
        BufferedImage bufferedImage1 = null;

        if (image1Passed) {
            File image1File = new File(commandLine.getOptionValue(defaults.getImage1OptionName()));
            if (!image1File.exists()) {
                context.addError(new ErrorMessage.Builder().message("First image (the source) doesn't exists. Please make sure you provide correct path").build());
            } else {
                try {
                    bufferedImage1 = ImageIO.read(image1File);
                } catch (IOException ex) {
                    String message = "Could not read first image (the source): " + image1File.getAbsolutePath();
                    logger.debug(message, ex);
                    context.addError(new ErrorMessage.Builder().message(message).build());
                }
            }
        }

        context.setBufferedImage1(bufferedImage1);
    }

    private void validateSecondImage(boolean image2Passed, ValidationContext context) {
        CommandLine commandLine = context.getCommandLine();
        BufferedImage bufferedImage2 = null;

        if (image2Passed) {
            File image2File = new File(commandLine.getOptionValue(defaults.getImage2OptionName()));
            if (!image2File.exists()) {
                context.addError(new ErrorMessage.Builder().message("Second image (the template) doesn't exists. Please make sure you provide correct path").build());
            } else {
                try {
                    bufferedImage2 = ImageIO.read(image2File);
                } catch (IOException ex) {
                    String message = "Could not read second image (the template): " + image2File.getAbsolutePath();
                    logger.debug(message, ex);
                    context.addError(new ErrorMessage.Builder().message(message).build());
                }
            }
        }

        context.setBufferedImage2(bufferedImage2);
    }

    private boolean isHelpArgument(CommandLine commandLine) {
        return commandLine.hasOption(defaults.getHelpOptionName());
    }

    private boolean isSampleArgument(CommandLine commandLine) {
        return commandLine.hasOption(defaults.getFindDiffSampleOptionName())
            || commandLine.hasOption(defaults.getFindDiffRoiSampleOptionName())
            || commandLine.hasOption(defaults.getFindTemplateInSourceImageSampleOptionName())
            || commandLine.hasOption(defaults.getFindTemplateInSourceImageRoiSampleOptionName());
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

        public CmdValidator build() {
            CmdValidator validator = new CmdValidator(cmdArgs);

            if (defaults == null) {
                logger.debug("Default instance will be used for defaults values");
                defaults = new Defaults();
            }
            if (cmdCreator == null) {
                logger.debug("Default instance will be used for cmd creator");
                cmdCreator = new CmdCreator.Builder().defaults(defaults).build();
            }
            validator.defaults = defaults;
            validator.cmdCreator = cmdCreator;

            return validator;
        }
    }
}
