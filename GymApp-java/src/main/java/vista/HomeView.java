
package vista;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Cursor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

import service.UserService;
import service.WorkoutService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.Socket;
import model.Workout;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;

public class HomeView extends JFrame {

    private static final long serialVersionUID = 1L;

    //Recursos JFRAME
    private JPanel contentPane;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextArea userInfoArea;
    private JPanel workoutsListPanel;
    private JPanel historyListPanel;
    private JTextField tfFirstName, tfLastName, tfEmail, tfBirthDate;
    private int currentLevel = 0;
    

    //Atributos de clase
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private String currentUserId;
    private final Gson gson = new Gson();

    // PALETA DE COLORES
    private final Color BACKGROUND = new Color(243, 246, 252);
    private final Color NAVY = new Color(25, 35, 58);
    private final Color BLUE = new Color(55, 105, 220);
    private final Color TEXT = new Color(45, 55, 72);
    private final Color MUTED = new Color(115, 125, 145);
    private final Color WHITE = Color.WHITE;
    private final Color BORDER = new Color(220, 226, 237);
    private final Color LIGHT_BLUE = new Color(232, 239, 255);

    /**
     * Punto de entrada de la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	
                	//Inicio vacio para poder abrir la ventana
                    HomeView frame = new HomeView("test_uid_123",
                            null,
                            null,
                            null);
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
    
    public HomeView(String uid,
            Socket socket,
            PrintWriter output,
            BufferedReader input) {
    	
    	//guarda el uid del usuario logeado
    	this.currentUserId = uid;
        this.socket = socket;
        this.output = output;
        this.input = input;

        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(760, 500));
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout(20, 0));
        contentPane.setBackground(BACKGROUND);
        contentPane.setBorder(new EmptyBorder(25, 25, 25, 25));
        setContentPane(contentPane);

        // MENU LATERAL
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(6, 1, 0, 12));
        sidebar.setPreferredSize(new Dimension(170, 0));
        sidebar.setBackground(NAVY);
        sidebar.setBorder(new EmptyBorder(25, 15, 25, 15));

        JLabel lblBrand = new JLabel("GYMAPP", SwingConstants.CENTER);
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 23));
        lblBrand.setForeground(WHITE);
        sidebar.add(lblBrand);

        JButton btnPerfil = createSidebarButton("Perfil");
        btnPerfil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "VIEW_MODE");
            }
        });
        sidebar.add(btnPerfil);

        JButton btnWorkouts = createSidebarButton("Workouts");
        btnWorkouts.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "WORKOUTS_MODE");
                loadAvailableWorkouts();
            }
        });
        sidebar.add(btnWorkouts);

        JButton btnHistory = createSidebarButton("Historial");
        btnHistory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "HISTORY_MODE");
                loadWorkoutHistory();
            }
        });
        sidebar.add(btnHistory);

        contentPane.add(sidebar, BorderLayout.WEST);

        // CONTENEDOR PRINCIPAL
        JPanel mainPanel = new JPanel(new BorderLayout(0, 18));
        mainPanel.setBackground(BACKGROUND);

        JLabel lblPageTitle = new JLabel("Mi perfil");
        lblPageTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblPageTitle.setForeground(NAVY);
        mainPanel.add(lblPageTitle, BorderLayout.NORTH);

        // Card donde se muestra la opcion elegida por usuario
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(WHITE);
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // VIEW MODE
        JPanel viewCard = new JPanel(new BorderLayout(10, 20));
        viewCard.setBackground(WHITE);
        viewCard.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblProfileTitle = new JLabel("Información personal");
        lblProfileTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblProfileTitle.setForeground(NAVY);
        viewCard.add(lblProfileTitle, BorderLayout.NORTH);

        userInfoArea = new JTextArea();
        userInfoArea.setEditable(false);
        userInfoArea.setWrapStyleWord(true);
        userInfoArea.setLineWrap(true);
        userInfoArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        userInfoArea.setForeground(TEXT);
        userInfoArea.setBackground(WHITE);
        userInfoArea.setBorder(new EmptyBorder(10, 0, 10, 0));

        viewCard.add(userInfoArea, BorderLayout.CENTER);

        JButton btnEdit = createPrimaryButton("Editar perfil");
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "EDIT_MODE");
            }
        });

        JPanel editButtonPanel = new JPanel(new BorderLayout());
        editButtonPanel.setBackground(WHITE);
        editButtonPanel.add(btnEdit, BorderLayout.EAST);
        viewCard.add(editButtonPanel, BorderLayout.SOUTH);

        cardPanel.add(viewCard, "VIEW_MODE");

        // EDIT MODE
        JPanel editCard = new JPanel(new BorderLayout(10, 20));
        editCard.setBackground(WHITE);
        editCard.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblEditTitle = new JLabel("Editar información");
        lblEditTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEditTitle.setForeground(NAVY);
        editCard.add(lblEditTitle, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 12, 12));
        fieldsPanel.setBackground(WHITE);

        tfFirstName = new JTextField();
        tfLastName = new JTextField();
        tfEmail = new JTextField();
        tfBirthDate = new JTextField();

        styleInput(tfFirstName);
        styleInput(tfLastName);
        styleInput(tfEmail);
        styleInput(tfBirthDate);

        fieldsPanel.add(createFieldLabel("Nombre"));
        fieldsPanel.add(tfFirstName);

        fieldsPanel.add(createFieldLabel("Apellidos"));
        fieldsPanel.add(tfLastName);

        fieldsPanel.add(createFieldLabel("Email"));
        fieldsPanel.add(tfEmail);

        fieldsPanel.add(createFieldLabel("Fecha de nacimiento"));
        fieldsPanel.add(tfBirthDate);

        editCard.add(fieldsPanel, BorderLayout.CENTER);

        JPanel actionButtonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        actionButtonsPanel.setBackground(WHITE);

        JButton btnSave = createPrimaryButton("Guardar");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Lógica original: leer los nuevos datos
                String newFirst = tfFirstName.getText();
                String newLast = tfLastName.getText();
                String newEmail = tfEmail.getText();
                String newBirth = tfBirthDate.getText();

                // Enviamos los cambios al servidor para actualizar Firestore
                saveUserData(newFirst, newLast, newEmail, newBirth);
            }
        });

        JButton btnCancel = createSecondaryButton("Cancelar");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "VIEW_MODE");
            }
        });

        actionButtonsPanel.add(btnSave);
        actionButtonsPanel.add(btnCancel);
        editCard.add(actionButtonsPanel, BorderLayout.SOUTH);

        cardPanel.add(editCard, "EDIT_MODE");

        // WORKOUTS MODE
        JPanel workoutsCard = new JPanel(new BorderLayout(10, 20));
        workoutsCard.setBackground(WHITE);
        workoutsCard.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblWorkoutsTitle = new JLabel("Workouts disponibles");
        lblWorkoutsTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblWorkoutsTitle.setForeground(NAVY);
        workoutsCard.add(lblWorkoutsTitle, BorderLayout.NORTH);

        workoutsListPanel = new JPanel();
        workoutsListPanel.setLayout(
                new BoxLayout(workoutsListPanel, BoxLayout.Y_AXIS)
        );
        workoutsListPanel.setBackground(BACKGROUND);
        workoutsListPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JScrollPane workoutsScrollPane = new JScrollPane(workoutsListPanel);
        workoutsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        workoutsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        workoutsScrollPane.getViewport().setBackground(BACKGROUND);

        workoutsCard.add(workoutsScrollPane, BorderLayout.CENTER);

        JButton btnBackToProfile = createSecondaryButton("Volver al perfil");
        btnBackToProfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "VIEW_MODE");
            }
        });

        JPanel workoutsButtonPanel = new JPanel(new BorderLayout());
        workoutsButtonPanel.setBackground(WHITE);
        workoutsButtonPanel.add(btnBackToProfile, BorderLayout.EAST);
        workoutsCard.add(workoutsButtonPanel, BorderLayout.SOUTH);

        cardPanel.add(workoutsCard, "WORKOUTS_MODE");

        // HISTORY MODE
        JPanel historyCard = new JPanel(new BorderLayout(10, 20));
        historyCard.setBackground(WHITE);
        historyCard.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblHistoryTitle = new JLabel("Mi historial");
        lblHistoryTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHistoryTitle.setForeground(NAVY);
        historyCard.add(lblHistoryTitle, BorderLayout.NORTH);

        historyListPanel = new JPanel();
        historyListPanel.setLayout(
                new BoxLayout(historyListPanel, BoxLayout.Y_AXIS)
        );
        historyListPanel.setBackground(BACKGROUND);
        historyListPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JScrollPane historyScrollPane = new JScrollPane(historyListPanel);
        historyScrollPane.setBorder(BorderFactory.createEmptyBorder());
        historyScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        historyScrollPane.getViewport().setBackground(BACKGROUND);
        historyCard.add(historyScrollPane, BorderLayout.CENTER);

        JButton btnHistoryBack = createSecondaryButton("Volver al perfil");
        btnHistoryBack.addActionListener(e ->
                cardLayout.show(cardPanel, "VIEW_MODE")
        );

        JPanel historyButtonPanel = new JPanel(new BorderLayout());
        historyButtonPanel.setBackground(WHITE);
        historyButtonPanel.add(btnHistoryBack, BorderLayout.EAST);
        historyCard.add(historyButtonPanel, BorderLayout.SOUTH);

        cardPanel.add(historyCard, "HISTORY_MODE");

        mainPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        contentPane.add(mainPanel, BorderLayout.CENTER);

        // Initial view
        cardLayout.show(cardPanel, "VIEW_MODE");

        // Carga el perfil solo cuando existe una conexión con el servidor
        if (socket != null && output != null && input != null) {
            loadUserData(uid);
        }
    }

    
	// Sidebar button styling
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(WHITE);
        button.setBackground(NAVY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Primary blue button
    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(WHITE);
        button.setBackground(BLUE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));
        return button;
    }

    // Secondary button
    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(NAVY);
        button.setBackground(LIGHT_BLUE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Input field styling
    private void styleInput(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(WHITE);
        field.setCaretColor(BLUE);
        field.setPreferredSize(new Dimension(200, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(7, 9, 7, 9)
        ));
    }

    // Form label styling
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);
        return label;
    }


    //Carga el perfil del usuario
	private void loadUserData(String uid) {
	
	    new Thread(new Runnable() {
	
	        @Override
	        public void run() {
	
	            try {
	
	                // Prepares the request
	                JsonObject request = new JsonObject();
	                request.addProperty("action", "getUserData");
	                request.addProperty("uid", uid);
	
	                // Converts the request into JSON
	                String json = new Gson().toJson(request);
	
	                // Envía la petición y recibe la respuesta sin mezclarla con otra petición
	                String response;
	                synchronized (socket) {
	                    output.println(json);
	                    output.flush();
	                    response = input.readLine();
	                }
	
	                if (response == null) {
	                    throw new Exception("Server closed the connection.");
	                }
	
	                JsonObject jsonResponse =
	                        new Gson().fromJson(response, JsonObject.class);
	
	                if (jsonResponse.get("success").getAsBoolean()) {
	
	                    final String firstName =
	                            jsonResponse.get("firstName").isJsonNull()
	                            ? "" : jsonResponse.get("firstName").getAsString();
	
	                    final String lastName =
	                            jsonResponse.get("lastName").isJsonNull()
	                            ? "" : jsonResponse.get("lastName").getAsString();
	
	                    final String email =
	                            jsonResponse.get("email").isJsonNull()
	                            ? "" : jsonResponse.get("email").getAsString();
	
	                    final String birthDate =
	                            jsonResponse.get("birthDate").isJsonNull()
	                            ? "" : jsonResponse.get("birthDate").getAsString();
	
	                    final int level =
	                            jsonResponse.get("level").getAsInt();
	
	                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	
	                        @Override
	                        public void run() {
	
	                            updateUserData(
	                                firstName, lastName,
	                                email, birthDate, level
	                            );
	                        }
	                    });
	
	                } else {
	
	                    System.err.println(
	                        "Perfil de usuario con ID " + uid
	                        + " no existe en Firestore."
	                    );
	
	                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	
	                        @Override
	                        public void run() {
	
	                            JOptionPane.showMessageDialog(
	                                HomeView.this,
	                                "Error: No se ha encontrado el perfil de usuario.",
	                                "Error",
	                                JOptionPane.ERROR_MESSAGE
	                            );
	                        }
	                    });
	                }
	
	            } catch (Exception e) {
	
	                e.printStackTrace();
	
	                final String errorMessage = e.getMessage();
	
	                javax.swing.SwingUtilities.invokeLater(new Runnable() {
	
	                    @Override
	                    public void run() {
	
	                        JOptionPane.showMessageDialog(
	                            HomeView.this,
	                            "No se han podido cargar los datos del perfil: "
	                            + errorMessage,
	                            "Error de conexión",
	                            JOptionPane.ERROR_MESSAGE
	                        );
	                    }
	                });
	            }
	        }
	
	    }).start();
	}

    //Carga todos los workouts disponibles para el usuario logeado
    private void loadAvailableWorkouts() {

    	//limpia el panel para volver a recargar otra vez
        workoutsListPanel.removeAll();

        JLabel loadingLabel = new JLabel("Cargando workouts...");
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadingLabel.setForeground(MUTED);
        loadingLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        workoutsListPanel.add(loadingLabel);
        workoutsListPanel.revalidate();
        workoutsListPanel.repaint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                	
                	//prepara el request al servidor
                	JsonObject request = new JsonObject();
                	request.addProperty("action", "getAvailableWorkouts");
                	request.addProperty("uid", currentUserId);

                	String json = new Gson().toJson(request);

                	// Envía la petición y recibe la respuesta como una única operación
                	String response;
                	synchronized (socket) {
                	    output.println(json);
                	    output.flush();
                	    response = input.readLine();
                	}

                	JsonObject jsonResponse = new Gson().fromJson(
                	        response, JsonObject.class
                	);

                	// Comprueba si la operación ha sido correcta
                	if (!jsonResponse.get("success").getAsBoolean()) {
                	    throw new Exception("Could not load available workouts");
                	}

                	// Convierte el JSON en una lista de Workout
                	List<Workout> workouts = new Gson().fromJson(
                	        jsonResponse.get("workouts"),
                	        new com.google.gson.reflect.TypeToken<List<Workout>>() {}.getType()
                	);

                	//el hilo hace posible que se actualice la interfaz
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                        	
                        	//limpia el panel donde se lista los workouts
                            workoutsListPanel.removeAll();

                            //si la lista esta vacia
                            if (workouts.isEmpty()) {
                                JLabel emptyLabel = new JLabel(
                                        "No hay workouts disponibles para tu nivel."
                                );
                                emptyLabel.setFont(
                                        new Font("Segoe UI", Font.PLAIN, 14)
                                );
                                emptyLabel.setForeground(MUTED);
                                workoutsListPanel.add(emptyLabel);
                                
                                //si existen workouts, se listara mostrando algunos detalles
                            } else {
                            	
                            	//por cada workout
                                for (Workout workout : workouts) {
                   
                                	//se crea una card pasandole el objeto workout
                                    JPanel workoutCard = createWorkoutCard(workout );

                                    //se añade a lista
                                    workoutsListPanel.add(workoutCard);
                                    workoutsListPanel.add(
                                            Box.createVerticalStrut(12)
                                    );
                                }
                            }

                            workoutsListPanel.revalidate();
                            workoutsListPanel.repaint();
                        }
                    });

                    //si existe algun problema
                } catch (Exception e) {
                    e.printStackTrace();

                    final String errorMessage = e.getMessage();

                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                        	
                        	//limpia el listado del panel
                            workoutsListPanel.removeAll();

                            JLabel errorLabel = new JLabel(
                                    "No se han podido cargar los workouts."
                                    + errorMessage
                            );
                            errorLabel.setForeground(Color.RED);
                            errorLabel.setFont(
                                    new Font("Segoe UI", Font.PLAIN, 14)
                            );

                            workoutsListPanel.add(errorLabel);
                            workoutsListPanel.revalidate();
                            workoutsListPanel.repaint();
                        }
                    });
                }
            }
        }).start();
    }

    
private JPanel createWorkoutCard(Workout workout) {

    String name = workout.getNombre();
    int level = workout.getNivel();
    String description = workout.getDescripcion();

    JPanel card = new JPanel(new BorderLayout(15, 10));
    card.setBackground(WHITE);
    card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(16, 16, 16, 16)
    ));

    card.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
    card.setPreferredSize(new Dimension(450, 135));

    JPanel informationPanel = new JPanel();
    informationPanel.setLayout(
            new BoxLayout(informationPanel, BoxLayout.Y_AXIS)
    );
    informationPanel.setBackground(WHITE);

    JLabel nameLabel = new JLabel(name);
    nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
    nameLabel.setForeground(NAVY);
    nameLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

    JLabel levelLabel = new JLabel("Nivel " + level);
    levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
    levelLabel.setForeground(BLUE);
    levelLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

    String safeDescription =
            description != null && !description.isBlank()
            ? description : "Sin descripción disponible.";

    JLabel descriptionLabel = new JLabel(
            "<html><div style='width:260px;'>"
            + safeDescription
            + "</div></html>"
    );

    descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    descriptionLabel.setForeground(TEXT);
    descriptionLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

    informationPanel.add(nameLabel);
    informationPanel.add(Box.createVerticalStrut(5));
    informationPanel.add(levelLabel);
    informationPanel.add(Box.createVerticalStrut(8));
    informationPanel.add(descriptionLabel);

    JButton btnStart = createPrimaryButton("VER MAS");
    btnStart.setPreferredSize(new Dimension(100, 40));

   

    // OPEN WORKOUT DETAILS

	 btnStart.addActionListener(e -> {
	
	     new Thread(() -> { // Sin thread la interfaz se congela
	
	         try {
	
	             // Prepares the request
	             JsonObject request = new JsonObject();
	             request.addProperty("action", "getWorkoutDetails");
	             request.addProperty("workoutId", workout.getId());
	
	             // Converts the request into JSON
	             String json = new Gson().toJson(request);
	
	             // Sends the request to the server
	             String response;
	
	             synchronized (socket) {
	                 output.println(json);
	                 output.flush();
	
	                 // Receives the server response
	                 response = input.readLine();
	             }
	
	             if (response == null) {
	                 throw new Exception("Server closed the connection.");
	             }
	
	             JsonObject jsonResponse =
	                     new Gson().fromJson(response, JsonObject.class);
	
	             if (!jsonResponse.get("success").getAsBoolean()) {
	                 throw new Exception(
	                         jsonResponse.has("message")
	                         ? jsonResponse.get("message").getAsString()
	                         : "Could not load workout details."
	                 );
	             }
	
	             // Converts the JSON workout into a Workout object
	             Workout completeWorkout = new Gson().fromJson(
	                     jsonResponse.get("workout"),
	                     Workout.class
	             );
	
	             javax.swing.SwingUtilities.invokeLater(() -> {
	
	                 WorkoutDetailView detailView =new WorkoutDetailView(
	                         completeWorkout,
	                         currentUserId,
	                         output,
	                         input
	                 );
	
	                 JFrame detailFrame = new JFrame(
	                         completeWorkout.getNombre()
	                 );
	
	                 detailFrame.setDefaultCloseOperation(
	                         JFrame.DISPOSE_ON_CLOSE
	                 );
	
	                 detailFrame.setSize(1000, 750);
	                 detailFrame.setLocationRelativeTo(this);
	                 detailFrame.setContentPane(detailView);
	                 detailFrame.setVisible(true);
	             });
	
	         } catch (Exception ex) {
	
	             ex.printStackTrace();
	
	             javax.swing.SwingUtilities.invokeLater(() ->
	                     JOptionPane.showMessageDialog(
	                             this,
	                             "Could not load workout details: "
	                                     + ex.getMessage(),
	                             "Error",
	                             JOptionPane.ERROR_MESSAGE
	                     )
	             );
	         }
	
	     }).start();
	 });


    JPanel buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setBackground(WHITE);
    buttonPanel.add(btnStart, BorderLayout.CENTER);

    card.add(informationPanel, BorderLayout.CENTER);
    card.add(buttonPanel, BorderLayout.EAST);

    return card;
}

/**
     * Carga el historial de workouts del usuario desde el servidor.
     */
    private void loadWorkoutHistory() {

        if (socket == null || output == null || input == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay conexión con el servidor.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Mostramos un mensaje mientras se recupera el historial
        historyListPanel.removeAll();
        JLabel loadingLabel = new JLabel("Cargando historial...");
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadingLabel.setForeground(MUTED);
        historyListPanel.add(loadingLabel);
        historyListPanel.revalidate();
        historyListPanel.repaint();

        // Realizamos la petición en segundo plano para no bloquear la interfaz
        new Thread(() -> {
            try {
                // Preparamos la petición para S_socket
                JsonObject request = new JsonObject();
                request.addProperty("action", "getWorkoutHistory");
                request.addProperty("uid", currentUserId);

                String response;

                // Enviamos la petición y esperamos su respuesta
                synchronized (socket) {
                    output.println(gson.toJson(request));
                    output.flush();
                    response = input.readLine();
                }

                if (response == null) {
                    throw new Exception("El servidor ha cerrado la conexión.");
                }

                JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);

                if (!jsonResponse.get("success").getAsBoolean()) {
                    throw new Exception(
                            jsonResponse.has("message")
                            ? jsonResponse.get("message").getAsString()
                            : "No se ha podido cargar el historial."
                    );
                }

                List<Map<String, Object>> history = gson.fromJson(
                        jsonResponse.get("history"),
                        new com.google.gson.reflect.TypeToken<List<Map<String, Object>>>() {}.getType()
                );

                javax.swing.SwingUtilities.invokeLater(() -> {
                    historyListPanel.removeAll();

                    if (history == null || history.isEmpty()) {
                        JLabel emptyLabel = new JLabel(
                                "Todavía no tienes workouts en tu historial."
                        );
                        emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        emptyLabel.setForeground(MUTED);
                        historyListPanel.add(emptyLabel);
                    } else {
                        for (Map<String, Object> entry : history) {
                            historyListPanel.add(createHistoryCard(entry));
                            historyListPanel.add(Box.createVerticalStrut(12));
                        }
                    }

                    historyListPanel.revalidate();
                    historyListPanel.repaint();
                });

            } catch (Exception e) {
                e.printStackTrace();

                javax.swing.SwingUtilities.invokeLater(() -> {
                    historyListPanel.removeAll();

                    JLabel errorLabel = new JLabel(
                            "No se ha podido cargar el historial: " + e.getMessage()
                    );
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    historyListPanel.add(errorLabel);
                    historyListPanel.revalidate();
                    historyListPanel.repaint();
                });
            }
        }).start();
    }

    /**
     * Crea una tarjeta con la información de un workout realizado.
     *
     * @param entry datos del registro recuperado desde Firestore
     * @return tarjeta visual del registro
     */
    private JPanel createHistoryCard(Map<String, Object> entry) {

        JPanel card = new JPanel(new BorderLayout(10, 8));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(15, 16, 15, 16)
        ));
        card.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        String workoutName = String.valueOf(entry.get("workoutName"));
        int totalTime = ((Number) entry.getOrDefault("totalTime", 0.0)).intValue();
        int estimatedTime = ((Number) entry.getOrDefault("estimatedTime", 0.0)).intValue();
        int percentage = ((Number) entry.getOrDefault("completionPercentage", 0.0)).intValue();

        JLabel nameLabel = new JLabel(workoutName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nameLabel.setForeground(NAVY);

        long dateMillis = 0;
        Object dateValue = entry.get("date");
        if (dateValue instanceof Number) {
            dateMillis = ((Number) dateValue).longValue();
        }

        String dateText = dateMillis > 0
                ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(dateMillis))
                : "Fecha no disponible";

        JLabel dateLabel = new JLabel(dateText);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(MUTED);

        JLabel timeLabel = new JLabel(
                "Tiempo empleado: " + formatDuration(totalTime)
                + "   |   Tiempo estimado: " + formatDuration(estimatedTime)
        );
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        timeLabel.setForeground(TEXT);

        JLabel percentageLabel = new JLabel(
                "Completado: " + percentage + "%"
        );
        percentageLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        percentageLabel.setForeground(BLUE);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(WHITE);
        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createVerticalStrut(4));
        detailsPanel.add(dateLabel);
        detailsPanel.add(Box.createVerticalStrut(8));
        detailsPanel.add(timeLabel);
        detailsPanel.add(Box.createVerticalStrut(4));
        detailsPanel.add(percentageLabel);

        card.add(detailsPanel, BorderLayout.CENTER);
        return card;
    }

    /**
     * Convierte una duración en segundos al formato minutos y segundos.
     */
    private String formatDuration(int seconds) {
        int safeSeconds = Math.max(0, seconds);
        return String.format("%02d:%02d", safeSeconds / 60, safeSeconds % 60);
    }

    /**
     * Actualiza los datos del perfil en la interfaz.
     * Este método se utiliza tanto al cargar el perfil como después de guardarlo.
     */
    private void updateUserData(String firstName, String lastName,
            String email, String birthDate, int level) {

        // Guardamos el nivel actual del usuario
        currentLevel = level;

        // Actualizamos los campos del formulario de edición
        tfFirstName.setText(firstName);
        tfLastName.setText(lastName);
        tfEmail.setText(email);
        tfBirthDate.setText(birthDate);

        // Mostramos los datos actualizados en la vista del perfil
        userInfoArea.setText(
                "Nombre: " + firstName + "\n"
                + "Apellidos: " + lastName + "\n"
                + "Email: " + email + "\n"
                + "Fecha de nacimiento: " + birthDate + "\n"
                + "Nivel: " + level
        );
    }

    /**
     * Envía los nuevos datos del perfil a S_socket.
     * S_socket será quien llame a UserService para actualizar Firestore.
     */
    private void saveUserData(String firstName, String lastName,
            String email, String birthDate) {

        // Comprueba que la conexión con el servidor está disponible
        if (socket == null || output == null || input == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay conexión con el servidor.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Realizamos la comunicación en otro hilo para no bloquear la interfaz
        new Thread(() -> {
            try {
                // Creamos la petición JSON con los datos actualizados
                JsonObject request = new JsonObject();
                request.addProperty("action", "updateUserData");
                request.addProperty("uid", currentUserId);
                request.addProperty("firstName", firstName);
                request.addProperty("lastName", lastName);
                request.addProperty("email", email);
                request.addProperty("birthDate", birthDate);

                String response;

                // Enviamos la petición y esperamos su respuesta como una única operación
                synchronized (socket) {
                    output.println(gson.toJson(request));
                    output.flush();

                    response = input.readLine();
                }

                if (response == null) {
                    throw new Exception("El servidor ha cerrado la conexión.");
                }

                // Interpretamos la respuesta enviada por S_socket
                JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);

                if (jsonResponse == null
                        || !jsonResponse.has("success")
                        || !jsonResponse.get("success").getAsBoolean()) {

                    String message = jsonResponse != null
                            && jsonResponse.has("message")
                            ? jsonResponse.get("message").getAsString()
                            : "No se han podido actualizar los datos.";

                    throw new Exception(message);
                }

                // Si el servidor confirma la actualización, refrescamos la interfaz
                javax.swing.SwingUtilities.invokeLater(() -> {
                    updateUserData(
                            firstName, lastName, email, birthDate, currentLevel
                    );

                    cardLayout.show(cardPanel, "VIEW_MODE");

                    JOptionPane.showMessageDialog(
                            HomeView.this,
                            "Perfil actualizado correctamente.",
                            "Perfil",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                });

            } catch (Exception e) {
                e.printStackTrace();

                javax.swing.SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(
                                HomeView.this,
                                "No se han podido actualizar los datos: "
                                        + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        )
                );
            }
        }).start();
    }
}