
package org.team4131.serial.widget;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import com.fazecast.jSerialComm.SerialPort;

@Description(
    name = "Serial Bridge",
    dataTypes = String.class,
    summary = "Bridges string events to a local serial port"
)
public class SerialWidget extends SimpleAnnotatedWidget<String> {
  @FXML
  private Pane root;
  @FXML
  private Label label;
  @FXML
  private void initialize() {
    label.setText("HI");
  }

  @Override
  public Pane getView() {
    return root;
  }
}
