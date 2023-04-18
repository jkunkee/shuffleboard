
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Requires;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;

import com.fazecast.jSerialComm.SerialPort;

@Requires(group = "edu.wpi.first.shuffleboard", name = "Base", minVersion = "1.0.0")
@Description(group = "org.team4131.serial", name = "SerialPlugin", version = "0.0.1", summary = "Links NetworkTables events to PC-side serial events")
public class SerialPlugin extends Plugin {
    void init(com.sun.source.util.JavacTask arg0, java.lang.String... arg1) {
        var foo = SerialPort.getCommPorts();
        System.out.println(foo);
    }
}
