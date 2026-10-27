package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Workout implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String video;
	private int level;
	private List<Exercise> ejercicios;
	
	public Workout() {
	}

	public Workout(String nombre, String video, int level, List<Exercise> ejercicios) {
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
	public List<Exercise> getEjercicios() {
		return ejercicios;
	}

	/**
	 * @param ejercicios the ejercicios to set
	 */
	public void setEjercicios(ArrayList<Exercise> ejercicios) {
		this.ejercicios = ejercicios;
	}
	
	

}
