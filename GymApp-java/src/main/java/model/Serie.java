package model;

<<<<<<< HEAD
public class Serie {
=======
import java.io.Serializable;

public class Serie implements Serializable{
	
	private static final long serialVersionUID = 1L;
>>>>>>> creacionBackupsDAT
	private int descanso;
	private int duracion;
	private String foto;
	private String nombre;
	private int repeticiones;
<<<<<<< HEAD
=======
	private String id;
	
>>>>>>> creacionBackupsDAT
	
	public Serie() {
		
	}

<<<<<<< HEAD
	public Serie(int descanso, int duracion, String foto, String nombre, int repeticiones) {
=======
	public Serie(int descanso, int duracion, String foto, String nombre, int repeticiones, String id) {
>>>>>>> creacionBackupsDAT
		this.descanso = descanso;
		this.duracion = duracion;
		this.foto = foto;
		this.nombre = nombre;
		this.repeticiones = repeticiones;
<<<<<<< HEAD
=======
		this.id= id;
>>>>>>> creacionBackupsDAT
	}

	public int getDescanso() {
		return descanso;
	}

	public void setDescanso(int descanso) {
		this.descanso = descanso;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getRepeticiones() {
		return repeticiones;
	}

	public void setRepeticiones(int repeticiones) {
		this.repeticiones = repeticiones;
	}
<<<<<<< HEAD
=======

	
	public String getId() {
		return id;
	}

	
	public void setId(String id) {
		this.id = id;
	}
	
	
>>>>>>> creacionBackupsDAT
	
	
}
