package by.bsu.rfe.clustering.app.ui.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import by.bsu.rfe.clustering.app.InputCallback;
import by.bsu.rfe.clustering.app.InputValidator;

public class ValidatableInputDialog extends JDialog implements ActionListener, PropertyChangeListener {

  private static final long serialVersionUID = -1923253828313211583L;

  private String _typedText;
  private JTextField _textField;

  private String _btnOK = "OK";
  private String _btnCancel = "Cancel";

  private final InputValidator _inputValidator;

  private final InputCallback _callback;

  private JOptionPane _optionPane;

  @Override
  public void actionPerformed(ActionEvent e) {
    _optionPane.setValue(_btnOK);
  }

  @Override
  public void propertyChange(PropertyChangeEvent e) {
    String prop = e.getPropertyName();

    if (isVisible() && (e.getSource() == _optionPane)
        && (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
      Object value = _optionPane.getValue();

      if (value == JOptionPane.UNINITIALIZED_VALUE) {
        // ignore reset
        return;
      }

      // Reset the JOptionPane's value.
      // If you don't do this, then if the user
      // presses the same button next time, no
      // property change event will be fired.
      _optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

      if (_btnOK.equals(value)) {
        // OK pressed
        _typedText = _textField.getText();
        if (_inputValidator.validate(_typedText)) {
          if (_callback != null) {
            _callback.inputValid(_typedText);
          }
          dispose();
        }
        else {
          _textField.selectAll();
          JOptionPane.showMessageDialog(this, _inputValidator.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
      else {
        // canceled or closed dialog
        if (_callback != null) {
          _callback.inputCancelled();
        }
        dispose();
      }
    }
  }

  public ValidatableInputDialog(JFrame frame, String prompt, InputValidator validator, InputCallback callback) {
    super(frame, true);
    setSize(380, 150);// default size

    _inputValidator = validator;
    _callback = callback;

    _textField = new JTextField(10);

    // dialog components
    Object[] array = { prompt, _textField };

    // buttons
    Object[] options = { _btnOK, _btnCancel };

    _optionPane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, options,
        options[0]);

    setContentPane(_optionPane);

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        /*
         * Instead of directly closing the window, we're going to change the
         * JOptionPane's value property.
         */
        _optionPane.setValue(JOptionPane.CLOSED_OPTION);
      }
    });

    // Ensure the text field always gets the first focus.
    addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent ce) {
        _textField.requestFocusInWindow();
      }
    });

    // Register an event handler that puts the text into the option pane.
    _textField.addActionListener(this);

    // Register an event handler that reacts to option pane state changes.
    _optionPane.addPropertyChangeListener(this);

  }

  public String getTypedText() {
    return _typedText;
  }

}
