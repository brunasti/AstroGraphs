package it.brunasti.graph.fractals.newtons;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CancellationException;

/**
 * From:
 * https://github.com/gustavohb/newton-fractals
 */
public class FractalViewer extends JPanel implements ActionListener, PropertyChangeListener {

    static final int DEFAULT_WIDTH = 890;
    static final int DEFAULT_HEIGHT = 700;

    static JScrollPane previewScrollPane;

    static JTextField z0Field;
    static JTextField z1Field;
    static JTextField z2Field;
    static JTextField z3Field;
    static JTextField z4Field;
    static JTextField z5Field;
    static JTextField z6Field;
    static JTextField z7Field;
    static JTextField z8Field;
    static JTextField z9Field;
    static JButton genButton;
    static ProgressMonitor progressMonitor;
    NewtonFractal operation;

    double[] coefficients;

    public FractalViewer() {
        coefficients = new double[10];

        JPanel settingsPanel = new JPanel();
        settingsPanel.setFocusable(true);

        JLabel equationFieldLabel = new JLabel("Equation:");
        settingsPanel.add(equationFieldLabel);


        z9Field = new JTextField(2);
        z9Field.setText("0");
        z9Field.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel z9Label = new JLabel("z⁹ +");
        settingsPanel.add(z9Field);
        settingsPanel.add(z9Label);

        z8Field = new JTextField(2);
        z8Field.setText("0");
        z8Field.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel z8Label = new JLabel("z⁸ +");
        settingsPanel.add(z8Field);
        settingsPanel.add(z8Label);

        z7Field = new JTextField(2);
        z7Field.setText("0");
        z7Field.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel z7Label = new JLabel("z⁷ +");
        settingsPanel.add(z7Field);
        settingsPanel.add(z7Label);

        z6Field = new JTextField(2);
        z6Field.setText("0");
        z6Field.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel z6Label = new JLabel("z⁶ +");
        settingsPanel.add(z6Field);
        settingsPanel.add(z6Label);

        z5Field = new JTextField(2);
        z5Field.setText("0");
        z5Field.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel z5Label = new JLabel("z⁵ +");
        settingsPanel.add(z5Field);
        settingsPanel.add(z5Label);

        z4Field = new JTextField(2);
        z4Field.setText("0");
        z4Field.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel z4Label = new JLabel("z⁴ +");
        settingsPanel.add(z4Field);
        settingsPanel.add(z4Label);

        z3Field = new JTextField(2);
        z3Field.setText("1");
        z3Field.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel z3Label = new JLabel("z³ +");
        settingsPanel.add(z3Field);
        settingsPanel.add(z3Label);

        z2Field = new JTextField(2);
        z2Field.setText("0");
        z2Field.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel z2Label = new JLabel("z² +");
        settingsPanel.add(z2Field);
        settingsPanel.add(z2Label);

        z1Field = new JTextField(2);
        z1Field.setText("0");
        z1Field.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel z1Label = new JLabel("z +");
        settingsPanel.add(z1Field);
        settingsPanel.add(z1Label);

        z0Field = new JTextField(2);
        z0Field.setText("-1");
        z0Field.setHorizontalAlignment(SwingConstants.RIGHT);
        settingsPanel.add(z0Field);

        genButton = new JButton("Generate");
        genButton.setActionCommand("generate");
        genButton.addActionListener(this);

        settingsPanel.add(genButton);

        previewScrollPane = new JScrollPane();

        setLayout(new BorderLayout(10, 0));
        JScrollPane topScrollPanel = new JScrollPane(settingsPanel);

        this.add(topScrollPanel, BorderLayout.PAGE_START);
        this.add(previewScrollPane, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if ("generate".equals(event.getActionCommand())) {
            try {
                coefficients[0] = Double.parseDouble(z0Field.getText());
                coefficients[1] = Double.parseDouble(z1Field.getText());
                coefficients[2] = Double.parseDouble(z2Field.getText());
                coefficients[3] = Double.parseDouble(z3Field.getText());
                coefficients[4] = Double.parseDouble(z4Field.getText());
                coefficients[5] = Double.parseDouble(z5Field.getText());
                coefficients[6] = Double.parseDouble(z6Field.getText());
                coefficients[7] = Double.parseDouble(z7Field.getText());
                coefficients[8] = Double.parseDouble(z8Field.getText());
                coefficients[9] = Double.parseDouble(z9Field.getText());

                double absCoeffSum = 0;
                for (int i = 9; i > 1; i--) {
                    absCoeffSum += Math.abs(coefficients[i]);
                }

                if (absCoeffSum == 0) {
                    JOptionPane.showMessageDialog(this,
                            "The polynomial must be of at least order 2, ie include a Z² or higher term",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                progressMonitor = new ProgressMonitor(FractalViewer.this, "Generating fractal...", "",0, 100);
                progressMonitor.setProgress(0);

                operation = new NewtonFractal(DEFAULT_WIDTH-4, DEFAULT_WIDTH-65, new Polynomial(coefficients));
                operation.addPropertyChangeListener(this);
                operation.execute();

                setFieldsEnabled(false);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "You should input a valid number (" + e + ")",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setFieldsEnabled(boolean b) {

        z0Field.setEnabled(b);
        z1Field.setEnabled(b);
        z2Field.setEnabled(b);
        z3Field.setEnabled(b);
        z4Field.setEnabled(b);
        z5Field.setEnabled(b);
        z6Field.setEnabled(b);
        z7Field.setEnabled(b);
        z8Field.setEnabled(b);
        z9Field.setEnabled(b);

        genButton.setEnabled(b);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (progressMonitor.isCanceled()) {
            operation.cancel(true);
        } else if (event.getPropertyName().equals("progress")) {
            int progress = ((Integer)event.getNewValue()).intValue();
            progressMonitor.setProgress(progress);
        }

        if (operation.isDone()) {
            try {
                previewScrollPane.getViewport().add(new JLabel(new ImageIcon(operation.get())));
            } catch (CancellationException e) {
                // Fails silently
				/*
				JOptionPane.showMessageDialog(this,
						"Couldn't display the generated image (" + e + ")",
						"Error", JOptionPane.ERROR_MESSAGE);
				*/
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Newton Fractal Explorer");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        JComponent contentPane = new FractalViewer();
        contentPane.setOpaque(true);
        frame.setContentPane(contentPane);
        centreWindow(frame);

        frame.setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}