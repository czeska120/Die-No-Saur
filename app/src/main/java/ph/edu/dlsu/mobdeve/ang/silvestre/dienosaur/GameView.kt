package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.util.AttributeSet
import android.view.Display
import android.view.View
import androidx.core.content.res.ResourcesCompat
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.*
import kotlin.math.roundToInt

class GameView(context: Context, attributes: AttributeSet? = null) : View(context),
    SensorEventListener {
    companion object {
        var dWidth: Int = 0
        var dHeight: Int = 0

        // pause game
        var isPaused: Boolean = false

        // defaults
        var chosenBG = 0
        var chosenDino = 0
    }

    private var UPDATE_MILLIS: Long = 30
    private var runnable: Runnable

    // customize based on user preferences
    private var bg: BG
    private var dino: DinoSprite

    // background
    private var bgTop: Bitmap
    private var bgBottom: Bitmap
    private var rectTop: Rect
    private var rectBottom: Rect

    // dino
    private var dinoRunInt: Array<Int>
    private var dinoRun = ArrayList<Bitmap>()
    private var dinoHitInt: Array<Int>
    private var dinoHit = ArrayList<Bitmap>()
    private val flipMatrix = Matrix().apply { preScale(-1f, 1f) }
    private var motionX: Float = 0F
    private var dinoX: Float
    private var dinoY: Float
    //private var oldX: Float = 0.0f
    //private var oldDinoX: Float = 0.0f

    // score
    private val fibberish = ResourcesCompat.getFont(context, R.font.fibberish)
    private var TEXT_SIZE: Float = 150F

    private val timerCardHeight = 240F
    private val timerCard = resources.getDrawable(R.drawable.timercard, null)
    private val textScore = Paint().apply {
        textSize = TEXT_SIZE
        color = resources.getColor(R.color.text, null)
        textAlign = Paint.Align.CENTER
        typeface = fibberish
    }

    // life
    private var life: Int = 4
    private var isHit: Boolean = false
    private var hearts: ArrayList<Heart>

    // asteroids
    private var asteroids: ArrayList<Asteroid>
    private var explosions: ArrayList<Explosion>

    //timer
    private var score = 0.0 //var score: Int = 0
    private var scoreString: String = ""
    private var timerStarted = false
    private var serviceIntent: Intent
    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            score = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            scoreString = getTimeStringFromDouble(score)
        }
    }

    //sensors
    private var sensorManager: SensorManager

    //activity
    private var gameActivity: GameActivity? = null
    init {
        //display size
        val display: Display = (context as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        dWidth = size.x
        dHeight = size.y

        //reset() :
        // customize based on user preferences
        bg = BGs[chosenBG]
        dino = Dinos[chosenDino]

        //bg
        bgTop = BitmapFactory.decodeResource(context.resources, bg.top)
        bgBottom = BitmapFactory.decodeResource(context.resources, bg.bottom)
        rectTop = Rect(0, 0, dWidth, bgTop.height)
        rectBottom = Rect(0, bgTop.height, dWidth, dHeight)

        //dino
        dinoRunInt = dino.run
        for (i in dinoRunInt.indices) {
            dinoRun.add(BitmapFactory.decodeResource(context.resources, dinoRunInt[i]))
        }
        dino.runFrame = 0

        dinoHitInt = dino.hit
        for (i in dinoHitInt.indices) {
            dinoHit.add(BitmapFactory.decodeResource(context.resources, dinoHitInt[i]))
        }
        dino.hitFrame = 0

        dinoX = ((dWidth - dinoRun[0].width) / 2).toFloat()
        dinoY = (dHeight - rectBottom.height() - dinoRun[0].height + 70).toFloat()

        //timer
        serviceIntent = Intent(context, TimerService::class.java)
        context.registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
        startTimer()

        //asteroids
        asteroids = ArrayList()
        explosions = ArrayList()
        for (count in 0 until 3) { //3 = number of asteroids on screen at a time
            val asteroid = Asteroid(context)
            asteroids.add(asteroid)
        }

        //sensors
        sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        //hearts
        hearts = ArrayList()
        for (count in 0 until 3) {
            val heart = Heart(context)
            hearts.add(heart)
        }

        //activity
        if (context is GameActivity){
            gameActivity = context
        }

        //run
        runnable = Runnable { //animate dino
            if (isHit) {
                dino.hitFrame++
                if (dino.hitFrame == dinoHit.size) {
                    dino.hitFrame = 0
                }
            } else {
                dino.runFrame++
                if (dino.runFrame == dinoRun.size) {
                    dino.runFrame = 0
                }
            }

            //update screen
            if (!isPaused) {
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //draw bg
        canvas.drawBitmap(bgTop, null, rectTop, null)
        canvas.drawBitmap(bgBottom, null, rectBottom, null)

        //draw dino
        if (isHit) {
            canvas.drawBitmap(dinoHit[dino.hitFrame], dinoX, dinoY, null)
            postDelayed({
                isHit = false
            }, 500)
        } else {
            if (motionX > 0) {
                val flippedDinoRun = Bitmap.createBitmap(
                    dinoRun[dino.runFrame],
                    0,
                    0,
                    dinoRun[dino.runFrame].width,
                    dinoRun[dino.runFrame].height,
                    flipMatrix,
                    true
                )
                canvas.drawBitmap(flippedDinoRun, dinoX, dinoY, null)
            } else {
                canvas.drawBitmap(dinoRun[dino.runFrame], dinoX, dinoY, null)
            }
        }

        for (i in 0 until asteroids.size) {
            //draw asteroids
            canvas.drawBitmap(
                asteroids[i].getAsteroid(asteroids[i].asteroidFrame),
                asteroids[i].asteroidX.toFloat(),
                asteroids[i].asteroidY.toFloat(),
                null
            )

            //animate asteroid sprites
            asteroids[i].asteroidFrame++
            if (asteroids[i].asteroidFrame > 4) {
                asteroids[i].asteroidFrame = 0
            }

            //move asteroid down screen
            asteroids[i].asteroidY += asteroids[i].velocity

            //if asteroid reached ground
            val asteroidY = asteroids[i].asteroidY
            val asteroidHeight = asteroids[i].getAsteroid(asteroids[i].asteroidFrame).height

            if (asteroidY + asteroidHeight >= dHeight - rectBottom.height() + 100) {
                val explosion = Explosion(context)
                explosion.explosionX = asteroids[i].asteroidX
                explosion.explosionY = asteroids[i].asteroidY
                explosions.add(explosion)
                asteroids[i].resetPosition()
            }
        }

        for (i in 0 until asteroids.size - 1) {
            //if asteroid collides with dinosaur
            val asteroidX = asteroids[i].asteroidX
            val asteroidY = asteroids[i].asteroidY
            val asteroidWidth = asteroids[i].getAsteroid(asteroids[i].asteroidFrame).width
            val asteroidHeight = asteroids[i].getAsteroid(asteroids[i].asteroidFrame).height
            val dinoWidth = dinoRun[0].width
            val dinoHeight = dinoRun[0].height

            if (asteroidX + asteroidWidth >= dinoX
                && asteroidX <= dinoX + dinoWidth
                && asteroidY + asteroidHeight >= dinoY
                && asteroidY + asteroidHeight <= dinoY + dinoHeight
            ) {
                life-- //subtract life
                isHit = true //set boolean to true
                asteroids[i].resetPosition()

                //if player is out of lives
                if (life == 0) {
                    hearts[2].heartFrame = 4
                    stopTimer()

                    postDelayed({
                        val intent = Intent(context, GameOverActivity::class.java) //34.09
                        intent.putExtra("score", scoreString)

                        quit()
                        context.startActivity(intent)
                        gameActivity?.finish()
                    }, 500)
                }
            }
        }

        for (i in explosions.size - 1 downTo 0) {
            //draw explosions
            canvas.drawBitmap(
                explosions[i].getExplosion(explosions[i].explosionFrame),
                explosions[i].explosionX.toFloat(),
                explosions[i].explosionY.toFloat(),
                null
            )
            explosions[i].explosionFrame++

            if (explosions[i].explosionFrame > 5) {
                explosions.removeAt(i)
            }
        }

        timerCard.setBounds(100, 100, width - 100, 200 + timerCardHeight.toInt())
        timerCard.draw(canvas)

        //left heart
        val heart1X =
            (dWidth - hearts[2].getHeart(hearts[2].heartFrame).width) / 2f - hearts[2].getHeart(
                hearts[2].heartFrame
            ).width - 50
        canvas.drawBitmap(hearts[2].getHeart(hearts[2].heartFrame), heart1X, 50f, null)
        //center heart
        val heart2X = (dWidth - hearts[1].getHeart(hearts[1].heartFrame).width) / 2f
        canvas.drawBitmap(hearts[1].getHeart(hearts[1].heartFrame), heart2X, 50f, null)
        //right heart
        val heart3X =
            (dWidth - hearts[0].getHeart(hearts[0].heartFrame).width) / 2f + hearts[0].getHeart(
                hearts[0].heartFrame
            ).width + 50
        canvas.drawBitmap(hearts[0].getHeart(hearts[0].heartFrame), heart3X, 50f, null)

        //modify life
        if (life == 3) {
            hearts[0].heartFrame++
            if (hearts[0].heartFrame == 4) {
                hearts[0].heartFrame = 0
            }
        } else if (life == 2) {
            hearts[0].heartFrame = 4
            hearts[1].heartFrame++
            if (hearts[1].heartFrame == 4) {
                hearts[1].heartFrame = 0
            }
        } else if (life == 1) {
            hearts[1].heartFrame = 4
            hearts[2].heartFrame++
            if (hearts[2].heartFrame == 4) {
                hearts[2].heartFrame = 0
            }
        }

        val textX = width / 2f
        val textY = 200 + timerCardHeight / 2f - (textScore.ascent() + textScore.descent()) / 2f
        canvas.drawText(scoreString, textX, textY, textScore)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handler.postDelayed({
            reset()
            start()
        }, 100)
    }

    /*
    //touch controls
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var touchX: Float = event.x
        var touchY: Float = event.y

        if (touchY >= dinoY) {
            var action: Int = event.action
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.x
                oldDinoX = dinoX
            }
            if (action == MotionEvent.ACTION_MOVE) {
                var shift: Float = oldX - touchX
                var newDinoX: Float = oldDinoX - shift
                if (newDinoX <= 0) {
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
    */

    //sensors: tilt controls
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            motionX = event.values[0] //left/right tilt

            //map tilt
            val moveDino = -motionX / 100 * dWidth //100 = sensitivity of tilt
            val newDinoX = dinoX + moveDino

            //keep within screen bounds
            if (newDinoX <= 0) {
                dinoX = 0f
            } else if (newDinoX >= dWidth - dinoRun[0].width) {
                dinoX = (dWidth - dinoRun[0].width).toFloat()
            } else {
                dinoX = newDinoX
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    //timer
    private fun getTimeStringFromDouble(score: Double): String {
        val resultInt = score.roundToInt()
        val minutes = resultInt / 60000
        val seconds = (resultInt / 1000) % 60
        val milliseconds = resultInt % 1000
        return makeTimeString(minutes, seconds, milliseconds)
    }

    private fun makeTimeString(m: Int, s: Int, ms: Int): String =
        String.format("%02d:%02d.%02d", m, s, ms / 10)

    private fun resetTimer() {
        stopTimer()
        score = 0.0
        scoreString = getTimeStringFromDouble(score)
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, score)
        context.startService(serviceIntent)
        timerStarted = true
    }

    private fun stopTimer() {
        context.stopService(serviceIntent)
        timerStarted = false
    }

    // pause game
    fun reset() {
        // customize based on user preferences
        bg = BGs[chosenBG]
        dino = Dinos[chosenDino]

        //reset variables
        isPaused = false
        dino.runFrame = 0
        dino.hitFrame = 0
        life = 4

        //bg
        bgTop = BitmapFactory.decodeResource(context.resources, bg.top)
        bgBottom = BitmapFactory.decodeResource(context.resources, bg.bottom)
        rectTop = Rect(0, 0, dWidth, bgTop.height)
        rectBottom = Rect(0, bgTop.height, dWidth, dHeight)

        //dino
        dinoRun.clear()
        dinoRunInt = dino.run
        for (i in dinoRunInt.indices) {
            dinoRun.add(BitmapFactory.decodeResource(context.resources, dinoRunInt[i]))
        }
        dinoHit.clear()
        dinoHitInt = dino.hit
        for (i in dinoHitInt.indices) {
            dinoHit.add(BitmapFactory.decodeResource(context.resources, dinoHitInt[i]))
        }

        dinoX = ((dWidth - dinoRun[0].width) / 2).toFloat()
        dinoY = (dHeight - rectBottom.height() - dinoRun[0].height + 70).toFloat()

        //timer
        resetTimer()
        startTimer()

        //asteroids
        asteroids = ArrayList()
        explosions = ArrayList()
        for (count in 0 until 3) { //3 = number of asteroids on screen at a time
            val asteroid = Asteroid(context)
            asteroids.add(asteroid)
        }

        //sensors
        sensorManager.unregisterListener(this)
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        //hearts
        hearts = ArrayList()
        for (count in 0 until 3) {
            val heart = Heart(context)
            hearts.add(heart)
        }

        //run
        runnable = Runnable { //animate dino
            if (isHit) {
                dino.hitFrame++
                if (dino.hitFrame == dinoHit.size) {
                    dino.hitFrame = 0
                }
            } else {
                dino.runFrame++
                if (dino.runFrame == dinoRun.size) {
                    dino.runFrame = 0
                }
            }

            //update screen
            if (!isPaused) {
                invalidate()
            }

            start()
        }
    }

    fun start() {
        this.handler.postDelayed(runnable, UPDATE_MILLIS)
    }

    fun pause() {
        isPaused = true
        stopTimer()
        sensorManager.unregisterListener(this)
        this.handler.removeCallbacks(runnable)
    }

    fun resume() {
        isPaused = false
        startTimer()
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
        start()
    }

    fun quit() {
        isPaused = false
        resetTimer()
        sensorManager.unregisterListener(this)
        this.handler.removeCallbacks(runnable)
    }

    fun setCustom(bgKey: Int, dinoKey: Int){
        chosenBG = bgKey
        chosenDino = dinoKey
    }
}
