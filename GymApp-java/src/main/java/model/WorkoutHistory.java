package model;

import java.util.ArrayList;
import java.util.List;

public class WorkoutHistory {

    private String id;
    private double completionPercentage;
    private List<Workout> workouts;

    public WorkoutHistory() {
        this.completionPercentage = 0.0;
        this.workouts = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }
}