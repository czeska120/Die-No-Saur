package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.content.Intent
import android.os.Bundle
import android.view.FrameMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.GameActivity
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.MainActivity
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.SoundPoolManager
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentIngameBottomBinding

class FragmentIngameBottom : Fragment() {
    private lateinit var binding: FragmentIngameBottomBinding
    private lateinit var soundPoolManager: SoundPoolManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentIngameBottomBinding.inflate(layoutInflater)
        soundPoolManager = SoundPoolManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = binding.root
        val buttonClick = AlphaAnimation(1F, 0.8F)

        // Finding IDs
        val backBtn = binding.btnBack
        val soundBtn = binding.btnSound

        // Listeners
        backBtn.setOnClickListener{
            soundPoolManager.playSound(R.raw.sfx_tick)
            backBtn.startAnimation(buttonClick)
            requireActivity().finish()
        }

        var tick = 0
        val soundSwitch = ArrayList<Int>()
        soundSwitch.add(R.drawable.btn_sound)
        soundSwitch.add(R.drawable.btn_sound_mute)

        soundBtn.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_tick)
            soundBtn.startAnimation(buttonClick)
            if(tick==0){
                soundBtn.setBackgroundResource(soundSwitch[1])
                tick++
            }
            else{
                soundBtn.setBackgroundResource(soundSwitch[0])
                tick--
            }
        }
        return rootView
    }
}