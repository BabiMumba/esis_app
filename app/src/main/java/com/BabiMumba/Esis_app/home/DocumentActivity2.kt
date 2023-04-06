package com.BabiMumba.Esis_app.home

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.BabiMumba.Esis_app.R
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.gms.ads.*
import java.io.File

class DocumentActivity2 : AppCompatActivity() {
    var filepath: String = ""

    private companion object{
        private const val TAG = "BANNER_AD_TAG"
    }
    private var adview: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document2)
        val pdfView = findViewById<PDFView>(R.id.pdfView)


        MobileAds.initialize(this){
            Log.d(TAG,"inias complet")
        }
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("f01a4b37-2568-4128-9894-6d6453fd67bb"))
                .build()
        )
        adview = findViewById(R.id.bannerAd)
        val adRequest = AdRequest.Builder().build()
        adview?.loadAd(adRequest)
        adview?.adListener = object : AdListener() {
            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG, "onAdClicked: ")
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.d(TAG, "onAdClosed: ")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.d(TAG, "onAdFailedToLoad: $p0")
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.d(TAG, "onAdImpression: ")
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d(TAG, "onAdLoaded: ")
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.d(TAG, "onAdOpened: ")
            }
        }
        filepath = intent.getStringExtra("path").toString()
        val file = File(filepath)
        val path = Uri.fromFile(file)
        pdfView.fromUri(path)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .load()


    }
    override fun onPause() {
        adview?.pause()
        super.onPause()
        Log.d(TAG, "onPause: ")

    }
    override fun onResume() {
        adview?.resume()
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onDestroy() {
        adview?.destroy()
        super.onDestroy()
    }
}