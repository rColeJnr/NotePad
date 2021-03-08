package com.rick.notepad.util

import android.text.format.DateFormat
import java.util.*

object SetTime {
    
    val myDate = "EEE, MMM d, HH:mm"
    val date = Calendar.getInstance().time
    val dateFormat = DateFormat.format(myDate, date)
    
}