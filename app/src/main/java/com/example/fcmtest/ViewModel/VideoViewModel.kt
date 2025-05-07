package com.example.fcmtest.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class VideoViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private var _player: ExoPlayer? = null

    val player: ExoPlayer
        get() {
            if (_player == null) {
                _player = ExoPlayer.Builder(context).build()
            }
            return _player!!
        }

    fun preparePlayer(url: String, forceReload: Boolean = false) {
        val currentItem = player.currentMediaItem?.localConfiguration?.uri.toString()

        if (forceReload || currentItem != url) {
            val mediaItem = MediaItem.fromUri(url)
            player.setMediaItem(mediaItem, /* resetPosition = */ true)
            player.prepare()
        }
    }

    override fun onCleared() {
        super.onCleared()
        _player?.release()
    }
}