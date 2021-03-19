package com.rick.notepad.util

import java.util.concurrent.atomic.AtomicInteger

object Useful {

    const val DATABASE_NAME: String = "table_note"
    const val DATABASE_NAME2: String = "table_task"

    const val EXTRA_EXACT_ALARM_TIME = "EXTRA_EXACT_ALARM_TIME"
    const val ACTION_SET_REMINDER = "ACTION_SET_REMINDER"
    const val ACTION_SET_WEEKLY_REMINDER = "ACTION_SET_WEEKLY_REMINDER"
    
    const val ACTION_SET_YEARLY_REMINDER = "ACTION_SET_YEARLY_REMINDER"
    const val ACTION_SET_DAILY_REMINDER = "ACTION_SET_DAILY_REMINDER"
    const val ACTION_SET_WEEKDAY_REMINDER = "ACTION_SET_WEEKDAY_REMINDER"
    const val ACTION_SET_MONTHLY_REMINDER = "ACTION_SET_MONTHLY_REMINDER"

    //sets an unique notification/alarm id
    private val seed = AtomicInteger()
    fun getRandomInt() = seed.getAndIncrement() + System.currentTimeMillis().toInt()

    var dailyTimes: Int = 0 //triggers the daily alarm at least a day after setting the reminder

    /*
    * these strings are used to get the title and desciption of
    * the current task and send a notification with the values
    * */

    var task_title = ""
    var task_description = ""
}