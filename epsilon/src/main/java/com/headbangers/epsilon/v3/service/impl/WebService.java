package com.headbangers.epsilon.v3.service.impl;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.headbangers.epsilon.v3.preferences.EpsilonPrefs_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@EBean
public abstract class WebService {

    public static String TAG = "WEBSERVICE";

    protected ObjectMapper jsonMapper;

    @Pref
    EpsilonPrefs_ epsilonPrefs;

    public WebService() {
        this.jsonMapper = new ObjectMapper();
    }

    protected String post(String url, Map<String, String> postParams) {
        return callHttp(url, "POST", postParams);
    }

    protected String post(String url, Map<String, String> params, String fileFieldName, File file) {
        try {
            MultipartUtility multipart = new MultipartUtility(url, "UTF-8",  epsilonPrefs.token().get());

            for (Map.Entry<String, String> entry : params.entrySet()) {
                multipart.addFormField(entry.getKey(), entry.getValue());
            }

            multipart.addFilePart(fileFieldName, file);

            List<String> response = multipart.finish();
            return response.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected String put(String url, Map<String, String> putParams) {
        return callHttp(url, "PUT", putParams);
    }

    protected String delete(String url) {
        return callHttp(url, "DELETE", null);
    }

    protected String get(String url) {
        return callHttp(url, "GET", null);
    }

    private String callHttp(String url, String method, Map<String, String> params) {
        Log.i(TAG, "CALL " + method + " " + url);
        HttpURLConnection urlConnection = null;
        try {
            URL zeUrl = new URL(url);
            urlConnection = (HttpURLConnection) zeUrl.openConnection();
            urlConnection.setReadTimeout(50000);
            urlConnection.setConnectTimeout(50000);
            urlConnection.setRequestMethod(method);

            urlConnection.setRequestProperty("WWW-Authenticate", epsilonPrefs.token().get());

            if (params != null) {
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(forgeQuery(params));
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
            if (entry.getValue() != null) {
                if (first) {
                    first = false;
                } else {
                    result.append("&");
                }

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
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
