package com.rick.notepad.util.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.rick.notepad.util.Useful.ACTION_SET_DAILY_REMINDER
import com.rick.notepad.util.Useful.ACTION_SET_MONTHLY_REMINDER
import com.rick.notepad.util.Useful.ACTION_SET_REMINDER
import com.rick.notepad.util.Useful.ACTION_SET_WEEKDAY_REMINDER
import com.rick.notepad.util.Useful.ACTION_SET_WEEKLY_REMINDER
import com.rick.notepad.util.Useful.ACTION_SET_YEARLY_REMINDER
import com.rick.notepad.util.Useful.EXTRA_EXACT_ALARM_TIME
import com.rick.notepad.util.Useful.dailyTimes
import com.rick.notepad.util.Useful.getRandomInt
import com.rick.notepad.util.Useful.task_description
import com.rick.notepad.util.Useful.task_title

class AlarmService(private val context: Context) {
    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?


    fun setReminder(timeInMillis: Long) {
        setMainReminder(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = ACTION_SET_REMINDER
                    putExtra(EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    putExtra("task_title", task_title)
                    putExtra("task_description", task_description)
                }
            )
        )
    }

    //set daily
    fun setDailyReminder(timeInMillis: Long) {
        if(dailyTimes > 0) {
            setMainReminder(
                timeInMillis,
                getPendingIntent(
                    getIntent().apply {
                        action = ACTION_SET_DAILY_REMINDER
                        putExtra(EXTRA_EXACT_ALARM_TIME, timeInMillis)
                        putExtra("task_title", task_title)
                        putExtra("task_description", task_description)
                    }
                )
            )
        }
    }

    //each Week
    fun setWeeklyReminder(timeInMillis: Long) {
        setMainReminder(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = ACTION_SET_WEEKLY_REMINDER
                    putExtra(EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    putExtra("task_title", task_title)
                    putExtra("task_description", task_description)
                }
            )
        )
    }

    //each Weekday
    fun setWeekdayReminder(timeInMillis: Long) {
        setMainReminder(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = ACTION_SET_WEEKDAY_REMINDER
                    putExtra(EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    putExtra("task_title", task_title)
                    putExtra("task_description", task_description)
                }
            )
        )
    }

    //each mpnthly
    fun setMonthlyReminder(timeInMillis: Long) {
        setMainReminder(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = ACTION_SET_MONTHLY_REMINDER
                    putExtra(EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    putExtra("task_title", task_title)
                    putExtra("task_description", task_description)
                }
            )
        )
    }

    //each year
    fun setYearlyReminder(timeInMillis: Long) {
        setMainReminder(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = ACTION_SET_YEARLY_REMINDER
                    putExtra(EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    putExtra("task_title", task_title)
                    putExtra("task_description", task_description)
                }
            )
        )
    }

    private fun setMainReminder(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager?.let {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }
    }

    private fun getPendingIntent(intent: Intent) =
        PendingIntent.getBroadcast(
            context,
            getRandomRequestCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun getIntent() = Intent(context, AlarmReceiver::class.java)

    private fun getRandomRequestCode() = getRandomInt()
}


