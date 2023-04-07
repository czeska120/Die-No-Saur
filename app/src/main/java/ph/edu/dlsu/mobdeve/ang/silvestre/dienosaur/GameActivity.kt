package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityGameBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentGamePause
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.BGs
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.Dinos

class GameActivity : AppCompatActivity() {
    companion object{
        lateinit var pauseBtn: AppCompatImageButton
        var game: GameView? = null
    }

    private lateinit var parent: ViewGroup
    private lateinit var binding: ActivityGameBinding
    private lateinit var mAuth: FirebaseAuth
    private var SHARED_PREFS = "sharedPrefs"
    private var chosenBG = 0
    private var chosenDino = 0
    private lateinit var soundPoolManager: SoundPoolManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        //setContentView(GameView(this))

        parent = binding.gameParent
        game = parent.getChildAt(0) as GameView
        game!!.reset()

        val frame = binding.gameFramelayout.id

        pauseBtn = binding.btnPause

        pauseBtn.setOnClickListener{
            soundPoolManager.playSound(R.raw.sfx_tick)
            game!!.pause()

            pauseBtn.visibility = View.INVISIBLE //hide pause button
            loadFragment(frame, FragmentGamePause()) //show pause screen
        }
    }

    override fun onPause(){
        super.onPause()
        game!!.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        game!!.quit()
    }

    private fun loadFragment(frame:Int, fragment: Fragment) {
        // create a FragmentManager
        val fm = supportFragmentManager

        // create a FragmentTransaction to begin the transaction and replace the Fragment
        val fragmentTransaction = fm.beginTransaction()

        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(frame, fragment)
        fragmentTransaction.commit() // save the changes
    }

    fun loadData(){
        var sharedPreferences : SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        chosenBG = sharedPreferences.getInt("bgKey", 0)
        chosenDino = sharedPreferences.getInt("dinoKey", 0)

//        binding.bgTop.setImageResource(BGs[chosenBG].top)
//        binding.bgBot.setImageResource(BGs[chosenBG].bottom)
//        binding.homeDino.setImageResource(Dinos[chosenDino].walk)
    }

    /*override fun onBackPressed() {
        val currentUser = mAuth.currentUser
        val goBack: Intent

        if (currentUser != null) {
            goBack = Intent(this, MainLoggedInActivity::class.java )

        }else{
            goBack = Intent(this, MainActivity::class.java )
        }
        startActivity(goBack)
    }*/
}