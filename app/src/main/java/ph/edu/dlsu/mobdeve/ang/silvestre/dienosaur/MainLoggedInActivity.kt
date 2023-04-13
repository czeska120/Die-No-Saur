package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.*
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityMainLoggedInBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.BGs
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.Dinos

class MainLoggedInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainLoggedInBinding
    private lateinit var mAuth: FirebaseAuth

    private var SHARED_PREFS = "sharedPrefs"
    private var chosenBG = 0
    private var chosenDino = 0

    private lateinit var soundPoolManager: SoundPoolManager
    private lateinit var audioManager: AudioManager

    private lateinit var serviceIntent: Intent
    private lateinit var service: MusicService
    private lateinit var serviceConn: ServiceConnection
    private var serviceStatus: Int = 0
    private var serviceLevel: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLoggedInBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        soundPoolManager = SoundPoolManager.getInstance(applicationContext)
        serviceIntent =  Intent(applicationContext, MusicService::class.java)
        val buttonClick = AlphaAnimation(1F, 0.8F);

        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Hides action bar (bottom)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        setContentView(binding.root)

        // --- PLAY ---
        binding.mainloggedBtnPlay.setOnClickListener{
            binding.mainloggedBtnPlay.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_button)

            // prevent double clicking
            binding.mainloggedBtnPlay.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                binding.mainloggedBtnPlay.isEnabled = true
            }, 5000) // re-enable after 5 seconds

            val startGame = Intent(this, GameActivity::class.java)
            startActivity(startGame)
        }

        // --- SETTINGS ---
        binding.mainloggedBtnSettings.setOnClickListener {
            binding.mainloggedBtnSettings.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_button)
            val goToSettings = Intent(this,SettingsActivity::class.java)
            startActivity(goToSettings)
        }

        // --- LEADERBOARD ---
        binding.mainloggedBtnLeaderboard.setOnClickListener {
            binding.mainloggedBtnLeaderboard.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_button)
            val goToLeaderboard = Intent(this, LeaderboardActivity::class.java)
            startActivity(goToLeaderboard)
        }

        // --- LOGOUT PROMPT ---
        binding.mainloggedTvLogout.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_text)
            mAuth.signOut()
            val returnMain = Intent(this, MainActivity::class.java)
            startActivity(returnMain)
            finish()
        }
        loadData()
    }
    fun loadData(){
        var sharedPreferences : SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        chosenBG = sharedPreferences.getInt("bgKey", 0)
        chosenDino = sharedPreferences.getInt("dinoKey", 0)
        serviceStatus = sharedPreferences.getInt("isMuted", 0)
        serviceLevel = sharedPreferences.getInt("musicKey", 100)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, serviceLevel, 0)

        binding.bgTop.setImageResource(BGs[chosenBG].top)
        binding.bgBot.setImageResource(BGs[chosenBG].bottom)
        binding.mainloggedDino.setImageResource(Dinos[chosenDino].walk)
    }
    override fun onPause() {
        super.onPause()
        service.muteVolume()
    }
    override fun onResume() {
        super.onResume()
        serviceConn = object : ServiceConnection{
            override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
                val localBinder = iBinder as MusicService.LocalBinder
                service = localBinder.getMusicServiceInstance()
                if(serviceStatus == 0){
                    service.unmuteVolume()
                }else{
                    service.muteVolume()
                }
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
            }
        }
        bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE)
    }
}