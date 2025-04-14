package red.point.dailydhamma.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import red.point.dailydhamma.DailyDhamma
import red.point.dailydhamma.model.QuestionAnswer
import red.point.dailydhamma.utils.AlarmManagerHandler
import red.point.dailydhamma.utils.MPreferenceManager
import kotlin.coroutines.suspendCoroutine


class AlarmReceiver : BroadcastReceiver() {

    private lateinit var dbReference: DatabaseReference
    private var questionAnswerList: List<QuestionAnswer> = emptyList()
    private var randomQuestionAnswer: QuestionAnswer? = null

    private val database = FirebaseDatabase.getInstance()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.i(javaClass.simpleName, "onReceive: ${intent?.action}")

        if (intent?.action == ACTION_REMAINDER_TIME && context != null) {

            val pendingResult = goAsync()

            val notifHandler = (context.applicationContext as DailyDhamma).notifHandler
            val isEnableNotif = MPreferenceManager.readStringInformation(
                context, "NOTIFICATION_ENABLE", false,
            )

            val notifTime = MPreferenceManager.readStringInformation(
                context, "notification_time_list", false,
            )

            Log.i(javaClass.simpleName, "onReceive: is enable notif $isEnableNotif")
            if (isEnableNotif != "true") return pendingResult.finish()

            GlobalScope.launch {

                dbReference = getDatabaseReference(context)
                questionAnswerList = getDataFromDatabaseReference()
                randomQuestionAnswer = getRandomQuestionAnswer()

                notifHandler.showNotification(randomQuestionAnswer!!)
                AlarmManagerHandler.startAlarm(context, notifTime)
                Log.i(javaClass.simpleName, "onReceive: $randomQuestionAnswer")

                pendingResult.finish()

            }
        }
    }

    private suspend fun getDataFromDatabaseReference(): List<QuestionAnswer> {

        return suspendCoroutine {

            dbReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val questionAnswerList = mutableListOf<QuestionAnswer>()

                    for (data in snapshot.children) {

                        val questionAnswer = data.getValue(QuestionAnswer::class.java)

                        if (questionAnswer != null) {
                            questionAnswerList.add(questionAnswer)
                            questionAnswer.key = data.ref.getKey();
                        }
                    }

                    it.resumeWith(Result.success(questionAnswerList))
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i(javaClass.simpleName, "onCancelled: $error")
                }
            })
        }
    }

    private fun getDatabaseReference(context: Context): DatabaseReference {
        val isSelectedDefaultLang =
            MPreferenceManager.readBoolInformation(context, MPreferenceManager.DEFAULT_LANG)
        return if (isSelectedDefaultLang) database.getReference("question-answer")
        else database.getReference("question-answer-en")
    }

    private fun getRandomQuestionAnswer(): QuestionAnswer? {
        if (questionAnswerList.isEmpty()) return null
        return questionAnswerList.random()
    }

    companion object {
        const val ACTION_REMAINDER_TIME = "red.point.dailydhamma.ACTION_REMAINDER_TIME"
    }
}