


public class HomeView extends JPanel {

    private JLabel welcomeLabel = new JLabel("Welcome!", SwingConstants.CENTER);
    private JButton goToSettings = new JButton("Go to Settings");

    public HomeView() {
        setLayout(new BorderLayout());
        add(welcomeLabel, BorderLayout.CENTER);
        add(goToSettings, BorderLayout.SOUTH);
    }

    public void setWelcomeText(String text) {
        welcomeLabel.setText(text);
    }

    public void onGoSettings(ActionListener listener) {
        goToSettings.addActionListener(listener);
    }
}


public class HomeController {

    private final HomeView view;
    private final MainModel model;
    private final MainView mainView;

    public HomeController(HomeView view, MainModel model, MainView mainView) {
        this.view = view;
        this.model = model;
        this.mainView = mainView;

        // Update UI with model
        view.setWelcomeText("Welcome, " + model.getUsername() + "!");

        // Navigation event
        view.onGoSettings(e -> mainView.showView(MainView.SETTINGS));
    }

    public void refresh() {
        view.setWelcomeText("Welcome, " + model.getUsername() + "!");
    }
}

public class SettingsView extends JPanel {

    private JTextField nameField = new JTextField();
    private JButton saveButton = new JButton("Save");
    private JButton goHomeButton = new JButton("Back to Home");

    public SettingsView() {
        setLayout(new BorderLayout());

        JPanel center = new JPanel(new GridLayout(2, 1));
        center.add(new JLabel("Enter new username:"));
        center.add(nameField);

        JPanel bottom = new JPanel();
        bottom.add(saveButton);
        bottom.add(goHomeButton);

        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public String getEnteredName() {
        return nameField.getText();
    }

    public void onSave(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void onGoHome(ActionListener listener) {
        goHomeButton.addActionListener(listener);
    }
}
public class SettingsController {

    private final SettingsView view;
    private final MainModel model;
    private final MainView mainView;
    private final HomeController homeController;

    public SettingsController(SettingsView view, MainModel model,
                              MainView mainView, HomeController homeCtrl) {

        this.view = view;
        this.model = model;
        this.mainView = mainView;
        this.homeController = homeCtrl;

        // Save button logic
        view.onSave(e -> {
            String newName = view.getEnteredName().trim();

            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Name cannot be empty");
                return;
            }

            model.setUsername(newName);
            homeController.refresh();

            mainView.showView(MainView.HOME);
        });

        // Navigation event
        view.onGoHome(e -> mainView.showView(MainView.HOME));
    }
}

public class MainView extends JFrame {

    public static final String HOME = "home";
    public static final String SETTINGS = "settings";

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    public MainView() {
        super("Large MVC Example");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        add(cardPanel);
    }

    public void addView(String name, JPanel View) {
        cardPanel.add(View, name);
    }

    public void showView(String name) {
        cardLayout.show(cardPanel, name);
    }
}

public class MainController {

    private final MainModel model = new MainModel();
    private final MainView mainView = new MainView();

    public MainController() {
        // Create Views
        HomeView homeView = new HomeView();
        SettingsView settingsView = new SettingsView();

        // Add to view
        mainView.addView(MainView.HOME, homeView);
        mainView.addView(MainView.SETTINGS, settingsView);

        // Create controllers
        HomeController homeController = new HomeController(homeView, model, mainView);
        new SettingsController(settingsView, model, mainView, homeController);

        mainView.showView(MainView.HOME);
        mainView.setVisible(true);
    }
}
public class MainModel {
    private String username = "Guest";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

public class AppMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainController::new);
    }
}

public class GridPage extends JPanel {

    private JButton[][] buttons = new JButton[10][10];

    public GridPage() {
        setLayout(new GridLayout(10, 10));

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                JButton b = new JButton(r + "," + c);
                buttons[r][c] = b;
                add(b);
            }
        }
    }

    // Allow controller to register a listener for each cell
    public void onCellClick(int row, int col, ActionListener listener) {
        buttons[row][col].addActionListener(listener);
    }
}

public class GridController {

    private final GridPage view;
    private final MainController mainController;

    public GridController(GridPage view, MainController mainController) {
        this.view = view;
        this.mainController = mainController;

        // Register listeners for all 100 buttons
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {

                int row = r;
                int col = c;

                view.onCellClick(row, col, e ->
                    mainController.onGridCellClicked(row, col)
                );
            }
        }
    }
}

