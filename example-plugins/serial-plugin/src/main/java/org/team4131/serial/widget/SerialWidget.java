
package org.team4131.serial.widget;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.nio.ByteBuffer;

import com.fazecast.jSerialComm.SerialPort;

@Description(
    name = "Serial Bridge",
    dataTypes = String.class,
    summary = "Bridges string events to a local serial port"
)
@ParametrizedController("SerialWidget.fxml")
public class SerialWidget extends SimpleAnnotatedWidget<String> {
  // NetworkTables -> Serial
  // NetworkTables -> UI
  // UI -> Serial
  // Serial -> UI
  // Serial -> NetworkTables
  // no UI -> NetworkTables because the only UI widgets are for serial setup

  private char lastSentChar = 0;
  SerialPort currentPort = null;

  @FXML
  private Pane root;
  @FXML
  private ComboBox<String> PortSelector;
  @FXML
  private Label LastStringFromNetwork;
  @FXML
  private Label LastSentToSerial;
  @FXML
  private Label SerialText;

  private void ComboBoxChangeHandler(ActionEvent event) {
    // concurrency?
    if (PortSelector != event.getSource()) {
      return;
    }
    event.consume();
    // Find new port.
    SerialPort newPort = SerialPort.getCommPort(PortSelector.getValue());
    // If it's the same port, do nothing.
    if (newPort == currentPort ||
        (newPort != null &&
         currentPort != null &&
         newPort.getDescriptivePortName().equals(currentPort.getDescriptivePortName()))) {
      return;
    }
    synchronized (currentPort) {
      // It's a different port, so
      // If current port is not null, shut down current port
      if (currentPort != null) {
        //currentPort.removeDataListener();
        currentPort.closePort();
        currentPort = null;
      }
      // If new port is not null, set up the new port
      if (newPort != null) {
        newPort.openPort();
        newPort.setBaudRate(115200);
        //newPort.addDataListener(todo);
        currentPort = newPort;
      }
    }
  }

  private String LastToSerialMapper(Object o) {
    if (o == null || !String.class.isInstance(o)) {
      return "problem input";
    }
    String str = String.class.cast(o);
    byte[] b = str.getBytes();
    if (currentPort != null) {
      currentPort.writeBytes(b, b.length);
    }
    return str;
  }

  @FXML
  private void initialize() {
    PortSelector.onActionProperty();
    PortSelector.addEventHandler(ActionEvent.ACTION, this::ComboBoxChangeHandler);
    // Set up the COM port selector
    PortSelector.setEditable(false);
    ObservableList<String> portList = FXCollections.observableArrayList();
    for (SerialPort port : SerialPort.getCommPorts()) {
      portList.add(port.getDescriptivePortName());
    }
    PortSelector.setItems(portList);
    if (portList.size() > 0) {
      PortSelector.setValue(portList.get(0));
    }
    // TODO: default to first Arduino
    // TODO: rework on serial enumeration events

    // Register a view of the last data seen
    // TODO: drop this?
    LastStringFromNetwork.textProperty().bind(dataOrDefault.map(str -> "data: \""+str.toString()+"\""));

    // Register a view of the last data sent to the serial port
    LastSentToSerial.textProperty().bind(dataOrDefault.map(this::LastToSerialMapper));

    // Scratch space
    String s = new String();
    for (SerialPort port : SerialPort.getCommPorts()) {
      s += port.getDescriptivePortName();
      s += '\n';
    }
    SerialText.setText(s);
  }

  @Override
  public Pane getView() {
    return root;
  }
}
