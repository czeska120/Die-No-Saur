package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var serviceIntent: Intent
    private var SHARED_PREFS = "sharedPrefs"
    private var chosenBG = 0
    private var chosenDino = 0
    private lateinit var soundPoolManager: SoundPoolManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        soundPoolManager = SoundPoolManager.getInstance(applicationContext)

        // Adding button click animation
        val buttonClick = AlphaAnimation(1F, 0.8F);
        // Hides title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //Hides action bar (bottom)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

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
        startService(serviceIntent)
        loadData()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            startActivity(Intent(this,MainLoggedInActivity::class.java))
            this.overridePendingTransition(0, 0)
            finish()
        }
    }
    fun loadData(){
        var sharedPreferences : SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        chosenBG = sharedPreferences.getInt("bgKey", 0)
        chosenDino = sharedPreferences.getInt("dinoKey", 0)

        binding.bgTop.setImageResource(BGs[chosenBG].top)
        binding.bgBot.setImageResource(BGs[chosenBG].bottom)
        binding.homeDino.setImageResource(Dinos[chosenDino].walk)
    }
}