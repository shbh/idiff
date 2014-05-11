package com.image.diff.ui;

import com.image.diff.core.Match;
import com.image.diff.core.MatchContext;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import org.apache.commons.lang3.Validate;

public class FindResultWindow {

    private final Dimension DEFAULT_SIZE = new Dimension(1024, 768);
    private ImageFrame frame = new ImageFrame();
    private JLabel imageLabel;
    private ImageIcon imageIcon;
    private MatchContext context;

    private class ImageFrame extends JFrame {

        private ImageFrame() {
            getContentPane().setLayout(new FlowLayout());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(DEFAULT_SIZE);
            setResizable(false);
            setLocationRelativeTo(null);
        }
    }

    public FindResultWindow(MatchContext context, List<Match> matchResults) {
        Validate.notNull(context, "Context must not be null");
        this.context = context;
        postInit(matchResults);
    }

    private void postInit(final List<Match> matchResults) {
        URL resultImageURL;
        try {
            resultImageURL = context.getResultImage().toURI().toURL();
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize window with result image: " + context.getResultImage().getAbsolutePath(), e);
        }

        imageIcon = new ImageIcon(resultImageURL);
        imageLabel = new JLabel(imageIcon);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(imageLabel);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle(context.getTitle());
        frame.pack();
        imageLabel.addMouseMotionListener(new MouseAdapter() {
            private final int defaultDismissTimeout = ToolTipManager.sharedInstance().getDismissDelay();

            @Override
            public void mouseMoved(MouseEvent e) {
                boolean foundToolTipArea = false;
                for (Match m : matchResults) {
                    if (m.contains(e.getX(), e.getY())) {
                        imageLabel.setToolTipText("" + m);
                        imageLabel.revalidate();
                        imageLabel.repaint();
                        foundToolTipArea = true;
                        break;
                    }
                }
                if (foundToolTipArea) {
                    ToolTipManager.sharedInstance().setDismissDelay(60000);
                } else {
                    ToolTipManager.sharedInstance().setDismissDelay(defaultDismissTimeout);
                }
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }
}
