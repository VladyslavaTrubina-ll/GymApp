package model;

import java.io.Serializable;
import java.util.List;

public class Workout implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String descripcion;
	private String id;
	private String nombre;
	private String video;
	private int nivel;
	private List<Exercise> ejercicios;
	
	public Workout() {
	}

	

	public Workout(String descripcion,String id, String nombre, String video, int level, List<Exercise> ejercicios) {
		this.descripcion= descripcion;
		this.id = id;
		this.nombre = nombre;
		this.video = video;
		this.nivel = level;
		this.ejercicios = ejercicios;
	}
	
	
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}



	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



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
	public int getNivel() {
		return nivel;
	}

	/**
	 * @param level the level to set
	 */
	public void setNivel(int level) {
		this.nivel = level;
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
	public void setEjercicios(List<Exercise> ejercicios) {
		this.ejercicios = ejercicios;
	}
	
	

}
