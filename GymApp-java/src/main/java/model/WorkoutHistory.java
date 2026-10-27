package model;

import java.io.Serializable;

public class WorkoutHistory implements Serializable{

	private static final long serialVersionUID = 1L;
	private float completionPercentage;
	private String date;
	private int estimatedTime;
	private int totalTime;
	private String workoutID;
	
	public WorkoutHistory() {
	}

	public WorkoutHistory(float completionPercentage, String date, int estimatedTime, int totalTime, String workoutID) {
		this.completionPercentage = completionPercentage;
		this.date = date;
		this.estimatedTime = estimatedTime;
		this.totalTime = totalTime;
		this.workoutID = workoutID;
	}

	/**
	 * @return the completionPercentage
	 */
	public float getCompletionPercentage() {
		return completionPercentage;
	}

	/**
	 * @param completionPercentage the completionPercentage to set
	 */
	public void setCompletionPercentage(float completionPercentage) {
		this.completionPercentage = completionPercentage;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the estimatedTime
	 */
	public int getEstimatedTime() {
		return estimatedTime;
	}

	/**
	 * @param estimatedTime the estimatedTime to set
	 */
	public void setEstimatedTime(int estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	/**
	 * @return the totalTime
	 */
	public int getTotalTime() {
		return totalTime;
	}

	/**
	 * @param totalTime the totalTime to set
	 */
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	/**
	 * @return the workoutID
	 */
	public String getWorkoutID() {
		return workoutID;
	}

	/**
	 * @param workoutID the workoutID to set
	 */
	public void setWorkoutID(String workoutID) {
		this.workoutID = workoutID;
	}
	
	

}
