package vista;

import model.Workout;
import model.Exercise;
import model.Serie;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.io.BufferedReader;
import java.io.PrintWriter;
import service.WorkoutSession;

public class WorkoutDetailView extends JPanel {

    private final Color NAVY = new Color(25, 36, 60);
    private final Color BLUE = new Color(55, 105, 220);
    private final Color BACKGROUND = new Color(243, 246, 252);
    private final Color TEXT = new Color(35, 45, 65);

    private Workout workout;
    private WorkoutSession workoutSession;

    private JList<Exercise> exerciseList;
    private DefaultListModel<Exercise> exerciseModel;

    private JPanel seriesPanel;
    private JLabel exerciseTitle;
    private JLabel exerciseDescription;

    
    // Original constructor
    public WorkoutDetailView(Workout workout) {
        this(workout, null, null, null);
    }

    // Constructor que recibe el workout seleccionado y el ID del usuario logeado
    public WorkoutDetailView(Workout workout, String currentUserId) {
        this(workout, currentUserId, null, null);
    }

    // Constructor que recibe también los streams para comunicarse con el servidor
    public WorkoutDetailView(
            Workout workout,
            String currentUserId,
            PrintWriter output,
            BufferedReader input) {

        this.workout = workout;

        // Initialize workout session
        if (currentUserId != null && workout.getId() != null
                && output != null && input != null) {

            int estimatedTime = 0;
            int totalExercises = 0;

            List<Exercise> exercises = workout.getEjercicios();

            if (exercises != null) {

                totalExercises = exercises.size();

                for (Exercise exercise : exercises) {

                    List<Serie> series = exercise.getSeries();

                    if (series != null) {

                    	for (Serie serie : series) {
                    	    estimatedTime += serie.getRepeticiones() * serie.getDuracion();
                    	}
                    }
                }
            }

            // Creamos la sesión y le pasamos la conexión con el servidor
            workoutSession = new WorkoutSession(
                    currentUserId,
                    workout.getId(),
                    estimatedTime,
                    totalExercises,
                    output,
                    input
            );
        }

        setLayout(new BorderLayout(0, 0));
        setBackground(BACKGROUND);

        // HEADER
        add(createHeader(), BorderLayout.NORTH);

        // MAIN CONTENT
        JPanel content = new JPanel(new BorderLayout(20, 0));
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        content.add(createExerciseSidebar(), BorderLayout.WEST);
        content.add(createDetailsPanel(), BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        // Select first exercise automatically
        if (!exerciseModel.isEmpty()) {
            exerciseList.setSelectedIndex(0);
        }

        JButton btnBack = new JButton("Volver");

        btnBack.addActionListener(e -> {

            if (workoutSession != null && workoutSession.hasStarted()) {
                workoutSession.saveProgress();
            }

            Window window = SwingUtilities.getWindowAncestor(this);

            if (window != null) {
                window.dispose();
            }
        });
    }

    // =====================================================
    // HEADER (nombre del workout seleccionado)
    // =====================================================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout(20, 10));
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(20, 25, 20, 25));

        // WORKOUT INFORMATION
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.WHITE);

        JLabel title = new JLabel(workout.getNombre());
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(NAVY);

        JLabel level = new JLabel("Nivel " + workout.getNivel());
        level.setFont(new Font("Arial", Font.BOLD, 14));
        level.setForeground(BLUE);

        String description = workout.getDescripcion();

        if (description == null || description.isBlank()) {
            description = "Sin descripción disponible.";
        }

        JLabel descriptionLabel = new JLabel(
                "<html><div style='width:500px;'>"
                + description
                + "</div></html>"
        );

        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setForeground(TEXT);

        info.add(title);
        info.add(Box.createVerticalStrut(5));
        info.add(level);
        info.add(Box.createVerticalStrut(10));
        info.add(descriptionLabel);

        header.add(info, BorderLayout.CENTER);

        return header;
    }

    // =====================================================
    // MENU LATERAL EJERCICIOS
    // =====================================================

    private JPanel createExerciseSidebar() {

        JPanel sidebar = new JPanel(new BorderLayout(0, 15));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(NAVY);
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel title = new JLabel("EJERCICIOS");
        title.setFont(new Font("Arial", Font.BOLD, 17));
        title.setForeground(Color.WHITE);

        sidebar.add(title, BorderLayout.NORTH);

        exerciseModel = new DefaultListModel<>();

        List<Exercise> exercises = workout.getEjercicios();

        if (exercises != null) {
            for (Exercise exercise : exercises) {
                exerciseModel.addElement(exercise);
            }
        }

        exerciseList = new JList<>(exerciseModel);

        exerciseList.setBackground(NAVY);
        exerciseList.setForeground(Color.WHITE);
        exerciseList.setSelectionBackground(BLUE);
        exerciseList.setSelectionForeground(Color.WHITE);
        exerciseList.setFont(new Font("Arial", Font.BOLD, 14));
        exerciseList.setFixedCellHeight(55);

        exerciseList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        exerciseList.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean selected,
                    boolean focused) {

                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, selected, focused);

                Exercise exercise = (Exercise) value;

                label.setText(String.format("%02d  ·  %s",
                        index + 1, exercise.getNombre()));

                label.setBorder(new EmptyBorder(10, 10, 10, 10));
                label.setOpaque(true);

                if (selected) {
                    label.setBackground(BLUE);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(NAVY);
                    label.setForeground(Color.WHITE);
                }

                return label;
            }
        });

        JScrollPane scroll = new JScrollPane(exerciseList);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(NAVY);

        sidebar.add(scroll, BorderLayout.CENTER);

        JLabel count = new JLabel(
                exercises == null ? "0 ejercicios"
                        : exercises.size() + " ejercicios");

        count.setForeground(new Color(190, 200, 220));
        count.setFont(new Font("Arial", Font.PLAIN, 12));

        sidebar.add(count, BorderLayout.SOUTH);

        // Update details when an exercise is selected
        exerciseList.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                Exercise selected = exerciseList.getSelectedValue();

                if (selected != null) {
                    showExerciseDetails(selected);
                }
            }
        });

        return sidebar;
    }

    // =====================================================
    // PANEL DETALLES
    // =====================================================

    private JPanel createDetailsPanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(BACKGROUND);

        JPanel exerciseInfo = new JPanel();
        exerciseInfo.setLayout(new BoxLayout(
                exerciseInfo, BoxLayout.Y_AXIS));
        exerciseInfo.setBackground(BACKGROUND);

        exerciseTitle = new JLabel("Selecciona un ejercicio");
        exerciseTitle.setFont(new Font("Arial", Font.BOLD, 24));
        exerciseTitle.setForeground(NAVY);

        exerciseDescription = new JLabel("");
        exerciseDescription.setFont(
                new Font("Arial", Font.PLAIN, 14));
        exerciseDescription.setForeground(TEXT);

        exerciseInfo.add(exerciseTitle);
        exerciseInfo.add(Box.createVerticalStrut(10));
        exerciseInfo.add(exerciseDescription);

        panel.add(exerciseInfo, BorderLayout.NORTH);

        seriesPanel = new JPanel();
        seriesPanel.setLayout(new BoxLayout(
                seriesPanel, BoxLayout.Y_AXIS));
        seriesPanel.setBackground(BACKGROUND);

        JScrollPane scroll = new JScrollPane(seriesPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(BACKGROUND);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // =====================================================
    // MOSTRAR EJERCICIO SELECCIONADO
    // =====================================================

    private void showExerciseDetails(Exercise exercise) {

        exerciseTitle.setText(exercise.getNombre());

        exerciseDescription.setText(
                "<html>" + exercise.getDescripcion() + "</html>");

        seriesPanel.removeAll();

        List<Serie> seriesList = exercise.getSeries();

        if (seriesList != null && !seriesList.isEmpty()) {

            for (int i = 0; i < seriesList.size(); i++) {

                Serie serie = seriesList.get(i);

                seriesPanel.add(createSeriesCard(
                        serie,
                        exercise.getNombre(),
                        exerciseList.getSelectedIndex() + 1,
                        i + 1,
                        seriesList.size()
                ));

                seriesPanel.add(Box.createVerticalStrut(15));
            }

        } else {

            JLabel empty = new JLabel(
                    "No hay series disponibles para este ejercicio.");

            empty.setFont(new Font("Arial", Font.PLAIN, 14));
            empty.setForeground(TEXT);

            seriesPanel.add(empty);
        }

        seriesPanel.revalidate();
        seriesPanel.repaint();
    }

    // =====================================================
    // CARD DE SERIES
    // =====================================================

    private JPanel createSeriesCard(
            Serie serie,
            String exerciseName,
            int exerciseNumber,
            int seriesNumber,
            int totalSeriesForExercise) {

        JPanel card = new JPanel(new BorderLayout(20, 0));

        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 225, 235)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        card.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 180));

        // SERIES INFORMATION
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.WHITE);

        JLabel name = new JLabel(serie.getNombre());
        name.setFont(new Font("Arial", Font.BOLD, 18));
        name.setForeground(NAVY);

        JLabel repetitions = createInfoLabel(
                "Repeticiones: " + serie.getRepeticiones());

        JLabel duration = createInfoLabel(
                "Duración: " + serie.getDuracion() + " segundos");

        JLabel rest = createInfoLabel(
                "Descanso: " + serie.getDescanso() + " segundos");

        info.add(name);
        info.add(Box.createVerticalStrut(12));
        info.add(repetitions);
        info.add(Box.createVerticalStrut(6));
        info.add(duration);
        info.add(Box.createVerticalStrut(6));
        info.add(rest);

        // START BUTTON
        JButton btnEmpezar = new JButton("EMPEZAR");

        btnEmpezar.setFont(new Font("Arial", Font.BOLD, 13));
        btnEmpezar.setBackground(BLUE);
        btnEmpezar.setForeground(Color.WHITE);
        btnEmpezar.setFocusPainted(false);
        btnEmpezar.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        btnEmpezar.addActionListener(e -> {

        	String exerciseKey = String.valueOf(exerciseNumber);
            String seriesKey = exerciseKey + "-" + seriesNumber;

            new SerieTimerView(
                    serie,
                    workout.getNombre(),
                    exerciseName,
                    exerciseNumber,
                    workoutSession,
                    seriesKey,
                    exerciseKey,
                    totalSeriesForExercise
            ).setVisible(true);
        });

        info.add(Box.createVerticalStrut(12));
        info.add(btnEmpezar);

        card.add(info, BorderLayout.CENTER);

        // SERIES IMAGE
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(150, 110));

        String imagePath = serie.getFoto();

        if (imagePath != null && !imagePath.isEmpty()) {

            File imageFile = new File(imagePath);

            if (imageFile.exists()) {

                ImageIcon icon = new ImageIcon(imagePath);

                Image image = icon.getImage().getScaledInstance(
                        150, 110, Image.SCALE_SMOOTH);

                imageLabel.setIcon(new ImageIcon(image));
            }
        }

        card.add(imageLabel, BorderLayout.EAST);

        return card;
    }

    // =====================================================
    // LABELS
    // =====================================================

    private JLabel createInfoLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(TEXT);

        return label;
    }
}