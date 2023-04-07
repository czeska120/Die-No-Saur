package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.SoundPoolManager
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentCreditsBinding

class FragmentCredits : Fragment() {
    private lateinit var binding: FragmentCreditsBinding
    private lateinit var soundPoolManager: SoundPoolManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCreditsBinding.inflate(layoutInflater)
        soundPoolManager = SoundPoolManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = binding.root
        val fragmentManager = requireActivity().supportFragmentManager
        // Finding IDs
        val backBtn = binding.creditsBack

        // Listeners
        backBtn.setOnClickListener{
            soundPoolManager.playSound(R.raw.sfx_tick)
            fragmentManager.beginTransaction().remove(this).commit()
        }
        return rootView
    }
}