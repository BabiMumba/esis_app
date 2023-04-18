package com.BabiMumba.Esis_app.fcm

import android.content.Context
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import kotlin.Throws
import com.android.volley.AuthFailureError
import com.android.volley.Response
import org.json.JSONException
import java.util.HashMap

class FcmNotificationsSender {
       companion object {
        private const val BASE_URL = "https://fcm.googleapis.com/fcm/send"
        private const val ServerKey =
            "key=AAAAoPlT1mk:APA91bGugFlpIQSOmLlFfNpRT7Q_0elXBnVoKpT2BxYCoRW-0LognYS8T37hTxnmU9LKICoPSmtDtqXlNL-lD8wRDBs70t5XRiyn0UkEvPIs2Uyg5bST41kRvoy0CcvfcBSrma4Ykg0N"


        fun pushNotification(context: Context?, token: String?, title: String, message: String?) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val queue = Volley.newRequestQueue(context)
            try {
                val json = JSONObject()
                json.put("to", token)
                val notification = JSONObject()
                notification.put("title", title)
                notification.put("body", message)
                json.put("notification", notification)
                val request: JsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, BASE_URL, json, Response.Listener {
                        // code run is got response
                    }, Response.ErrorListener {
                        // code run is got error
                    }) {
                        @Throws(AuthFailureError::class)
                        override fun getHeaders(): Map<String, String> {
                            val header: MutableMap<String, String> = HashMap()
                            header["content-type"] = "application/json"
                            header["Authorization"] = ServerKey
                            return header
                        }
                    }
                queue.add(request)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}