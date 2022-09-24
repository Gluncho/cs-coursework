import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;


public class SudokuFrame extends JFrame {
    private JTextArea source, result;
    private JButton check;
    private JCheckBox autoCheck;

    public SudokuFrame() {
        super("Sudoku Solver");
        LayoutManager layout = new BorderLayout(4, 4);
        this.setLayout(layout);

        source = new JTextArea(15, 20);
        result = new JTextArea(15, 20);
        add(source, BorderLayout.CENTER);
        add(result, BorderLayout.EAST);
        source.setBorder(new TitledBorder("Puzzle"));
        result.setBorder(new TitledBorder("Solution"));

        check = new JButton("Check");
        autoCheck = new JCheckBox("Auto check");
        addListeners();
        Box box = Box.createHorizontalBox();
        box.add(check);
        box.add(autoCheck);
        add(box, BorderLayout.SOUTH);
        //Could do this:
        //setLocationByPlatform(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void addListeners() {
        check.addActionListener(e -> doCheck());

        source.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (autoCheck.isSelected()) doCheck();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (autoCheck.isSelected()) doCheck();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (autoCheck.isSelected()) doCheck();
            }
        });
    }

    private void doCheck() {
        try {
            Sudoku sudoku = new Sudoku(Sudoku.textToGrid(source.getText()));
            int numSolutions = sudoku.solve();
            String solutionText = sudoku.getSolutionText();
            long elapsed = sudoku.getElapsed();

            if (numSolutions > 0) {
                String text = solutionText + "\n" + "solutions: " + numSolutions + "\n";
                text += "elapsed: " + elapsed + " ms\n";
                result.setText(text);
            } else result.setText("No solutions found");
        } catch (RuntimeException ex) {
            result.setText("Parsing Problem!");
        }
    }

    public static void main(String[] args) {
        // GUI Look And Feel
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SudokuFrame frame = new SudokuFrame();
    }

}
