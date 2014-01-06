package cl.suika.cineschile;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cl.suika.cineschile.R;
import cl.suika.cineschile.connection.AsyncResponse;
import cl.suika.cineschile.connection.ConnectionFacebookLogin;
import cl.suika.cineschile.connection.ConnectionLogIn;
import cl.suika.cineschile.database.DbHelper;
import cl.suika.cineschile.utils.HeightResizeAnimation;
import cl.suika.cineschile.utils.LoadingDialog;
import cl.suika.cineschile.utils.Varios;

import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.analytics.tracking.android.EasyTracker;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class LogIn extends Activity implements AsyncResponse {

	private UiLifecycleHelper uiHelper;
	private LoadingDialog preloader;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
				
		Parse.initialize(this, getString(R.string.parse_appId), getString(R.string.parse_clientKey));
		PushService.subscribe(this, getString(R.string.parse_channel), Notificaciones.class);
		PushService.setDefaultPushCallback(this, Notificaciones.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();

		if (!Varios.getMasterTokn(this).equals("null")) {
			startActivity(new Intent(this, Main.class));
			finish();
			return;
		}

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setReadPermissions(Arrays.asList("email", "user_birthday"));
		preloader = new LoadingDialog(this);

		if (Varios.database == null || !Varios.database.isOpen()) {
			DbHelper dbHelper = new DbHelper(this);
			Varios.database = dbHelper.getWritableDatabase();
		}
		setListeners();

		Button suikaLoginButton = (Button) findViewById(R.id.suikaLoginButton);
		suikaLoginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				LinearLayout suikaLogCont = (LinearLayout) findViewById(R.id.suikaLoginContenedor);
				int toHeight = 0;
				if (suikaLogCont.getLayoutParams().height == 0) {
					toHeight = 200;
				}
				HeightResizeAnimation anim = new HeightResizeAnimation(suikaLogCont, Varios.cDpToPx(toHeight, LogIn.this), false);
				anim.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationEnd(Animation arg0) {
						((ScrollView) findViewById(R.id.masterScroll)).fullScroll(View.FOCUS_DOWN);
						if(((EditText) findViewById(R.id.User)).requestFocus()) {
						    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
						}
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
					}

					@Override
					public void onAnimationStart(Animation arg0) {
					}
				});
				anim.setFillAfter(true);
				anim.setDuration(500);
				suikaLogCont.startAnimation(anim);

				LoginButton login_button = (LoginButton) findViewById(R.id.login_button);
				int toHeightFb = 0;
				if (login_button.getLayoutParams().height == 0) {
					toHeightFb = 55;
				}
				HeightResizeAnimation anim2 = new HeightResizeAnimation(login_button, Varios.cDpToPx(toHeightFb, LogIn.this), false);
				anim2.setFillAfter(true);
				anim2.setDuration(500);
				login_button.startAnimation(anim2);

				View separatior = (View) findViewById(R.id.separatior);
				int separatiorH = 0;
				if (separatior.getLayoutParams().height == 0) {
					separatiorH = 20;
				}
				HeightResizeAnimation anim3 = new HeightResizeAnimation(separatior, Varios.cDpToPx(separatiorH, LogIn.this), false);
				anim3.setFillAfter(true);
				anim3.setDuration(500);
				separatior.startAnimation(anim3);

			}
		});
	}

	@Override
	public void processFinish(Object[] output) {
		preloader.dismiss();
		if (output[0].equals("OK")) {
			startActivity(new Intent(LogIn.this, Main.class));
			finish();
		} else {
			Toast.makeText(this, "" + output[0], Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
		Varios.clearAllNotifications = false;
		AppEventsLogger.activateApp(this, getString(R.string.facebook_app_id));
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
		EasyTracker.getInstance(this).activityStop(this);
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, SessionState state, Exception exception) {
			if (state.toString().equals("OPENED")) {
				preloader.show();

				Request.newMeRequest(session, new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null && user.asMap().get("email") != null) {
							ConnectionFacebookLogin fblog = new ConnectionFacebookLogin(LogIn.this, LogIn.this, user);
							fblog.execute();
						} else {
							Session session = Session.getActiveSession();
							if (session != null && (session.isOpened())) {
								session.close();
							}
							preloader.dismiss();
							Toast.makeText(getApplicationContext(), "No se ha logrado iniciar sesi�n con Facebook", Toast.LENGTH_SHORT).show();
						}
					}
				}).executeAsync();
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (Varios.database != null && Varios.database.isOpen()) {
			Varios.database.close();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (((LinearLayout) findViewById(R.id.suikaLoginContenedor)).getLayoutParams().height != 0) {
				((Button) findViewById(R.id.suikaLoginButton)).performClick();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setListeners() {
		((Button) findViewById(R.id.inicioSesion)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preloader.show();
				String user = ((EditText) findViewById(R.id.User)).getText().toString();
				String pass = ((EditText) findViewById(R.id.Pass)).getText().toString();

				ConnectionLogIn log = new ConnectionLogIn(LogIn.this, LogIn.this, user, pass);
				log.execute();
			}
		});

		((TextView) findViewById(R.id.registro)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(LogIn.this, Registro.class));
			}
		});

		((TextView) findViewById(R.id.forgotPass)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/html");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "soporte@chilediarios.cl" });
				intent.putExtra(Intent.EXTRA_SUBJECT, "Recuperar Contrase�a");
				intent.putExtra(Intent.EXTRA_TEXT, "");
				startActivity(Intent.createChooser(intent, "Recuperar Contrase�a"));
			}
		});

	}

}