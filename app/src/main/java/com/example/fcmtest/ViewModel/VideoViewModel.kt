package com.example.fcmtest.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
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

    fun preparePlayer(url: String, forceReload: Boolean = true) {

        val currentItem = player.currentMediaItem?.localConfiguration?.uri.toString()
        Log.d("비디오url",url)
        if (forceReload || currentItem != url) {
            val mediaItem = MediaItem.fromUri(url)
            player.setMediaItem(mediaItem, /* resetPosition = */ true)
            player.prepare()

            player.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    Log.e("VideoPlayer", "재생 실패: ${error.message}")
                    player.stop()
                    player.clearMediaItems()
                }
            })
        }
    }

    override fun onCleared() {
        super.onCleared()
        _player?.release()
        _player = null
    }
    fun releasePlayer() {
        _player?.release()
        _player = null
    }
}