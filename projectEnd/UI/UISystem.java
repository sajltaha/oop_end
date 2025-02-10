package UI;

import Objects.*;
import RAR.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UISystem extends JFrame {

    private final ZooRAR ZooRAR = new ZooRAR();
    private final CageRAR CageRAR = new CageRAR();
    private final AnimalRAR AnimalRAR = new AnimalRAR();
    private Zoo currentZoo;
    private JPanel contentPanel; // Instance variable to hold the content panel

    public UISystem() {
        setTitle("Zoo Park System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        contentPanel = new JPanel(new BorderLayout()); // Initialize contentPanel
        add(contentPanel, BorderLayout.CENTER); // Add contentPanel to the JFrame

        createSidebar();
        mainMenuUI();
    }

    private void createSidebar() {
        JPanel sidebar = new JPanel(new GridLayout(6, 1, 5, 5));
        sidebar.setBackground(Color.LIGHT_GRAY);

        JButton btnHome = new JButton("Home");
        JButton btnZoo = new JButton("Zoo Management");
        JButton btnCage = new JButton("Cage Management");
        JButton btnAnimal = new JButton("Animal Management");
        JButton btnView = new JButton("View Records");
        JButton btnExit = new JButton("Exit");

        btnHome.addActionListener(e -> mainMenuUI());
        btnZoo.addActionListener(e -> manageZooUI());
        btnCage.addActionListener(e -> {
            if (currentZoo == null) {
                JOptionPane.showMessageDialog(this, "Please select a zoo first.", "Error", JOptionPane.ERROR_MESSAGE);
                manageZooUI(); // Go to zoo management to allow selection
            } else {
                manageCageUI(); // Go to cage management UI
            }
        });
        btnAnimal.addActionListener(e -> {
            if (currentZoo == null) {
                JOptionPane.showMessageDialog(this, "Please select a zoo first.", "Error", JOptionPane.ERROR_MESSAGE);
                manageZooUI();
            } else {
                manageAnimalUI(); // Go to animal management UI
            }
        });
        btnView.addActionListener(e -> displayAnimalsUI());
        btnExit.addActionListener(e -> System.exit(0));

        sidebar.add(btnHome);
        sidebar.add(btnZoo);
        sidebar.add(btnCage);
        sidebar.add(btnAnimal);
        sidebar.add(btnView);
        sidebar.add(btnExit);

        add(sidebar, BorderLayout.WEST);
    }

    // ************* UI DISPLAY FUNCTIONS **********************

    public void mainMenuUI() {
        clearContent();

        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the Zoo Park System!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        contentPanel.add(panel, BorderLayout.CENTER); // Add to contentPanel
        revalidate();
        repaint();
    }

    private void manageZooUI() {
        clearContent();
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        JButton createZooButton = new JButton("Create Zoo");
        JButton editZooButton = new JButton("Edit Zoo");
        JButton deleteZooButton = new JButton("Delete Zoo");
        JButton selectZooButton = new JButton("Select Zoo");

        createZooButton.addActionListener(e -> createZooUI());
        editZooButton.addActionListener(e -> editZooUI());
        deleteZooButton.addActionListener(e -> deleteZooUI());
        selectZooButton.addActionListener(e -> selectZooUI());

        panel.add(createZooButton);
        panel.add(editZooButton);
        panel.add(deleteZooButton);
        panel.add(selectZooButton);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void createZooUI() {
        clearContent(); // Use clearContent to properly reset the UI
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        JLabel label = new JLabel("Enter Zoo Name:");
        JTextField zooNameField = new JTextField();
        JButton createButton = new JButton("Create Zoo");

        createButton.addActionListener(e -> createZoo(zooNameField.getText().trim()));

        panel.add(label);
        panel.add(zooNameField);
        panel.add(createButton);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void editZooUI() {
        clearContent();
        try {
            List<Zoo> zoos = ZooRAR.getAllZoos();
            if (zoos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No zoos available to edit.", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                manageZooUI(); // Go back to the manage zoo UI
                return;
            }

            JComboBox<String> zooComboBox = new JComboBox<>(zoos.stream().map(Zoo::getName).toArray(String[]::new));
            JTextField newNameField = new JTextField();
            JButton editButton = new JButton("Edit Zoo");

            editButton.addActionListener(e -> {
                String selectedZooName = (String) zooComboBox.getSelectedItem();
                Zoo selectedZoo = zoos.stream().filter(z -> z.getName().equals(selectedZooName)).findFirst()
                        .orElse(null);
                String newName = newNameField.getText().trim();

                if (selectedZoo != null && !newName.isEmpty()) {
                    editZoo(selectedZoo, newName);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a zoo and enter new name.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
            panel.add(new JLabel("Select Zoo to Edit:"));
            panel.add(zooComboBox);
            panel.add(new JLabel("Enter New Name:"));
            panel.add(newNameField);
            panel.add(editButton);
            contentPanel.add(panel, BorderLayout.CENTER); // Use contentPanel
            revalidate();
            repaint();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching zoos: " + ex.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteZooUI() {
        clearContent();
        try {
            List<Zoo> zoos = ZooRAR.getAllZoos();
            if (zoos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No zoos available to Delete.", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                manageZooUI(); // Go back to the manage zoo UI
                return;
            }

            JComboBox<String> zooComboBox = new JComboBox<>(zoos.stream().map(Zoo::getName).toArray(String[]::new));
            JButton deleteButton = new JButton("Delete Zoo");

            deleteButton.addActionListener(e -> {
                String selectedZooName = (String) zooComboBox.getSelectedItem();
                Zoo selectedZoo = zoos.stream().filter(z -> z.getName().equals(selectedZooName)).findFirst()
                        .orElse(null);
                if (selectedZoo != null) {
                    deleteZoo(selectedZoo);
                }
            });
            JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
            panel.add(new JLabel("Select Zoo to delete:"));
            panel.add(zooComboBox);
            panel.add(deleteButton);
            contentPanel.add(panel, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching zoos: " + ex.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectZooUI() {
        clearContent();
        try {
            List<Zoo> zoos = ZooRAR.getAllZoos(); // Assuming you have a method to get all zoos
            if (zoos.isEmpty()) {
                JLabel noZoosLabel = new JLabel("No zoos available. Please create a zoo first.", SwingConstants.CENTER);
                contentPanel.add(noZoosLabel, BorderLayout.CENTER);

            } else {
                JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5)); // Use GridLayout
                JLabel selectLabel = new JLabel("Select a Zoo:");
                JComboBox<String> zooComboBox = new JComboBox<>();
                zoos.forEach(zoo -> zooComboBox.addItem(zoo.getName())); // Add zoo names

                JButton selectButton = new JButton("Select");

                selectButton.addActionListener(e -> {
                    String selectedZooName = (String) zooComboBox.getSelectedItem();
                    if (selectedZooName != null) {
                        // Find the selected Zoo object. Important for getting the ID.
                        Zoo selectedZoo = zoos.stream()
                                .filter(zoo -> zoo.getName().equals(selectedZooName))
                                .findFirst()
                                .orElse(null); // This should never be null in this context, but it's good practice.

                        if (selectedZoo != null) {
                            currentZoo = selectedZoo; // Set the current zoo
                            JOptionPane.showMessageDialog(this, "Selected Zoo: " + currentZoo.getName());
                            mainMenuUI(); // Go back to the main menu
                        }
                    }
                });

                panel.add(selectLabel);
                panel.add(zooComboBox);
                panel.add(selectButton);
                contentPanel.add(panel, BorderLayout.CENTER); // Use contentPanel

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching zoos: " + ex.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);

        }
        revalidate();
        repaint();
    }

    private void manageCageUI() {
        clearContent();
        if (currentZoo == null) {
            JOptionPane.showMessageDialog(this, "Please select a zoo first.", "Error", JOptionPane.ERROR_MESSAGE);
            manageZooUI();
            return;
        }

        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        JLabel zooLabel = new JLabel("Current Zoo: " + currentZoo.getName(), SwingConstants.CENTER); // Show current zoo
        JButton addCageButton = new JButton("Add Cage");
        JButton editCageButton = new JButton("Edit Cage");
        JButton deleteCageButton = new JButton("Delete Cage");

        addCageButton.addActionListener(e -> createCageUI());
        editCageButton.addActionListener(e -> editCageUI());
        deleteCageButton.addActionListener(e -> deleteCageUI());

        panel.add(zooLabel); // Add the zoo label
        panel.add(addCageButton);
        panel.add(editCageButton);
        panel.add(deleteCageButton);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void createCageUI() {
        clearContent();
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Create Cage"));

        JLabel lblNumber = new JLabel("Cage Number:");
        JTextField txtNumber = new JTextField();
        JLabel lblSize = new JLabel("Cage Size:");
        // Dropdown for cage size
        JComboBox<String> cmbSize = new JComboBox<>(new String[] { "Small", "Medium", "Large" });
        JLabel lblMaxAnimals = new JLabel("Max Animals:");
        JTextField txtMaxAnimals = new JTextField();

        JButton btnCreate = new JButton("Create Cage");

        btnCreate.addActionListener(e -> createCage(txtNumber, cmbSize, txtMaxAnimals));

        panel.add(lblNumber);
        panel.add(txtNumber);
        panel.add(lblSize);
        panel.add(cmbSize);
        panel.add(lblMaxAnimals);
        panel.add(txtMaxAnimals);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(btnCreate);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void editCageUI() {
        clearContent();
        try {
            List<Cage> cages = CageRAR.getCagesByZooId(currentZoo.getId()); // Get cages for the current zoo
            if (cages.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No cages available for this zoo.", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                manageCageUI(); // Return to manage cages UI
                return;
            }
            JComboBox<String> cageComboBox = new JComboBox<>(
                    cages.stream().map(this::formatCageInfo).toArray(String[]::new));

            // Create input fields for editing
            JTextField txtNumber = new JTextField();
            JComboBox<String> cmbSize = new JComboBox<>(new String[] { "Small", "Medium", "Large" });
            JTextField txtMaxAnimals = new JTextField();
            JButton btnSave = new JButton("Save Changes");

            btnSave.addActionListener(e -> {
                String selectedCageInfo = (String) cageComboBox.getSelectedItem();
                // Extract the cage ID from the selected string.
                int cageId = extractCageIdFromInfo(selectedCageInfo);
                Cage selectedCage = cages.stream().filter(c -> c.getNumber() == cageId).findFirst().orElse(null);

                if (selectedCage != null) {
                    editCage(selectedCage, txtNumber, cmbSize, txtMaxAnimals);
                }

            });

            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5)); // Use GridLayout
            panel.add(new JLabel("Select Cage:"));
            panel.add(cageComboBox);
            panel.add(new JLabel("Cage Number (ID):"));
            panel.add(txtNumber);
            panel.add(new JLabel("Cage Size:"));
            panel.add(cmbSize);
            panel.add(new JLabel("Max Animals:"));
            panel.add(txtMaxAnimals);
            panel.add(new JLabel(""));
            panel.add(btnSave);

            contentPanel.add(panel, BorderLayout.CENTER);
            revalidate();
            repaint();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching cages: " + ex.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);

        }
    }

    private void deleteCageUI() {
        clearContent();
        try {
            List<Cage> cages = CageRAR.getCagesByZooId(currentZoo.getId());
            if (cages.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No cages available to delete for this zoo.", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                manageCageUI();
                return;
            }
            JComboBox<String> cageComboBox = new JComboBox<>(
                    cages.stream().map(this::formatCageInfo).toArray(String[]::new));

            JButton deleteButton = new JButton("Delete Cage");
            deleteButton.addActionListener(e -> {
                String selectedCageInfo = (String) cageComboBox.getSelectedItem();
                int cageId = extractCageIdFromInfo(selectedCageInfo);
                Cage selectedCage = cages.stream().filter(c -> c.getNumber() == cageId).findFirst().orElse(null);
                if (selectedCage != null) {
                    deleteCage(selectedCage);
                }
            });

            JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
            panel.add(new JLabel("Select Cage to Delete"));
            panel.add(cageComboBox);
            panel.add(deleteButton);
            contentPanel.add(panel, BorderLayout.CENTER);
            revalidate();
            repaint();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching cages: " + ex.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (NumberFormatException ex) { // Catch potential NumberFormatException
            JOptionPane.showMessageDialog(this,
                    "Error parsing cage ID.  Please ensure the cage list is displayed correctly.", "Parsing Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void manageAnimalUI() {
        clearContent();
        if (currentZoo == null) {
            JOptionPane.showMessageDialog(this, "Please select a zoo first.", "Error", JOptionPane.ERROR_MESSAGE);
            manageZooUI();
            return;
        }

        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        JLabel zooLabel = new JLabel("Current Zoo: " + currentZoo.getName(), SwingConstants.CENTER);
        JButton addAnimalButton = new JButton("Add Animal");
        JButton editAnimalButton = new JButton("Edit Animal");
        JButton deleteAnimalButton = new JButton("Delete Animal");
        JButton searchAnimalButton = new JButton("Search Animal");

        addAnimalButton.addActionListener(e -> addAnimalUI());
        editAnimalButton.addActionListener(e -> editAnimalUI());
        deleteAnimalButton.addActionListener(e -> deleteAnimalUI());
        searchAnimalButton.addActionListener(e -> searchAnimalUI());

        panel.add(zooLabel);
        panel.add(addAnimalButton);
        panel.add(editAnimalButton);
        panel.add(deleteAnimalButton);
        panel.add(searchAnimalButton);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void addAnimalUI() {
        clearContent();
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5)); // Use GridLayout
        panel.setBorder(BorderFactory.createTitledBorder("Add Animal"));

        JLabel lblName = new JLabel("Animal Name:");
        JTextField txtName = new JTextField();
        JLabel lblPredator = new JLabel("Is Predator:");
        JCheckBox chkPredator = new JCheckBox();
        JLabel lblCage = new JLabel("Select Cage:");
        JComboBox<String> cmbCage = new JComboBox<>();
        JButton btnAdd = new JButton("Add Animal");

        // Populate the cage ComboBox
        try {
            List<Cage> cages = CageRAR.getCagesByZooId(currentZoo.getId());
            if (cages.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No cages available.  Please create a cage first.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                manageCageUI();
                return;
            }
            for (Cage cage : cages) {
                cmbCage.addItem(formatCageInfo(cage));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching cages: " + ex.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return; // Important: Return to prevent further execution if cage loading fails.
        }

        btnAdd.addActionListener(e -> {
            String selectedCageInfo = (String) cmbCage.getSelectedItem();
            // Extract cage number. More robust parsing:
            int cageNumber = -1; // Default value in case of parsing error.
            try {
                cageNumber = extractCageIdFromInfo(selectedCageInfo);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error in the cage selection", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Get the actual Cage object from the database using cageNumber
            Cage selectedCage = null;
            try {
                List<Cage> cages = CageRAR.getCagesByZooId(currentZoo.getId());
                for (Cage cage : cages) {
                    if (cage.getNumber() == cageNumber) {
                        selectedCage = cage;
                        break;
                    }
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error getting cage: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (selectedCage == null) { // Check we got cage
                JOptionPane.showMessageDialog(this, "Selected cage does not exists", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            addAnimal(txtName, chkPredator, selectedCage.getId()); // Pass cage ID
        });

        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblPredator);
        panel.add(chkPredator);
        panel.add(lblCage);
        panel.add(cmbCage);
        panel.add(new JLabel()); // Spacer
        panel.add(btnAdd);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void editAnimalUI() {
        clearContent();
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Edit Animal"));

        JLabel lblCage = new JLabel("Select Cage:");
        JComboBox<String> cmbCage = new JComboBox<>();
        JLabel lblAnimal = new JLabel("Select Animal:");
        JComboBox<String> cmbAnimal = new JComboBox<>(); // ComboBox for animals
        JLabel lblName = new JLabel("Animal Name:"); // Declare lblName here
        JTextField txtName = new JTextField();
        JLabel lblPredator = new JLabel("Is Predator:"); // Declare lblPredator here
        JCheckBox chkPredator = new JCheckBox();
        JButton btnSave = new JButton("Save Changes");
        btnSave.setEnabled(false); // Disable initially
        txtName.setEnabled(false);
        chkPredator.setEnabled(false);

        // Populate cage ComboBox
        try {
            List<Cage> cages = CageRAR.getCagesByZooId(currentZoo.getId());
            if (cages.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No cages available. Please create a cage first.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                manageCageUI();
                return;
            }
            for (Cage cage : cages) {
                cmbCage.addItem(formatCageInfo(cage));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching cages: " + ex.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add ActionListener to cage ComboBox to populate animal ComboBox
        cmbCage.addActionListener(e -> {
            String selectedCageInfo = (String) cmbCage.getSelectedItem();
            int cageNumber = extractCageIdFromInfo(selectedCageInfo);
            Cage selectedCage = null;
            try {
                List<Cage> cages = CageRAR.getCagesByZooId(currentZoo.getId());
                for (Cage cage : cages) {
                    if (cage.getNumber() == cageNumber) {
                        selectedCage = cage;
                        break;
                    }
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error getting cage: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (selectedCage == null) {
                JOptionPane.showMessageDialog(this, "Selected cage does not exists", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            cmbAnimal.removeAllItems(); // Clear previous items
            txtName.setText(""); // Clear previous animal name
            chkPredator.setSelected(false); // Clear previous selection

            try {
                List<Animal> animals = AnimalRAR.getAnimalsByCageId(selectedCage.getId());
                if (animals.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No animals in this cage.", "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    btnSave.setEnabled(false); // Disable if no animals
                    txtName.setEnabled(false);
                    chkPredator.setEnabled(false);
                    return;
                }
                for (Animal animal : animals) {
                    cmbAnimal.addItem("ID: " + animal.getId() + ", Name: " + animal.getName());
                }
                btnSave.setEnabled(true); // Enable if animals exist
                txtName.setEnabled(true);
                chkPredator.setEnabled(true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error fetching animals: " + ex.getMessage(), "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add ActionListener to animal ComboBox to populate fields
        cmbAnimal.addActionListener(e -> {
            String selectedAnimalInfo = (String) cmbAnimal.getSelectedItem();
            if (selectedAnimalInfo != null) {
                int animalId = Integer.parseInt(selectedAnimalInfo
                        .substring(selectedAnimalInfo.indexOf(":") + 2, selectedAnimalInfo.indexOf(",")).trim());
                try {
                    List<Animal> animals = AnimalRAR.getAnimalsByCageId(
                            CageRAR.getCagesByZooId(currentZoo.getId()).get(cmbCage.getSelectedIndex()).getId());
                    Animal selectedAnimal = null;
                    for (Animal animal : animals) {
                        if (animal.getId() == animalId) {
                            selectedAnimal = animal;
                            break;
                        }
                    }
                    if (selectedAnimal != null) {
                        txtName.setText(selectedAnimal.getName());
                        chkPredator.setSelected(selectedAnimal.isPredator());
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error fetching animal details: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSave.addActionListener(e -> {
            String selectedAnimalInfo = (String) cmbAnimal.getSelectedItem();
            if (selectedAnimalInfo == null)
                return; // Prevent NullPointerException
            int animalId = Integer.parseInt(selectedAnimalInfo
                    .substring(selectedAnimalInfo.indexOf(":") + 2, selectedAnimalInfo.indexOf(",")).trim());
            try {
                List<Animal> animals = AnimalRAR.getAnimalsByCageId(
                        CageRAR.getCagesByZooId(currentZoo.getId()).get(cmbCage.getSelectedIndex()).getId());
                Animal selectedAnimal = null;
                for (Animal animal : animals) {
                    if (animal.getId() == animalId) {
                        selectedAnimal = animal;
                        break;
                    }
                }
                if (selectedAnimal == null) {
                    JOptionPane.showMessageDialog(this, "Error: Could not find selected animal.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                editAnimal(selectedAnimal, txtName, chkPredator);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error editing animal: " + ex.getMessage(), "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        panel.add(lblCage);
        panel.add(cmbCage);
        panel.add(lblAnimal);
        panel.add(cmbAnimal);
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblPredator);
        panel.add(chkPredator);
        panel.add(new JLabel()); // Spacer
        panel.add(btnSave);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void deleteAnimalUI() {
        clearContent();
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Delete Animal"));

        JLabel lblCage = new JLabel("Select Cage:");
        JComboBox<String> cmbCage = new JComboBox<>();
        JLabel lblAnimal = new JLabel("Select Animal:");
        JComboBox<String> cmbAnimal = new JComboBox<>();  // ComboBox for animals
        JButton btnDelete = new JButton("Delete Animal");
        btnDelete.setEnabled(false); // Disable initially


        // Populate cage ComboBox
        try {
            List<Cage> cages = CageRAR.getCagesByZooId(currentZoo.getId());
            if (cages.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No cages available. Please create a cage first.", "Error", JOptionPane.ERROR_MESSAGE);
                manageCageUI();
                return;
            }
            for (Cage cage : cages) {
                cmbCage.addItem(formatCageInfo(cage));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching cages: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add ActionListener to cage ComboBox to populate animal ComboBox
        cmbCage.addActionListener(e -> {
            String selectedCageInfo = (String) cmbCage.getSelectedItem();
            int cageNumber =  extractCageIdFromInfo(selectedCageInfo);
            Cage selectedCage = null;
            try {
                List<Cage> cages = CageRAR.getCagesByZooId(currentZoo.getId());
                for(Cage cage: cages){
                    if(cage.getNumber() == cageNumber){
                        selectedCage = cage;
                        break;
                    }
                }

            }catch (SQLException ex){
                 JOptionPane.showMessageDialog(this,"Error getting cage: "+ ex.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
                 return;
            }
            if(selectedCage == null) { // Check we got cage
                JOptionPane.showMessageDialog(this,"Selected cage does not exists", "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            cmbAnimal.removeAllItems(); // Clear previous items

            try {
                List<Animal> animals = AnimalRAR.getAnimalsByCageId(selectedCage.getId());
                if (animals.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No animals in this cage.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    btnDelete.setEnabled(false); //Disable button
                    return;
                }
                for (Animal animal : animals) {
                    cmbAnimal.addItem("ID: " + animal.getId() + ", Name: " + animal.getName());
                }
                btnDelete.setEnabled(true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error fetching animals: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });



        btnDelete.addActionListener(e -> {
            String selectedAnimalInfo = (String) cmbAnimal.getSelectedItem();
            if (selectedAnimalInfo == null) return; // Important: Handle case where no animal is selected.

            int animalId = Integer.parseInt(selectedAnimalInfo.substring(selectedAnimalInfo.indexOf(":") + 2, selectedAnimalInfo.indexOf(",")).trim());
             try{
                 List<Animal> animals = AnimalRAR.getAnimalsByCageId(CageRAR.getCagesByZooId(currentZoo.getId()).get(cmbCage.getSelectedIndex()).getId());
                  Animal selectedAnimal = null;
                    for(Animal animal: animals){
                        if(animal.getId() == animalId){
                            selectedAnimal = animal;
                            break;
                        }
                    }
                if(selectedAnimal == null){
                    JOptionPane.showMessageDialog(this, "Error: Could not find selected animal.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                deleteAnimal(selectedAnimal); // Pass the selected animal
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error deleting animal: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        panel.add(lblCage);
        panel.add(cmbCage);
        panel.add(lblAnimal);
        panel.add(cmbAnimal);
        panel.add(new JLabel()); // Spacer
        panel.add(btnDelete);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    private void searchAnimalUI() {
        clearContent();
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5)); // Use GridLayout
        panel.setBorder(BorderFactory.createTitledBorder("Search Animals"));

        JLabel lblCage = new JLabel("Select Cage:");
        JComboBox<String> cmbCage = new JComboBox<>();
        JLabel lblSearch = new JLabel("Search Term:");
        JTextField txtSearch = new JTextField();
        JButton btnSearch = new JButton("Search");

        // Populate the cage ComboBox
        try {
            List<Cage> cages = CageRAR.getCagesByZooId(currentZoo.getId());
            if (cages.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No cages available.  Please create a cage first.", "Error", JOptionPane.ERROR_MESSAGE);
                manageCageUI();
                return;
            }
            for (Cage cage : cages) {
                cmbCage.addItem(formatCageInfo(cage));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching cages: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return; // Important: Return to prevent further execution if cage loading fails.
        }


        btnSearch.addActionListener(e -> {
             String selectedCageInfo = (String) cmbCage.getSelectedItem();
            int cageNumber =  extractCageIdFromInfo(selectedCageInfo);
            Cage selectedCage = null;
            try {
                List<Cage> cages = CageRAR.getCagesByZooId(currentZoo.getId());
                for(Cage cage: cages){
                    if(cage.getNumber() == cageNumber){
                        selectedCage = cage;
                        break;
                    }
                }

            }catch (SQLException ex){
                 JOptionPane.showMessageDialog(this,"Error getting cage: "+ ex.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
                 return;
            }
            if(selectedCage == null) { // Check we got cage
                JOptionPane.showMessageDialog(this,"Selected cage does not exists", "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            String searchTerm = txtSearch.getText().trim();
            searchAnimal(selectedCage.getId(), searchTerm); // Use the selected cage's ID

        });

        panel.add(lblCage);
        panel.add(cmbCage);
        panel.add(lblSearch);
        panel.add(txtSearch);
        panel.add(new JLabel()); // Spacer
        panel.add(btnSearch);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }


     public void displayAnimalsUI() {
        clearContent();
        JPanel panel = new JPanel(new BorderLayout()); // Use BorderLayout

        try {
            List<Animal> animals = AnimalRAR.getAllAnimals();
            if (animals.isEmpty()) {
                panel.add(new JLabel("No animals in the zoo yet.", SwingConstants.CENTER), BorderLayout.CENTER);
            } else {
                // Create a table model
                String[] columnNames = {"ID", "Name", "Predator", "Cage ID"}; // Added Cage ID
                Object[][] data = new Object[animals.size()][4]; // Increased size for Cage ID
                for (int i = 0; i < animals.size(); i++) {
                    Animal animal = animals.get(i);
                    data[i][0] = animal.getId();
                    data[i][1] = animal.getName();
                    data[i][2] = animal.isPredator() ? "Yes" : "No";
                    data[i][3] = animal.getCageId(); // Add Cage ID
                }
                JTable table = new JTable(data, columnNames);
                JScrollPane scrollPane = new JScrollPane(table); // Add table to scroll pane
                panel.add(scrollPane, BorderLayout.CENTER); // Add scroll pane to panel
            }
        } catch (SQLException ex) {
            panel.add(new JLabel("Error fetching animals: " + ex.getMessage(), SwingConstants.CENTER), BorderLayout.CENTER);
            ex.printStackTrace(); // Log the exception
        }


        contentPanel.add(panel, BorderLayout.CENTER); // Add the panel to the frame
        revalidate(); // Revalidate the frame to ensure components are displayed
        repaint();    // Repaint the frame
    }

// Action Logic methods (createZoo, editZoo, deleteZoo, createCage, editCage, deleteCage remain unchanged)
    private void createZoo(String zooName) {
        if (zooName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Zoo name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Check if zoo with the same name already exists
            List<Zoo> existingZoos = ZooRAR.getAllZoos();
            boolean nameExists = existingZoos.stream().anyMatch(zoo -> zoo.getName().equalsIgnoreCase(zooName));

            if(nameExists){
                JOptionPane.showMessageDialog(this, "Zoo with this name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method
            }
            Zoo zoo = new Zoo(zooName);
            ZooRAR.addZoo(zoo);
            currentZoo = zoo;
            JOptionPane.showMessageDialog(this, "Zoo created successfully!");
            mainMenuUI();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editZoo(Zoo selectedZoo, String newName){
        try{
            //check if new name taken
            List<Zoo> existingZoos = ZooRAR.getAllZoos();
            boolean nameExists = existingZoos.stream().anyMatch(zoo -> zoo.getName().equalsIgnoreCase(newName) && zoo.getId() != selectedZoo.getId());

            if(nameExists){
                JOptionPane.showMessageDialog(this,"Zoo with this name already exist.","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            selectedZoo.setName(newName);
            ZooRAR.updateZoo(selectedZoo); // Assuming you have an update method
            JOptionPane.showMessageDialog(this, "Zoo updated successfully!");
            mainMenuUI();


        }catch (SQLException ex){
            JOptionPane.showMessageDialog(this, "Error updating zoo: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);

        }
    }
    private void deleteZoo(Zoo selectedZoo){

        int response = JOptionPane.showConfirmDialog(this,
                "Do you really want to delete zoo \"" + selectedZoo.getName() + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            try {

                if(currentZoo != null && currentZoo.getId() == selectedZoo.getId()){
                    currentZoo = null;
                }
                ZooRAR.deleteZoo(selectedZoo.getId()); // Assuming you have a delete method.
                JOptionPane.showMessageDialog(this, "Zoo deleted successfully!");
                manageZooUI(); // Go back to manage zoo UI
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting zoo: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void createCage(JTextField txtNumber, JComboBox<String> cmbSize, JTextField txtMaxAnimals) {
        try {
            int number = Integer.parseInt(txtNumber.getText().trim());
            String size = (String) cmbSize.getSelectedItem();
            int maxAnimals = Integer.parseInt(txtMaxAnimals.getText().trim());

            // Check for positive maxAnimals
            if (maxAnimals <= 0) {
                JOptionPane.showMessageDialog(this, "Max Animals must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Check for positive cage number
            if (number <= 0) {
                JOptionPane.showMessageDialog(this, "Cage number must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //check cage number
            List<Cage> existingCages = CageRAR.getCagesByZooId(currentZoo.getId());
            boolean numberExists = existingCages.stream().anyMatch(cage -> cage.getNumber() == number);
            if(numberExists){
                JOptionPane.showMessageDialog(this,"Cage with this number already exist","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Size-based max animal limit
            int maxSizeLimit = 0;
            switch (size) {
                case "Small":
                    maxSizeLimit = 15;
                    break;
                case "Medium":
                    maxSizeLimit = 30;
                    break;
                case "Large":
                    maxSizeLimit = 50;
                    break;
            }

            if (maxAnimals > maxSizeLimit) {
                JOptionPane.showMessageDialog(this, "Max Animals cannot exceed " + maxSizeLimit + " for a " + size + " cage.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if(currentZoo == null){
                JOptionPane.showMessageDialog(this, "Please create a Zoo first.", "Error", JOptionPane.ERROR_MESSAGE);
                createZooUI(); // Redirect to create zoo UI
                return;
            }
            Cage cage = new Cage(currentZoo.getId(), number, convertSize(size), maxAnimals);
            CageRAR.addCage(cage);
            JOptionPane.showMessageDialog(this, "Cage created successfully!");
            manageCageUI(); // Return to manageCageUI after successful creation
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format. Please enter numeric values for Number and Max Animals.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving cage: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void editCage(Cage selectedCage, JTextField txtNumber, JComboBox<String> cmbSize, JTextField txtMaxAnimals){
        try {

            int number = !txtNumber.getText().trim().isEmpty() ? Integer.parseInt(txtNumber.getText().trim()) : selectedCage.getNumber();
            String size = (String) cmbSize.getSelectedItem() != null ? (String) cmbSize.getSelectedItem() : convertSizeToString(selectedCage.getSize()) ;
            int maxAnimals = !txtMaxAnimals.getText().trim().isEmpty() ? Integer.parseInt(txtMaxAnimals.getText().trim()) : selectedCage.getMaxAnimals();

            // Check for positive maxAnimals
            if (maxAnimals <= 0) {
                JOptionPane.showMessageDialog(this, "Max Animals must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Check for positive cage number
            if (number <= 0) {
                JOptionPane.showMessageDialog(this, "Cage number must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Size-based max animal limit (for edit)
            int maxSizeLimit = 0;
            switch (size) {
                case "Small":
                    maxSizeLimit = 15;
                    break;
                case "Medium":
                    maxSizeLimit = 30;
                    break;
                case "Large":
                    maxSizeLimit = 50;
                    break;
            }
            if (maxAnimals > maxSizeLimit) {
                JOptionPane.showMessageDialog(this, "Max Animals cannot exceed " + maxSizeLimit + " for a " + size + " cage.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Cage> existingCages = CageRAR.getCagesByZooId(currentZoo.getId());
            boolean numberExist = existingCages.stream().anyMatch(cage -> cage.getNumber() == number && cage.getNumber() != selectedCage.getNumber());
            if(numberExist){
                JOptionPane.showMessageDialog(this,"Cage with this number already exist","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            int currentAnimalCount = AnimalRAR.getAnimalsByCageId(selectedCage.getNumber()).size(); // Get current animal count - Use cage number, as ID might change
            if(maxAnimals < currentAnimalCount) {
                JOptionPane.showMessageDialog(this,
                        "Cannot reduce max animals below current number of animals in the cage ("+ currentAnimalCount + ").",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            selectedCage.setNumber(number);
            selectedCage.setSize(convertSize(size));
            selectedCage.setMaxAnimals(maxAnimals);
            CageRAR.updateCage(selectedCage); //update method
            JOptionPane.showMessageDialog(this,"Cage updated successfully!");
            manageCageUI();

        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Invalid input, please enter number","Error",JOptionPane.ERROR_MESSAGE);
        }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(this, "Error updating cage: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);

        }
    }
    private void deleteCage(Cage selectedCage){
        int response = JOptionPane.showConfirmDialog(this,
                "Do you really want to delete cage with ID: " + selectedCage.getNumber() + " and size: "+ convertSizeToString(selectedCage.getSize()) + " ?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            try {
                CageRAR.deleteCage(selectedCage.getId()); // Corrected: Use getId(), not getNumber()
                JOptionPane.showMessageDialog(this, "Cage deleted successfully!");
                manageCageUI(); // Go back to manage Cage UI
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting cage: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void addAnimal(JTextField txtName, JCheckBox chkPredator, int cageId) {
        String name = txtName.getText().trim();
        boolean isPredator = chkPredator.isSelected();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Animal name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Check if adding the animal exceeds the cage's capacity
            Cage cage = CageRAR.getCageById(cageId); // Get cage to check the max animals
            if(cage == null){ //check if cage exists
                JOptionPane.showMessageDialog(this, "Selected cage does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int currentAnimalCount = AnimalRAR.getAnimalsByCageId(cageId).size();
            if (currentAnimalCount >= cage.getMaxAnimals()) {
                JOptionPane.showMessageDialog(this, "This cage is already at maximum capacity.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Animal animal = new Animal(cageId, name, isPredator);
            AnimalRAR.addAnimal(animal);
            JOptionPane.showMessageDialog(this, "Animal added successfully!");
            manageAnimalUI(); // Go back to animal management
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding animal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editAnimal(Animal selectedAnimal, JTextField txtName, JCheckBox chkPredator) {
        try {
            String newName = txtName.getText().trim();
            boolean newIsPredator = chkPredator.isSelected();

            // Input validation (ensure name isn't empty)
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Animal name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            selectedAnimal.setName(newName);
            selectedAnimal.setPredator(newIsPredator);
            AnimalRAR.updateAnimal(selectedAnimal);
            JOptionPane.showMessageDialog(this, "Animal updated successfully!");
            manageAnimalUI(); // Return to the manage animal UI

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating animal: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAnimal(Animal selectedAnimal) {
        int response = JOptionPane.showConfirmDialog(this,
                "Do you really want to delete animal \"" + selectedAnimal.getName() + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            try {
                AnimalRAR.deleteAnimal(selectedAnimal.getId());
                JOptionPane.showMessageDialog(this, "Animal deleted successfully!");
                manageAnimalUI(); // Go back to manage animal UI
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting animal: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void searchAnimal(int cageId, String searchText) {
        clearContent();
        JPanel panel = new JPanel(new BorderLayout());
        try {
            List<Animal> foundAnimals = AnimalRAR.searchAnimalsByNameAndCage(searchText, cageId);
            if (foundAnimals.isEmpty()) {
                panel.add(new JLabel("No animals found matching the search criteria.", SwingConstants.CENTER), BorderLayout.CENTER);
            } else {
                // Display results (similar to displayAnimalsUI, but using foundAnimals)
                String[] columnNames = {"ID", "Name", "Predator", "Cage ID"};
                Object[][] data = new Object[foundAnimals.size()][4];
                for (int i = 0; i < foundAnimals.size(); i++) {
                    Animal animal = foundAnimals.get(i);
                    data[i][0] = animal.getId();
                    data[i][1] = animal.getName();
                    data[i][2] = animal.isPredator() ? "Yes" : "No";
                    data[i][3] = animal.getCageId();
                }
                JTable table = new JTable(data, columnNames);
                JScrollPane scrollPane = new JScrollPane(table);
                panel.add(scrollPane, BorderLayout.CENTER);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching for animals: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        JButton backButton = new JButton("Back to Manage Animals"); //back button
        backButton.addActionListener(e -> manageAnimalUI());
        panel.add(backButton,BorderLayout.SOUTH);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private String formatCageInfo(Cage cage) {
        try {
            int animalCount = AnimalRAR.getAnimalsByCageId(cage.getId()).size();
            return "ID: " + cage.getNumber() + ", Size: " + convertSizeToString(cage.getSize()) + ", Animals: " + animalCount + "/" + cage.getMaxAnimals();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error getting animal count: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return "ID: " + cage.getNumber() + ", Size: " + convertSizeToString(cage.getSize()) + ", Animals: Error"; // Return something informative
        }
    }
     private int extractCageIdFromInfo(String cageInfo){
        return Integer.parseInt(cageInfo.substring(cageInfo.indexOf(":") + 2, cageInfo.indexOf(",")).trim());
     }

    private String convertSizeToString(int size) {
        switch (size) {
            case 1:  return "Small";
            case 2:  return "Medium";
            case 3:  return "Large";
            default: return "Unknown"; // Or throw an exception, depending on your needs
        }
    }


    // *************  HELPER FUNCTIONS **********************
    private void clearContent() {
        contentPanel.removeAll(); // Remove all components from the contentPanel
        contentPanel.setLayout(new BorderLayout()); // Ensure BorderLayout is set
        revalidate(); // Revalidate after changes
        repaint(); // Repaint to reflect changes

    }
    private int convertSize(String size) {
        switch (size) {
            case "Small":
                return 1;
            case "Medium":
                return 2;
            case "Large":
                return 3;
            default:
                throw new IllegalArgumentException("Invalid cage size: " + size);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UISystem().setVisible(true);
        });
    }
}