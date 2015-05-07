package com.jorwys.project.blablaphone;


import android.content.Entity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 metodo para la conexiones con openshift ,todos son strings que devuelven un response de la pagina
 envian los parametros a enviar con namevalue pair , se crea un metodo httpclient una peticon httpost el namevalue pair
 y luego los parametros son agregados con clave y valor, se hace la peticion post y se agrega la entidad con los parametros.
 luego se verifica la repsuesta de la peticion post con httresponse y luego se crea una entidad que sera la entidad que envia la respuesta
 y se devuelve el string respuesta a cada una de las peticiones. a menos que openshift este caido el programa no fallará
 */
public class Conex {

    public String login(String user, String pw) { // string para el login de un usuario
        String url = "http://blablaphone-myappsjordys.rhcloud.com/static/login.php"; // direccion del script de php
        try { // un try para probar la conexion
            HttpClient httpClient = new DefaultHttpClient(); // se crea el httpclient
            HttpPost httpPost = new HttpPost(url); // peticion post con la url
            List<NameValuePair> params = new ArrayList<NameValuePair>();// el name value pair
            params.add(new BasicNameValuePair("email", user)); //se agregan ambos parametros al namevaluepair
            params.add(new BasicNameValuePair("clave", pw));
            httpPost.setEntity(new UrlEncodedFormEntity(params)); // se envian como una entidad
            HttpResponse resp = httpClient.execute(httpPost); //se ejecuta el httppost y se obtiene respuesta
            HttpEntity ent = resp.getEntity(); // se crea una entidad que sera la entidad que obtiene el httpresponse
            String txt = EntityUtils.toString(ent); // se convierte en string para retornar la respuesta
            return txt; // se obtiene la string con todos los datos del user

        } catch (Exception e) {
            return ""; // si ocurre un error se envia vacio.
        }


    }

    public String registro(List<NameValuePair> datos) { //se obtiene como parametro un list namevalue par llamado datos
        String url = "http://blablaphone-myappsjordys.rhcloud.com/static/registro.php"; // url para el registro script php
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(datos));
            HttpResponse resp = httpClient.execute(httpPost); // se envian los datos y se obtiene un response al registrarse se le envia un correo al user
            HttpEntity ent = resp.getEntity(); //se obtiene una variable del php que sera evaluada para ver la respuesta del servidor
            String txt = EntityUtils.toString(ent); // se combierte en string para verificar la respuesta
            return txt; // se retorna una variable para verificar el registro

        } catch (Exception e) {
            return "";
        }

    }


    public String recuperar(String correo) {//
        String url = "http://blablaphone-myappsjordys.rhcloud.com/static/recuperar.php";
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", correo)); // este escript primero verifica si el correo esta registrado
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse resp = httpClient.execute(httpPost); // convierte la respuesta en string para ver si esta registrado o se logro la recuperacion de contraseña a travez de un correo.
            HttpEntity ent = resp.getEntity();
            String txt = EntityUtils.toString(ent);
            return txt;
        } catch (Exception e) {
            return "";


        }


    }

    public String imagen(String imagen,String nombre) { // para enviar imagenes al servidor el string imagen es la direccion de la imagen en el telefono
        File sourceFile = new File(imagen); // se crea un file que sera la direccion de la imagen
        String url = "http://blablaphone-myappsjordys.rhcloud.com//fileUpload.php"; // direccion del script

        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("name", new StringBody(nombre)); // se crea un script que pueda aceptar envio de imagenes multipart entity
            entity.addPart("image", new FileBody(sourceFile));// tambien se envia el nombre que se le pondra a la imagen
            httpPost.setEntity(entity); //se envia la entidad
            HttpResponse response = httpClient.execute(httpPost); // se obtiene una respuesta
            HttpEntity ent = response.getEntity(); // el archivo no debe sobrepasar los 10 mb o dará error
            String txt = EntityUtils.toString(ent); // se obtiene la respuesta del servidor
            return txt; // se retorna la respuesta

        } catch (Exception e) {
            return "Error ocurrido";


        }


    }
}