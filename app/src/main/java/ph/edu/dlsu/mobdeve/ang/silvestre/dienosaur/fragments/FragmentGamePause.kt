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
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentGamePauseBinding

class FragmentGamePause : Fragment() {
    private lateinit var binding: FragmentGamePauseBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var soundPoolManager: SoundPoolManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentGamePauseBinding.inflate(layoutInflater)
        soundPoolManager = SoundPoolManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val buttonClick = AlphaAnimation(1F, 0.8F);
        mAuth = FirebaseAuth.getInstance()
        binding.btnResume.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_button)
            binding.btnResume.startAnimation(buttonClick)
            game!!.resume()

            pauseBtn.visibility = View.VISIBLE //show pause button

            // close fragment
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
        }

        binding.btnGameSettings.setOnClickListener{
            soundPoolManager.playSound(R.raw.sfx_button)
            binding.btnGameSettings.startAnimation(buttonClick)
            val goToSettings = Intent(requireActivity(), IngameSettings::class.java)
            startActivity(goToSettings)

        }

        binding.btnRetry.setOnClickListener{
            soundPoolManager.playSound(R.raw.sfx_button)
            binding.btnRetry.startAnimation(buttonClick)

            // close fragment
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()

            game!!.reset()
            game!!.start()

            pauseBtn.visibility = View.VISIBLE //show pause button
        }

        binding.btnQuit.setOnClickListener{
            soundPoolManager.playSound(R.raw.sfx_button)
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
            requireActivity().finishAffinity()
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}