package com.Esisalama.babim.home

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.Esisalama.babim.R
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Esisalama.babim.pdf.Pdf_listener_file
import com.Esisalama.babim.pdf.pdfAdapter2
import com.google.android.gms.ads.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_lecture_pdf.*
import kotlinx.android.synthetic.main.row_feed.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class LectureActivity_Pdf : AppCompatActivity(), Pdf_listener_file {
    lateinit var pdfAdapter: pdfAdapter2
    private lateinit var pdfList: MutableList<File>
    private lateinit var recyclerView: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager

    private companion object{
        private const val TAG = "BANNER_AD_TAG"
    }
    private var adview: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_pdf)
        supportActionBar?.hide()

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

        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val point = sharedPreferences.getInt("point",0)

        runtimePermissions()


    }
    private fun runtimePermissions() {
        Dexter.withContext(this@LectureActivity_Pdf)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    displaypdf()
                }
                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    Toast.makeText(
                        this@LectureActivity_Pdf,
                        "la permission est necessaire pour continuer",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).check()
    }
    fun findpdf(file: File): ArrayList<File> {
        val arrayList = ArrayList<File>()
        val files = file.listFiles()
        for (singlefile in files) {
            if (singlefile.isDirectory && !singlefile.isHidden) {
                arrayList.addAll(findpdf(singlefile))
            } else {
                if (singlefile.name.endsWith(".pdf")) {
                    arrayList.add(singlefile)
                }
            }
        }
        return arrayList
    }
    private fun displaypdf() {
        recyclerView = findViewById(R.id.my_recyclerview)
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.onSaveInstanceState()
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager
        pdfList = ArrayList()
        pdfList.addAll(
            findpdf( File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/" + Environment.DIRECTORY_DOWNLOADS
            ))

        )
        pdfAdapter = pdfAdapter2(this, pdfList, this)
        my_recyclerview.adapter = pdfAdapter
    }
    override fun onSelected(file: File) {
        startActivity(
            Intent(this, DocumentActivity2::class.java)
                .putExtra("path", file.absolutePath)
        )
    }

}