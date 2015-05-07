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
    private Button mregistrobutton;
    private EditText Enombre,Eapellido,Email,Pais,Passw,Enumero;
    private ImageButton eye;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mtb=(Toolbar)findViewById(R.id.tb);
        setSupportActionBar(mtb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       mregistrobutton=(Button)findViewById(R.id.button3);
        Enombre=(EditText)findViewById(R.id.editTextNombre);
        Eapellido=(EditText)findViewById(R.id.editText3);
        Email=(EditText)findViewById(R.id.editText4);
        Pais=(EditText)findViewById(R.id.editText5);
        Passw=(EditText)findViewById(R.id.editText6);
        Enumero=(EditText)findViewById(R.id.editText7);
        eye=(ImageButton)findViewById(R.id.imageButton);
        eye.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Passw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        return true;
                    case MotionEvent.ACTION_UP:
                        Passw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        return true;
                    default:
                        return false;
                }

            }
        });

        mregistrobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Enombre.getText().toString().length() > 2 && Eapellido.getText().toString().length() > 2 && Email.getText().toString().length() > 13 && validarcorreo(Email.getText().toString()) && Pais.getText().toString().length() > 4 && Passw.getText().toString().length() > 5 && Enumero.getText().toString().length() > 8) {
                    new sing().execute(Enombre.getText().toString(), Eapellido.getText().toString(), Email.getText().toString(), Pais.getText().toString(), Passw.getText().toString(), Enumero.getText().toString());
                    while(Email.getCurrentTextColor()==Color.RED ||Enumero.getCurrentTextColor()==Color.RED){
                        Email.setTextColor(Color.BLACK);
                        Enumero.setTextColor(Color.BLACK);

                    }

                }
                else if (Passw.getText().toString().length()<5){
                    Toast.makeText(registro.this, "La contraseña debe poseer almenos 6 caracteres ", Toast.LENGTH_SHORT).show();
                }
                else if(!validarcorreo(Email.getText().toString()) || Email.getText().toString().length() < 13){
                    Toast.makeText(registro.this,"El Correo no cumple con las caracteristicas",Toast.LENGTH_SHORT).show();

                }

                else {
                    Toast.makeText(registro.this, "Alguno de los campos esta vacio o incompleto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    class sing extends AsyncTask<String,String,String>{

        protected void onPreExecute() {
            // show the progress bar
            pDialog = new ProgressDialog(registro.this);
            pDialog.setMessage("Verificando información");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            Conex conex= new Conex();
            List<NameValuePair> datos = new ArrayList<NameValuePair>();
            datos.add(new BasicNameValuePair("nombre",params[0]));
            datos.add(new BasicNameValuePair("apellido",params[1]));
            datos.add(new BasicNameValuePair("email",params[2]));
            datos.add(new BasicNameValuePair("pais",params[3]));
            datos.add(new BasicNameValuePair("contra",params[4]));
            datos.add(new BasicNameValuePair("tel",params[5]));
            String txt=conex.registro(datos);
            return txt;
        }


        protected void onPostExecute(String s) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    pDialog.dismiss();
                }}, 1000);

            if (s.contains("1")){
                Toast.makeText(getBaseContext(),"Se ha enviado un correo para la activacion de tu cuenta",Toast.LENGTH_LONG).show();
                Enombre.setText("");
                Eapellido.setText("");
                Email.setText("");
                Enumero.setText("");
                Passw.setText("");
                Pais.setText("");

            }
            else if(s.contains("0")){
             Toast.makeText(getBaseContext(),"Ese correo o numero telefonico ya esta registrado",Toast.LENGTH_SHORT).show();
                Email.setTextColor(Color.RED);
                Enumero.setTextColor(Color.RED);

            }
            else {Toast.makeText(getBaseContext(),"No se logro la conexión intente de nuevo mas tarde",Toast.LENGTH_SHORT).show();
                }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.login) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static boolean validarcorreo(String email) {
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
