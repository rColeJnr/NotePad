package com.rick.notepad.util.service

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rick.notepad.ui.MainActivity
import com.rick.notepad.util.Useful.ACTION_SET_DAILY_REMINDER
import com.rick.notepad.util.Useful.ACTION_SET_MONTHLY_REMINDER
import com.rick.notepad.util.Useful.ACTION_SET_REMINDER
import com.rick.notepad.util.Useful.ACTION_SET_WEEKDAY_REMINDER
import com.rick.notepad.util.Useful.ACTION_SET_WEEKLY_REMINDER
import com.rick.notepad.util.Useful.ACTION_SET_YEARLY_REMINDER
import com.rick.notepad.util.Useful.dailyTimes
import io.karn.notify.Notify
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver: BroadcastReceiver() {

    private var taskTitle: String? = null
    private var taskdescription: String? = null

    override fun onReceive(context: Context, intent: Intent) {
        taskTitle = intent.getStringExtra("task_title")
        taskdescription = intent.getStringExtra("task_description")
        when (intent.action) {
            ACTION_SET_REMINDER -> {
                dailyTimes = 1
                buildNotification(context)
            }
            
            ACTION_SET_DAILY_REMINDER -> {
                setDaily(AlarmService(context))
                buildNotification(context)
            }

            ACTION_SET_WEEKLY_REMINDER -> {
                setWeekly(AlarmService(context))
                buildNotification(context)
            }

            ACTION_SET_WEEKDAY_REMINDER -> {
                setWeekday(AlarmService(context))
                buildNotification(context)
            }

            ACTION_SET_MONTHLY_REMINDER -> {
                setMonthly(AlarmService(context))
                buildNotification(context)
            }

            ACTION_SET_YEARLY_REMINDER -> {
                setYearly(AlarmService(context))
                buildNotification(context)
            }
        }
    }

    private fun buildNotification(context: Context) {
        Notify
            .with(context)
            .meta {
                clickIntent = PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    0
                )
            }
            .content {
                this.title =  taskTitle
                text = taskdescription
            }
            .show()
    }

    private fun setDaily(alarmService: AlarmService) {
        if (dailyTimes > 0) {
            val cal = Calendar.getInstance().apply {
                this.timeInMillis += TimeUnit.DAYS.toMillis(1)
            }
            alarmService.setDailyReminder(cal.timeInMillis)
        }
    }

    private fun setWeekly(alarmService: AlarmService) {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
        }
        alarmService.setWeeklyReminder(cal.timeInMillis)
    }

    private fun setWeekday(alarmService: AlarmService) {
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_WEEK)
        if (day != 7 && day != 1) {
            cal.timeInMillis += TimeUnit.DAYS.toMillis(30)
            alarmService.setWeekdayReminder(cal.timeInMillis)
        }
    }

    private fun setMonthly(alarmService: AlarmService) {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(30)
        }
        alarmService.setMonthlyReminder(cal.timeInMillis)
    }

    private fun setYearly(alarmService: AlarmService) {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(365)
        }
        alarmService.setYearlyReminder(cal.timeInMillis)

        //to chech for 366 days, would add a statemnet checking if year count = ? and
        // if so set duration to 366.
    }
}