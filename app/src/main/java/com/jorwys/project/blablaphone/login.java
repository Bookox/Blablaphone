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
    private EditText ed1,ed2;
    private ProgressDialog pDialog;
    private TextView recuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       b2=(Button)findViewById(R.id.button2);
        btnlogin=(Button)findViewById(R.id.button);
        ed1=(EditText)findViewById(R.id.editText);
        ed2=(EditText)findViewById(R.id.editText2);
        recuperar=(TextView)findViewById(R.id.textView4);
        final registro r=new registro();

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(login.this,registro.class);
                startActivity(i);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed1.getText().toString().length() > 0 && ed2.getText().toString().length() > 0 && r.validarcorreo(ed1.getText().toString())) {

               new carga().execute(ed1.getText().toString(), ed2.getText().toString());
                    ed1.setTextColor(Color.BLACK);
                    ed2.setTextColor(Color.BLACK);
                } else {
                    Toast.makeText(login.this, "Llena ambos campos correctamente", Toast.LENGTH_SHORT).show();
                    ed1.setTextColor(Color.RED);
                    ed2.setTextColor(Color.RED);

                }

            }});
        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this,contra.class);
                startActivity(i);
            }
        });
    }

class carga extends AsyncTask<String,String,String>{
    protected void onPreExecute() {
        // show the progress bar
        pDialog = new ProgressDialog(login.this);
        pDialog.setMessage("Iniciando Sesión");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

      @Override
    protected String doInBackground(String... params) {
          Conex conex= new Conex();
          String txt=conex.login(params[0],params[1]);
          return txt;

    }
    protected void onPostExecute(final String result) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }}, 1000);

        if (result.length()>1){
            Intent i=new Intent(getBaseContext(),Perfil.class);
            i.putExtra("data",result);
            ed2.setText("");
            startActivity(i);


        }
        else {Toast.makeText(getBaseContext(),"Usuario o contraseña incorrecto",Toast.LENGTH_SHORT).show();
        ed1.setTextColor(Color.RED);
            ed2.setTextColor(Color.RED);
        ed2.setText("");

        }

    }
}

}
