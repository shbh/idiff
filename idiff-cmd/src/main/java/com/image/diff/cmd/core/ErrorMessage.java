package com.image.diff.cmd.core;

import org.apache.commons.lang3.Validate;

public class ErrorMessage {

    private String message;

    private ErrorMessage() {
    }

    public String getMessage() {
        return message;
    }

    public static class Builder {

        private String message;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorMessage build() {
            Validate.notNull(message, "Message must not be null");

            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.message = message;

            return errorMessage;
        }
    }
}
