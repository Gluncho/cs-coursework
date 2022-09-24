import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MetropolisFrame extends JFrame {
    private MetropolisTableModel model;
    private JTextField nameField, continentField, populationField;
    private JButton addButton, searchButton;
    private JComboBox<String> populationComboBox, matchComboBox;

    /**
     * Called by the constructors to init the <code>JFrame</code> properly.
     */
    @Override
    protected void frameInit() {
        super.frameInit();

        nameField = new JTextField(15);
        continentField = new JTextField(15);
        populationField = new JTextField(15);

        addButton = new JButton("Add");
        searchButton = new JButton("Search");

        populationComboBox = new JComboBox<>(new String[]{"Population Larger Than", "Populations Smaller Than"});
        matchComboBox = new JComboBox<>(new String[]{"Exact Match", "Partial Match"});
    }

    public MetropolisFrame(MetropolisTableModel model) throws HeadlessException {
        super("Metropolis Viewer");
        this.model = model;
        setLayout(new BorderLayout(4,4));
        addUpperSection();
        addRightSection();
        addCenterSection();
        addListeners();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void addListeners() {
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String continent = continentField.getText();
            String populationText = populationField.getText();
            if(name.equals("") || continent.equals("") || populationText.equals("")) return;
            try{
                Long population = Long.parseUnsignedLong(populationText);
                model.add(new MetropolisEntry(name,continent,population));
//                    nameField.setText("");
//                    continentField.setText("");
//                    populationField.setText("");
            }catch(NumberFormatException exception){
                exception.printStackTrace();
            }
        });
        searchButton.addActionListener(e -> {
            String name = nameField.getText();
            String continent = continentField.getText();
            String populationText = populationField.getText();
            Long population = null;
            try{
                population = Long.parseUnsignedLong(populationText);
            }catch(NumberFormatException ignored){}
            MetropolisEntry entry = new MetropolisEntry(name, continent, population);
            int populationType = populationComboBox.getSelectedIndex();
            int matchType = matchComboBox.getSelectedIndex();
            model.search(entry, populationType, matchType);
        });
    }

    private void addUpperSection() {
        JPanel panel = new JPanel();

        JLabel nameLabel = new JLabel("Metropolis:");
        JLabel continentLabel = new JLabel("Continent:");
        JLabel populationLabel = new JLabel("Population:");
        nameLabel.setFont(new Font("Sans-serif", Font.BOLD, 11));
        continentLabel.setFont(new Font("Sans-serif", Font.BOLD, 11));
        populationLabel.setFont(new Font("Sans-serif", Font.BOLD, 11));

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(new JLabel("   "));
        panel.add(continentLabel);
        panel.add(continentField);
        panel.add(new JLabel("   "));
        panel.add(populationLabel);
        panel.add(populationField);

        add(panel, BorderLayout.NORTH);

    }

    private void addRightSection() {
        JPanel panel = new JPanel();
        addButtons(panel);
        addLittleBox(panel);
        add(panel, BorderLayout.EAST);
    }

    private void addButtons(JPanel panel){
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        int ButtonWidth = Math.max(addButton.getMinimumSize().width, searchButton.getMinimumSize().width);
        addButton.setMaximumSize(new Dimension(ButtonWidth, addButton.getMinimumSize().height));
        searchButton.setMaximumSize(new Dimension(ButtonWidth, searchButton.getMinimumSize().height));

        panel.add(addButton);
        panel.add(Box.createRigidArea(new Dimension(0, addButton.getMinimumSize().height/3)));
        panel.add(searchButton);
        panel.add(Box.createRigidArea(new Dimension(0, addButton.getMinimumSize().height/3)));
    }

    private void addLittleBox(JPanel panel) {
        Box box = new Box(BoxLayout.Y_AXIS);
        box.setBorder(new TitledBorder("Search Options"));
        // Set dimensions so that Combobox doesn't take up extra space
        populationComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, populationComboBox.getMinimumSize().height));
        matchComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, matchComboBox.getMinimumSize().height));
        box.add(populationComboBox);
        box.add(Box.createRigidArea(new Dimension(0, populationComboBox.getMinimumSize().height/3)));
        box.add(matchComboBox);
        box.setAlignmentX(JComponent.LEFT_ALIGNMENT); // Align with buttons
        panel.add(box);
    }

    private void addCenterSection() {
        JTable table = new JTable(model);
        JScrollPane pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);
    }
}
