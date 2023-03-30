package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.*
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.GameActivity.Companion.game
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.GameActivity.Companion.pauseBtn
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.GameView.Companion.isPaused
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentGamePauseBinding

class FragmentGamePause : Fragment() {
    private lateinit var binding: FragmentGamePauseBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentGamePauseBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val handler = Handler(Looper.getMainLooper())
        val buttonClick = AlphaAnimation(1F, 0.8F);
        mAuth = FirebaseAuth.getInstance()
        binding.btnResume.setOnClickListener {
            binding.btnResume.startAnimation(buttonClick)
            game!!.resume()

            pauseBtn.visibility = View.VISIBLE //show pause button

            // close fragment
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
        }

        binding.btnGameSettings.setOnClickListener{
            binding.btnGameSettings.startAnimation(buttonClick)
            val goToSettings = Intent(requireActivity(), IngameSettings::class.java)
            startActivity(goToSettings)

        }

        binding.btnRetry.setOnClickListener{
            binding.btnRetry.startAnimation(buttonClick)

            // close fragment
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()

//            game!!.quit()
//            game!!.reset()
//            game!!.start()
            val startGame = Intent(requireActivity(), GameActivity::class.java)
            startActivity(startGame)
        }

        binding.btnQuit.setOnClickListener{
            binding.btnQuit.startAnimation(buttonClick)
            game!!.quit()

            val goToHome: Intent
            val currentUser = mAuth.currentUser

            if (currentUser != null){
                 goToHome = Intent(requireActivity(), MainLoggedInActivity::class.java)
            }else{
                 goToHome = Intent(requireActivity(), MainActivity::class.java)
            }
            startActivity(goToHome)
        }

        // Inflate the layout for this fragment
        return binding.root
    }
    private fun loadFragment(frame:Int, fragment: Fragment) {
        // create a FragmentManager
        val fm = requireActivity().supportFragmentManager

        // create a FragmentTransaction to begin the transaction and replace the Fragment
        val fragmentTransaction = fm.beginTransaction()

        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(frame, fragment)
        fragmentTransaction.commit() // save the changes
    }
}