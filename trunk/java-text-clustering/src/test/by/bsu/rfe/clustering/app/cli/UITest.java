package test.by.bsu.rfe.clustering.app.cli;

import javax.swing.JFrame;

import by.bsu.rfe.clustering.ui.frames.TestKMeansFrame;

public class UITest {

    public static void main(String[] args) {
        TestKMeansFrame frame = new TestKMeansFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
