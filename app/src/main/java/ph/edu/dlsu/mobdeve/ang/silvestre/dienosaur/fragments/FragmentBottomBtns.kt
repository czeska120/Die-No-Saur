package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.MainActivity
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.SoundPoolManager
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentBottomBtnsBinding

class FragmentBottomBtns : Fragment() {
    private lateinit var binding: FragmentBottomBtnsBinding
    private lateinit var soundPoolManager: SoundPoolManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBottomBtnsBinding.inflate(layoutInflater)
        soundPoolManager = SoundPoolManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = binding.root
        val buttonClick = AlphaAnimation(1F, 0.8F)

        // Finding IDs
        val homeBtn = binding.btnHome
        val soundBtn = binding.btnSound

        // Listeners
        homeBtn.setOnClickListener {
            homeBtn.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_tick)
            val goToHome = Intent(activity, MainActivity::class.java)
            startActivity(goToHome)
            requireActivity().finishAffinity()
        }

        var tick = 0
        val soundSwitch = ArrayList<Int>()
        soundSwitch.add(R.drawable.btn_sound)
        soundSwitch.add(R.drawable.btn_sound_mute)

        soundBtn.setOnClickListener {
            soundBtn.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_tick)
            if(tick==0){
                soundBtn.setBackgroundResource(soundSwitch[1])
                tick++
            }
            else{
                soundBtn.setBackgroundResource(soundSwitch[0])
                tick--
            }
        }
        // Inflate the layout for this fragment
        return rootView
    }

}