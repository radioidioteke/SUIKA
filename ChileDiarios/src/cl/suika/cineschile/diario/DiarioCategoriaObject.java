package cl.suika.cineschile.diario;

import java.io.Serializable;
import java.util.List;

public class DiarioCategoriaObject implements Serializable{

	private static final long serialVersionUID = 1L;
	public String categoria;
	public List<DiarioCategoriaNoticiaObject> noticias;

	public DiarioCategoriaObject(String categoria, List<DiarioCategoriaNoticiaObject> noticias) {
		this.categoria = categoria;
		this.noticias = noticias;
	}

}
