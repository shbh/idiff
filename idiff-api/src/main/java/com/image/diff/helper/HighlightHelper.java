package com.image.diff.helper;

import com.image.diff.core.Match;
import com.image.diff.visual.HighlightElement;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HighlightHelper {

    public String getHighlightElementText(Match match) {
        double score = match.getScore();
        int roundedScore = (int) (score * 100);

        return roundedScore + "%";
    }

    public void highlightElement(File image, HighlightElement element) throws IOException {
        BufferedImage canvas = ImageIO.read(image);

        Graphics2D g2 = canvas.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        // border
        g2.setColor(element.getBorderColor());
        g2.drawRect(element.getX(), element.getY(), element.getWidth(), element.getHeight());

        // area
        g2.setColor(element.getAreaColor());
        g2.fillRect(element.getX(), element.getY(), element.getWidth(), element.getHeight());

        // text
        if (element.getText() != null && !element.getText().isEmpty()) {
            g2.setColor(element.getFontColor());
            int centerX = element.getX() + 1;
            int centerY = element.getY() + element.getHeight() / 2;
            g2.setFont(element.getFont());
            g2.drawString(element.getText(), centerX, centerY);
        }

        g2.dispose();

        // update result image with highlights
        ImageIO.write(canvas, "PNG", image);
    }
}
