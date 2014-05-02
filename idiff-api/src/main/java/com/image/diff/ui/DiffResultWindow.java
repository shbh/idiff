package com.image.diff.ui;

import com.image.diff.core.Match;
import com.image.diff.core.MatchContext;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiffResultWindow {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final Dimension DEFAULT_SIZE = new Dimension(1024, 768);
    private ImageFrame frame = new ImageFrame();
    private JLabel imageLabel;
    private ImageIcon resultImageIcon;
    private ImageIcon resultSourceImageIcon;
    private AtomicBoolean resultImageSelected = new AtomicBoolean();
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

    public DiffResultWindow(final MatchContext context) {
        this.context = context;
        init();
    }

    private void init() {
        URL resultImageURL;
        try {
            resultImageURL = context.getResultImage().toURI().toURL();
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize window with result image: " + context.getResultImage().getAbsolutePath(), e);
        }

        URL resultSourceImageURL;
        try {
            resultSourceImageURL = context.getResultSourceImage().toURI().toURL();
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize window with result source image: " + context.getResultSourceImage().getAbsolutePath(), e);
        }

        resultImageIcon = new ImageIcon(resultImageURL);
        resultSourceImageIcon = new ImageIcon(resultSourceImageURL);
        imageLabel = new JLabel(resultImageIcon);
        resultImageSelected.set(true);

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
                for (Match m : context.getMatches()) {
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
        imageLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (resultImageSelected.get()) {
                    imageLabel.setIcon(resultSourceImageIcon);
                    resultImageSelected.set(false);
                } else {
                    imageLabel.setIcon(resultImageIcon);
                    resultImageSelected.set(true);
                }
                imageLabel.revalidate();
                imageLabel.repaint();
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }
}
