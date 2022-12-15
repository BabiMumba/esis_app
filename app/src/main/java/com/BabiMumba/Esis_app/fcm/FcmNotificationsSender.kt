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
            "key=AAAA8fG5y6Q:APA91bH4SNLCmgHcg3Pf5XosC2WNK71BQ7y8Z6AyXfT50ukK3QFMqW4dO-M64f5GnPc7qQnIkfQ21_103w0F8AXhEVdFwpMZeglX-11QPDPZ0h0eks782YU1DzCrAjesWXilVh7nknCE"

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