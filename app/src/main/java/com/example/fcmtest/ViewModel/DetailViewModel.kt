package com.example.fcmtest.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.fcmtest.database.FallEvent

class DetailViewModel(application: Application) : ViewModel() {
    val context = application.applicationContext
    private var selectedEvent : FallEvent = FallEvent()
    var videomodel = VideoViewModel(application)
    fun setEvent(event : FallEvent){
        selectedEvent = event.copy()
    }
    fun getEvent(): FallEvent {
        return selectedEvent
    }

}