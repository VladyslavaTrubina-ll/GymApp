package model;

import java.io.Serializable;
import java.util.List;

public class Exercise implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String descripcion;
	private String id;
	private List<Serie> series;
	
	public Exercise() {
	}
	
	

	public Exercise(String nombre, String descripcion, String id, List<Serie> series) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.id = id;
		this.series = series;
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
	 * @return the series
	 */
	public List<Serie> getSeries() {
		return series;
	}

	/**
	 * @param series the series to set
	 */
	public void setSeries(List<Serie> series) {
		this.series = series;
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
	
	
	

}
