package com.BabiMumba.Esis_app.pdf

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.BabiMumba.Esis_app.R
import java.io.File

class pdfAdapter2(
    private val context: Context,
    private val pdffiles: List<File>,
    private val pdf_listener_file: Pdf_listener_file

) : RecyclerView.Adapter<pdf_viewholder2>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pdf_viewholder2 {
        return pdf_viewholder2(
            LayoutInflater.from(context).inflate(R.layout.item_pdf, parent, false)
        )
    }

    override fun onBindViewHolder(holder: pdf_viewholder2, position: Int) {
        holder.tvName.text = pdffiles[position].name
        holder.container.isSelected = true
        holder.container.setOnClickListener {
            pdf_listener_file.onSelected(
                pdffiles[position]
            )
        }
    }

    override fun getItemCount(): Int {
        return pdffiles.size
    }
}