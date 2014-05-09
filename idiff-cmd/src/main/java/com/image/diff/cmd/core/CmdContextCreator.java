package com.image.diff.cmd.core;

import com.image.diff.core.MatchContext;

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

        public Builder(String runArg, String[] cmdArgs) {
            this.runArg = runArg;
            this.cmdArgs = cmdArgs;
        }

        public Builder cmdProcessor(CmdProcessor cmdProcessor) {
            this.cmdProcessor = cmdProcessor;
            return this;
        }

        public Builder cmdValidator(CmdValidator cmdValidator) {
            this.cmdValidator = cmdValidator;
            return this;
        }

        public CmdContextCreator build() {
            if (cmdProcessor == null) {
                cmdProcessor = new CmdProcessor.Builder(cmdArgs).build();
            }
            if (cmdValidator == null) {
                cmdValidator = new CmdValidator.Builder(cmdArgs).build();
            }

            // validate application arguments
            cmdValidator.validate();

            CmdContextCreator creator = new CmdContextCreator();
            creator.runArg = runArg;
            creator.cmdProcessor = cmdProcessor;
            return creator;
        }
    }
}
