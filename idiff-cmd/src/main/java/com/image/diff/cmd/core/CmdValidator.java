package com.image.diff.cmd.core;

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
            validateErrors = validateFindMode();
        } else if (diffModeUsed) {
            validateErrors = validateDiffMode();
        }

        if (validateErrors != null && !validateErrors.isEmpty()) {
            errors.addAll(validateErrors);
        }

        return errors;
    }

    private List<ErrorMessage> validateFindMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private List<ErrorMessage> validateDiffMode() {
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
