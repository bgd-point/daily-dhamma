package red.point.dailydhamma.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import red.point.dailydhamma.utils.AlarmManagerHandler
import red.point.dailydhamma.utils.MPreferenceManager


class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent?.action) {

            Intent.ACTION_BOOT_COMPLETED, Intent.ACTION_TIME_TICK, Intent.ACTION_TIMEZONE_CHANGED -> {

                val isEnableNotif = MPreferenceManager.readStringInformation(
                    context, "NOTIFICATION_ENABLE", false,
                )

                var notifTime = MPreferenceManager.readStringInformation(
                    context, "notification_time_list"
                )

                if (notifTime.isEmpty()) notifTime = "06:00"
                if (isEnableNotif != "true" || context == null) return

                AlarmManagerHandler.startAlarm(context, notifTime)
            }

            else -> {}
        }

    }

}