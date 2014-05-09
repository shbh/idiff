package com.image.diff.cmd.core;

import com.image.diff.core.MatchContext;
import java.util.List;

public class CmdContextCreator {

    private String runArg;
    private CmdProcessor cmdProcessor;

    private CmdContextCreator() {
    }

    public MatchContext create() {
        MatchContext context = cmdProcessor.createContext(runArg);
        return context;
    }

    public static class Builder {

        private final String runArg;
        private final String[] cmdArgs;
        private CmdProcessor cmdProcessor;
        private CmdValidator cmdValidator;
        private CmdCreator cmdCreator;
        private Defaults defaults;

        public Builder(String runArg, String[] cmdArgs) {
            this.runArg = runArg;
            this.cmdArgs = cmdArgs;
        }

        public Builder defaults(Defaults defaults) {
            this.defaults = defaults;

            return this;
        }

        public Builder cmdProcessor(CmdProcessor cmdProcessor) {
            this.cmdProcessor = cmdProcessor;
            return this;
        }

        public Builder cmdValidator(CmdValidator cmdValidator) {
            this.cmdValidator = cmdValidator;
            return this;
        }

        public Builder cmdCreator(CmdCreator cmdCreator) {
            this.cmdCreator = cmdCreator;
            return this;
        }

        public CmdContextCreator build() {
            if (cmdProcessor == null) {
                cmdProcessor = new CmdProcessor.Builder(cmdArgs).
                    defaults(defaults).
                    cmdCreator(cmdCreator).
                    build();
            }
            if (cmdValidator == null) {
                cmdValidator = new CmdValidator.Builder(cmdArgs).
                    defaults(defaults).
                    cmdCreator(cmdCreator).
                    build();
            }

            // validate application arguments
            List<ErrorMessage> errorMessages = cmdValidator.validate();
            if (!errorMessages.isEmpty()) {
                String error = buildErrorMessage(errorMessages);
                throw new RuntimeException(error);
            }

            CmdContextCreator creator = new CmdContextCreator();
            creator.runArg = runArg;
            creator.cmdProcessor = cmdProcessor;
            return creator;
        }

        private String buildErrorMessage(List<ErrorMessage> errorMessages) {
            StringBuilder sb = new StringBuilder();
            for (ErrorMessage errorMessage : errorMessages) {
                sb.append(" --- ");
                sb.append(errorMessage.getMessage());
                sb.append("\n");
            }

            return sb.toString();
        }
    }
}
