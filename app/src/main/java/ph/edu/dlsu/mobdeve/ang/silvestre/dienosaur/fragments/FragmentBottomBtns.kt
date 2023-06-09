package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.*
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentBottomBtnsBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.BGs

class FragmentBottomBtns : Fragment() {
    private lateinit var binding: FragmentBottomBtnsBinding
    private lateinit var soundPoolManager: SoundPoolManager
    private lateinit var serviceIntent: Intent
    private lateinit var service: MusicService
    private var musicLevel: Int = 0
    private var SHARED_PREFS = "sharedPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBottomBtnsBinding.inflate(layoutInflater)
        soundPoolManager = SoundPoolManager.getInstance(requireContext())
        serviceIntent =  Intent(requireContext(), MusicService::class.java)


        val serviceConn = object : ServiceConnection{
            override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
                val localBinder = iBinder as MusicService.LocalBinder
                service = localBinder.getMusicServiceInstance()
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

        var sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS,
            AppCompatActivity.MODE_PRIVATE
        )
        var sharedPrefEdit = sharedPreferences.edit()
        var savedState = sharedPreferences.getInt("isMuted", 0)

        Log.d("TESTING", "$musicLevel")
        if(savedState==0){
            soundBtn.setBackgroundResource(soundSwitch[0])
        }
        else if ( savedState == 1 || musicLevel == 0){
            soundBtn.setBackgroundResource(soundSwitch[1])
        }


        soundBtn.setOnClickListener {
            soundBtn.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_tick)
            if(savedState==0){ // if unmuted
                savedState = 1
                soundBtn.setBackgroundResource(soundSwitch[1]) // to mute
                tick++
                service.muteVolume()
                sharedPrefEdit.putInt("isMuted", 1)
//                Log.d("TESTING", "UNMUTE CLICKED, $savedState")

            }
            else{ // if muted
                savedState = 0
                soundBtn.setBackgroundResource(soundSwitch[0]) // to unmute
                tick--
                service.unmuteVolume()
                sharedPrefEdit.putInt("isMuted", 0)
                Log.d("TESTING", "BOTTOM BTNS MUTE CLICKED  $savedState")

            }
            sharedPrefEdit.apply()
        }
        loadData()
        // Inflate the layout for this fragment
        return rootView
    }
    fun loadData(){
        var sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS,
            AppCompatActivity.MODE_PRIVATE
        )
        musicLevel = sharedPreferences.getInt("musicKey", 100)
        Log.d("TESTING", "MUSIC LEVEL: $musicLevel")
    }

}