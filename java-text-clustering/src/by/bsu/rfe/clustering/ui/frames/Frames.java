package by.bsu.rfe.clustering.ui.frames;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

// TODO create builder class for frames
public class Frames {

  private Frames() {
    // do not instantiate utility class
  }

  /**
   * Places the specified frame on the screen center
   * 
   * @throws NullPointerException
   *           if the frame is null
   * @param frame
   *          an instance of {@link JFrame}
   */
  public static void center(JFrame frame) {
    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screenSize = kit.getScreenSize();

    int screenHeight = screenSize.height;
    int screenWidth = screenSize.width;

    int frameLeft = screenWidth / 2 - frame.getWidth() / 2;
    int frameTop = screenHeight / 2 - frame.getHeight() / 2;

    frame.setLocation(frameLeft, frameTop);
  }

}
