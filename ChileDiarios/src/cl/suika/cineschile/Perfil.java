package cl.suika.cineschile;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cl.suika.cineschile.R;
import cl.suika.cineschile.connection.AsyncResponse;
import cl.suika.cineschile.connection.ConnectionUpdateUser;
import cl.suika.cineschile.utils.LoadingDialog;
import cl.suika.cineschile.utils.MenusCreator;
import cl.suika.cineschile.utils.Validators;
import cl.suika.cineschile.utils.Varios;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.banner.AdView;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.google.analytics.tracking.android.EasyTracker;

@SuppressLint("ValidFragment")
public class Perfil extends SherlockFragmentActivity implements AsyncResponse {

	private EditText nombre;
	private EditText apellido;
	private RadioGroup sexo;
	private EditText fecha;
	private EditText pais;
	private EditText passActual;
	private EditText pass1;
	private EditText pass2;
	private Spinner paisSpinner;
	private Button submit;

	private LoadingDialog preloader;

	private DialogFragment newFragment = new DatePickerFragment();

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
		MenusCreator.setupActionBar(this);

		preloader = new LoadingDialog(this);
		nombre = ((EditText) findViewById(R.id.nombre));
		apellido = ((EditText) findViewById(R.id.apellido));
		fecha = ((EditText) findViewById(R.id.fecha));
		pais = ((EditText) findViewById(R.id.pais));
		passActual = ((EditText) findViewById(R.id.passActual));
		pass1 = ((EditText) findViewById(R.id.pass1));
		pass2 = ((EditText) findViewById(R.id.pass2));
		sexo = ((RadioGroup) findViewById(R.id.sexo));
		paisSpinner = (Spinner) findViewById(R.id.paisSpinner);
		submit = (Button) findViewById(R.id.submit);

		((TextView) findViewById(R.id.correo)).setText(Varios.getMasterUsr(this));

		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened())) {
			((LinearLayout) findViewById(R.id.conenedorContrasena)).setVisibility(LinearLayout.GONE);
		} else {
			((LinearLayout) findViewById(R.id.conenedorContrasena)).setVisibility(LinearLayout.VISIBLE);
		}

		AdView mAdView = new AdView(this, "http://adserver.suika.cl/md.request.php", getString(R.string.ad_perfil));
		((LinearLayout) findViewById(R.id.dummieAlign)).removeAllViews();
		((LinearLayout) findViewById(R.id.dummieAlign)).addView(mAdView);

		setInitialState();
		setListeners();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Varios.clearAllNotifications = false;
		AppEventsLogger.activateApp(this, getString(R.string.facebook_app_id));
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	public void processFinish(Object[] output) {
		preloader.dismiss();
		if (output[0].equals("OK")) {
			saveState();
			Toast.makeText(this, getString(R.string.perfil_okSave), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "" + output[0], Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		menu.add(Menu.NONE, 0, Menu.NONE, "").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setCheckable(false).setEnabled(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setInitialState() {
		nombre.setText(Varios.getMasterName(this));
		apellido.setText(Varios.getMasterApellido(this));
		paisSpinner.setSelection(Varios.getMasterPais(this));
		fecha.setText(Varios.getMasterNac(this));

		if (Varios.getMasterSex(this) == 0) {
			sexo.check(R.id.sexo1);
		} else {
			sexo.check(R.id.sexo2);
		}
	}

	private void setListeners() {
		paisSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {
				pais.setText(parent.getItemAtPosition(pos).toString());
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		fecha.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				newFragment.show(getSupportFragmentManager(), "timePicker");
			}
		});

		pais.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				paisSpinner.performClick();
			}
		});

		submit.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				RadioButton radioButton = (RadioButton) sexo.findViewById(sexo.getCheckedRadioButtonId());

				String n = nombre.getText().toString();
				String a = apellido.getText().toString();
				String f = fecha.getText().toString();
				String p = pais.getText().toString();
				String s = radioButton.getText().toString();

				if (!Validators.validateForm(Perfil.this, n, a, f, p).equals("Ok")) {
					Toast.makeText(Perfil.this, Validators.validateForm(Perfil.this, n, a, f, p), Toast.LENGTH_LONG).show();
					return;
				}

				String pa = passActual.getText().toString();
				String p1 = pass1.getText().toString();
				String p2 = pass2.getText().toString();

				if (!Validators.validatePass(Perfil.this, pa, p1, p2).equals("Ok")) {
					Toast.makeText(Perfil.this, Validators.validatePass(Perfil.this, pa, p1, p2), Toast.LENGTH_LONG).show();
					return;
				}

				preloader.show();
				ConnectionUpdateUser userUp = new ConnectionUpdateUser(Perfil.this, Perfil.this);
				userUp.setUserStuff(n, a, s, f, p, pa, p1);
				userUp.execute();
			}
		});
	}

	private void saveState() {
		RadioButton radioButton = (RadioButton) sexo.findViewById(sexo.getCheckedRadioButtonId());

		Varios.setMasterName(this, nombre.getText().toString());
		Varios.setMasterApellido(this, apellido.getText().toString());
		Varios.setMasterSex(this, sexo.indexOfChild(radioButton));
		Varios.setMasterNac(this, fecha.getText().toString());
		Varios.setMasterPais(this, paisSpinner.getSelectedItemPosition());
	}

	// Class Date Picker //
	public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int year;
			int month;
			int day;

			Calendar c = Calendar.getInstance();
			if (fecha.length() == 0) {
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);
			} else {
				String[] parsedDate = fecha.getText().toString().split("-");
				year = Integer.parseInt(parsedDate[2]);
				month = Integer.parseInt(parsedDate[1]) - 1;
				day = Integer.parseInt(parsedDate[0]);
			}
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
			return dialog;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			Time time = new Time();
			time.set(0, 0, 0, day, month, year);
			fecha.setText(time.format("%d-%m-%Y"));
		}
	}

}