package com.BabiMumba.Esis_app.home

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.BabiMumba.Esis_app.R
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class DocumentActivity2 : AppCompatActivity() {
    var filepath: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document2)
        val pdfView = findViewById<PDFView>(R.id.pdfView)
        filepath = intent.getStringExtra("path").toString()
        val file = File(filepath)
        val path = Uri.fromFile(file)
        pdfView.fromUri(path).load()
    }
}