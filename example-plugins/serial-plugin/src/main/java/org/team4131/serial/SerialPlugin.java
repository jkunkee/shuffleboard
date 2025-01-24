
package org.team4131.serial;

import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Requires;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

import org.team4131.serial.widget.SerialWidget;

import java.util.List;

@Requires(group = "edu.wpi.first.shuffleboard", name = "Base", minVersion = "1.0.0")
@Description(
    group = "org.team4131.serial",
    name = "SerialPlugin",
    version = "0.0.1",
    summary = "Links NetworkTables events to PC-side serial events")

public class SerialPlugin extends Plugin {
  @Override
  public List<ComponentType> getComponents() {
    return List.of(
      WidgetType.forAnnotatedWidget(SerialWidget.class)
    );
  }
}
