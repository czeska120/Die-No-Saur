package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.*
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore.Audio
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivitySettingsBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentBottomBtns
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentCredits
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentCustomize
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentHelp
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.BGs

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var audioManager: AudioManager
    private lateinit var soundPoolManager: SoundPoolManager
    private lateinit var serviceIntent: Intent
    private lateinit var service: MusicService
    private lateinit var serviceConn: ServiceConnection
    private var serviceStatus: Int = 0
    private var SHARED_PREFS = "sharedPrefs"
    private var maxVolMusic = 0
    private var curVolMusic = 0
    private var progressMusic = 0
    private var chosenBG = 0
    private var chosenDino = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        soundPoolManager = SoundPoolManager.getInstance(applicationContext)
        serviceIntent =  Intent(this, MusicService::class.java)

        val buttonClick = AlphaAnimation(1F, 0.8F);

        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Hides action bar (bottom)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        setContentView(binding.root)

        val frame1 = R.id.settings_framelayout
        val frame2 = R.id.settings_framelayout2

        binding.settingsCustomize.setOnClickListener{
            soundPoolManager.playSound(R.raw.sfx_text)
            binding.settingsCustomize.startAnimation(buttonClick)
            loadFragment(frame1, FragmentCustomize())
        }
        binding.settingsHelp.setOnClickListener{
            soundPoolManager.playSound(R.raw.sfx_text)
            binding.settingsHelp.startAnimation(buttonClick)
            loadFragment(frame1, FragmentHelp())
        }
        binding.settingsCredits.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_text)
            binding.settingsCredits.startAnimation(buttonClick)
            loadFragment(frame1, FragmentCredits())
        }
        loadFragment(frame2, FragmentBottomBtns())

        binding.settingsSaveBtn.setOnClickListener {
            binding.settingsSaveBtn.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_button)
            saveData()
            val goToHome = Intent(this, MainActivity::class.java)
            startActivity(goToHome)
            finishAffinity()
        }

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        maxVolMusic = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        curVolMusic = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        val musicBtn = binding.seekbarMusic
        musicBtn.max = maxVolMusic
        musicBtn.progress = curVolMusic
        musicBtn.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // intentionally empty; required to override
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                var progress =  seekBar.progress
                Toast.makeText(applicationContext, "Music volume: $progress", Toast.LENGTH_SHORT).show()
            }
        })
        loadData()
    }

    private fun saveData(){
        var sharedPreferences : SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        var sharedPrefEdit = sharedPreferences.edit()
        sharedPrefEdit.putInt("musicKey", binding.seekbarMusic.progress)
        sharedPrefEdit.commit()
        soundPoolManager.setVolume(curVolMusic.toFloat())
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()
    }

    fun loadData(){
        var sharedPreferences : SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        progressMusic = sharedPreferences.getInt("musicKey", curVolMusic)
        chosenBG = sharedPreferences.getInt("bgKey", 0)
        chosenDino = sharedPreferences.getInt("dinoKey", 0)
        serviceStatus = sharedPreferences.getInt("isMuted", 0)

        binding.seekbarMusic.progress = progressMusic
        binding.settingsBg.setImageResource(BGs[chosenBG].dark)
    }
    private fun loadFragment(frame:Int, fragment: Fragment) {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()

        fragmentTransaction.replace(frame, fragment)
        fragmentTransaction.commit()
    }
    override fun onPause() {
        super.onPause()
        service.muteVolume()
    }
    override fun onResume() {
        super.onResume()
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
}