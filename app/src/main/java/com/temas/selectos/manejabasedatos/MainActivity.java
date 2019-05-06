package com.temas.selectos.manejabasedatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.temas.selectos.manejabasedatos.BaseDatos.AuxSQL;

public class MainActivity extends AppCompatActivity {

    EditText edtID;
    EditText edtNombre;
    EditText edtTelefono;
    EditText edtCorreo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtID= findViewById(R.id.edtID);
        edtNombre= findViewById(R.id.edtNombre);
        edtTelefono= findViewById(R.id.edtTelefono);
        edtCorreo= findViewById(R.id.edtCorreo);
    }

    public void onClickAgregar(View v)
    {
        AuxSQL auxSQL = new AuxSQL(this,"DBContactos",null,1);
        SQLiteDatabase db = auxSQL.getWritableDatabase();
        String id= edtID.getText().toString();
        String nombre= edtNombre.getText().toString();
        String telefono= edtTelefono.getText().toString();
        String correo= edtCorreo.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("id",id);
        registro.put("nombre",nombre);
        registro.put("telefono",telefono);
        registro.put("correo",correo);
        db.insert("Contactos",null,registro);
        db.close();
        edtID.setText("");
        edtCorreo.setText("");
        edtTelefono.setText("");
        edtNombre.setText("");
        Toast.makeText(this,"Se han ingresado",Toast.LENGTH_LONG).show();


    }

    public void onClickBuscar(View v){
        AuxSQL auxSQL = new AuxSQL(this,"DBContactos",null,1);
        SQLiteDatabase db = auxSQL.getReadableDatabase();
        String codigo= edtID.getText().toString();
        Cursor renglon = db.rawQuery("SELECT nombre,telefono,correo FROM Contactos WHERE id="+codigo,null);
        if(renglon.moveToFirst())
        {
            edtNombre.setText(renglon.getString(0));
            edtTelefono.setText(renglon.getString(1));
            edtCorreo.setText(renglon.getString(2));
        }else
        {
            Toast.makeText(this, "No existe el contacto con ese código", Toast.LENGTH_LONG).show();
        }

    }

}
