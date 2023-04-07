package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLoggedInBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        soundPoolManager = SoundPoolManager.getInstance(applicationContext)
        val buttonClick = AlphaAnimation(1F, 0.8F);

        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //Hides action bar (bottom)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        setContentView(binding.root)

        // --- PLAY ---
        binding.mainloggedBtnPlay.setOnClickListener{
            binding.mainloggedBtnPlay.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_button)
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

        binding.bgTop.setImageResource(BGs[chosenBG].top)
        binding.bgBot.setImageResource(BGs[chosenBG].bottom)
        binding.mainloggedDino.setImageResource(Dinos[chosenDino].walk)
    }
}