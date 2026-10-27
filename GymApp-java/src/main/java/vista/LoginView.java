package vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.UserController;
import model.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class LoginView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField passwordField;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null); // Hace que la ventana aparezca centrada en la pantalla
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(106, 13, 58, 14);
		contentPane.add(lblUsuario);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(169, 10, 104, 20);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		JLabel lblContrasena = new JLabel("Contraseña: ");
		lblContrasena.setBounds(106, 60, 71, 14);
		contentPane.add(lblContrasena);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(169, 57, 104, 20);
		contentPane.add(passwordField);
		
		JButton btnEntrar = new JButton("Entrar");
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
		        Socket socket = null; 
		        try {
		            // 3. Create the User object
		            User user = new User();
		            user.setEmail(email);
		            user.setPassword(password);
		            
		            
		            //4. Convert the User object into JSON
		            Gson gson = new Gson();
		            JsonObject jsonObject = gson.toJsonTree(user).getAsJsonObject();

		            //5. Add the request type
		            jsonObject.addProperty("action", "login");

		            //6. Convert the User object into JSON
		            String json = gson.toJson(jsonObject);

		            //7. Connect to the server
		            socket = new Socket("localhost", 5000);

		            //8. Send the JSON to the server
		            PrintWriter output = new PrintWriter(
		                    socket.getOutputStream(), true);

		            output.write(json+"\n");
		            output.flush();

		            //9. Receive the server response
		            BufferedReader input = new BufferedReader(
		                    new InputStreamReader(socket.getInputStream()));

		            String response = input.readLine();
		            
		         // Convert the String into a JSON object
		            JsonObject jsonResponse = new Gson().fromJson(
		                    response, JsonObject.class);

		            // Access individual fields
		            // Access individual fields
		            boolean success = jsonResponse.get("success").getAsBoolean();

		            String message = jsonResponse.has("message")
		                    && !jsonResponse.get("message").isJsonNull()
		                    ? jsonResponse.get("message").getAsString()
		                    : "No message received from server.";

		            System.out.println("Server response: " + response);

		            if (success) {
		                String uid = jsonResponse.get("uid").getAsString();
		                // Abrimos la pantalla principal (HomeView) de forma segura en el hilo de la interfaz gráfica (EDT)
		                EventQueue.invokeLater(new Runnable() {
		                    public void run() {
		                        try {
		                        	// Creamos la ventana HomeView pasando el UID del usuario autenticado
		                            HomeView homeFrame = new HomeView(uid); 
		                            homeFrame.setVisible(true);        
		                            // Cerramos y liberamos la memoria de la ventana de login actual
		                            LoginView.this.dispose(); 
		                            
		                        } catch (Exception e) {
		                            e.printStackTrace();
		                        }
		                    }
		                });
		                return; // El socket se cerrará automáticamente más abajo, en el bloque «finally»
		            }
		            JOptionPane.showMessageDialog(LoginView.this, message);
		            
		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(
		                    LoginView.this,
		                    "Error: " + ex.getMessage());
		        } finally {
		            //El bloque «finally» se ejecuta SIEMPRE, incluso si se ha producido un error en el bloque «try».
		            //Garantiza que el socket no quede abierto.
		            if (socket != null && !socket.isClosed()) {
		                try {
		                    socket.close();
		                } catch (Exception ex) {
		                    ex.printStackTrace();
		                }
		            }
		        }
			}
		});
		btnEntrar.setBounds(169, 135, 89, 23);
		contentPane.add(btnEntrar);
		
		JLabel lblRegistro = new JLabel("¿No estás registrado?");
		lblRegistro.setBounds(164, 169, 160, 14);
		contentPane.add(lblRegistro);
	}
}