package cl.suika.cineschile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	
	private static  String DATABASE_NAME = "chileDiariosDataBase.db";
	private static int DATABASE_VERSION = 1;
	//private Context context;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//this.context = context; 
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		//database.execSQL("CREATE TABLE IF NOT EXISTS Paises (_id INTEGER PRIMARY KEY, nombre TEXT, imagen TEXT, ad_server TEXT, status INT) ");
		//database.execSQL("CREATE TABLE IF NOT EXISTS Aplicaciones (_id INTEGER PRIMARY KEY, id_pais INT, nombre TEXT, imagen TEXT, pin TEXT, pinGroup TEXT, color TEXT, status INT, orden INT, ad_map TEXT, ad_detalle TEXT) ");
		//database.execSQL("CREATE TABLE IF NOT EXISTS Puntos (_id INTEGER PRIMARY KEY, id_app INT,nombre TEXT, direccion TEXT, imagen TEXT, lat TEXT, lon TEXT, campos TEXT, status INT)");
		//database.execSQL("CREATE TABLE IF NOT EXISTS Ranks (id_point INTEGER PRIMARY KEY, value INT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("DROP TABLE IF EXISTS Paises");
		//db.execSQL("DROP TABLE IF EXISTS Aplicaciones");
		//db.execSQL("DROP TABLE IF EXISTS Puntos");
		//db.execSQL("DROP TABLE IF EXISTS Ranks");
		
		onCreate(db);
	}

}