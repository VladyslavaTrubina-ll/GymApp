package model;

import java.util.ArrayList;

public class Workout {
	private String nombre;
	private String video;
	private int level;
	private ArrayList<Exercise> ejercicios;
	
	public Workout() {
	}

	public Workout(String nombre, String video, int level, ArrayList<Exercise> ejercicios) {
		this.nombre = nombre;
		this.video = video;
		this.level = level;
		this.ejercicios = ejercicios;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the video
	 */
	public String getVideo() {
		return video;
	}

	/**
	 * @param video the video to set
	 */
	public void setVideo(String video) {
		this.video = video;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the ejercicios
	 */
	public ArrayList<Exercise> getEjercicios() {
		return ejercicios;
	}

	/**
	 * @param ejercicios the ejercicios to set
	 */
	public void setEjercicios(ArrayList<Exercise> ejercicios) {
		this.ejercicios = ejercicios;
	}
	
	

}
