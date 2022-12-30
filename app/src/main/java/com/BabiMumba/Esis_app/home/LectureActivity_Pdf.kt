package com.BabiMumba.Esis_app.home

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.pdf.Pdf_listener_file
import com.BabiMumba.Esis_app.pdf.pdfAdapter2
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_lecture_pdf.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class LectureActivity_Pdf : AppCompatActivity(), Pdf_listener_file {
    lateinit var pdfAdapter: pdfAdapter2
    private lateinit var pdfList: MutableList<File>
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_pdf)
        title = "Lecture syllabus"
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
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this,)
        pdfList = ArrayList()
        pdfList.addAll( findpdf(Environment.getExternalStorageDirectory()))
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