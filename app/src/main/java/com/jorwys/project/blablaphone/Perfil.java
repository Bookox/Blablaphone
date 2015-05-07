package com.jorwys.project.blablaphone;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RecoverySystem;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class Perfil extends ActionBarActivity {
    JSONArray ja;
    private TextView nombretb,saldo,numerotb;
    private Toolbar tbperfil;
    private CircleImageView perfil;
    private String email; // se inician las variables
    private LinearLayout otrotb;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setContentView(R.layout.activity_perfil);
        nombretb=(TextView)findViewById(R.id.nombretb);
        numerotb=(TextView)findViewById(R.id.numerotb);
        tbperfil=(Toolbar)findViewById(R.id.mtb); // se asigna el toolbar transparente
        saldo=(TextView)findViewById(R.id.textView6);
        perfil=(CircleImageView)findViewById(R.id.perfil);
        otrotb=(LinearLayout)findViewById(R.id.otrotb);
        img=(ImageView)findViewById(R.id.imageView3);
        Bundle i=getIntent().getExtras(); // se obtienen los extras
        String data=i.getString("data"); // se obtienen los datos que envio
        setSupportActionBar(tbperfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);// no se puede navegar atras
        otrotb.bringToFront(); // se muestra el relative layout encima
        tbperfil.bringToFront(); // se muestra el toolbar encima del relative layout


        try {// se intenta convertir el string en json array para usarlo y obtener los datos de manera individual
            ja= new JSONArray(data);
            nombretb.setText(ja.getString(0)+" "+ja.getString(1)); // se asigna nombre y apellido
            numerotb.setText(ja.getString(4)); // numero
            saldo.setText(ja.getString(5)+" $"); // saldo
            email=ja.getString(2); // el correo para obtener la imagen del servidor
            new DownloadImageTask((ImageView) findViewById(R.id.perfil))
                    .execute("https://blablaphone-myappsjordys.rhcloud.com/images/"+email+".jpg"); // se busca la imagen en el servidor mediante asynctask

        } catch (JSONException e) {
            e.printStackTrace();
        }


perfil.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,1);
 // circle imageview para obtener la imagen y como request tiene el valor 1

    }
});

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // metodo para obtener resultado y numero de request
 //si el request es 1 es la imagen y si resulto ok entra en el codigo
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA }; // obtiene la direccion de la imagen

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null); // crea un cursor para obtenerla
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]); //
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bm = BitmapFactory.decodeFile(picturePath);
                perfil.setImageBitmap(bm); // asigna el bitmap a la imagen
                img.setImageDrawable(perfil.getDrawable()); // y el imageview detras de otrotb que es el linear layout se coloca la imagen como drawable pero otrotb tiene transparencia
                new imagensubir().execute(picturePath,email); // se llama al asyntask para subir la imagen


            }
            if (resultCode == RESULT_CANCELED) {// si se cancela la busca de imagenes no pasa nada

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu); // se infla el menu
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cerrar) {
            new AlertDialog.Builder(this) // con el dialog builder se crea un dialogo en el metodo para cerrar sesion del menu
                    .setTitle("Cerrar Sesion")
                    .setMessage("Desea cerrar sesion?")
                    .setIcon(R.drawable.ic_exit_to_app_black_48dp)
                    .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish(); // se cierra el activity al cerrar sesion
                        }
                    }).setNegativeButton("Me quedare", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            }).show(); //
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   private class imagensubir extends AsyncTask<String,String,String>{

       @Override
       protected String doInBackground(String... params) {
           Conex c=new Conex(); // se llama la instancia conex para usar el metodo imagen que usa dos parametros la direccion y el nombre de la imagen
           String txt=c.imagen(params[0],params[1]);
           return txt;
       }

       @Override
       protected void onPostExecute(String s) {
           super.onPostExecute(s);
           Toast.makeText(getBaseContext(),"La foto a sido subida exitosamente",Toast.LENGTH_SHORT).show();
           //al terminar de enviar la direccion y el nombre de la imagen lanza el toast y trata de actualizar a la nueva imagen desde el server con
           //el metodo downloadimagetask
           new DownloadImageTask((ImageView) findViewById(R.id.perfil))
                   .execute("https://blablaphone-myappsjordys.rhcloud.com/images/" + email + ".jpg");
       }
   }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0]; // desde la url trata de hacer un bitmap con el inputstream
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                 e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result); // asigna ambas vistas el bitmap obtenido desde la web en el servidor
            img.setImageDrawable(bmImage.getDrawable());

        }
    }

}
