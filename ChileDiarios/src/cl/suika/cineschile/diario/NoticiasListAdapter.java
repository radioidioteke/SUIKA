package cl.suika.cineschile.diario;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cl.suika.cineschile.R;

public class NoticiasListAdapter extends ArrayAdapter<DiarioCategoriaNoticiaObject> {
	private Context context;
	private List<DiarioCategoriaNoticiaObject> values;
	private int layout;

	public NoticiasListAdapter(Context context, int layout, List<DiarioCategoriaNoticiaObject> values) {
		super(context, layout, values);
		this.context = context;
		this.layout = layout;
		this.values = values;
	}

	static class NoticiaHolder {
		TextView texto;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		NoticiaHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layout, parent, false);

			holder = new NoticiaHolder();
			holder.texto  = (TextView) row.findViewById(R.id.texto);
			row.setTag(holder);
		} else {
			holder = (NoticiaHolder) row.getTag();
		}
		
		DiarioCategoriaNoticiaObject noticia = values.get(position);
		holder.texto.setText(Html.fromHtml("<font color=#cc0029>"+noticia.hora+"</font> "+noticia.texto));
	
		return row;

	}
}