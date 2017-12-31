import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(String title) {
        super(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(null);
        this.setSize(new Dimension(1200,820));
        this.setResizable(false);

        add(new GamePanel(this, 1200, 800));
        this.setLocationRelativeTo(null);
        setVisible(true);
    }
}
