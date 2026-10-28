package model;

import java.io.Serializable;
<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> creacionBackupsDAT
import java.util.List;

public class Workout implements Serializable{
	
	private static final long serialVersionUID = 1L;
<<<<<<< HEAD
=======
	private String id;
>>>>>>> creacionBackupsDAT
	private String nombre;
	private String video;
	private int level;
	private List<Exercise> ejercicios;
	
	public Workout() {
	}

<<<<<<< HEAD
	public Workout(String nombre, String video, int level, List<Exercise> ejercicios) {
=======
	

	public Workout(String id, String nombre, String video, int level, List<Exercise> ejercicios) {
		this.id = id;
>>>>>>> creacionBackupsDAT
		this.nombre = nombre;
		this.video = video;
		this.level = level;
		this.ejercicios = ejercicios;
	}
<<<<<<< HEAD
=======
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
>>>>>>> creacionBackupsDAT

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
<<<<<<< HEAD
	public void setEjercicios(ArrayList<Exercise> ejercicios) {
=======
	public void setEjercicios(List<Exercise> ejercicios) {
>>>>>>> creacionBackupsDAT
		this.ejercicios = ejercicios;
	}
	
	

}
