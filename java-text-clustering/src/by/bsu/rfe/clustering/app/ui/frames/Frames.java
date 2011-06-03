package by.bsu.rfe.clustering.app.ui.frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

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
   * @param window
   *          an instance of {@link JFrame}
   */
  public static void center(Window window) {
    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screenSize = kit.getScreenSize();

    int screenHeight = screenSize.height;
    int screenWidth = screenSize.width;

    int frameLeft = screenWidth / 2 - window.getWidth() / 2;
    int frameTop = screenHeight / 2 - window.getHeight() / 2;

    window.setLocation(frameLeft, frameTop);
  }

}
