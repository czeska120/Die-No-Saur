package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.GameActivity
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.GameActivity.Companion.game
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.GameActivity.Companion.pauseBtn
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.GameView
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.GameView.Companion.isPaused
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentGamePauseBinding

class FragmentGamePause : Fragment() {
    private lateinit var binding: FragmentGamePauseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentGamePauseBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.btnResume.setOnClickListener {
            game!!.resume()

            pauseBtn.visibility = View.VISIBLE //show pause button

            // close fragment
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}