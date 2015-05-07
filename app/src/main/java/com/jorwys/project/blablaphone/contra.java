package com.jorwys.project.blablaphone;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class contra extends ActionBarActivity {
    private Toolbar tbcontra; // se asignan nombres a las vistas
    private EditText mCorreo;
    private Button mEnviar;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contra);
        tbcontra=(Toolbar)findViewById(R.id.tbcontra); // se asigna cada vista coreespondiente a su variable
        mCorreo=(EditText)findViewById(R.id.mcorreo);
        mEnviar=(Button)findViewById(R.id.menv);
        setSupportActionBar(tbcontra); // se coloca el toolbar como set supportactionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // se coloca navegacion hacia arriba
        final registro registro = new registro(); // se hace una llama a la clase registro para obtener el metodo de validar correo


        mEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// click listener del boton enviar
                if(mCorreo.getText().toString().length()>15 && registro.validarcorreo(mCorreo.getText().toString())){
                new recuperar().execute(mCorreo.getText().toString());
 // se crean condiciones para verificar que sea un correo y que no este vacio  y luego se ejecuta el asyntask para enviar los datos al servidor con php
                }
                else // si no cumple los datos solo muestra un toast que dice esto
                {
                    Toast.makeText(getBaseContext(),"Verifica que la direccion sea un correo electronico",Toast.LENGTH_SHORT).show();

                }
            }
        });





    }

     private class recuperar extends AsyncTask<String,Void,String> {
         @Override
         protected void onPreExecute() {
             super.onPreExecute(); // se crea un dialog de progreso para hacer que el usuario espere respuesta del server
             pDialog = new ProgressDialog(contra.this);
             pDialog.setMessage("Verificando informacion");
             pDialog.setIndeterminate(false);
             pDialog.setCancelable(false);
             pDialog.show();
         }

         @Override
         protected String doInBackground(String... params) {
             Conex conex=new Conex(); // se crea una instancia de la clase conex
             String txt=conex.recuperar(params[0]); // se envia el unico parametro que es el correo
             return txt;  // esta variable es la respuesta del servidor

         }

         @Override
         protected void onPostExecute(String s) {
             super.onPostExecute(s);
             mCorreo.setText(""); // se coloca el correo vacio aqui por que es la unica instancia de asynk que puede interactuar con la vista
             pDialog.dismiss();// se elimina el dialog progres creado en el pre execute
             if(s.contains("1")){ // el servidor puede enviar dos respuestas 1 ó 0 si envia 1 envia un correo al usuario para recuperar la contra
                 Toast.makeText(contra.this,"Se ha enviado un correo para restablecer tu clave de acceso",Toast.LENGTH_SHORT).show();
                 // se crea este toast para decirle al usuario que correctamente se envio el correo para recuperacion
             }
             else if (s.contains("0")){ // si obtiene un 0 del servidor el usuario obviamente no esta registrado
                 Toast.makeText(contra.this,"Ese correo no esta registrado",Toast.LENGTH_SHORT).show();
                 //se muestra este toast diciendole lo anterior
             }
             // si no obtiene ninguna de esas dos respuestas ocurre un problema de conexion a internet o del servidor
             else {Toast.makeText(contra.this,"Ha ocurrido un error de conexion",Toast.LENGTH_SHORT).show();}

         }
     }

}
