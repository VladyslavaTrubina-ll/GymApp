package vista;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import service.UserService;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HomeView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    // Panel contenedor que usará el CardLayout para alternar entre ver y editar el perfil
    private JPanel cardPanel;
    
    // Componente para mostrar la información del usuario en modo lectura
    private JTextArea userInfoArea;
    
    // Campos de texto interactivos para modificar los datos en modo edición
    private JTextField tfFirstName, tfLastName, tfEmail, tfBirthDate;
    
    // Almacena el nivel actual del usuario (se mantiene en memoria al editar)
    private int currentLevel = 0;

    // Variables para la integración con Firestore
    private final UserService userService = new UserService();
    private String currentUserId; // Идентификатор текущего авторизованного пользователя
    /**
     * Punto de entrada de la aplicación. Levanta la interfaz gráfica.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HomeView frame = new HomeView("test_uid_123");
                    frame.setVisible(true); // Hace visible la ventana principal
                    
                    // Simulación inicial: Carga datos de prueba que vendrían de la base de datos
                    frame.updateUserData(
                        "Ana", "Krol", "anna.krol@example.com", "8 de febrero de 2001, 12:00 AM", 0
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public HomeView(String uid) {
    	this.currentUserId = uid; //Gvardar id usuario logiado
    	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 524, 615);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnWorkouts = new JButton("Workouts");
        btnWorkouts.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnWorkouts.setBounds(259, 416, 208, 58);
        contentPane.add(btnWorkouts);
        cardPanel = new JPanel();
        cardPanel.setBounds(23, 69, 427, 289);
        contentPane.add(cardPanel);
        cardPanel.setLayout(null);

        // --------------------------------------------------------
        // TARJETA 2: MODO EDICIÓN ("EDIT_MODE") - Formulario con campos de texto
        // --------------------------------------------------------
        JPanel editCard = new JPanel();
        editCard.setBounds(0, 0, 300, 145);
        editCard.setLayout(null);
        
        // Sub-panel organizado en una cuadrícula (4 filas, 2 columnas) para etiquetas y cajas de texto
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 3, 3));
        fieldsPanel.setBounds(0, 0, 300, 117);
        tfFirstName = new JTextField();
        tfLastName = new JTextField();
        tfEmail = new JTextField();
        tfBirthDate = new JTextField();

        fieldsPanel.add(new JLabel("firstName:"));
        fieldsPanel.add(tfFirstName);
        fieldsPanel.add(new JLabel("lastName:"));
        fieldsPanel.add(tfLastName);
        fieldsPanel.add(new JLabel("email:"));
        fieldsPanel.add(tfEmail);
        fieldsPanel.add(new JLabel("birthDate:"));
        fieldsPanel.add(tfBirthDate);
        editCard.add(fieldsPanel);

        // Panel de botones de control dentro del contenedor (CORREGIDO: Declaración e inicialización añadida)
        JPanel actionButtonsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        actionButtonsPanel.setBounds(0, 122, 300, 23);
        
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Leemos los nuevos datos
                String newFirst = tfFirstName.getText();
                String newLast = tfLastName.getText();
                String newEmail = tfEmail.getText();
                String newBirth = tfBirthDate.getText();
                
                // TODO: Vincular aquí la lógica de persistencia para enviar los datos a Firestore
                
                // Actualizamos y volvemos atrás
                updateUserData(newFirst, newLast, newEmail, newBirth, currentLevel);
                cardLayout.show(cardPanel, "VIEW_MODE");
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Simplemente volvemos atrás sin guardar los cambios
                cardLayout.show(cardPanel, "VIEW_MODE");
            }
        });

        actionButtonsPanel.add(btnSave);
        actionButtonsPanel.add(btnCancel);
        editCard.add(actionButtonsPanel);
        
        cardPanel.add(editCard);
        
                // Botón que actúa como disparador (trigger) para cambiar al formulario de edición
                JButton btnEdit = new JButton("Edit Profile");
                btnEdit.setBounds(0, 249, 427, 40);
                cardPanel.add(btnEdit);
                btnEdit.setFont(new Font("Arial", Font.PLAIN, 11));
                btnEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Al hacer clic, el CardLayout oculta la vista actual y despliega "EDIT_MODE"
                        cardLayout.show(cardPanel, "EDIT_MODE");
                    }
                });

        // ========================================================
        // Botón: History Workouts (se desplazó hacia abajo porque la tarjeta ahora es más grande)
        JButton btnHistory = new JButton("History Workouts");
        btnHistory.setBounds(36, 416, 213, 58);
        contentPane.add(btnHistory);
        
                // --------------------------------------------------------
                // TARJETA 1: MODO VISTA ("VIEW_MODE") - Muestra datos y botón "Edit"
                // --------------------------------------------------------
                JPanel viewCard = new JPanel();
                viewCard.setBounds(23, 69, 427, 250);
                contentPane.add(viewCard);
                viewCard.setLayout(null);
                userInfoArea = new JTextArea();
                userInfoArea.setBounds(0, 0, 300, 21);
                userInfoArea.setEditable(false);
                userInfoArea.setWrapStyleWord(true);
                userInfoArea.setLineWrap(true);
                userInfoArea.setOpaque(false);
                userInfoArea.setBackground(new Color(0,0,0,0));
                userInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                viewCard.add(userInfoArea);
                
                JLabel lblNewLabel = new JLabel("GymApp");
                lblNewLabel.setBounds(203, 21, 150, 31);
                contentPane.add(lblNewLabel);
     // Iniciamos la carga de BD los datos de usuario
        loadUserData(uid);
    }
    //Método para obtener datos a través de tu UserService
    private void loadUserData(String uid) {
    	// Iniciamos el flujo en segundo plano
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1. Realizamos una solicitud asíncrona a Firestore a través de UserService
                    // Metod .get() espera a que finalice la operación de red y devuelve DocumentSnapshot
                    com.google.cloud.firestore.DocumentSnapshot document = userService.getUserProfileAsync(uid).get();

                    // 2. Revisa si existe el documento del usuario en la BD
                    if (document.exists()) {
                        // Extraemos las cadenas comprobando si son nulas (por si acaso algún campo de la BD está vacío)
                        final String firstName = document.getString("firstName") != null ? document.getString("firstName") : "";
                        final String lastName = document.getString("lastName") != null ? document.getString("lastName") : "";
                        final String email = document.getString("email") != null ? document.getString("email") : "";
                        final String birthDate = document.getString("birthDate") != null ? document.getString("birthDate") : "";
                        
                        // Extraemos el valor como tipo «long», ya que Firestore almacena los números como «Long»
                        Long levelLong = document.getLong("level");
                        final int level = levelLong != null ? levelLong.intValue() : 0;

                        // 3. Actualizamos los elementos de la interfaz gráfica Swing estrictamente en el hilo EDT
                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                updateUserData(firstName, lastName, email, birthDate, level);
                            }
                        });
                    } else {
                        System.err.println("Perfil de usuario con ID " + uid + "no existe en Firestore.");
                        javax.swing.JOptionPane.showMessageDialog(HomeView.this, 
                            "Error: No se ha encontrado el perfil de usuario.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // En caso de que se produzca un error de red o de Firestore, mostramos una notificación al usuario.
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            javax.swing.JOptionPane.showMessageDialog(HomeView.this, 
                                "No se han podido cargar los datos del perfil: " + e.getMessage(), 
                                "Error de conexión", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }
            }
        }).start();
    }
    

    /**
     * Método para la actualización dinámica de datos
     */
    public void updateUserData(String firstName, String lastName, String email, String birthDate, int level) {
        this.currentLevel = level;
        
        // Sincronizamos los campos de entrada de texto
        tfFirstName.setText(firstName);
        tfLastName.setText(lastName);
        tfEmail.setText(email);
        tfBirthDate.setText(birthDate);

        // Sincronizamos la descripción textual en modo lectura
        StringBuilder sb = new StringBuilder();
        sb.append("firstName: \"").append(firstName).append("\"\n");
        sb.append("lastName: \"").append(lastName).append("\"\n");
        sb.append("email: \"").append(email).append("\"\n");
        sb.append("birthDate: ").append(birthDate).append("\n");
        sb.append("level: ").append(level);
        userInfoArea.setText(sb.toString());
    }
}
