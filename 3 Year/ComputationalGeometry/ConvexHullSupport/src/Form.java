import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Main class which creates UI components
 * Created by anastasia on 4/12/17.
 */
public class Form {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        final DrawArea drawArea = new DrawArea();
        content.add(drawArea, BorderLayout.CENTER);

        JPanel controls = new JPanel();

        JButton addPointsButton = new JButton("Add");
        JButton removePointsButton = new JButton("Remove");
        JButton clearButton = new JButton("Clear");
        JButton okButton = new JButton("OK");

        ActionListener actionListener = e -> {
            if (e.getSource() == clearButton) {
                drawArea.clearData();
            } else if (e.getSource() == okButton) {
                drawArea.createConvexHull();
            } else if (e.getSource() == addPointsButton) {
                drawArea.setAddingPointsMode();
            } else if (e.getSource() == removePointsButton) {
                drawArea.setRemovingPointsMode();
            }
        };

        addPointsButton.addActionListener(actionListener);
        removePointsButton.addActionListener(actionListener);
        clearButton.addActionListener(actionListener);
        okButton.addActionListener(actionListener);
        controls.add(addPointsButton);
        controls.add(removePointsButton);
        controls.add(clearButton);
        controls.add(okButton);
        content.add(controls, BorderLayout.NORTH);

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
