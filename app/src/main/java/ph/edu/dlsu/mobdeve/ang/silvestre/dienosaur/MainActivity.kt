package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Adding button click animation
        val buttonClick = AlphaAnimation(1F, 0.8F);
        // Hides title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //Hides action bar (bottom)
         getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(binding.root)

        // --- PLAY (Button 1) ---
        binding.homeBtn1.setOnClickListener{
            val startGame = Intent(this, GameActivity::class.java)
            startActivity(startGame)
        }

        // --- SETTINGS (Button 2) ---
        binding.homeBtn2.setOnClickListener {

            binding.homeBtn2.startAnimation(buttonClick)

            val goToSettings = Intent(this,SettingsActivity::class.java)
            startActivity(goToSettings)
        }

        // --- LEADERBOARD (Button 3) ---
    }


}