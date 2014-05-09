package com.image.diff.cmd.core;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdCreator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Defaults defaults;

    private CmdCreator() {
    }

    public Options getOptions() {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt(defaults.getHelpOptionName()).
            withDescription("print this message").
            create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getShowResultOptionName()).
            withDescription("Show result in UI.").
            withType(Boolean.class).
            create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getFindOptionName()).
            withDescription("Find the template (second image argument) inside of the image (first image argument).").
            withType(Boolean.class).
            create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getDiffOptionName()).
            withDescription("Find all differences between two images. Images should have the same size.").
            withType(Boolean.class).
            create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getTitleOptionName())
            .withDescription("Set title into GUI frame. Default: " + defaults.getTitleValue())
            .hasArg()
            .withArgName(defaults.getTitleOptionName())
            .withType(String.class)
            .create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getImage1OptionName())
            .withDescription("The first image. In case of looking for template this argument used as source image.")
            .hasArg()
            .withArgName(defaults.getImage1OptionName())
            .withType(String.class)
            .create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getImage2OptionName())
            .withDescription("The second image. In case of looking for template this argument used as template image.")
            .hasArg()
            .withArgName(defaults.getImage2OptionName())
            .withType(String.class)
            .create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getResultImageOptionName())
            .withDescription("Path to save result of image comparison. Default location: " + defaults.getResultImageValue().getAbsolutePath())
            .hasArg()
            .withArgName(defaults.getResultImageOptionName())
            .withType(String.class)
            .create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getResultSourceImageOptionName())
            .withDescription("Path to save result of source image comparison. Default location: " + defaults.getResultSourceImageValue().getAbsolutePath())
            .hasArg()
            .withArgName(defaults.getResultSourceImageOptionName())
            .withType(String.class)
            .create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getMatchSimilarityOptionName())
            .withDescription("Use match similarity to search for. Default value: " + defaults.getMatchSimilarityValue())
            .hasArg()
            .withArgName(defaults.getMatchSimilarityOptionName())
            .withType(Number.class)
            .create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getLimitOptionName())
            .withDescription("Use limit to limit number of found blocks. Default value: " + defaults.getLimitValue())
            .hasArg()
            .withArgName(defaults.getLimitOptionName())
            .withType(Number.class)
            .create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getRoisOptionName())
            .withDescription("Use region of interests as x1,y1,w1,h1;x2,y2,w2,h2; to search for any differences/templates. "
                + "For example: 247,137,325,35;1,111,155,155 - describes x,y,width,height for two regions. "
                + "First - x:247,y:137,width:325,height:35; Second - x:1,y:111,width:155,height:155")
            .hasArg()
            .withArgName(defaults.getRoisOptionName())
            .withType(String.class)
            .create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getFindDiffSampleOptionName()).
            withDescription("Find difference with the same bounds sample.").
            create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getFindDiffRoiSampleOptionName()).
            withDescription("Find difference with the same bounds inside of predefined region of interests sample.").
            create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getFindTemplateInSourceImageSampleOptionName()).
            withDescription("Find the template in the source image sample.").
            create());
        options.addOption(OptionBuilder.withLongOpt(defaults.getFindTemplateInSourceImageRoiSampleOptionName()).
            withDescription("Find the template in the source image inside of predefined region of interests sample.").
            create());

        return options;
    }

    public CommandLine getCommandLine(Options options, String[] cmdArgs) throws ParseException {
        final CommandLineParser cmdLinePosixParser = new PosixParser();
        CommandLine commandLine = cmdLinePosixParser.parse(options, cmdArgs);

        return commandLine;
    }

    public static class Builder {

        private Logger logger = LoggerFactory.getLogger(getClass());

        private Defaults defaults;

        public Builder() {
        }

        public Builder defaults(Defaults defaults) {
            this.defaults = defaults;

            return this;
        }

        public CmdCreator build() {
            CmdCreator creator = new CmdCreator();

            if (defaults == null) {
                logger.debug("Default instance will be used for defaults values");
                defaults = new Defaults();
            }
            creator.defaults = defaults;

            return creator;
        }
    }
}
