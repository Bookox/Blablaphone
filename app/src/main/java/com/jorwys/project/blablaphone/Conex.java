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
 * Created by Jorwys on 01-04-2015.
 */
public class Conex {

    public String login(String user, String pw) {
        String url = "http://blablaphone-myappsjordys.rhcloud.com/static/login.php";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", user));
            params.add(new BasicNameValuePair("clave", pw));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse resp = httpClient.execute(httpPost);
            HttpEntity ent = resp.getEntity();
            String txt = EntityUtils.toString(ent);
            return txt;

        } catch (Exception e) {
            return "";
        }


    }

    public String registro(List<NameValuePair> datos) {
        String url = "http://blablaphone-myappsjordys.rhcloud.com/static/registro.php";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(datos));
            HttpResponse resp = httpClient.execute(httpPost);
            HttpEntity ent = resp.getEntity();
            String txt = EntityUtils.toString(ent);
            return txt;

        } catch (Exception e) {
            return "";
        }

    }


    public String recuperar(String correo) {
        String url = "http://blablaphone-myappsjordys.rhcloud.com/static/recuperar.php";
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", correo));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse resp = httpClient.execute(httpPost);
            HttpEntity ent = resp.getEntity();
            String txt = EntityUtils.toString(ent);
            return txt;
        } catch (Exception e) {
            return "";


        }


    }

    public String imagen(String imagen,String nombre) {
        File sourceFile = new File(imagen);
        String url = "http://blablaphone-myappsjordys.rhcloud.com//fileUpload.php";

        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("name", new StringBody(nombre));
            entity.addPart("image", new FileBody(sourceFile));
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity ent = response.getEntity();
            String txt = EntityUtils.toString(ent);
            return txt;

        } catch (Exception e) {
            return "Error ocurrido";


        }


    }
}