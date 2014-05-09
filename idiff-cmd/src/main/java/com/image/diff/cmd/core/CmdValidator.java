package com.image.diff.cmd.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdValidator {

    private final String[] cmdArgs;
    private Defaults defaults;

    private CmdValidator(String[] cmdArgs) {
        this.cmdArgs = cmdArgs;
    }

    void validate() {
        throw new UnsupportedOperationException();
    }

    public static class Builder {

        private Logger logger = LoggerFactory.getLogger(getClass());

        private final String[] cmdArgs;
        private Defaults defaults;

        public Builder(String[] cmdArgs) {
            this.cmdArgs = cmdArgs;
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
            validator.defaults = defaults;

            return validator;
        }
    }
}
