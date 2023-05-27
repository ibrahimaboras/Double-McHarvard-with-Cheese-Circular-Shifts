import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AssemblySimulatorGUI extends JFrame {
    private JTextArea textEditor;
    private JTextArea outputArea;
    private JFrame consoleFrame;

    public AssemblySimulatorGUI() {
        setTitle("Assembly Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Text Editor
        textEditor = new JTextArea(30, 80);
        JScrollPane editorScrollPane = new JScrollPane(textEditor);
        add(editorScrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buttonPanel, BorderLayout.NORTH);

        // Open Button
        JButton openButton = new JButton("Open");
        openButton.setFont(new Font("Arial", Font.PLAIN, 14));
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                            stringBuilder.append("\n");
                        }
                        reader.close();
                        textEditor.setText(stringBuilder.toString());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        buttonPanel.add(openButton);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 14));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
                        writer.write(textEditor.getText());
                        writer.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        buttonPanel.add(saveButton);

        // Run Button
        JButton runButton = new JButton("Run");
        runButton.setFont(new Font("Arial", Font.PLAIN, 14));
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String assemblyCode = textEditor.getText();
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("src/resources/AssemblyCode.txt"));
                    writer.write(assemblyCode);
                    writer.close();

                    // Clear the outputArea
                    outputArea.setText("");

                    // Redirect standard output to the outputArea
                    PrintStream printStream = new PrintStream(new CustomOutputStream(outputArea));
                    System.setOut(printStream);

                    // Run the assembly simulator
                    CheesyMcHarvard simulator = new CheesyMcHarvard();
                    try {
                        simulator.run();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel.add(runButton);

        // Console Button
        JButton consoleButton = new JButton("Console");
        consoleButton.setFont(new Font("Arial", Font.PLAIN, 14));
        consoleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (consoleFrame == null) {
                    createConsoleFrame();
                } else {
                    consoleFrame.setVisible(true);
                }
            }
        });
        buttonPanel.add(consoleButton);

        setVisible(true);
    }

    private void createConsoleFrame() {
        consoleFrame = new JFrame("Console");
        consoleFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        consoleFrame.setSize(800, 400);
        consoleFrame.setLayout(new BorderLayout());

        // Output Area
        outputArea = new JTextArea(10, 80);
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        consoleFrame.add(outputScrollPane, BorderLayout.CENTER);

        consoleFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Set the look and feel to the system's default
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }

                AssemblySimulatorGUI gui = new AssemblySimulatorGUI();
            }
        });
    }

    // Custom OutputStream implementation that writes to a JTextArea
    private class CustomOutputStream extends OutputStream {
        private JTextArea textArea;

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            textArea.append(String.valueOf((char) b));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}
