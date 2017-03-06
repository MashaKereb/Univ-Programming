import java.io.File;

/**
 * Created by masha on 05.03.17.
 */
public class CustomMemoryManager {
    public static void main(String[] args) {
        ControlPanel controlPanel;
        Kernel kernel;

        kernel = new Kernel();
        controlPanel = new ControlPanel("Memory Management");

        controlPanel.init(kernel, "commands", "memory.conf");
    }
}
