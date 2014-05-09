package com.image.diff.cmd.core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;

public class ValidationContext {

    private final List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
    private CommandLine commandLine;
    private BufferedImage bufferedImage1;
    private BufferedImage bufferedImage2;

    public List<ErrorMessage> getErrors() {
        return errors;
    }

    public void addErrors(List<ErrorMessage> errors) {
        this.errors.addAll(errors);
    }

    public void addError(ErrorMessage error) {
        this.errors.add(error);
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }

    public void setCommandLine(CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    public BufferedImage getBufferedImage1() {
        return bufferedImage1;
    }

    public void setBufferedImage1(BufferedImage bufferedImage1) {
        this.bufferedImage1 = bufferedImage1;
    }

    public BufferedImage getBufferedImage2() {
        return bufferedImage2;
    }

    public void setBufferedImage2(BufferedImage bufferedImage2) {
        this.bufferedImage2 = bufferedImage2;
    }

    @Override
    public String toString() {
        return "errors=" + errors + ", commandLine=" + commandLine + ", bufferedImage1=" + bufferedImage1 + ", bufferedImage2=" + bufferedImage2;
    }
}
