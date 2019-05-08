package com.temas.selectos.manejabasedatos.BaseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AuxSQL extends SQLiteOpenHelper {

    String sqlTabla="CREATE TABLE Contactos(id int primary key,nombre text,telefono text,correo text,foto blob) ";

    public AuxSQL(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlTabla);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Contactos");
        db.execSQL(sqlTabla);

    }
}
