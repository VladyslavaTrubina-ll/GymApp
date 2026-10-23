package model;

public class Serie {
	private int descanso;
	private int duracion;
	private String foto;
	private String nombre;
	private int repeticiones;
	
	public Serie() {
		
	}

	public Serie(int descanso, int duracion, String foto, String nombre, int repeticiones) {
		this.descanso = descanso;
		this.duracion = duracion;
		this.foto = foto;
		this.nombre = nombre;
		this.repeticiones = repeticiones;
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
	
	
}
