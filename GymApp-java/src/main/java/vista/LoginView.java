
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

public class LoginView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtUsuario;
    private JPasswordField passwordField;

    // Modern color palette
    private final Color BACKGROUND = new Color(243, 246, 252);
    private final Color NAVY = new Color(25, 35, 58);
    private final Color BLUE = new Color(55, 105, 220);
    private final Color TEXT = new Color(45, 55, 72);
    private final Color MUTED = new Color(115, 125, 145);
    private final Color WHITE = Color.WHITE;
    private final Color BORDER = new Color(220, 226, 237);

    //atributos de clase
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginView frame = new LoginView();
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
    public LoginView() {
        setTitle("GymApp | Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 540);
        setMinimumSize(new Dimension(480, 540));
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(BACKGROUND);
        contentPane.setBorder(new EmptyBorder(30, 35, 30, 35));
        setContentPane(contentPane);

        // Contenedor card
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(30, 35, 30, 35)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 0, 7, 0);

        // Titulo App
        JLabel lblTitle = new JLabel("GYMAPP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(NAVY);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(lblTitle, gbc);

        

        // Email label
        JLabel lblUsuario = new JLabel("Email");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(TEXT);

        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 5, 0);
        card.add(lblUsuario, gbc);

        // Email field
        txtUsuario = new JTextField();
        styleInput(txtUsuario);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 15, 0);
        card.add(txtUsuario, gbc);

        // Password label
        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblContrasena.setForeground(TEXT);

        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 5, 0);
        card.add(lblContrasena, gbc);

        // Password field
        passwordField = new JPasswordField();
        styleInput(passwordField);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 22, 0);
        card.add(passwordField, gbc);

        // Login button
        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setForeground(WHITE);
        btnEntrar.setBackground(BLUE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setOpaque(true);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.setPreferredSize(new Dimension(300, 44));

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 18, 0);
        card.add(btnEntrar, gbc);

        // Registration text
        JLabel lblRegistro = new JLabel(
                "¿No estás registrado?",
                SwingConstants.CENTER
        );
        lblRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Abre la ventana RegisterView
        lblRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                RegisterView registerFrame = new RegisterView();
                registerFrame.setVisible(true);
                LoginView.this.dispose();
            }
        });

        gbc.gridy = 7;
        gbc.insets = new Insets(5, 0, 0, 0);
        card.add(lblRegistro, gbc);

        // Add card to the window
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.weightx = 1;
        mainGbc.weighty = 1;
        mainGbc.fill = GridBagConstraints.BOTH;
        contentPane.add(card, mainGbc);

        // BOTON LOGIN
        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // 1. Collect the values from the form
                String email = txtUsuario.getText().trim();
                String password = new String(passwordField.getPassword());

                // 2. no empty fields
                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            LoginView.this,
                            "Introduce tu email y contraseña.");
                    return;
                }

                
                try {
                    // 3. Create the User object
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(password);

                    // 4. Convert the User object into JSON
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.toJsonTree(user).getAsJsonObject();

                    // 5. Add the request type
                    jsonObject.addProperty("action", "login");

                    // 6. Convert the User object into JSON
                    String json = gson.toJson(jsonObject);

                    // 7. Conecta el servidor y sus Streams
                    socket = new Socket("localhost", 5000);

                    output = new PrintWriter(socket.getOutputStream(), true);

                    
                    input = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    
                    // 8. Send login request
                    output.println(json);
                    output.flush();
                    
                    // 9. Receive the server response
                    String response = input.readLine();

                    // Convert the String into a JSON object
                    JsonObject jsonResponse = new Gson().fromJson(
                            response, JsonObject.class);

                    // Access individual fields
                    boolean success = jsonResponse.get("success").getAsBoolean();

                    String message = jsonResponse.has("message")
                            && !jsonResponse.get("message").isJsonNull()
                            ? jsonResponse.get("message").getAsString()
                            : "No message received from server.";

                    System.out.println("Server response: " + response);

                    if (success) {
                        String uid = jsonResponse.get("uid").getAsString();

                        // Abrimos HomeView en el hilo de la interfaz gráfica
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    // Creamos la ventana HomeView pasando el UID
                                	HomeView homeFrame = new HomeView(
                                	        uid,
                                	        socket,
                                	        output,
                                	        input
                                	);
                                    homeFrame.setVisible(true);

                                    // Cerramos la ventana de login actual
                                    LoginView.this.dispose();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        return;
                    }

                    JOptionPane.showMessageDialog(LoginView.this, message);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            LoginView.this,
                            "Error: " + ex.getMessage());
                }
            }
        });
    }

    // Estilo de los campos del formulario
    private void styleInput(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(WHITE);
        field.setCaretColor(BLUE);
        field.setPreferredSize(new Dimension(300, 42));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
    }
}
