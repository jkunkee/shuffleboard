
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
import java.nio.charset.StandardCharsets;

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
  private SerialPort currentPort = null;
  private final Object currentPortLock = new Object();

  @FXML
  private Pane root;
  @FXML
  private ComboBox<String> portSelector;
  @FXML
  private Label lastStringFromNetwork;
  @FXML
  private Label lastSentToSerial;
  @FXML
  private Label serialText;

  private void comboBoxChangeHandler(ActionEvent event) {
    // concurrency?
    if (portSelector != event.getSource()) {
      return;
    }
    event.consume();
    // Find new port.
    SerialPort newPort = SerialPort.getCommPort(portSelector.getValue());
    // If it's the same port, do nothing.
    if (newPort == currentPort
        || (newPort != null
        && currentPort != null 
        && newPort.getDescriptivePortName().equals(currentPort.getDescriptivePortName()))) {
      return;
    }
    // It's a different port, so
    synchronized (currentPortLock) {
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

  private String lastToSerialMapper(Object o) {
    if (o == null || !String.class.isInstance(o)) {
      return "problem input";
    }
    String str = String.class.cast(o);
    byte[] bytes = str.getBytes(StandardCharsets.US_ASCII);
    synchronized (currentPortLock) {
      if (currentPort != null) {
        currentPort.writeBytes(bytes, bytes.length);
      }
    }
    return str;
  }

  @FXML
  private void initialize() {
    portSelector.onActionProperty();
    portSelector.addEventHandler(ActionEvent.ACTION, this::comboBoxChangeHandler);
    // Set up the COM port selector
    portSelector.setEditable(false);
    ObservableList<String> portList = FXCollections.observableArrayList();
    for (SerialPort port : SerialPort.getCommPorts()) {
      portList.add(port.getDescriptivePortName());
    }
    portSelector.setItems(portList);
    if (portList.size() > 0) {
      portSelector.setValue(portList.get(0));
    }
    // TODO: default to first Arduino
    // TODO: rework on serial enumeration events

    // Register a view of the last data seen
    // TODO: drop this?
    lastStringFromNetwork.textProperty().bind(dataOrDefault.map(str -> "data: \"" + str.toString() + "\""));

    // Register a view of the last data sent to the serial port
    lastSentToSerial.textProperty().bind(dataOrDefault.map(this::lastToSerialMapper));

    // Scratch space
    String str = "";
    for (SerialPort port : SerialPort.getCommPorts()) {
      str += port.getDescriptivePortName();
      str += '\n';
    }
    serialText.setText(str);
  }

  @Override
  public Pane getView() {
    return root;
  }
}
