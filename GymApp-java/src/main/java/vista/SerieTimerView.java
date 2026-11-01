
package vista;

import model.Serie;
import service.WorkoutSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class SerieTimerView extends JFrame {

    private int exerciseSecondsLeft;
    private int restSecondsLeft;

    private Timer timer;

    private boolean resting = false;
    private boolean seriesFinished = false;
    private boolean awaitingNextRepetition = false;

    private JLabel exerciseTimeLabel;
    private JLabel restTimeLabel;
    private JLabel repetitionsLabel;

    private JButton playButton;

    private int currentRepetition = 1;
    private final int totalRepetitions;

    private final Serie serie;
    private final WorkoutSession session;
    private final String seriesKey;
    private final String exerciseKey;
    private final int totalSeriesForExercise;

    private final Color BACKGROUND =
            new Color(38, 42, 50);

    private final Color PANEL =
            new Color(48, 53, 62);

    private final Color GREEN =
            new Color(76, 175, 80);

    private final Color RED =
            new Color(230, 65, 65);

    public SerieTimerView(
            Serie serie,
            String workoutName,
            String exerciseName,
            int exerciseNumber,
            WorkoutSession session,
            String seriesKey,
            String exerciseKey,
            int totalSeriesForExercise) {

        this.exerciseKey = exerciseKey;
        this.totalSeriesForExercise = totalSeriesForExercise;

        this.serie = serie;
        this.session = session;
        this.seriesKey = seriesKey;

        this.totalRepetitions = Math.max(1, serie.getRepeticiones());

        exerciseSecondsLeft = Math.max(0, serie.getDuracion());
        restSecondsLeft = Math.max(0, serie.getDescanso());

        setTitle("Serie: " + serie.getNombre());

        setSize(750, 650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(15, 15));

        main.setBackground(BACKGROUND);

        main.setBorder(new EmptyBorder(15, 15, 15, 15));

        // =================================================
        // HEADER
        // =================================================

        JPanel header = new JPanel(
                new GridLayout(1, 3, 10, 0)
        );

        header.setBackground(BACKGROUND);

        header.add(createInfoPanel(
                "WORKOUT",
                workoutName
        ));

        header.add(createInfoPanel(
                "EJERCICIO " + exerciseNumber,
                exerciseName
        ));

        header.add(createInfoPanel(
                "SERIE",
                serie.getNombre() + " - "
                        + serie.getRepeticiones() + " repeticiones"
        ));

        main.add(header, BorderLayout.NORTH);

        // =================================================
        // CENTER
        // =================================================

        JPanel center = new JPanel();

        center.setLayout(
                new BoxLayout(center, BoxLayout.Y_AXIS)
        );

        center.setBackground(BACKGROUND);

        // REPETITIONS

        JPanel repetitionsPanel =
                createTimerPanel("REPETICIÓN");

        repetitionsLabel = new JLabel(
                formatRepetitions()
        );

        styleTimerValue(repetitionsLabel, Color.YELLOW);

        repetitionsPanel.add(
                repetitionsLabel,
                BorderLayout.EAST
        );

        center.add(repetitionsPanel);

        center.add(Box.createVerticalStrut(10));

        // EXERCISE TIMER

        JPanel exerciseTimerPanel =
                createTimerPanel("TIEMPO EJERCICIO");

        exerciseTimeLabel = new JLabel(
                formatTime(exerciseSecondsLeft)
        );

        styleTimerValue(exerciseTimeLabel, GREEN);

        exerciseTimerPanel.add(
                exerciseTimeLabel,
                BorderLayout.EAST
        );

        center.add(exerciseTimerPanel);

        center.add(Box.createVerticalStrut(10));

        // REST TIMER

        JPanel restTimerPanel =
                createTimerPanel("DESCANSO");

        restTimeLabel = new JLabel(
                formatTime(restSecondsLeft)
        );

        styleTimerValue(restTimeLabel, Color.WHITE);

        restTimerPanel.add(
                restTimeLabel,
                BorderLayout.EAST
        );

        center.add(restTimerPanel);

        center.add(Box.createVerticalStrut(20));

        // SERIES PHOTO

        JLabel imageLabel = new JLabel();

        imageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        imageLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        String imagePath = serie.getFoto();

        if (imagePath != null && !imagePath.isEmpty()) {

            File imageFile = new File(imagePath);

            if (imageFile.exists()) {

                ImageIcon icon = new ImageIcon(imagePath);

                Image image = icon.getImage().getScaledInstance(
                        300,
                        220,
                        Image.SCALE_SMOOTH
                );

                imageLabel.setIcon(new ImageIcon(image));

            } else {

                imageLabel.setText(
                        "Imagen no encontrada: " + imagePath
                );

                imageLabel.setForeground(Color.WHITE);
            }
        }

        center.add(imageLabel);

        main.add(center, BorderLayout.CENTER);

        // =================================================
        // BUTTONS
        // =================================================

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 20, 5)
        );

        buttons.setBackground(BACKGROUND);

        playButton = new JButton("▶ PLAY");

        playButton.setBackground(GREEN);

        playButton.setForeground(Color.WHITE);

        playButton.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        playButton.setFocusPainted(false);

        JButton exitButton = new JButton("SALIR");

        exitButton.setBackground(RED);

        exitButton.setForeground(Color.WHITE);

        exitButton.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        exitButton.setFocusPainted(false);

        buttons.add(playButton);

        buttons.add(exitButton);

        main.add(buttons, BorderLayout.SOUTH);

        add(main);

        // =================================================
        // TIMER
        // =================================================

        timer = new Timer(1000, e -> updateTimer());

        // PLAY / PAUSE / NEXT REPETITION

        playButton.addActionListener(e -> {

            // Pause the timer
            if (timer.isRunning()) {

                timer.stop();

                playButton.setText("▶ CONTINUAR");

                return;
            }

            if (seriesFinished) {
                return;
            }

            try {

                // Creates history only on the first PLAY.
                session.startIfNeeded();

                // Start the next repetition
                if (awaitingNextRepetition) {

                    currentRepetition++;

                    repetitionsLabel.setText(
                            formatRepetitions()
                    );

                    exerciseSecondsLeft = Math.max(
                            0,
                            serie.getDuracion()
                    );

                    restSecondsLeft = Math.max(
                            0,
                            serie.getDescanso()
                    );

                    exerciseTimeLabel.setText(
                            formatTime(exerciseSecondsLeft)
                    );

                    restTimeLabel.setText(
                            formatTime(restSecondsLeft)
                    );

                    resting = false;

                    awaitingNextRepetition = false;

                    JOptionPane.showMessageDialog(
                            this,
                            "Comenzando la repetición "
                                    + currentRepetition
                                    + " de "
                                    + totalRepetitions
                    );
                }

                timer.start();

                playButton.setText("⏸ PAUSAR");

            } catch (RuntimeException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo guardar el historial:\n"
                                + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // EXIT

        exitButton.addActionListener(e -> {

            timer.stop();

            saveSessionSafely();

            dispose();

        });
    }

    // =====================================================
    // TIMER PANELS
    // =====================================================

    private JPanel createTimerPanel(String title) {

        JPanel panel = new JPanel(
                new BorderLayout()
        );

        panel.setBackground(PANEL);

        panel.setBorder(
                new EmptyBorder(10, 15, 10, 15)
        );

        panel.setMaximumSize(
                new Dimension(330, 75)
        );

        JLabel label = new JLabel(title);

        label.setForeground(Color.WHITE);

        panel.add(label, BorderLayout.WEST);

        return panel;
    }

    private void styleTimerValue(
            JLabel label,
            Color color) {

        label.setForeground(color);

        label.setFont(
                new Font("Arial", Font.BOLD, 24)
        );
    }

    // =====================================================
    // UPDATE TIMER
    // =====================================================

    private void updateTimer() {

        if (!resting) {

            // Count only exercise seconds
            session.addElapsedSecond();

            exerciseSecondsLeft--;

            exerciseTimeLabel.setText(
                    formatTime(Math.max(0, exerciseSecondsLeft))
            );

            if (exerciseSecondsLeft <= 0) {

                exerciseSecondsLeft = 0;

                exerciseTimeLabel.setText("00:00");

                if (restSecondsLeft > 0) {

                    resting = true;

                } else {

                    finishRepetition();
                }
            }

        } else {

            // REST TIMER - do not add to totalTime
            restSecondsLeft--;

            restTimeLabel.setText(
                    formatTime(Math.max(0, restSecondsLeft))
            );

            if (restSecondsLeft <= 0) {

                restSecondsLeft = 0;

                restTimeLabel.setText("00:00");

                finishRepetition();
            }
        }
    }

    // =====================================================
    // FINISH REPETITION
    // =====================================================

    private void finishRepetition() {

        timer.stop();

        resting = false;

        // More repetitions remaining

        if (currentRepetition < totalRepetitions) {

            awaitingNextRepetition = true;

            playButton.setText("▶ SIGUIENTE REPETICIÓN");

            JOptionPane.showMessageDialog(
                    this,
                    "Descanso terminado.\n"
                            + "Pulsa SIGUIENTE REPETICIÓN para continuar."
            );

        } else {

            // All repetitions completed

            finishSeries();
        }
    }

    // =====================================================
    // FINISH SERIES
    // =====================================================

    private void finishSeries() {

        timer.stop();

        if (!seriesFinished) {

            seriesFinished = true;

            session.completeSeries(
                    exerciseKey,
                    seriesKey,
                    totalSeriesForExercise
            );
        }

        playButton.setText("FINALIZADO");

        playButton.setEnabled(false);

        JOptionPane.showMessageDialog(
                this,
                "¡Serie completada!"
        );
    }

    // =====================================================
    // SAVE HISTORY
    // =====================================================

    private void saveSessionSafely() {

        try {

            session.saveProgress();

        } catch (RuntimeException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el historial:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        }
    }

    // =====================================================
    // INFORMATION PANELS
    // =====================================================

    private JPanel createInfoPanel(
            String title,
            String value) {

        JPanel panel = new JPanel();

        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS)
        );

        panel.setBackground(PANEL);

        panel.setBorder(
                new EmptyBorder(12, 12, 12, 12)
        );

        JLabel titleLabel = new JLabel(title);

        titleLabel.setForeground(
                new Color(180, 190, 205)
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        JLabel valueLabel = new JLabel(value);

        valueLabel.setForeground(Color.WHITE);

        valueLabel.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(8));

        panel.add(valueLabel);

        return panel;
    }

    // =====================================================
    // FORMAT REPETITIONS
    // =====================================================

    private String formatRepetitions() {

        return String.format(
                "%02d / %02d",
                currentRepetition,
                totalRepetitions
        );
    }

    // =====================================================
    // FORMAT TIME
    // =====================================================

    private String formatTime(int seconds) {

        int minutes = seconds / 60;

        int remainingSeconds = seconds % 60;

        return String.format(
                "%02d:%02d",
                minutes,
                remainingSeconds
        );
    }

}
