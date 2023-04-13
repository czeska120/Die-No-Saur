package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Context
import android.media.SoundPool

class SoundPoolManager private constructor(context: Context) {
    private val soundPool = SoundPool.Builder().setMaxStreams(10).build()
    private val soundMap = mutableMapOf<Int, Int>()
    private var savedVolume : Float = 1.0f

    init {
        // Load sound effects into the SoundPool
        // 0 = highest priority; 255 = lowest priority
        soundMap[R.raw.sfx_tick] = soundPool.load(context, R.raw.sfx_tick, 4)
        soundMap[R.raw.sfx_button] = soundPool.load(context, R.raw.sfx_button, 3)
        soundMap[R.raw.sfx_text] = soundPool.load(context, R.raw.sfx_text, 2)
        soundMap[R.raw.sfx_gameover] = soundPool.load(context, R.raw.sfx_gameover, 1)
        soundMap[R.raw.sfx_hit] = soundPool.load(context, R.raw.sfx_hit, 0)
    }

    fun playSound(soundId: Int) {
        val sound = soundMap[soundId]
        sound?.let { soundPool.play(it, savedVolume, savedVolume, 0, 0, 1.0f) }
    }

    fun setVolume(volValue: Float){
        savedVolume = volValue
    }

    companion object {
        @Volatile
        private var instance: SoundPoolManager? = null

        fun getInstance(context: Context): SoundPoolManager {
            return instance ?: synchronized(this) {
                instance ?: SoundPoolManager(context).also { instance = it }
            }
        }
    }
}



