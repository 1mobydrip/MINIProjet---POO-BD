package tn.univ.pharmacie.ui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Login");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblUser = new JLabel("Username:");
        JLabel lblPass = new JLabel("Password:");

        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);

        btnLogin = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblUser, gbc);

        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblPass, gbc);

        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(btnLogin, gbc);

        add(panel);

        btnLogin.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // CALL SERVICE HERE (no DB, no SQL)
        System.out.println("Login attempt: " + username);
    }
}
