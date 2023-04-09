package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.*
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentBottomBtnsBinding

class FragmentBottomBtns : Fragment() {
    private lateinit var binding: FragmentBottomBtnsBinding
    private lateinit var soundPoolManager: SoundPoolManager
    private lateinit var serviceIntent: Intent
    private lateinit var service: MusicService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBottomBtnsBinding.inflate(layoutInflater)
        soundPoolManager = SoundPoolManager.getInstance(requireContext())
        serviceIntent =  Intent(requireContext(), MusicService::class.java)

        val serviceConn = object : ServiceConnection{
            override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
                val localBinder = iBinder as MusicService.LocalBinder
                service = localBinder.getMusicServiceInstance()
                service.test()
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                TODO("Not yet implemented")
            }
        }
        requireActivity().bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE)
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
                service.muteVolume()
            }
            else{
                soundBtn.setBackgroundResource(soundSwitch[0])
                tick--
                service.unmuteVolume()
            }
        }
        // Inflate the layout for this fragment
        return rootView
    }

}