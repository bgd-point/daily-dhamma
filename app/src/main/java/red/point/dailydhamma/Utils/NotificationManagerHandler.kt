package red.point.dailydhamma.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import red.point.dailydhamma.MainActivity
import red.point.dailydhamma.R
import red.point.dailydhamma.model.QuestionAnswer

class NotificationManagerHandler(
    private val context: Context,
) {
    private var notificationId = 1
    private val notificationChannelId = context.getString(R.string.app_notification_channel_id)

    private val notificationManager: NotificationManager
        get() = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val notificationImportance = NotificationManager.IMPORTANCE_HIGH
        val notificationDescriptions = context.getString(R.string.app_notification_description)

        val channel = NotificationChannel(
            notificationChannelId, notificationDescriptions, notificationImportance
        ).apply {
            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }

        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(questionAnswer: QuestionAnswer) {

        val notifTitle = context.getString(R.string.app_notification_title)
        val notifPendingIntent = buildPendingIntent(questionAnswer.key)
        val notificationBuilder = NotificationCompat.Builder(context, notificationChannelId).apply {
            setSmallIcon(R.drawable.ic_notification)
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setCategory(NotificationCompat.CATEGORY_REMINDER)
            setContentTitle(notifTitle)
            setContentText(questionAnswer.title)
            setContentIntent(notifPendingIntent)
            setAutoCancel(true)
        }

        Log.i(javaClass.simpleName, "showNotification: show notification")
        notificationManager.notify(notificationId, notificationBuilder.build())
        notificationId++
    }

    private fun buildPendingIntent(id: String): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("id", id)
        }
        val flag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(context, 0, intent, flag)
    }
}