package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.alpha
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityGameBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentGamePause

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(GameView(this))

        val frame = binding.gameFramelayout.id

        binding.btnPause.setOnClickListener{
            binding.btnPause.visibility = View.INVISIBLE //hide pause button
            loadFragment(frame, FragmentGamePause()) //show pause screen
        }
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
}