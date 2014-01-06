package cl.suika.cineschile.main;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cl.suika.cineschile.R;

public class DiariosListAdapter extends ArrayAdapter<DiarioObject> {
	private Context context;
	private List<DiarioObject> values;
	private int layout;

	public DiariosListAdapter(Context context, int layout, List<DiarioObject> values) {
		super(context, layout, values);
		this.context = context;
		this.layout = layout;
		this.values = values;
	}

	static class NoticiaHolder {
		TextView texto;
		ImageView imagen;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		NoticiaHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layout, parent, false);

			holder = new NoticiaHolder();
			holder.texto = (TextView) row.findViewById(R.id.text1);
			holder.imagen = (ImageView) row.findViewById(R.id.imagen1);
			row.setTag(holder);
		} else {
			holder = (NoticiaHolder) row.getTag();
		}

		DiarioObject noticia = values.get(position);
		holder.texto.setText(Html.fromHtml(noticia.diario));
		
		if(!ImageLoader.getInstance().isInited()){
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build(); //.discCacheExtraOptions(width, width, CompressFormat.JPEG, 75, null)
			ImageLoader.getInstance().init(config);
		}
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.menu_chilediario_default).showImageForEmptyUri(R.drawable.menu_chilediario_default).showImageOnFail(R.drawable.menu_chilediario_default).cacheInMemory(true).cacheOnDisc(true).build();
		ImageLoader.getInstance().displayImage(noticia.urlImg, holder.imagen, options);
		
		//if(position%2==0){
			//holder.imagen.setImageResource(R.drawable.dummie_icon1);
		//}else{
			//holder.imagen.setImageResource(R.drawable.dummie_icon2);
		//}
		

		return row;

	}
}