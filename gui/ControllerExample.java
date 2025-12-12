public class HomePage extends JPanel {
    JButton saveButton = new JButton("Save");

    public HomePage() {
        add(saveButton);
    }

    public void onSave(ActionListener listener) {
        saveButton.addActionListener(listener);
    }
}

public class HomeController {
    private final HomePage view;
    private final UserModel model;

    public HomeController(HomePage view, UserModel model) {
        this.view = view;
        this.model = model;

        view.onSave(e -> saveUser());
    }

    private void saveUser() {
        model.save();
        JOptionPane.showMessageDialog(view, "Saved!");
    }
}

