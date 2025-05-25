package com.example.fcmtest.Util

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class MyApp : Application(), LifecycleEventObserver {
    companion object{
    }
    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event
    ) {
        when(event){
            Lifecycle.Event.ON_CREATE -> TODO()
            Lifecycle.Event.ON_START -> TODO()
            Lifecycle.Event.ON_RESUME -> TODO()
            Lifecycle.Event.ON_PAUSE -> TODO()
            Lifecycle.Event.ON_STOP -> TODO()
            Lifecycle.Event.ON_DESTROY -> TODO()
            Lifecycle.Event.ON_ANY -> TODO()
        }
    }
}