package com.BabiMumba.Esis_app.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.home.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MyFirebaseMessaging : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notifId = Random().nextInt()

        val title = remoteMessage.notification!!.title
        val message_txt = remoteMessage.notification!!.body

        val resultIntent = Intent(this, MainActivity::class.java)

        //val pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_ONE_SHOT)
        }

        //creer une notification
        val notificationBuilder = NotificationCompat.Builder(this, CANAL)
        notificationBuilder.setContentTitle(title)
        notificationBuilder.setContentText(message_txt)
        notificationBuilder.setSmallIcon(R.mipmap.launcher_icon)
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setStyle(
            NotificationCompat.BigTextStyle().bigText(
                remoteMessage.notification!!.body
            )
        )
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.priority = Notification.PRIORITY_MAX
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.noti_channellid)
            val channelTitle = getString(R.string.notif_channel_title)
            val channelDescription = getString(R.string.notif_channel_desc)
            val channel =
                NotificationChannel(channelId, channelTitle, NotificationManager.IMPORTANCE_HIGH)
            channel.description = channelDescription
            notificationManager.createNotificationChannel(channel)
            notificationBuilder.setChannelId(channelId)
        }
        notificationManager.notify(notifId, notificationBuilder.build())
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(applicationContext, notification)
        r.play()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.isLooping = false
        }

        // vibration
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(100, 300, 300, 300)
        v.vibrate(pattern, -1)
    }

    companion object {
        private const val CANAL = "message"
    }
}