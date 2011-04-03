package by.bsu.rfe.clustering.ui.frames;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class ApplicationFrame extends JFrame {

    private static final long serialVersionUID = -3331823388635050038L;

    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;

    private final JSplitPane _mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    public ApplicationFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        Frames.center(this);
        init();
    }

    private void init() {
        getContentPane().add(_mainPanel);
    }

}
