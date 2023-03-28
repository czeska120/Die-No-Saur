package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models

import android.annotation.SuppressLint
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
import android.view.Display
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat.registerReceiver
import androidx.core.content.res.ResourcesCompat
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.GameOverActivity
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.TimerService
import kotlin.math.roundToInt
import kotlin.random.Random

class GameView(context: Context) : View(context), SensorEventListener {
    companion object{
        var dWidth: Int = 0
        var dHeight: Int = 0
    }

    var bg: BG
    var bgTop: Bitmap
    var bgBottom: Bitmap

    var dino: DinoSprite
    var dinoRun: Bitmap
    //var dinoHit: Bitmap

    var rectTop: Rect
    var rectBottom: Rect

    var UPDATE_MILLIS: Long = 30
    var runnable: Runnable

    val fibberish = ResourcesCompat.getFont(context, R.font.fibberish)
    var TEXT_SIZE: Float = 100F
    var textScore: Paint = Paint(). apply {
        color = Color.BLACK
        textSize = TEXT_SIZE
        typeface = fibberish
    }

    //timer
    var score = 0.0 //var score: Int = 0
    var scoreString: String = ""
    private var timerStarted = false
    private var serviceIntent: Intent
    private val updateTime: BroadcastReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            score = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            scoreString = getTimeStringFromDouble(score)
        }
    }

    var health: Paint = Paint()
    var life: Int = 3

    var random: Random

    var dinoX: Float
    var dinoY: Float
    var oldX: Float = 0.0f
    var oldDinoX: Float = 0.0f

    var asteroids: ArrayList<Asteroid>
    //var explosions: ArrayList<Explosion>

    //sensors
    private lateinit var sensorManager: SensorManager

    init {
        //change based on user preferences
        bg = BGs[0]
        dino = Dinos[0]

        bgTop = BitmapFactory.decodeResource(context.resources, bg.top)
        bgBottom = BitmapFactory.decodeResource(context.resources, bg.bottom)

        dinoRun = BitmapFactory.decodeResource(context.resources, dino.run)
        //dinoHit = BitmapFactory.decodeResource(context.resources, dino.hit)

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
        //explosions = ArrayList<Explosion>()

        //timer
        serviceIntent = Intent(context, TimerService::class.java)
        context.registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
        startTimer()

        //asteroids
        for (count in 0 until 3){ //3 = number of asteroids on screen at a time
            var asteroid = Asteroid(context)
            asteroids.add(asteroid)
        }

        //sensors
        sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bgTop, null, rectTop, null)
        canvas.drawBitmap(bgBottom, null, rectBottom, null)
        canvas.drawBitmap(dinoRun, dinoX, dinoY, null)

        for (i in 0 until asteroids.size){
            //draw asteroid
            canvas.drawBitmap(asteroids[i].getAsteroid(asteroids[i].asteroidFrame), asteroids[i].asteroidX.toFloat(), asteroids[i].asteroidY.toFloat(), null)

            //animate asteroid sprites
            asteroids[i].asteroidFrame++
            if (asteroids[i].asteroidFrame > 4){
                asteroids[i].asteroidFrame = 0
            }

            //move asteroid down screen
            asteroids[i].asteroidY += asteroids[i].velocity

            //if asteroid reached ground
            if (asteroids[i].asteroidY + asteroids[i].getAsteroid(asteroids[i].asteroidFrame).height >= dHeight - rectBottom.height() +100) {
                //score += 10
                /*var explosion = Explosion(context)
                explosion.explosionX = asteroids[i].asteroidX
                explosion.explosionY = asteroids[i].asteroidY
                explosions.add(explosion)*/
                asteroids[i].resetPosition()
            }
        }

        for (i in 0 until asteroids.size - 1){
            //if asteroid collides with dinosaur
            if (asteroids[i].asteroidX + asteroids[i].getAsteroid(asteroids[i].asteroidFrame).width >= dinoX
            && asteroids[i].asteroidX <= dinoX + dinoRun.width
            && asteroids[i].asteroidY + asteroids[i].getAsteroid(asteroids[i].asteroidFrame).width >= dinoY
            && asteroids[i].asteroidY + asteroids[i].getAsteroid(asteroids[i].asteroidFrame).width <= dinoY + dinoRun.height){
                life-- //subtract life
                asteroids[i].resetPosition()

                //if player is out of lives
                if (life == 0){
                    val intent = Intent(context, GameOverActivity::class.java) //34.09
                    intent.putExtra("score", scoreString)
                    context.startActivity(intent)
                    sensorManager.unregisterListener(this)
                    resetTimer()
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
        canvas.drawText("$scoreString", 20F, TEXT_SIZE, textScore)
        handler.postDelayed(runnable, UPDATE_MILLIS)
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
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val motionX = event.values[0] //left/right tilt

            //map tilt
            val moveDino = -motionX / 100 * dWidth //100 = sensitivity of tilt
            val newDinoX = dinoX + moveDino

            //keep within screen bounds
            if (newDinoX <= 0) {
                dinoX = 0f
            } else if (newDinoX >= dWidth - dinoRun.width) {
                dinoX = (dWidth - dinoRun.width).toFloat()
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

    private fun makeTimeString(m: Int, s: Int, ms: Int): String = String.format("%02d:%02d.%02d", m, s, ms/10)

    private fun resetTimer(){
        stopTimer()
        score = 0.0
        scoreString = getTimeStringFromDouble(score)
    }

    private fun startTimer(){
        serviceIntent.putExtra(TimerService.TIME_EXTRA, score)
        context.startService(serviceIntent)
        timerStarted = true
    }

    private fun stopTimer(){
        context.stopService(serviceIntent)
        timerStarted = false
    }
}
