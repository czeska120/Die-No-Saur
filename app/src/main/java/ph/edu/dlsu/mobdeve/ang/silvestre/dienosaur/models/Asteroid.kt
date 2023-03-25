package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R
import kotlin.random.Random

class Asteroid(context: Context) {
    private var asteroid: Bitmap
    var asteroidX: Int = 0
    var asteroidY: Int = 0
    var velocity: Int = 0
    private val random: Random = Random.Default

    init {
        asteroid = BitmapFactory.decodeResource(context.resources, R.drawable.asteroid_large)
        resetPosition()
    }

    fun getAsteroid(): Bitmap {
        return asteroid
    }

    fun resetPosition() {
        asteroidX = random.nextInt(GameView.dWidth - asteroid.width)
        asteroidY = -200 + random.nextInt(600) * -1
        velocity = 35 + random.nextInt(16)
    }
}
