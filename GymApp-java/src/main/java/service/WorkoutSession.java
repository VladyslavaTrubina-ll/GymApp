package service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorkoutSession {

    private final WorkoutService workoutService =
            new WorkoutService();

    private final String userId;
    private final String workoutId;

    private final int estimatedTime;
    private final int totalExercises;

    private String historyId;

    private int totalTime = 0;

    private final Map<String, Set<String>> completedSeries =
            new HashMap<>();

    private final Set<String> completedExerciseKeys =
            new HashSet<>();

    public WorkoutSession(
            String userId,
            String workoutId,
            int estimatedTime,
            int totalExercises) {

        this.userId = userId;
        this.workoutId = workoutId;
        this.estimatedTime = estimatedTime;
        this.totalExercises = totalExercises;
    }

    // Create history only on the first PLAY
    public synchronized void startIfNeeded() {

        if (historyId == null) {

            historyId = workoutService.startWorkoutHistory(
                    userId,
                    workoutId,
                    estimatedTime
            );
        }
    }

    // Count elapsed seconds
    public synchronized void addElapsedSecond() {

        if (historyId != null) {
            totalTime++;
        }
    }

    // Mark a series as completed
    public synchronized void completeSeries(
            String exerciseKey,
            String seriesKey,
            int totalSeriesForExercise) {

        if (historyId == null) {
            return;
        }

        Set<String> series = completedSeries.computeIfAbsent(
                exerciseKey,
                key -> new HashSet<>()
        );

        series.add(seriesKey);

        if (totalSeriesForExercise > 0
                && series.size() >= totalSeriesForExercise) {

            completedExerciseKeys.add(exerciseKey);
        }

        saveProgress();
    }

    // Update the same history document
    public synchronized void saveProgress() {

        if (historyId == null) {
            return;
        }

        int percentage = totalExercises > 0
                ? (completedExerciseKeys.size() * 100) / totalExercises
                : 0;

        workoutService.updateWorkoutHistory(
                userId,
                historyId,
                totalTime,
                percentage
        );
    }

    public synchronized boolean hasStarted() {
        return historyId != null;
    }

    public synchronized int getTotalTime() {
        return totalTime;
    }

    public synchronized int getCompletedExercises() {
        return completedExerciseKeys.size();
    }
}