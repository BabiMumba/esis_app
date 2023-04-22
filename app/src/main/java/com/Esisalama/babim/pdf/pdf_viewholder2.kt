package com.Esisalama.babim.pdf

import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.Esisalama.babim.R


class pdf_viewholder2(itemview: View) : RecyclerView.ViewHolder(itemview) {
    var tvName: TextView
    var container: RelativeLayout

    init {
        tvName = itemview.findViewById(R.id.pdf_name)
        container = itemview.findViewById(R.id.container)
    }
}