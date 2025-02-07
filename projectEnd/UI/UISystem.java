package UI;

import RAR.*;
import Objects.*;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UISystem extends JFrame {

    private final ZooRAR ZooRAR = new ZooRAR();
    private final CageRAR CageRAR = new CageRAR();
    private final AnimalRAR AnimalRAR = new AnimalRAR();

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    private Zoo selectedZoo = null;

    public UISystem() {
        setTitle("Zoo Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();

        setLocationRelativeTo(null);
    }

    private void initUI() {
        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton selectZooButton = new JButton("Select Zoo");
        JButton cageButton = new JButton("Manage Cages");
        JButton animalButton = new JButton("Manage Animals");
        JButton zooButton = new JButton("Manage Zoos");
        JButton exitButton = new JButton("Exit");

        menuPanel.add(selectZooButton);
        menuPanel.add(cageButton);
        menuPanel.add(animalButton);
        menuPanel.add(zooButton);
        menuPanel.add(exitButton);

        JPanel menuContainer = new JPanel(new GridBagLayout());
        menuContainer.add(menuPanel);

        selectZooButton.addActionListener(e -> selectZoo());
        cageButton.addActionListener(e -> {
            if (selectedZoo != null) {
                showCagePanel();
            } else {
                showError("Please select a zoo first.");
            }
        });
        animalButton.addActionListener(e -> {
            if (selectedZoo != null) {
                showAnimalPanel();
            } else {
                showError("Please select a zoo first.");
            }
        });
        zooButton.addActionListener(e -> showZooPanel());
        exitButton.addActionListener(e -> System.exit(0));

        mainPanel.add(menuContainer, "Menu");
        mainPanel.add(createZooPanel(), "Zoo");
        mainPanel.add(createCagePanel(), "Cage");
        mainPanel.add(createAnimalPanel(), "Animal");

        add(mainPanel);

        cardLayout.show(mainPanel, "Menu");
    }

    private void selectZoo() {
        List<Zoo> zoos;
        try {
            zoos = ZooRAR.getAllZoos();
            if (zoos.isEmpty()) {
                showError("No zoos available. Please add a zoo first.");
                return;
            }
        } catch (SQLException e) {
            showError("Error retrieving zoos: " + e.getMessage());
            return;
        }

        Zoo zoo = (Zoo) JOptionPane.showInputDialog(
                this,
                "Select Zoo to work with:",
                "Select Zoo",
                JOptionPane.PLAIN_MESSAGE,
                null,
                zoos.toArray(),
                selectedZoo);

        if (zoo != null) {
            selectedZoo = zoo;
            JOptionPane.showMessageDialog(this, "Selected Zoo: " + selectedZoo.getName());
        }
    }

    private JPanel createZooPanel() {
        JPanel zooPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        JButton addZooButton = new JButton("Add Zoo");
        JButton editZooButton = new JButton("Edit Zoo");
        JButton deleteZooButton = new JButton("Delete Zoo");
        JButton backButton = new JButton("Back to Menu");

        topPanel.add(addZooButton);
        topPanel.add(editZooButton);
        topPanel.add(deleteZooButton);
        topPanel.add(backButton);

        DefaultListModel<Zoo> zooListModel = new DefaultListModel<>();
        JList<Zoo> zooList = new JList<>(zooListModel);
        JScrollPane scrollPane = new JScrollPane(zooList);

        loadZoos(zooListModel);

        addZooButton.addActionListener(e -> addZoo(zooListModel));
        editZooButton.addActionListener(e -> editZoo(zooList.getSelectedValue(), zooListModel));
        deleteZooButton.addActionListener(e -> deleteZoo(zooList.getSelectedValue(), zooListModel));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        zooPanel.add(topPanel, BorderLayout.NORTH);
        zooPanel.add(scrollPane, BorderLayout.CENTER);

        return zooPanel;
    }

    private JPanel createCagePanel() {
        JPanel cagePanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        JButton addCageButton = new JButton("Add Cage");
        JButton editCageButton = new JButton("Edit Cage");
        JButton deleteCageButton = new JButton("Delete Cage");
        JButton backButton = new JButton("Back to Menu");

        String zooName = (selectedZoo != null) ? selectedZoo.getName() : "None";
        JLabel selectedZooLabel = new JLabel("Working with Zoo: " + zooName);
        topPanel.add(selectedZooLabel);
    
        topPanel.add(addCageButton);
        topPanel.add(editCageButton);
        topPanel.add(deleteCageButton);
        topPanel.add(backButton);

        DefaultListModel<Cage> cageListModel = new DefaultListModel<>();
        JList<Cage> cageList = new JList<>(cageListModel);
        JScrollPane scrollPane = new JScrollPane(cageList);

        if (selectedZoo != null) {
            loadCages(cageListModel);
        }

        boolean hasZooSelected = selectedZoo != null;
        addCageButton.setEnabled(hasZooSelected);
        editCageButton.setEnabled(hasZooSelected);
        deleteCageButton.setEnabled(hasZooSelected);

        addCageButton.addActionListener(e -> addCage(cageListModel));
        editCageButton.addActionListener(e -> editCage(cageList.getSelectedValue(), cageListModel));
        deleteCageButton.addActionListener(e -> deleteCage(cageList.getSelectedValue(), cageListModel));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
    
        cagePanel.add(topPanel, BorderLayout.NORTH);
        cagePanel.add(scrollPane, BorderLayout.CENTER);
    
        return cagePanel;
    }
    
    private void loadCagesIntoComboBox(JComboBox<Cage> comboBox, int zooId) {
        comboBox.removeAllItems();
        try {
            List<Cage> cages = CageRAR.getCagesByZooId(zooId);
            for (Cage cage : cages) {
                comboBox.addItem(cage);
            }
        } catch (SQLException e) {
            showError("Error loading cages: " + e.getMessage());
        }
    }

    private void searchAnimalsByName(DefaultListModel<Animal> model, String name) {
        model.clear();
        try {
            List<Animal> animals = AnimalRAR.searchAnimalsByNameInZoo(name, selectedZoo.getId());
            for (Animal animal : animals) {
                model.addElement(animal);
            }
        } catch (SQLException e) {
            showError("Error searching animals: " + e.getMessage());
        }
    }
    
    private JPanel createAnimalPanel() {
        JPanel animalPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel zooCagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton addAnimalButton = new JButton("Add Animal");
        JButton editAnimalButton = new JButton("Edit Animal");
        JButton deleteAnimalButton = new JButton("Delete Animal");
        JButton backButton = new JButton("Back to Menu");

        String zooName = (selectedZoo != null) ? selectedZoo.getName() : "None";
        JLabel selectedZooLabel = new JLabel("Working with Zoo: " + zooName);

        JComboBox<Cage> cageComboBox = new JComboBox<>();
        if (selectedZoo != null) {
            loadCagesIntoComboBox(cageComboBox, selectedZoo.getId());
        }

        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        zooCagePanel.add(selectedZooLabel);
        zooCagePanel.add(new JLabel("Select Cage:"));
        zooCagePanel.add(cageComboBox);

        searchPanel.add(new JLabel("Search by Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        buttonPanel.add(addAnimalButton);
        buttonPanel.add(editAnimalButton);
        buttonPanel.add(deleteAnimalButton);

        backPanel.add(backButton);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(searchPanel);
        centerPanel.add(buttonPanel);

        topPanel.add(zooCagePanel, BorderLayout.NORTH);
        topPanel.add(centerPanel, BorderLayout.CENTER);
        topPanel.add(backPanel, BorderLayout.EAST);

        DefaultListModel<Animal> animalListModel = new DefaultListModel<>();
        JList<Animal> animalList = new JList<>(animalListModel);
        JScrollPane scrollPane = new JScrollPane(animalList);

        Cage selectedCage = (Cage) cageComboBox.getSelectedItem();
    
        if (selectedCage != null) {
            loadAnimals(animalListModel, selectedCage.getId());
        }

        boolean hasCageSelected = selectedCage != null;
        addAnimalButton.setEnabled(hasCageSelected);
        editAnimalButton.setEnabled(hasCageSelected);
        deleteAnimalButton.setEnabled(hasCageSelected);

        cageComboBox.addActionListener(e -> {
            Cage cage = (Cage) cageComboBox.getSelectedItem();
            if (cage != null) {
                loadAnimals(animalListModel, cage.getId());
                addAnimalButton.setEnabled(true);
                editAnimalButton.setEnabled(true);
                deleteAnimalButton.setEnabled(true);
            } else {
                animalListModel.clear();
                addAnimalButton.setEnabled(false);
                editAnimalButton.setEnabled(false);
                deleteAnimalButton.setEnabled(false);
            }
        });

        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                searchAnimalsByName(animalListModel, searchTerm);
            } else {
                Cage cage = (Cage) cageComboBox.getSelectedItem();
                if (cage != null) {
                    loadAnimals(animalListModel, cage.getId());
                }
            }
        });

        addAnimalButton.addActionListener(e -> addAnimal(animalListModel, cageComboBox));
        editAnimalButton.addActionListener(e -> editAnimal(animalList.getSelectedValue(), animalListModel));
        deleteAnimalButton.addActionListener(e -> deleteAnimal(animalList.getSelectedValue(), animalListModel));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        animalPanel.add(topPanel, BorderLayout.NORTH);
        animalPanel.add(scrollPane, BorderLayout.CENTER);
    
        return animalPanel;
    }
    
    
    private void showZooPanel() {
        cardLayout.show(mainPanel, "Zoo");
    }

    private void showCagePanel() {
        mainPanel.remove(2);
        mainPanel.add(createCagePanel(), "Cage");
        cardLayout.show(mainPanel, "Cage");
    }

    private void showAnimalPanel() {
        mainPanel.remove(3);
        mainPanel.add(createAnimalPanel(), "Animal");
        cardLayout.show(mainPanel, "Animal");
    }

    private void loadZoos(DefaultListModel<Zoo> model) {
        model.clear();
        try {
            List<Zoo> zoos = ZooRAR.getAllZoos();
            for (Zoo zoo : zoos) {
                model.addElement(zoo);
            }
        } catch (SQLException e) {
            showError("Error loading zoos: " + e.getMessage());
        }
    }

    private void loadCages(DefaultListModel<Cage> model) {
        model.clear();
        try {
            List<Cage> cages = CageRAR.getCagesByZooId(selectedZoo.getId());
            for (Cage cage : cages) {
                model.addElement(cage);
            }
        } catch (SQLException e) {
            showError("Error loading cages: " + e.getMessage());
        }
    }

    private void loadAnimals(DefaultListModel<Animal> model, int cageId) {
        model.clear();
        try {
            List<Animal> animals = AnimalRAR.getAnimalsByCageId(cageId);
            for (Animal animal : animals) {
                model.addElement(animal);
            }
        } catch (SQLException e) {
            showError("Error loading animals: " + e.getMessage());
        }
    }
    
    private void addZoo(DefaultListModel<Zoo> model) {
        String name = JOptionPane.showInputDialog(this, "Enter Zoo Name:");
        if (name != null && !name.trim().isEmpty()) {
            Zoo zoo = new Zoo();
            zoo.setName(name.trim());
            try {
                ZooRAR.addZoo(zoo);
                model.addElement(zoo);
            } catch (SQLException e) {
                showError("Error adding zoo: " + e.getMessage());
            }
        }
    }

    private void editZoo(Zoo zoo, DefaultListModel<Zoo> model) {
        if (zoo == null) {
            showError("Please select a zoo to edit.");
            return;
        }
        String name = JOptionPane.showInputDialog(this, "Enter New Zoo Name:", zoo.getName());
        if (name != null && !name.trim().isEmpty()) {
            zoo.setName(name.trim());
            try {
                ZooRAR.updateZoo(zoo);
                int index = model.indexOf(zoo);
                model.set(index, zoo);
            } catch (SQLException e) {
                showError("Error updating zoo: " + e.getMessage());
            }
        }
    }

    private void deleteZoo(Zoo zoo, DefaultListModel<Zoo> model) {
        if (zoo == null) {
            showError("Please select a zoo to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this zoo?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ZooRAR.deleteZoo(zoo.getId());
                model.removeElement(zoo);
                if (selectedZoo != null && selectedZoo.getId() == zoo.getId()) {
                    selectedZoo = null;
                }
            } catch (SQLException e) {
                showError("Error deleting zoo: " + e.getMessage());
            }
        }
    }

    private void addCage(DefaultListModel<Cage> model) {
        if (selectedZoo == null) {
            showError("No zoo selected.");
            return;
        }
        JTextField numberField = new JTextField();
        JTextField sizeField = new JTextField();
        JTextField maxAnimalsField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Cage Number:"));
        panel.add(numberField);
        panel.add(new JLabel("Cage Size:"));
        panel.add(sizeField);
        panel.add(new JLabel("Max Animals:"));
        panel.add(maxAnimalsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Cage", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Cage cage = new Cage();
                cage.setZooId(selectedZoo.getId());
                cage.setNumber(Integer.parseInt(numberField.getText()));
                cage.setSize(Integer.parseInt(sizeField.getText()));
                cage.setMaxAnimals(Integer.parseInt(maxAnimalsField.getText()));
                cage.setCurrentAnimals(0);

                CageRAR.addCage(cage);
                model.addElement(cage);
            } catch (SQLException | NumberFormatException e) {
                showError("Error adding cage: " + e.getMessage());
            }
        }
    }

    private void editCage(Cage cage, DefaultListModel<Cage> model) {
        if (cage == null) {
            showError("Please select a cage to edit.");
            return;
        }
        JTextField numberField = new JTextField(String.valueOf(cage.getNumber()));
        JTextField sizeField = new JTextField(String.valueOf(cage.getSize()));
        JTextField maxAnimalsField = new JTextField(String.valueOf(cage.getMaxAnimals()));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Cage Number:"));
        panel.add(numberField);
        panel.add(new JLabel("Cage Size:"));
        panel.add(sizeField);
        panel.add(new JLabel("Max Animals:"));
        panel.add(maxAnimalsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Cage", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                cage.setNumber(Integer.parseInt(numberField.getText()));
                cage.setSize(Integer.parseInt(sizeField.getText()));
                cage.setMaxAnimals(Integer.parseInt(maxAnimalsField.getText()));

                CageRAR.updateCage(cage);
                int index = model.indexOf(cage);
                model.set(index, cage); // Update the list model
            } catch (SQLException | NumberFormatException e) {
                showError("Error updating cage: " + e.getMessage());
            }
        }
    }

    private void deleteCage(Cage cage, DefaultListModel<Cage> model) {
        if (cage == null) {
            showError("Please select a cage to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this cage?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                CageRAR.deleteCage(cage.getId());
                model.removeElement(cage);
            } catch (SQLException e) {
                showError("Error deleting cage: " + e.getMessage());
            }
        }
    }

    private void addAnimal(DefaultListModel<Animal> model, JComboBox<Cage> cageComboBox) {
        Cage selectedCage = (Cage) cageComboBox.getSelectedItem();
        if (selectedCage == null) {
            showError("Please select a cage first.");
            return;
        }
    
        try {
            if (CageRAR.isCageAtMaxCapacity(selectedCage.getId())) {
                showError("Selected cage is at maximum capacity.");
                return;
            }
        } catch (SQLException e) {
            showError("Error checking cage capacity: " + e.getMessage());
            return;
        }
    
        JTextField nameField = new JTextField();
        JCheckBox isPredatorCheckbox = new JCheckBox("Is Predator");
    
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Animal Name:"));
        panel.add(nameField);
        panel.add(isPredatorCheckbox);
    
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Animal", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Animal animal = new Animal();
                animal.setCageId(selectedCage.getId());
                animal.setName(nameField.getText().trim());
                animal.setPredator(isPredatorCheckbox.isSelected());
    
                AnimalRAR.addAnimal(animal);
                CageRAR.incrementCurrentAnimals(selectedCage.getId());
                model.addElement(animal);
            } catch (SQLException e) {
                showError("Error adding animal: " + e.getMessage());
            }
        }
    }

    private void editAnimal(Animal animal, DefaultListModel<Animal> model) {
        if (animal == null) {
            showError("Please select an animal to edit.");
            return;
        }

        JTextField nameField = new JTextField(animal.getName());
        JCheckBox isPredatorCheckbox = new JCheckBox("Is Predator", animal.isPredator());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Animal Name:"));
        panel.add(nameField);
        panel.add(isPredatorCheckbox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Animal", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                animal.setName(nameField.getText().trim());
                animal.setPredator(isPredatorCheckbox.isSelected());

                AnimalRAR.updateAnimal(animal);
                int index = model.indexOf(animal);
                model.set(index, animal);
            } catch (SQLException e) {
                showError("Error updating animal: " + e.getMessage());
            }
        }
    }

    private void deleteAnimal(Animal animal, DefaultListModel<Animal> model) {
        if (animal == null) {
            showError("Please select an animal to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this animal?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                AnimalRAR.deleteAnimal(animal.getId());
                CageRAR.decrementCurrentAnimals(animal.getCageId());
                model.removeElement(animal);
            } catch (SQLException e) {
                showError("Error deleting animal: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}