package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicService: Service() {
    companion object {
        private const val TAG = "MusicService"
    }
    private lateinit var mediaPlayer : MediaPlayer
    val localBinder = LocalBinder()
    inner class LocalBinder: Binder(){
        fun getMusicServiceInstance() : MusicService{
            return this@MusicService
        }
    }
    override fun onBind(p0: Intent?): IBinder? {
        return localBinder
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

    fun test(){
        Log.d("TESTING", "Hello from MusicService!")
    }

    fun muteVolume() {
            mediaPlayer.pause()
    }

    fun unmuteVolume() {
            mediaPlayer.start()
    }

}