package com.rick.notepad.util

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs


open class OnSwipeTouchListener: View.OnTouchListener {
    
    private val gestureDetector = GestureDetector(GestureListener())

    fun onTouch(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
    
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener(){
        
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
    
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }
    
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            onTouch(e)
            return true
        }
        
    
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            val result = false
            try {
                val diffy = e2?.y?.minus(e1?.y!!)
                val diffx = e2?.x?.minus(e1?.x!!)
                if (abs(diffx!!) > abs(diffy!!)) {
                    if (abs(diffx) > SWIPE_THRESHOLD && velocityX.let { abs(it) } > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffx > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                    }
                }
            } catch (e : Exception){ e.printStackTrace() }
            
            return result
        }
        
    }
    
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
    
    open fun onSwipeLeft() {}
    open fun onSwipeRight() {}
}