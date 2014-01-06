package cl.suika.cineschile.object;

import java.util.List;

public class PeliculaObj {

	private String id;
	private String pelicula;
	private String urlTrailer;
	private String urlImagen;
	private String fechaEstreno;
	private String duracion;
	private String genero;
	private String director;
	private List<String> reparto;
	private String productora;
	private String sinopsis;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPelicula() {
		return pelicula;
	}
	public void setPelicula(String pelicula) {
		this.pelicula = pelicula;
	}
	public String getUrlTrailer() {
		return urlTrailer;
	}
	public void setUrlTrailer(String urlTrailer) {
		this.urlTrailer = urlTrailer;
	}
	public String getFechaEstreno() {
		return fechaEstreno;
	}
	public void setFechaEstreno(String fechaEstreno) {
		this.fechaEstreno = fechaEstreno;
	}
	public String getDuracion() {
		return duracion;
	}
	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public List<String> getReparto() {
		return reparto;
	}
	public void setReparto(List<String> reparto) {
		this.reparto = reparto;
	}
	public String getProductora() {
		return productora;
	}
	public void setProductora(String productora) {
		this.productora = productora;
	}
	public String getSinopsis() {
		return sinopsis;
	}
	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}
	public String getUrlImagen() {
		return urlImagen;
	}
	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}
	
	
	
}
