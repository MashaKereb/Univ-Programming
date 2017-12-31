import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private SimulationPanel simulationPanel;
    private JButton stopButton;
    private JButton randButton;
    private JButton startButton;
    private JComboBox civComboBox;
    private JSlider slider;

    public MainFrame() {
        super("Game of life");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);


        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.SOUTH);

        simulationPanel = new SimulationPanel(80, 80, 9);
        add(simulationPanel);
        startButton = new JButton("Start");
        toolBar.add(startButton);
        startButton.setEnabled(false);

        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        toolBar.add(stopButton);

        randButton = new JButton("Random");
        randButton.setEnabled(true);
        toolBar.add(randButton);

        String [] generationNumbers = {"One generation", "Two generations",
                "Three generations", "Four generations", "Five generations"};

        civComboBox = new JComboBox(generationNumbers);
        civComboBox.setSelectedIndex(0);
        toolBar.add(civComboBox);

        slider = new JSlider(1, 100);
        slider.setValue(25);
        toolBar.add(slider);


        randButton.addActionListener(e->{
            startButton.setEnabled(false);
            stopButton.setEnabled(false);
            simulationPanel.stopSimulation();
            simulationPanel.init(civComboBox.getSelectedIndex() + 1,
                    (float)slider.getValue()/100);
            simulationPanel.repaint();
            startButton.setEnabled(true);
        });

        startButton.addActionListener(e -> {
            simulationPanel.startSimulation();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        });

        stopButton.addActionListener(e -> {
            simulationPanel.stopSimulation();
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
