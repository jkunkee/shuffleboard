
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Requires;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;

import com.fazecast.jSerialComm.SerialPort;

@Requires(group = "edu.wpi.first.shuffleboard", name = "Base", minVersion = "1.0.0")
@Description(group = "com.team4131.xboxcontrollerwidget", name = "XboxControllerWidget", version = "0.0.1", summary = "Displays the state of an Xbox controller")
public class XboxControllerWidgetPlugin extends Plugin {
    void init(com.sun.source.util.JavacTask arg0, java.lang.String... arg1) {
        var foo = SerialPort.getCommPorts();
        System.out.println(foo);
    }
}
