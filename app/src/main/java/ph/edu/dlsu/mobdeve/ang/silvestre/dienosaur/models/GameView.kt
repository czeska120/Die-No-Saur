package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Handler
import android.view.Display
import android.view.MotionEvent
import android.view.View
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.GameOverActivity
import kotlin.random.Random

class GameView(context: Context) : View(context) {
    companion object{
        var dWidth: Int = 0
        var dHeight: Int = 0
    }
    var bg: BG
    var bgTop: Bitmap
    var bgBottom: Bitmap
    var dino: DinoSprite
    var dinoRun: Bitmap
    var dinoHit: Bitmap
    var rectTop: Rect
    var rectBottom: Rect
    var UPDATE_MILLIS: Long = 30
    var runnable: Runnable
    var textScore: Paint = Paint(). apply {
        color = Color.BLACK
        textSize = 100F
    }
    var health: Paint = Paint()
    var TEXT_SIZE: Float = 100F
    var score: Int = 0
    var life: Int = 3
    var random: Random
    var dinoX: Float
    var dinoY: Float
    var oldX: Float = 0.0f
    var oldDinoX: Float = 0.0f
    var asteroids: ArrayList<Asteroid>
    var explosions: ArrayList<Explosion>

    init {
        bg = BGs[0]
        bgTop = BitmapFactory.decodeResource(context.resources, bg.top)
        bgBottom = BitmapFactory.decodeResource(context.resources, bg.bottom)
        dino = Dinos[0]
        dinoRun = BitmapFactory.decodeResource(context.resources, dino.run)
        dinoHit = BitmapFactory.decodeResource(context.resources, dino.hit)
        val display: Display = (context as Activity).windowManager.defaultDisplay
        var size = Point()
        display.getSize(size)
        dWidth = size.x
        dHeight = size.y
        rectTop = Rect(0, 0, dWidth, bgTop.height)
        rectBottom = Rect(0, bgTop.height, dWidth, dHeight)
        runnable = Runnable(){
            invalidate()
        }
        random = Random
        dinoX = ((dWidth - dinoRun.width) / 2).toFloat()
        dinoY = (dHeight - rectBottom.height() - dinoRun.height + 70).toFloat()
        asteroids = ArrayList<Asteroid>()
        explosions = ArrayList<Explosion>()
        for (count in 0 until 2){
            var asteroid: Asteroid = Asteroid(context)
            asteroids.add(asteroid)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bgTop, null, rectTop, null)
        canvas.drawBitmap(bgBottom, null, rectBottom, null)
        canvas.drawBitmap(dinoRun, dinoX, dinoY, null)

        for (i in 0 until asteroids.size){
            canvas.drawBitmap(asteroids[i].getAsteroid(asteroids[i].asteroidFrame), asteroids[i].asteroidX.toFloat(), asteroids[i].asteroidY.toFloat(), null)
            asteroids[i].asteroidFrame++
            if (asteroids[i].asteroidFrame > 4){
                asteroids[i].asteroidFrame = 0
            }
            asteroids[i].asteroidY += asteroids[i].velocity
            if (asteroids[i].asteroidY + asteroids[i].getAsteroid(asteroids[i].asteroidFrame).height >= dHeight - rectBottom.height()) {
                score += 10
                /*var explosion = Explosion(context)
                explosion.explosionX = asteroids[i].asteroidX
                explosion.explosionY = asteroids[i].asteroidY
                explosions.add(explosion)*/
                asteroids[i].resetPosition()
            }
        }

        for (i in 0 until asteroids.size - 1){
            //collision
            if (asteroids[i].asteroidX + asteroids[i].getAsteroid(asteroids[i].asteroidFrame).width >= dinoX
            && asteroids[i].asteroidX <= dinoX + dinoRun.width
            && asteroids[i].asteroidY + asteroids[i].getAsteroid(asteroids[i].asteroidFrame).width >= dinoY
            && asteroids[i].asteroidY + asteroids[i].getAsteroid(asteroids[i].asteroidFrame).width <= dinoY + dinoRun.height){
                life--
                asteroids[i].resetPosition()
                if (life == 0){
                    val intent = Intent(context, GameOverActivity::class.java) //34.09
                    intent.putExtra("score", score)
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
            }
        }

        /*
        for (i in 0 until explosions.size){
            canvas.drawBitmap(explosions[i].getExplosion(), explosions[i].explosionX.toFloat(), explosions[i].explosionY.toFloat(), null)

            val startTime = System.currentTimeMillis() // capture the start time

            if (System.currentTimeMillis() - startTime >= 3000) {
                explosions.removeAt(i)
            }
        }
        */

        //modify life = 28:36
        if (life == 2){
            health.setColor(Color.YELLOW)
        } else if (life == 1){
            health.setColor(Color.RED)
        }

        canvas.drawRect((dWidth - 200).toFloat(), 30F, (dWidth - 200 + 60 * life).toFloat(), 80F, health)
        canvas.drawText("Score: $score", 20F, TEXT_SIZE, textScore)
        handler.postDelayed(runnable, UPDATE_MILLIS)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var touchX: Float = event.x
        var touchY: Float = event.y

        if (touchY >= dinoY) {
            var action: Int = event.action
            if (action == MotionEvent.ACTION_DOWN){
                oldX = event.x
                oldDinoX = dinoX
            }
            if (action == MotionEvent.ACTION_MOVE){
                var shift: Float = oldX - touchX
                var newDinoX: Float = oldDinoX - shift
                if (newDinoX <= 0){
                    dinoX = 0F
                } else if (newDinoX >= dWidth - dinoRun.width) {
                    dinoX = dWidth - dinoRun.width.toFloat()
                } else {
                    dinoX = newDinoX
                }
            }
        }

        return true
    }
}
