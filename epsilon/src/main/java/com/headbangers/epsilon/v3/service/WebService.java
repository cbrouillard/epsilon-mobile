package com.headbangers.epsilon.v3.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public abstract class WebService {

    public static String TAG = "WEBSERVICE";

    protected ObjectMapper jsonMapper;
    
    public WebService() {
        this.jsonMapper = new ObjectMapper();
    }

    protected String callHttp(String url) {
        HttpGet getMethod = new HttpGet(url);
        Log.i(TAG, "Appel de l'url " + url);
        return callAndGetResponseAsString(getMethod);
    }

    private String callAndGetResponseAsString(HttpRequestBase request) {
        HttpClient httpclient = new DefaultHttpClient();
        request.getParams().setParameter("http.socket.timeout",
                new Integer(50000));

        try {
            HttpResponse response = httpclient.execute(request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            return reader.readLine();

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
            Log.d(TAG, "Erreur : " + e);
        }

        return null;
    }

    protected String callHttp(String url, Map<String, String> postParams) {
        HttpPost postMethod = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Entry<String, String> entry : postParams.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            postMethod.setEntity(new UrlEncodedFormEntity(params));
            return callAndGetResponseAsString(postMethod);
        } catch (UnsupportedEncodingException e) {
        }

        return null;
    }
    
    protected <J> J parseJson(String json, TypeReference<J> typeReference) {
        try {

            J result = jsonMapper.readValue(json, typeReference);
            return (J) result;

        } catch (JsonParseException e) {
            Log.d(TAG, "Erreur de parsing JSON", e);
            Log.d(TAG, "JSON = " + json);
        } catch (JsonMappingException e) {
            Log.d(TAG, "Erreur de mapping JSON", e);
        } catch (IOException e) {
            Log.d(TAG, "Erreur de lecture JSON", e);
        }
        return null;
    }
}
