package com.image.diff.cmd.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdValidator {

    private final String[] cmdArgs;
    private CmdCreator cmdCreator;
    private Defaults defaults;

    private CmdValidator(String[] cmdArgs) {
        this.cmdArgs = cmdArgs;
    }

    List<ErrorMessage> validate() {
        final List<ErrorMessage> errors = new ArrayList<ErrorMessage>();

        CommandLine commandLine;
        try {
            Options options = cmdCreator.getOptions();
            commandLine = cmdCreator.getCommandLine(options, cmdArgs);
        } catch (ParseException ex) {
            errors.add(new ErrorMessage.Builder().message("Error happened during parsing of a command-line arguments").build());
            return errors;
        }

        boolean findModeUsed = commandLine.hasOption(defaults.getFindOptionName());
        boolean diffModeUsed = commandLine.hasOption(defaults.getDiffOptionName());

        boolean notOneOptionChosen = (!findModeUsed && !diffModeUsed) || (findModeUsed && diffModeUsed);

        if (notOneOptionChosen) {
            errors.add(new ErrorMessage.Builder().message("Find or Diff mode should be used").build());
            return errors;
        }

        List<ErrorMessage> validateErrors = null;
        if (findModeUsed) {
            validateErrors = validateFindMode(commandLine);
        } else if (diffModeUsed) {
            validateErrors = validateDiffMode(commandLine);
        }

        if (validateErrors != null && !validateErrors.isEmpty()) {
            errors.addAll(validateErrors);
        }

        return errors;
    }

    private List<ErrorMessage> validateFindMode(CommandLine commandLine) {
        final List<ErrorMessage> errors = new ArrayList<ErrorMessage>();

        boolean image1Passed = commandLine.hasOption(defaults.getImage1OptionName());
        if (!image1Passed) {
            errors.add(new ErrorMessage.Builder().message("First image (the source) argument expected").build());
        }

        if (image1Passed) {
            File image1File = new File(commandLine.getOptionValue(defaults.getImage1OptionName()));
            if (!image1File.exists()) {
                errors.add(new ErrorMessage.Builder().message("First image (the source) doesn't exists. Please make sure you provide correct path").build());
            }
        }

        boolean image2Passed = commandLine.hasOption(defaults.getImage2OptionName());
        if (!image2Passed) {
            errors.add(new ErrorMessage.Builder().message("Second image (the template) argument expected").build());
        }

        if (image2Passed) {
            File image2File = new File(commandLine.getOptionValue(defaults.getImage2OptionName()));
            if (!image2File.exists()) {
                errors.add(new ErrorMessage.Builder().message("Second image (the template) doesn't exists. Please make sure you provide correct path").build());
            }
        }

        boolean resultImagePassed = commandLine.hasOption(defaults.getResultImageOptionName());
        if (!resultImagePassed) {
            errors.add(new ErrorMessage.Builder().message("Result image argument expected").build());
        }

        return errors;
    }

    private List<ErrorMessage> validateDiffMode(CommandLine commandLine) {
        throw new UnsupportedOperationException("Not supported yet.");
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
