package com.headbangers.epsilon.v3.service.impl;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public abstract class WebService {

    public static String TAG = "WEBSERVICE";

    protected ObjectMapper jsonMapper;

    public WebService() {
        this.jsonMapper = new ObjectMapper();
    }

    protected String callHttp(String url, Map<String, String> postParams) {
        Log.i(TAG, "Appel de l'url " + url);
        HttpURLConnection urlConnection = null;
        try {
            URL zeUrl = new URL(url);
            urlConnection = (HttpURLConnection) zeUrl.openConnection();

            if (postParams != null) {

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setReadTimeout(50000);
                urlConnection.setConnectTimeout(50000);
                urlConnection.setRequestMethod("POST");

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(forgeQuery(postParams));
                writer.flush();
                writer.close();
                os.close();
            }

            return callAndGetResponse(urlConnection);

        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed url " + url);
        } catch (IOException e) {
            Log.e(TAG, "IO on url " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    private String forgeQuery(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private String callAndGetResponse(HttpURLConnection urlConnection) {
        try {
            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            return reader.readLine();

        } catch (IOException e) {
            Log.e(TAG, "IO on reading WS content");
        }

        return null;
    }

    protected <J> J parseJson(String json, TypeReference<J> typeReference) {
        try {

            J result = jsonMapper.readValue(json, typeReference);
            return result;

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
