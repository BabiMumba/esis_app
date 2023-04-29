package com.Esisalama.babim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.Esisalama.babim.home.ActualiteActivity;
import com.Esisalama.babim.home.DocumentActivity2;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HoraireViewActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
  //  String pdfurl="https://www.esisalama.com/assets/upload/horaire/pdf/HORAIRE%20L2_v.pdf";
    PDFView pdfView;
    CircularProgressIndicator progress_circular;
    String theDir = Environment.getExternalStorageDirectory().toString() + "/Download/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horaire_view);

        Intent intent = getIntent();
        String promot_link = intent.getStringExtra("promot_link");
        String  lien = "https://www.esisalama.com/assets/upload/horaire/pdf/HORAIRE%20"+promot_link+".pdf";
       // String concatenate = "https://www.esisalama.com/assets/upload/horaire/pdf/HORAIRE%20"+promot_link+".pdf";
        setTitle("Horaire "+promot_link);
        pdfView = findViewById(R.id.pdfView);
        progress_circular = findViewById(R.id.progress_circular);
        progress_circular.setVisibility(View.VISIBLE);
        //calling async task to load pdf from URL.
        new loadpdffromUrl().execute(lien);

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
                Log.d("Erreurpdf",e.getMessage());

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
            //rediriger vers la page ActualiteActivity::class.java
            actual();

        }
    }

    public  void actual(){
        String actulaity_link = "https://www.esisalama.com/index.php?module=horaire";
        Intent intent = new Intent(HoraireViewActivity.this, ActualiteActivity.class);
        intent.putExtra("url_link", actulaity_link);
        startActivity(intent);
    }
    public void downloadPDF(String downloadLink, String fileName) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadLink));
        request.setTitle(fileName);
        request.setDescription("Downloading PDF...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(downloadLink);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        request.setMimeType(mimeType);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        long downloadID = downloadManager.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Toast.makeText(getApplicationContext(), "PDF Downloaded!", Toast.LENGTH_SHORT).show();
                unregisterReceiver(this);
            }
        };

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //create a menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.horaire_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = getIntent();
        String promot_link = intent.getStringExtra("promot_link");
        String  lien = "https://www.esisalama.com/assets/upload/horaire/pdf/HORAIRE%20"+promot_link+".pdf";

        switch (item.getItemId()) {
            case R.id.download_hor:
                //fab_download.setOnClickListener(v -> downloadPDF(pdfurl,"Horaire.pdf"));
                //downloadPDF(lien,"Horaire"+promot_link+".pdf");
                File file = new File(theDir, "Horaire"+promot_link+".pdf");
                _createFile(file);
                return true;
            case R.id.show_all:
                actual();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //verifier si le fichier existe
    public void _createFile(File file) {
        Intent intent = getIntent();
        String promot_link = intent.getStringExtra("promot_link");
        String  lien = "https://www.esisalama.com/assets/upload/horaire/pdf/HORAIRE%20"+promot_link+".pdf";

        if (!file.exists()) {
            try {
                boolean succ = file.createNewFile();
                if (succ) {
                    downloadPDF(lien,"Horaire"+promot_link+".pdf");
                } else
                    Toast.makeText(getApplicationContext(), "Error creating file", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (file.exists()) {
            //dialog demander si il veut supprimer le fichier
            AlertDialog.Builder builder = new AlertDialog.Builder(HoraireViewActivity.this);
            builder.setTitle("Fichier existe");
            builder.setCancelable(true);
            builder.setMessage("Voulez-vous Ecraser le fichier existant?");
            //bouton ouvrir
            builder.setNegativeButton("Ouvrir l'ancien fichier", (dialog, which) -> {

                Intent intent1 = new Intent(HoraireViewActivity.this, DocumentActivity2.class);
                intent1.putExtra("path", file.getAbsolutePath());
                startActivity(intent1);

            });
            builder.setPositiveButton("Oui", (dialog, which) -> _deleteFile(file));
        }
    }
    //delete file
    public void _deleteFile(File file) {
        Intent intent = getIntent();
        String promot_link = intent.getStringExtra("promot_link");
        String  lien = "https://www.esisalama.com/assets/upload/horaire/pdf/HORAIRE%20"+promot_link+".pdf";
        if (file.exists()) {
            boolean succ = file.delete();
            if (succ) {
                downloadPDF(lien,"Horaire"+promot_link+".pdf");
            } else
                Toast.makeText(getApplicationContext(), "Error deleting file", Toast.LENGTH_SHORT).show();
        } else if (!file.exists()) {
            Toast.makeText(getApplicationContext(), "File does not exist!", Toast.LENGTH_SHORT).show();
        }
    }



}