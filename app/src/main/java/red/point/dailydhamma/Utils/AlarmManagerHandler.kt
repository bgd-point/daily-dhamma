package red.point.dailydhamma.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import red.point.dailydhamma.receiver.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object AlarmManagerHandler {

    @JvmStatic
    fun startAlarm(context: Context, time: String) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val hour = time.split(":").first()
        val currentTime = System.currentTimeMillis()

        val notifTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour.toInt())
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if ((currentTime + 5 * 60 * 1000) >= notifTime.timeInMillis) {
            Log.i(javaClass.simpleName, "startAlarm: 1 day to notification time")
            notifTime.add(Calendar.DATE, 1)
        }

        val formattedTime = formatDate(notifTime.time)
        val pendingIntent = getPendingIntent(context)

        Log.i(javaClass.simpleName, "startAlarm: $formattedTime")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            if (alarmManager.canScheduleExactAlarms()) {
                Log.i(javaClass.simpleName, "startAlarm: setExactAndAllowWhileIdle")
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, notifTime.timeInMillis, pendingIntent
                )
            } else {
                Log.w(javaClass.simpleName, "Exact alarm not permitted")
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, notifTime.timeInMillis, pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP, notifTime.timeInMillis, pendingIntent
                )
            }
        }

        Log.i(javaClass.simpleName, "startAlarm: set alarm manager")
    }

    @JvmStatic
    fun cancelAlarm(context: Context) {
        Log.i(javaClass.simpleName, "cancelAlarm: cancel alarm manager")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getPendingIntent(context)
        alarmManager.cancel(pendingIntent)
    }

    private fun formatDate(
        date: Date,
        format: String = "dd/MM/yyyy HH:mm",
        locale: Locale = Locale.US,
    ): String? {
        try {
            val dateFormat = SimpleDateFormat(format, locale)
            return dateFormat.format(date.time)
        } catch (e: Exception) {
            return null
        }
    }

    private fun getPendingIntent(context: Context): PendingIntent {

        val flag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_REMAINDER_TIME
        }

        return PendingIntent.getBroadcast(context, 11, alarmIntent, flag)
    }


}