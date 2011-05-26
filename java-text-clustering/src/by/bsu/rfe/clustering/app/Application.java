package by.bsu.rfe.clustering.app;

import javax.swing.*;

import by.bsu.rfe.clustering.app.ui.frames.ApplicationFrame;
import by.bsu.rfe.clustering.app.ui.frames.Frames;

/**
 * Created by IntelliJ IDEA.
 * User: Siarhei_Yarashevich
 * Date: 5/25/11
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Application {

  public static void main(String... args) {
    ApplicationFrame frame = new ApplicationFrame();
    Frames.center(frame);

    frame.setVisible(true);
    frame.setTitle("Text Clustering");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

}
