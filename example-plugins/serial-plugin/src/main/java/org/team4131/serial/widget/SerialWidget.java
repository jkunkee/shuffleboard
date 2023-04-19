
package org.team4131.serial.widget;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
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
@ParametrizedController("SerialWidget.fxml")
public class SerialWidget extends SimpleAnnotatedWidget<String> {
  private String lastReceivedString;
  private char lastSentChar = 0;

  @FXML
  private Pane root;
  @FXML
  private Label LastReceived;
  @FXML
  private Label LastSent;
  @FXML
  private Label SerialText;

  @FXML
  private void initialize() {
    LastReceived.textProperty().bind(dataOrDefault.map(str -> str.toString()).map(str -> "s:'"+str+"'"));

    String s = new String();
    for (SerialPort port : SerialPort.getCommPorts()) {
      s += port.getDescriptivePortName();
      s += '\n';
    }
    SerialText.setText(s);
    //LastReceived.setText(SerialPort.getVersion());
    //LastSent.setText(SerialPort.getVersion());
  }

  @Override
  public Pane getView() {
    return root;
  }
}
