package vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.UserController;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtNombre;
    private JTextField txtApellidos;
    private JTextField txtEmail;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField txtFechaNacimiento;

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
        setTitle("Registro de Usuario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 450);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Campo: Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(50, 30, 130, 14);
        contentPane.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(200, 27, 180, 20);
        contentPane.add(txtNombre);
        txtNombre.setColumns(10);

        // Campo: Apellidos
        JLabel lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setBounds(50, 70, 130, 14);
        contentPane.add(lblApellidos);

        txtApellidos = new JTextField();
        txtApellidos.setBounds(200, 67, 180, 20);
        contentPane.add(txtApellidos);
        txtApellidos.setColumns(10);

        // Campo: Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 110, 130, 14);
        contentPane.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(200, 107, 180, 20);
        contentPane.add(txtEmail);
        txtEmail.setColumns(10);

        // Campo: Contraseña
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(50, 150, 130, 14);
        contentPane.add(lblContrasena);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 147, 180, 20);
        contentPane.add(passwordField);

        // Campo: Confirmar Contraseña
        JLabel lblConfirmarContrasena = new JLabel("Confirmar Contraseña:");
        lblConfirmarContrasena.setBounds(50, 190, 150, 14);
        contentPane.add(lblConfirmarContrasena);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(200, 187, 180, 20);
        contentPane.add(confirmPasswordField);

        // Campo: Fecha de Nacimiento
        JLabel lblFechaNacimiento = new JLabel("Fecha Nac. (DD/MM/AAAA):");
        lblFechaNacimiento.setBounds(50, 230, 150, 14);
        contentPane.add(lblFechaNacimiento);

        txtFechaNacimiento = new JTextField();
        txtFechaNacimiento.setBounds(200, 227, 180, 20);
        contentPane.add(txtFechaNacimiento);
        txtFechaNacimiento.setColumns(10);

        // Botón: Registrarse
        JButton btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setBounds(150, 290, 130, 30);
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// Get the values from the fields
                String nombre = txtNombre.getText().trim();
                String apellidos = txtApellidos.getText().trim();
                String email = txtEmail.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmarPassword =
                        new String(confirmPasswordField.getPassword());
                String fechaNacimiento = txtFechaNacimiento.getText().trim();

                // Check that the fields are not empty
                if (nombre.isEmpty() || apellidos.isEmpty()
                        || email.isEmpty() || password.isEmpty()
                        || confirmarPassword.isEmpty()
                        || fechaNacimiento.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            RegisterView.this,
                            "Completa todos los campos.");
                    return;
                }

                // Check that both passwords match
                if (!password.equals(confirmarPassword)) {
                    JOptionPane.showMessageDialog(
                            RegisterView.this,
                            "Las contraseñas no coinciden.");
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
                            "¡Registro completado correctamente!");

                    // Return to the login screen
                    LoginView login = new LoginView();
                    login.setVisible(true);
                    dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            RegisterView.this,
                            "Error al registrarse: " + ex.getMessage());
                }
            }
        });
        contentPane.add(btnRegistrar);

        // Enlace/Botón: Volver al Login
        JButton btnVolverLogin = new JButton("Volver al Login");
        btnVolverLogin.setBounds(150, 340, 130, 23);
        btnVolverLogin.setBorderPainted(false); // Hace que parezca más un enlace
        btnVolverLogin.setContentAreaFilled(false);
        btnVolverLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para cerrar esta ventana y abrir LoginView
                LoginView login = new LoginView();
                login.setVisible(true);
                dispose(); // Cierra la ventana actual de registro
            }
        });
        contentPane.add(btnVolverLogin);
    }
}
