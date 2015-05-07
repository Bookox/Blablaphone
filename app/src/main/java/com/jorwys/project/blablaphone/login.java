package com.jorwys.project.blablaphone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class login extends ActionBarActivity {
   private Button b2,btnlogin;
    private EditText ed1,ed2; // declaracion de variables
    private ProgressDialog pDialog;
    private TextView recuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       b2=(Button)findViewById(R.id.button2);
        btnlogin=(Button)findViewById(R.id.button);
        ed1=(EditText)findViewById(R.id.editText);
        ed2=(EditText)findViewById(R.id.editText2); // asignacion de variables con sus respectivas vistas
        recuperar=(TextView)findViewById(R.id.textView4);
        final registro r=new registro(); // se crea una instancia de la clase registro para verificar los correos y reutilizar codigo

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(login.this,registro.class);
                startActivity(i); // el boton registrarse para ir al activity registro
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { /* el boton login tiene unas condiciones que verificarse para poder
            intentar hacer el login con el servidor las dos primeras es que no esten vacios y la otra es la instancia
            de la clase registro para validar correos*/
                if (ed1.getText().toString().length() > 0 && ed2.getText().toString().length() > 0 && r.validarcorreo(ed1.getText().toString())) {

               new carga().execute(ed1.getText().toString(), ed2.getText().toString()); // si se cumplen las condiciones executar el asyntask con los parametros ingresados
                    ed1.setTextColor(Color.BLACK); // se coloca el texto de color negro
                    ed2.setTextColor(Color.BLACK);
                } else {
                    Toast.makeText(login.this, "Llena ambos campos correctamente", Toast.LENGTH_SHORT).show();
                    ed1.setTextColor(Color.RED); // se coloca el texto de color rojo si no cumple con las condiciones
                    ed2.setTextColor(Color.RED);

                }

            }});
        recuperar.setOnClickListener(new View.OnClickListener() { // textview olvido su contraseña con un clicklistener
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this,contra.class);
                startActivity(i); // intent que inicia el activity para recuperar contraseña
            }
        });
    }

class carga extends AsyncTask<String,String,String>{ // carga de la funcion asyntask que envia 3 strings
    protected void onPreExecute() { // crea un progress dialog que sera mostrado mientras dure el hilo
        // show the progress bar
        pDialog = new ProgressDialog(login.this);
        pDialog.setMessage("Iniciando Sesión");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

      @Override
    protected String doInBackground(String... params) {
          Conex conex= new Conex(); // se crea una instancia de la variable conex para utilizar su metodo login que es una string
          String txt=conex.login(params[0],params[1]); // se envian ambos parametros y se espera respuesta del servidor
          return txt; // la respuesta es retornada en esta string

    }
    protected void onPostExecute(final String result) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }}, 1000); // aqui se crea un retardo de 1 segundo para crear el efecto que el servidor da acceso

        if (result.length()>1){ // si el resultado del servidor es mas largo que 1 entonces obtuvo una respuesta que son los datos
            Intent i=new Intent(getBaseContext(),Perfil.class); //en json que obtiene del server
            i.putExtra("data",result); // envia estos datos con el nombre 'data' en el intent
            ed2.setText(""); // se vacia el edittext 2 que es el de la contrasñea
            startActivity(i); // se inicia el activity perfil y se envian los datos como intent


        }
        else {Toast.makeText(getBaseContext(),"Usuario o contraseña incorrecto",Toast.LENGTH_SHORT).show();
        ed1.setTextColor(Color.RED);
            ed2.setTextColor(Color.RED); // se colocan ambos edittext en rojo por que algo no esta bien escrito
        ed2.setText(""); // y se vacia el edittext de la contraseña

        }

    }
}

}
