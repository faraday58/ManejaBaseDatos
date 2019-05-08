package com.temas.selectos.manejabasedatos;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.temas.selectos.manejabasedatos.BaseDatos.AuxSQL;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    EditText edtID;
    EditText edtNombre;
    EditText edtTelefono;
    EditText edtCorreo;
    ImageView imgFoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtID= findViewById(R.id.edtID);
        edtNombre= findViewById(R.id.edtNombre);
        edtTelefono= findViewById(R.id.edtTelefono);
        edtCorreo= findViewById(R.id.edtCorreo);
        imgFoto = findViewById(R.id.imgFoto);
    }

    public void onClickAgregar(View v)
    {
        AuxSQL auxSQL = new AuxSQL(this,"DBContactos",null,1);
        SQLiteDatabase db = auxSQL.getWritableDatabase();
        String id= edtID.getText().toString();
        String nombre= edtNombre.getText().toString();
        String telefono= edtTelefono.getText().toString();
        String correo= edtCorreo.getText().toString();
        //Convertir la imagen en un flujo de bytes
        Bitmap bitmFoto=((BitmapDrawable)imgFoto.getDrawable()).getBitmap();
        ByteArrayOutputStream bytesImagen =new  ByteArrayOutputStream(20480);
        bitmFoto.compress(Bitmap.CompressFormat.PNG,0,bytesImagen);
        //El blob que es un arreglo de bytes es lo que se salva en la Base de datos
        byte[] blob = bytesImagen.toByteArray();

        ContentValues registro = new ContentValues();
        registro.put("id",id);
        registro.put("nombre",nombre);
        registro.put("telefono",telefono);
        registro.put("correo",correo);
        registro.put("foto",blob);
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
        Bitmap bitmapFoto=null;
        Cursor renglon = db.rawQuery("SELECT nombre,telefono,correo,foto FROM Contactos WHERE id="+codigo,null);

        if(renglon.moveToFirst())
        {
            edtNombre.setText(renglon.getString(0));
            edtTelefono.setText(renglon.getString(1));
            edtCorreo.setText(renglon.getString(2));
            try
            {
                byte [] blob = renglon.getBlob(3);
                ByteArrayInputStream byteFoto = new ByteArrayInputStream(blob);
                bitmapFoto = BitmapFactory.decodeStream(byteFoto);
                imgFoto.setImageBitmap(bitmapFoto );
            }catch (Exception error)
            {
                Log.d("EbaseFoto","Error " + error.getMessage());
            }


        }else
        {
            Toast.makeText(this, "No existe el contacto con ese código", Toast.LENGTH_LONG).show();
        }

    }

    public void onClickTomaFoto(View v)
    {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        Intent intcamara= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intcamara,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Obteniendo imagen de la cámara
        Bitmap bitmapImagen=(Bitmap)data.getExtras().get("data");
        imgFoto.setImageBitmap(bitmapImagen);
    }
}
