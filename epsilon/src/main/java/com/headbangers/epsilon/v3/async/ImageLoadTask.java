package com.headbangers.epsilon.v3.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.id;

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private final String wishId;
    private final Context context;
    private String url;
    private ImageView imageView;

    public ImageLoadTask(Context context, String wishId, String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
        this.wishId = wishId;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        File thumbnailOnFS = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), this.wishId);
        try {
            FileInputStream input = new FileInputStream(thumbnailOnFS);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (FileNotFoundException e) {
            // not found. ok download it.
        }

        Bitmap myBitmap = null;
        HttpURLConnection connection = null;
        try {
            URL urlConnection = new URL(url);
             connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            Log.e ("ImageLoadTask", e.getMessage());
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }

        // cache it
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(thumbnailOnFS);
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            Log.e ("ImageLoadTask", e.getMessage());
        }finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    // lol.
                }
            }
        }

        return myBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }

}
