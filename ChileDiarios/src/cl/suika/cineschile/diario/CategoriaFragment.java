package cl.suika.cineschile.diario;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cl.suika.cineschile.R;
import cl.suika.cineschile.DetalleNoticia;

public class CategoriaFragment extends Fragment {


	public static final CategoriaFragment newInstance(int pageNumber, List<DiarioCategoriaObject> categorias, String adServer) {
		CategoriaFragment f = new CategoriaFragment();
		Bundle bdl = new Bundle(1);
		bdl.putInt("EXTRA_MESSAGE", pageNumber);
		bdl.putString("ADSERVER", adServer);
		bdl.putSerializable("OBJETO", (Serializable) categorias);
		f.setArguments(bdl);
		
		
		
		return f;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final int pageIndex = getArguments().getInt("EXTRA_MESSAGE", 0);
		final String adServer = getArguments().getString("ADSERVER");
		final List<DiarioCategoriaObject> categorias = (List<DiarioCategoriaObject>) getArguments().getSerializable("OBJETO");
				
		ListView lista = new ListView(getActivity());
		lista.setBackgroundColor(Color.parseColor("#FFFFFF"));
		lista.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		NoticiasListAdapter noticias = new NoticiasListAdapter(getActivity(), R.layout.item_diario_list, categorias.get(pageIndex).noticias);

		lista.setAdapter(noticias);

		lista.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intento = new Intent(getActivity(), DetalleNoticia.class);
				intento.putExtra("IDNOTICIA", categorias.get(pageIndex).noticias.get(arg2).id);
				intento.putExtra("IDAD", adServer);
				getActivity().startActivity(intento);
			}
		});

		return lista;
	}

}
