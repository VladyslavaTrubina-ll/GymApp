
package vista;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.Desktop;
import java.net.URI;

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

import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.List;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HomeView extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextArea userInfoArea;
    private JPanel workoutsListPanel;
    private JTextField tfFirstName, tfLastName, tfEmail, tfBirthDate;
    private int currentLevel = 0;

    private final UserService userService = new UserService();
    private final WorkoutService workoutService = new WorkoutService();

    private String currentUserId;

    // Modern GymApp color palette
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
                    HomeView frame = new HomeView("test_uid_123");
                    frame.setVisible(true);

                    // Datos de prueba originales
                    frame.updateUserData(
                        "Ana", "Krol",
                        "anna.krol@example.com",
                        "8 de febrero de 2001, 12:00 AM", 0
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

        this.currentUserId = uid;

        setTitle("GymApp | Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(760, 500));
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout(20, 0));
        contentPane.setBackground(BACKGROUND);
        contentPane.setBorder(new EmptyBorder(25, 25, 25, 25));
        setContentPane(contentPane);

        // menu lateral
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
        sidebar.add(btnHistory);

        JLabel lblFooter = new JLabel(
                "<html><center>Tu progreso<br>empieza aquí</center></html>",
                SwingConstants.CENTER
        );
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(new Color(190, 201, 222));
        sidebar.add(lblFooter);

        contentPane.add(sidebar, BorderLayout.WEST);

        // Contenedor principal
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

                // TODO: Vincular aquí la persistencia con Firestore

                // Actualizamos y volvemos atrás
                updateUserData(
                    newFirst, newLast, newEmail,
                    newBirth, currentLevel
                );

                cardLayout.show(cardPanel, "VIEW_MODE");
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

        mainPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        contentPane.add(mainPanel, BorderLayout.CENTER);

        // Initial view
        cardLayout.show(cardPanel, "VIEW_MODE");

        // Original Firestore loading
        loadUserData(uid);
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

    // Original Firestore profile-loading method
    private void loadUserData(String uid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    com.google.cloud.firestore.DocumentSnapshot document =
                            userService.getUserProfileAsync(uid).get();

                    if (document.exists()) {
                        final String firstName =
                                document.getString("firstName") != null
                                ? document.getString("firstName") : "";

                        final String lastName =
                                document.getString("lastName") != null
                                ? document.getString("lastName") : "";

                        final String email =
                                document.getString("email") != null
                                ? document.getString("email") : "";

                        final String birthDate =
                                document.getString("birthDate") != null
                                ? document.getString("birthDate") : "";

                        Long levelLong = document.getLong("level");
                        final int level =
                                levelLong != null ? levelLong.intValue() : 0;

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

    /**
     * Método original para actualizar los datos.
     */
    private void loadAvailableWorkouts() {

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
                    List<QueryDocumentSnapshot> workouts =
                            workoutService.getAvailableWorkouts(currentUserId);

                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            workoutsListPanel.removeAll();

                            if (workouts.isEmpty()) {
                                JLabel emptyLabel = new JLabel(
                                        "No hay workouts disponibles para tu nivel."
                                );
                                emptyLabel.setFont(
                                        new Font("Segoe UI", Font.PLAIN, 14)
                                );
                                emptyLabel.setForeground(MUTED);
                                workoutsListPanel.add(emptyLabel);
                            } else {
                                for (QueryDocumentSnapshot workout : workouts) {
                                    String name = workout.getString("nombre");

                                    if (name == null || name.isBlank()) {
                                        name = workout.getId();
                                    }

                                    Long levelValue = workout.getLong("nivel");
                                    int level = levelValue != null
                                            ? levelValue.intValue() : 0;

                                    String description =
                                            workout.getString("descripcion");

                                    String video = workout.getString("video");

                                    JPanel workoutCard = createWorkoutCard(
                                            name,
                                            level,
                                            description,
                                            video
                                    );

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

                } catch (Exception e) {
                    e.printStackTrace();

                    final String errorMessage = e.getMessage();

                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            workoutsListPanel.removeAll();

                            JLabel errorLabel = new JLabel(
                                    "<html>No se han podido cargar los workouts.<br>"
                                    + errorMessage + "</html>"
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

    private JPanel createWorkoutCard(
            String name,
            int level,
            String description,
            String video) {

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

        JButton btnStart = createPrimaryButton("START");
        btnStart.setPreferredSize(new Dimension(100, 40));

        if (video == null || video.isBlank()) {
            btnStart.setEnabled(false);
            btnStart.setToolTipText(
                    "Este workout no tiene un vídeo disponible."
            );
        } else {
            btnStart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        URI videoUri = new URI(video);

                        if (!"https".equalsIgnoreCase(videoUri.getScheme())
                                && !"http".equalsIgnoreCase(videoUri.getScheme())) {
                            throw new IllegalArgumentException(
                                    "La URL del vídeo no es válida."
                            );
                        }

                        if (Desktop.isDesktopSupported()
                                && Desktop.getDesktop().isSupported(
                                        Desktop.Action.BROWSE)) {
                            Desktop.getDesktop().browse(videoUri);
                        } else {
                            JOptionPane.showMessageDialog(
                                    HomeView.this,
                                    "No se puede abrir el navegador en este equipo.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                HomeView.this,
                                "No se ha podido abrir el vídeo:\n"
                                + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            });
        }

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(btnStart, BorderLayout.CENTER);

        card.add(informationPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    public void updateUserData(
            String firstName,
            String lastName,
            String email,
            String birthDate,
            int level) {

        this.currentLevel = level;

        // Sincronizamos los campos de entrada de texto
        tfFirstName.setText(firstName);
        tfLastName.setText(lastName);
        tfEmail.setText(email);
        tfBirthDate.setText(birthDate);

        // Sincronizamos la descripción textual en modo lectura
        StringBuilder sb = new StringBuilder();

        sb.append("Nombre: \"").append(firstName).append("\"\n");
        sb.append("Apellidos: \"").append(lastName).append("\"\n");
        sb.append("Email: \"").append(email).append("\"\n");
        sb.append("Nacimiento: ").append(birthDate).append("\n");
        sb.append("Nivel: ").append(level);

        userInfoArea.setText(sb.toString());
    }
}
