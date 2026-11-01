
package vista;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import controller.UserController;

public class RegisterView extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField txtNombre;
    private JTextField txtApellidos;
    private JTextField txtEmail;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField txtFechaNacimiento;

    // Modern color palette
    private final Color BACKGROUND = new Color(243, 246, 252);
    private final Color NAVY = new Color(25, 35, 58);
    private final Color BLUE = new Color(55, 105, 220);
    private final Color TEXT = new Color(45, 55, 72);
    private final Color MUTED = new Color(115, 125, 145);
    private final Color WHITE = Color.WHITE;
    private final Color BORDER = new Color(220, 226, 237);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    RegisterView frame = new RegisterView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public RegisterView() {
        setTitle("GymApp | Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(540, 760);
        setMinimumSize(new Dimension(540, 760));
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(BACKGROUND);
        contentPane.setBorder(new EmptyBorder(25, 30, 25, 30));
        setContentPane(contentPane);

        // Main white card
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(22, 30, 22, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 0);

        // App title
        JLabel lblTitle = new JLabel("GYMAPP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(NAVY);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 2, 0);
        card.add(lblTitle, gbc);

        JLabel lblSubtitle = new JLabel(
                "Crea tu cuenta",
                SwingConstants.CENTER
        );
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(MUTED);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        card.add(lblSubtitle, gbc);

        // Nombre
        JLabel lblNombre = createFieldLabel("Nombre");

        gbc.gridy = 2;
        gbc.insets = new Insets(4, 0, 3, 0);
        card.add(lblNombre, gbc);

        txtNombre = new JTextField();
        styleInput(txtNombre);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(txtNombre, gbc);

        // Apellidos
        JLabel lblApellidos = createFieldLabel("Apellidos");

        gbc.gridy = 4;
        gbc.insets = new Insets(4, 0, 3, 0);
        card.add(lblApellidos, gbc);

        txtApellidos = new JTextField();
        styleInput(txtApellidos);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(txtApellidos, gbc);

        // Email
        JLabel lblEmail = createFieldLabel("Email");

        gbc.gridy = 6;
        gbc.insets = new Insets(4, 0, 3, 0);
        card.add(lblEmail, gbc);

        txtEmail = new JTextField();
        styleInput(txtEmail);

        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(txtEmail, gbc);

        // Contraseña
        JLabel lblContrasena = createFieldLabel("Contraseña");

        gbc.gridy = 8;
        gbc.insets = new Insets(4, 0, 3, 0);
        card.add(lblContrasena, gbc);

        passwordField = new JPasswordField();
        styleInput(passwordField);

        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(passwordField, gbc);

        // Confirmar contraseña
        JLabel lblConfirmarContrasena =
                createFieldLabel("Confirmar contraseña");

        gbc.gridy = 10;
        gbc.insets = new Insets(4, 0, 3, 0);
        card.add(lblConfirmarContrasena, gbc);

        confirmPasswordField = new JPasswordField();
        styleInput(confirmPasswordField);

        gbc.gridy = 11;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(confirmPasswordField, gbc);

        // Fecha de nacimiento
        JLabel lblFechaNacimiento = createFieldLabel(
                "Fecha de nacimiento (DD/MM/AAAA)"
        );

        gbc.gridy = 12;
        gbc.insets = new Insets(4, 0, 3, 0);
        card.add(lblFechaNacimiento, gbc);

        txtFechaNacimiento = new JTextField();
        styleInput(txtFechaNacimiento);

        gbc.gridy = 13;
        gbc.insets = new Insets(0, 0, 12, 0);
        card.add(txtFechaNacimiento, gbc);

        // Botón: Registrarse
        JButton btnRegistrar = new JButton("Crear cuenta");
        stylePrimaryButton(btnRegistrar);

        gbc.gridy = 14;
        gbc.insets = new Insets(3, 0, 5, 0);
        card.add(btnRegistrar, gbc);

        // Enlace/Botón: Volver al Login
        JButton btnVolverLogin = new JButton("¿Ya tienes cuenta? Inicia sesión");
        styleLinkButton(btnVolverLogin);

        gbc.gridy = 15;
        gbc.insets = new Insets(3, 0, 0, 0);
        card.add(btnVolverLogin, gbc);

        // Add card to the window
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.weightx = 1;
        mainGbc.weighty = 1;
        mainGbc.fill = GridBagConstraints.BOTH;

        contentPane.add(card, mainGbc);

        // ORIGINAL REGISTRATION LOGIC
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Get the values from the fields
                String nombre = txtNombre.getText().trim();
                String apellidos = txtApellidos.getText().trim();
                String email = txtEmail.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmarPassword =
                        new String(confirmPasswordField.getPassword());
                String fechaNacimiento =
                        txtFechaNacimiento.getText().trim();

                // Check that the fields are not empty
                if (nombre.isEmpty()
                        || apellidos.isEmpty()
                        || email.isEmpty()
                        || password.isEmpty()
                        || confirmarPassword.isEmpty()
                        || fechaNacimiento.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            RegisterView.this,
                            "Completa todos los campos."
                    );
                    return;
                }

                // Check that both passwords match
                if (!password.equals(confirmarPassword)) {
                    JOptionPane.showMessageDialog(
                            RegisterView.this,
                            "Las contraseñas no coinciden."
                    );
                    return;
                }

                // Check that the name contains valid characters
                if (!nombre.matches("[\\p{L}][\\p{L} '\\-]*")) {
                    JOptionPane.showMessageDialog(
                            RegisterView.this,
                            "Introduce un nombre válido."
                    );
                    return;
                }

                // Check that the surname contains valid characters
                if (!apellidos.matches("[\\p{L}][\\p{L} '\\-]*")) {
                    JOptionPane.showMessageDialog(
                            RegisterView.this,
                            "Introduce unos apellidos válidos."
                    );
                    return;
                }

                // Check that the email has a valid format
                if (!email.matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    JOptionPane.showMessageDialog(
                            RegisterView.this,
                            "Introduce un email válido."
                    );
                    return;
                }

                // Check that the password has at least 8 characters
                if (password.length() < 8) {
                    JOptionPane.showMessageDialog(
                            RegisterView.this,
                            "La contraseña debe tener al menos 8 caracteres."
                    );
                    return;
                }

                // Check that the birth date has the format dd/MM/yyyy
                try {
                    java.time.format.DateTimeFormatter formatter =
                            java.time.format.DateTimeFormatter
                                    .ofPattern("dd/MM/uuuu")
                                    .withResolverStyle(
                                            java.time.format.ResolverStyle.STRICT
                                    );

                    java.time.LocalDate fecha =
                            java.time.LocalDate.parse(
                                    fechaNacimiento, formatter
                            );

                    if (fecha.isAfter(java.time.LocalDate.now())) {
                        JOptionPane.showMessageDialog(
                                RegisterView.this,
                                "La fecha de nacimiento no puede ser futura."
                        );
                        return;
                    }

                } catch (java.time.format.DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(
                            RegisterView.this,
                            "Introduce una fecha válida con formato dd/MM/yyyy."
                    );
                    return;
                }

                // Register the user
                try {
                    UserController controller = new UserController();

                    controller.register(
                            nombre,
                            apellidos,
                            email,
                            password,
                            fechaNacimiento
                    );

                    JOptionPane.showMessageDialog(
                            RegisterView.this,
                            "¡Registro completado correctamente!"
                    );

                    // Return to the login screen
                    LoginView login = new LoginView();
                    login.setVisible(true);
                    dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            RegisterView.this,
                            "Error al registrarse: " + ex.getMessage()
                    );
                }
            }
        });

        btnVolverLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para cerrar esta ventana y abrir LoginView
                LoginView login = new LoginView();
                login.setVisible(true);
                dispose(); // Cierra la ventana actual de registro
            }
        });
    }

    // Styling helper for input fields
    private void styleInput(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(WHITE);
        field.setCaretColor(BLUE);
        field.setPreferredSize(new Dimension(300, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(7, 10, 7, 10)
        ));
    }

    // Form label styling
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);
        return label;
    }

    // Primary blue button styling
    private void stylePrimaryButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(WHITE);
        button.setBackground(BLUE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 42));
    }

    // Link button styling
    private void styleLinkButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(BLUE);
        button.setBackground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
