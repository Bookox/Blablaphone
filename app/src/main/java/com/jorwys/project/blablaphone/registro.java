package com.jorwys.project.blablaphone;

import android.app.ProgressDialog;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NavUtils;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class registro extends ActionBarActivity {
    private Toolbar mtb;
    private ProgressDialog pDialog;
    private Button mregistrobutton; // se inician las variables
    private EditText Enombre,Eapellido,Email,Pais,Passw,Enumero;
    private ImageButton eye;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mtb=(Toolbar)findViewById(R.id.tb); // se asigna la vista del toolbar
        setSupportActionBar(mtb); // se asigna el toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // se coloca la navegacion hacia arriba como verdadera
       mregistrobutton=(Button)findViewById(R.id.button3);
        Enombre=(EditText)findViewById(R.id.editTextNombre);
        Eapellido=(EditText)findViewById(R.id.editText3);
        Email=(EditText)findViewById(R.id.editText4); // se asignan las demas vistas a sus variables
        Pais=(EditText)findViewById(R.id.editText5);
        Passw=(EditText)findViewById(R.id.editText6);
        Enumero=(EditText)findViewById(R.id.editText7);
        eye=(ImageButton)findViewById(R.id.imageButton); // imagen pequeña para ver la contraseña
        eye.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { // accion al toque

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN: // si lo toca se vuelve visible la contraseña
                        Passw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        return true;
                    case MotionEvent.ACTION_UP: // si no lo toca la contraseña permanece en ******
                        Passw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        return true;
                    default:
                        return false;
                }

            }
        });

        mregistrobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //click en el boton registrar que primero verifica que todas las condiciones se cumplan incluso el validarcorreo
                if (Enombre.getText().toString().length() > 2 && Eapellido.getText().toString().length() > 2 && Email.getText().toString().length() > 13 && validarcorreo(Email.getText().toString()) && Pais.getText().toString().length() > 4 && Passw.getText().toString().length() > 5 && Enumero.getText().toString().length() > 8) {
                    new sing().execute(Enombre.getText().toString(), Eapellido.getText().toString(), Email.getText().toString(), Pais.getText().toString(), Passw.getText().toString(), Enumero.getText().toString());
                    //se ejecuta el asyntask y se pasan todos los parametros
                    while(Email.getCurrentTextColor()==Color.RED ||Enumero.getCurrentTextColor()==Color.RED){
                        Email.setTextColor(Color.BLACK);
                        Enumero.setTextColor(Color.BLACK);
                         //si los colores del email y el numero estan en la base de datos estos se pondrán rojos
                    }

                }
                else if (Passw.getText().toString().length()<5){
                    Toast.makeText(registro.this, "La contraseña debe poseer almenos 6 caracteres ", Toast.LENGTH_SHORT).show();
                    // luego si la contraseña es menor de 5 caracteres enviara este toast
                }
                else if(!validarcorreo(Email.getText().toString()) || Email.getText().toString().length() < 13){
                    Toast.makeText(registro.this,"El Correo no cumple con las caracteristicas",Toast.LENGTH_SHORT).show();
                       // si no se valida el correo o es muy corto lanza este toast
                }

                else {
                    Toast.makeText(registro.this, "Alguno de los campos esta vacio o incompleto", Toast.LENGTH_SHORT).show();
                    //uno de los campos esta vacio
                }
            }
        });
    }


    class sing extends AsyncTask<String,String,String>{
// tarea asynctask
        protected void onPreExecute() {
            // show the progress bar
            pDialog = new ProgressDialog(registro.this);
            pDialog.setMessage("Verificando información");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();// muestra el dialogo de progreso
        }
        @Override
        protected String doInBackground(String... params) {
            Conex conex= new Conex(); // crea la instancia de conex para llamar el metodo registro que tiene como parametro namevaluepair
            List<NameValuePair> datos = new ArrayList<NameValuePair>();
            datos.add(new BasicNameValuePair("nombre",params[0]));
            datos.add(new BasicNameValuePair("apellido",params[1]));
            datos.add(new BasicNameValuePair("email",params[2]));
            datos.add(new BasicNameValuePair("pais",params[3])); // c
            datos.add(new BasicNameValuePair("contra",params[4]));
            datos.add(new BasicNameValuePair("tel",params[5]));
            String txt=conex.registro(datos); // se llama el metodo con el name value pair como parametro
            return txt; // se envia txt a oppostexecute
        }


        protected void onPostExecute(String s) {
            Handler handler = new Handler(); // se crea un handler que haga un delay de 1 segundo para registrar al usuario
            handler.postDelayed(new Runnable() {
                public void run() {
                    pDialog.dismiss();
                }}, 1000); // se inicia el handler

            if (s.contains("1")){ //si envia como respuesta el 1 significa que fue exitoso el registro y que se envio el correo para activarla
                Toast.makeText(getBaseContext(),"Se ha enviado un correo para la activacion de tu cuenta",Toast.LENGTH_LONG).show();
                Enombre.setText("");
                Eapellido.setText("");
                Email.setText("");
                Enumero.setText("");
                Passw.setText("");
                Pais.setText("");

            }
            else if(s.contains("0")){//si el servidor envia como respuesta el numero 0 este correo o numero se encuentra registrado.
             Toast.makeText(getBaseContext(),"Ese correo o numero telefonico ya esta registrado",Toast.LENGTH_SHORT).show();
                Email.setTextColor(Color.RED);
                Enumero.setTextColor(Color.RED); // convierte los edittext respectivos a color rojo para notificar el error

            }
            else {Toast.makeText(getBaseContext(),"No se logro la conexión intente de nuevo mas tarde",Toast.LENGTH_SHORT).show();
                } // si no obtiene nada del servidor la conexion o el servidor esta caido

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro, menu); // crea el menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId(); // obtiene el id del item que le fue dado click en el menu

        //noinspection SimplifiableIfStatement
        if (id == R.id.login) {
            NavUtils.navigateUpFromSameTask(this); // navega hacia el parent activity que es login para una correcta navegacion en la aplicacion
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static boolean validarcorreo(String email) { // metodo para verificar si el correo esta correcto
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
