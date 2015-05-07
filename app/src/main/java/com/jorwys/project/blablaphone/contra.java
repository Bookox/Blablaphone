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
    private Toolbar tbcontra;
    private EditText mCorreo;
    private Button mEnviar;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contra);
        tbcontra=(Toolbar)findViewById(R.id.tbcontra);
        mCorreo=(EditText)findViewById(R.id.mcorreo);
        mEnviar=(Button)findViewById(R.id.menv);
        setSupportActionBar(tbcontra);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final registro registro = new registro();
        mEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCorreo.getText().toString().length()>15 && registro.validarcorreo(mCorreo.getText().toString())){
                new recuperar().execute(mCorreo.getText().toString());

                }
                else
                {
                    Toast.makeText(getBaseContext(),"Verifica que la direccion sea un correo electronico",Toast.LENGTH_SHORT).show();

                }
            }
        });





    }

     private class recuperar extends AsyncTask<String,Void,String> {
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             pDialog = new ProgressDialog(contra.this);
             pDialog.setMessage("Verificando informacion");
             pDialog.setIndeterminate(false);
             pDialog.setCancelable(false);
             pDialog.show();
         }

         @Override
         protected String doInBackground(String... params) {
             Conex conex=new Conex();
             String txt=conex.recuperar(params[0]);
             return txt;

         }

         @Override
         protected void onPostExecute(String s) {
             super.onPostExecute(s);
             mCorreo.setText("");
             pDialog.dismiss();
             if(s.contains("1")){
                 Toast.makeText(contra.this,"Se ha enviado un correo para restablecer tu clave de acceso",Toast.LENGTH_SHORT).show();
             }
             else if (s.contains("0")){
                 Toast.makeText(contra.this,"Ese correo no esta registrado",Toast.LENGTH_SHORT).show();
             }
             else {Toast.makeText(contra.this,"Ha ocurrido un error",Toast.LENGTH_SHORT).show();}

         }
     }

}
