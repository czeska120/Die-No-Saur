package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityGameOverBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentBottomBtns

class GameOverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val frame2 = R.id.settings_framelayout2
        loadFragment(frame2, FragmentBottomBtns())

        val data = intent.extras
        val score = data?.getInt("score")
        binding.playerScore.text = score.toString()

        binding.btnPlayAgain.setOnClickListener{
            val startGame = Intent(this, GameActivity::class.java)
            startActivity(startGame)
        }
    }

    private fun loadFragment(frame:Int, fragment: Fragment) {
        // create a FragmentManager
        val fm = supportFragmentManager

        // create a FragmentTransaction to begin the transaction and replace the Fragment
        val fragmentTransaction = fm.beginTransaction()

        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(frame, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit() // save the changes

    }
}