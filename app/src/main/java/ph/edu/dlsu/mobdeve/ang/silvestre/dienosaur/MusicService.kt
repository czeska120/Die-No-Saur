package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService: Service() {
    private lateinit var mediaPlayer : MediaPlayer

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.bgmusic)
        mediaPlayer.isLooping = true
        mediaPlayer.setVolume(100F, 100F)
    }

    override fun onStart(intent: Intent?, startId: Int) {
        mediaPlayer.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer.start()
        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer.stop()
    }


}