package com.BabiMumba.Esis_app.pdf

import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.BabiMumba.Esis_app.R


class pdf_viewholder2(itemview: View) : RecyclerView.ViewHolder(itemview) {
    var tvName: TextView
    var container: RelativeLayout

    init {
        tvName = itemview.findViewById(R.id.pdf_name)
        container = itemview.findViewById(R.id.container)
    }
}