package by.bsu.rfe.clustering.app.ui.frames;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBarDialog extends JDialog {

  private static final long serialVersionUID = 1289693714614765691L;

  private final JPanel _panel = new JPanel();

  private final JLabel _messageLabel = new JLabel();

  private final JProgressBar _progressBar = new JProgressBar();

  public ProgressBarDialog(JFrame parent) {
    this(parent, "Please wait...");
  }

  public ProgressBarDialog(JFrame parent, String text) {
    super(parent, true);
    _messageLabel.setText(text);

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    initLayout();
  }

  public JProgressBar getProgressBar() {
    return _progressBar;
  }

  private void initLayout() {
    _panel.setLayout(new FlowLayout(FlowLayout.CENTER));
    _panel.add(_messageLabel);
    _panel.add(_progressBar);

    _progressBar.setIndeterminate(true);

    getContentPane().setLayout(new BorderLayout());
    add(_panel, BorderLayout.CENTER);
  }

}
