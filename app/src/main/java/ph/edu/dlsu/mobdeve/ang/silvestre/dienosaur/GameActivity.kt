package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.GameView.Companion.isPaused
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityGameBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentGamePause

class GameActivity : AppCompatActivity() {
    companion object{
        lateinit var pauseBtn: AppCompatImageButton
        var game: GameView? = null
    }

    private lateinit var parent: ViewGroup
    private lateinit var binding: ActivityGameBinding
    private lateinit var mAuth: FirebaseAuth

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

    override fun onBackPressed() {
        val currentUser = mAuth.currentUser
        val goBack: Intent

        if (currentUser != null) {
            goBack = Intent(this, MainLoggedInActivity::class.java )

        }else{
            goBack = Intent(this, MainActivity::class.java )
        }
        startActivity(goBack)
    }
}