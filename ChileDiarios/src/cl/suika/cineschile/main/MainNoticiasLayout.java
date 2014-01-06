package cl.suika.cineschile.main;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cl.suika.cineschile.R;
import cl.suika.cineschile.DetalleNoticia;
import cl.suika.cineschile.utils.EllipsizingTextView;

@SuppressLint("ViewConstructor")
public class MainNoticiasLayout extends LinearLayout {

	public MainNoticiasLayout(final Context context, List<MainNewObject> noticias) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setPadding(0, 0, 0, 10);

		for (int i = 0; i < noticias.size(); i++) {
			LayoutInflater iconsInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View mView = iconsInflater.inflate(R.layout.item_main_noticia, null);

			final MainNewObject noticia = noticias.get(i);
//
//			((TextView) mView.findViewById(R.id.nombreDiario1)).setText(Html.fromHtml(noticia.diario));
//			
//			EllipsizingTextView titulo = ((EllipsizingTextView) mView.findViewById(R.id.tituloDiario1));
//			titulo.setText(Html.fromHtml(noticia.titulo));
//			titulo.setMaxLines(5);
//
//			
//			EllipsizingTextView bajada = ((EllipsizingTextView) mView.findViewById(R.id.bajadaDiario1)); 
//			bajada.setText(Html.fromHtml(noticia.bajada));
//			bajada.setMaxLines(3);

			((LinearLayout) mView.findViewById(R.id.contenedorNoticia1)).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intento = new Intent(context, DetalleNoticia.class);
					intento.putExtra("IDNOTICIA", noticia.id);
					intento.putExtra("IDAD", noticia.adServer);
					context.startActivity(intento);
				}
			});

			i += 1;
			if (i < noticias.size()) {
				final MainNewObject noticia2 = noticias.get(i);

//				((TextView) mView.findViewById(R.id.nombreDiario2)).setText(Html.fromHtml(noticia2.diario));
//				
//				EllipsizingTextView titulo2 = ((EllipsizingTextView) mView.findViewById(R.id.tituloDiario2));
//				titulo2.setText(Html.fromHtml(noticia2.titulo));
//				titulo2.setMaxLines(5);
//
//				EllipsizingTextView bajada2 = ((EllipsizingTextView) mView.findViewById(R.id.bajadaDiario2)); 
//				bajada2.setText(Html.fromHtml(noticia2.bajada));
//				bajada2.setMaxLines(3);

				((LinearLayout) mView.findViewById(R.id.contenedorNoticia2)).setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intento = new Intent(context, DetalleNoticia.class);
						intento.putExtra("IDNOTICIA", noticia2.id);
						intento.putExtra("IDAD", noticia.adServer);
						context.startActivity(intento);
					}
				});
			} else {
				((LinearLayout) mView.findViewById(R.id.contenedorNoticia2)).setVisibility(LinearLayout.INVISIBLE);
			}

			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.main_new_item_height));
			this.addView(mView, lp);

			View separador = new View(context);
			separador.setBackgroundColor(Color.parseColor("#808080"));
			this.addView(separador, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		}

	}
}
