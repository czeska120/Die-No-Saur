package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityGameBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentGamePause
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.BGs
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.Dinos

class GameActivity : AppCompatActivity() {
    companion object{
        lateinit var pauseBtn: AppCompatImageButton
        var game: GameView? = null
    }
    private lateinit var serviceIntent: Intent
    private lateinit var service: MusicService
    private lateinit var serviceConn: ServiceConnection
    private var serviceStatus: Int = 0

    private lateinit var parent: ViewGroup
    private lateinit var binding: ActivityGameBinding
    private var frame: Int = 0

    private lateinit var mAuth: FirebaseAuth
    private var SHARED_PREFS = "sharedPrefs"
    private var chosenBG = 0
    private var chosenDino = 0

    private lateinit var soundPoolManager: SoundPoolManager

    private var isBackPressed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)

        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Prevent screen from sleep
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        soundPoolManager = SoundPoolManager.getInstance(applicationContext)
        serviceIntent =  Intent(this, MusicService::class.java)
        //setContentView(GameView(this))

        parent = binding.gameParent
        game = parent.getChildAt(0) as GameView
        loadData()
        game!!.reset()

        frame = binding.gameFramelayout.id

        pauseBtn = binding.btnPause

        pauseBtn.setOnClickListener{
            soundPoolManager.playSound(R.raw.sfx_tick)
            game!!.pause()

            pauseBtn.visibility = View.INVISIBLE //hide pause button
            loadFragment(frame, FragmentGamePause()) //show pause screen
        }

        // catch when activity is finished due to back pressed
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isBackPressed = true
                remove()
                onBackPressedDispatcher.onBackPressed()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    // activity removed from foreground
    override fun onPause(){
        super.onPause()
        game!!.pause()
        service.muteVolume()

        //if game is not over and back is not pressed = show pause screen
        if(!game!!.getOverBool() && !isBackPressed){
            pauseBtn.visibility = View.INVISIBLE //hide pause button
            loadFragment(frame, FragmentGamePause()) //show pause screen
        }
        isBackPressed = false //reset boolean
    }

    // activity returned to foreground
    override fun onResume() {
        super.onResume()

        // if game has already started only will this run
        if(game!!.handler != null){
            if(!game!!.getPauseBool()){
                game!!.resume()
            }else if(game!!.getPauseBool()){
                game!!.pause()
            }
        }

        serviceConn = object : ServiceConnection{
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
        bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE)
    }

    // activity finished
    override fun onDestroy() {
        super.onDestroy()
        game!!.quit()
    }

    private fun loadFragment(frame:Int, fragment: Fragment) {
        // create a FragmentManager
        val fm = supportFragmentManager

        // create a FragmentTransaction to begin the transaction and replace the Fragment
        val fragmentTransaction = fm.beginTransaction()

        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(frame, fragment)
        fragmentTransaction.commit() // save the changes
    }

    fun loadData(){
        var sharedPreferences : SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        chosenBG = sharedPreferences.getInt("bgKey", 0)
        chosenDino = sharedPreferences.getInt("dinoKey", 0)
        serviceStatus = sharedPreferences.getInt("isMuted", 0)

        game!!.setCustom(chosenBG, chosenDino)
    }
}