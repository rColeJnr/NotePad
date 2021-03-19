package com.rick.notepad.util

import android.text.format.DateFormat
import java.util.*

object SetTime {

    //at this point in time, i have no idea of where i use this..
    //i really need to start commenting my code
    //i saw the imortance of doing it, not only when i revisit old code, but when i had to read
    //somebody's else code, written in Java, but since it was doc, i could understand and work with it
    const val myDate = "EEE, MMM d, HH:mm"
    private val date: Date = Calendar.getInstance().time
    val dateFormat = DateFormat.format(myDate, date)
    
}