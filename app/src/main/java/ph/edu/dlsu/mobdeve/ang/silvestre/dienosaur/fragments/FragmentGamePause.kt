package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var soundPoolManager: SoundPoolManager
    private var SHARED_PREFS = "sharedPrefs"
    private lateinit var serviceIntent: Intent
    private lateinit var service: MusicService
    private lateinit var serviceConn: ServiceConnection
    private var serviceStatus: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentGamePauseBinding.inflate(layoutInflater)
        soundPoolManager = SoundPoolManager.getInstance(requireContext())
        serviceIntent =  Intent(requireActivity(), MusicService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val handler = Handler(Looper.getMainLooper())
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

            /*
            val startGame = Intent(requireActivity(), GameActivity::class.java)
            startActivity(startGame)
            handler.postDelayed({ activity?.finish() }, 1000)
            */
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
        Log.d("TESTING", "GAMEPAUSE SERVICESTATUS  $serviceStatus")
        serviceConn = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
                val localBinder = iBinder as MusicService.LocalBinder
                service = localBinder.getMusicServiceInstance()
                if(serviceStatus == 0){
                    service.unmuteVolume()
                }else{
                    service.muteVolume()
                }
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
            }
        }
        requireActivity().bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE)
        loadData()
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

    fun loadData(){
        var sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS,
            AppCompatActivity.MODE_PRIVATE
        )
        serviceStatus = sharedPreferences.getInt("isMuted", 0)
    }
}