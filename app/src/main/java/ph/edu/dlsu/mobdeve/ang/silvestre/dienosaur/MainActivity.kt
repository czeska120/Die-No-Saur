package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.*
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import com.google.firebase.auth.FirebaseAuth
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityMainBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.BGs
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.Dinos

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth : FirebaseAuth

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        soundPoolManager = SoundPoolManager.getInstance(applicationContext)

        // Adding button click animation
        val buttonClick = AlphaAnimation(1F, 0.8F)

        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Hides action bar (bottom)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        setContentView(binding.root)


        // --- PLAY AS GUEST ---
        binding.homeBtnGuest.setOnClickListener{
            binding.homeBtnGuest.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_button)
            val startGame = Intent(this, GameActivity::class.java)
            startActivity(startGame)
        }

        // --- SIGN-IN ---
        binding.homeBtnSignin.setOnClickListener {
            binding.homeBtnSignin.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_button)
            val signIn = Intent(this, SignInActivity::class.java)
            startActivity(signIn)
        }

        // --- SETTINGS ---
        binding.homeBtnSettings.setOnClickListener {
            binding.homeBtnSettings.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_button)
            val goToSettings = Intent(this,SettingsActivity::class.java)
            startActivity(goToSettings)
        }

        // --- LEADERBOARD ---
        binding.homeBtnLeaderboard.setOnClickListener {
            binding.homeBtnLeaderboard.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_button)
            val goToLeaderboard = Intent(this, LeaderboardActivity::class.java)
            startActivity(goToLeaderboard)
        }

        // --- LOGIN PROMPT ---
        binding.homeTvLoginPrompt.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_text)
            val logIn = Intent(this, LoginActivity::class.java)
            startActivity(logIn)
        }
        serviceIntent =  Intent(applicationContext, MusicService::class.java)

        serviceConn = object : ServiceConnection{
            override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
                val localBinder = iBinder as MusicService.LocalBinder
                service = localBinder.getMusicServiceInstance()
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
            }
        }
        bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE)
        startService(serviceIntent)
        loadData()
    }

    public override fun onResume() {
        super.onResume()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            startActivity(Intent(this,MainLoggedInActivity::class.java))
            this.overridePendingTransition(0, 0)
            finish()
        }
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
        binding.homeDino.setImageResource(Dinos[chosenDino].walk)
    }
    override fun onPause() {
        super.onPause()
        if(serviceStatus == 0){
            service.unmuteVolume()
        }else{
            service.muteVolume()
        }
//        unbindService(serviceConn)
    }
}