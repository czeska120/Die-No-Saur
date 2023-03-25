package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R
import kotlin.random.Random

class Asteroid(context: Context) {
    private var asteroid: Array<Bitmap>
    var asteroidFrame: Int = 0
    var asteroidX: Int = 0
    var asteroidY: Int = 0
    var velocity: Int = 0
    private val random: Random = Random.Default

    init {
        asteroid = arrayOf(
            BitmapFactory.decodeResource(context.resources, R.drawable.a_small_1),
            BitmapFactory.decodeResource(context.resources, R.drawable.a_small_2),
            BitmapFactory.decodeResource(context.resources, R.drawable.a_small_3),
            BitmapFactory.decodeResource(context.resources, R.drawable.a_small_4),
            BitmapFactory.decodeResource(context.resources, R.drawable.a_small_5),
        )
        resetPosition()
    }

    fun getAsteroid(frame: Int): Bitmap {
        return asteroid[frame]
    }

    fun resetPosition() {
        asteroidX = random.nextInt(GameView.dWidth - asteroid[0].width)
        asteroidY = -100 + random.nextInt(600) * -1
        velocity = 35 + random.nextInt(10)
    }
}
