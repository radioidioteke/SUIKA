package cl.suika.cineschile.diario;

import java.io.Serializable;

public class DiarioCategoriaNoticiaObject implements Serializable{

	private static final long serialVersionUID = 2L;
	public String id, hora, texto;

	public DiarioCategoriaNoticiaObject(String id, String hora, String texto) {
		this.id = id;
		this.hora = hora;
		this.texto = texto;
	}

}
