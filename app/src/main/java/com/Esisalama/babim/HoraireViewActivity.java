package com.Esisalama.babim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HoraireViewActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    String pdfurl="https://perso.univ-rennes1.fr/virginie.sans/l2pw/UElibre_Internet.pdf";
    PDFView pdfView;
    CircularProgressIndicator progress_circular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horaire_view);

        pdfView = findViewById(R.id.pdfView);
        progress_circular = findViewById(R.id.progress_circular);
        progress_circular.setVisibility(View.VISIBLE);
        new loadpdffromUrl().execute(pdfurl);

    }

    //create an async task to load pdf from URL.
    class loadpdffromUrl extends AsyncTask<String, Void, InputStream> implements OnLoadCompleteListener, OnErrorListener {
        @Override
        protected InputStream doInBackground(String... strings) {
            //We use InputStream to get PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // if response is success. we are getting input stream from url and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                //method to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            //after the executing async task we load pdf in to pdfview.
            pdfView.fromStream(inputStream).onLoad(this).onError(this).load();
        }

        @Override
        public void loadComplete(int nbPages) {
            progress_circular.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable t) {
            progress_circular.setVisibility(View.GONE);
            Toast.makeText(HoraireViewActivity.this,"Error:" +t.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}