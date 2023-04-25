package com.Esisalama.babim.Utils

import android.content.Context
import android.widget.Toast


fun show_toast_util(context: Context,message:String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

}