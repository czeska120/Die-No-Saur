package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityIngameSettingsBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.*

class IngameSettings : AppCompatActivity() {
    private lateinit var binding: ActivityIngameSettingsBinding
    private lateinit var soundPoolManager: SoundPoolManager
    private lateinit var serviceIntent: Intent
    private lateinit var service: MusicService
    private lateinit var serviceConn: ServiceConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngameSettingsBinding.inflate(layoutInflater)
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
        binding.settingsHelp.setOnClickListener{
            binding.settingsHelp.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_text)
            loadFragment(frame1, FragmentHelp())
        }
        binding.settingsCredits.setOnClickListener {
            binding.settingsCredits.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_text)
            loadFragment(frame1, FragmentCredits())
        }
        loadFragment(frame2, FragmentIngameBottom())

        binding.settingsSaveBtn.setOnClickListener {
            binding.settingsSaveBtn.startAnimation(buttonClick)
            soundPoolManager.playSound(R.raw.sfx_button)
            finish()
        }
        binding.seekbarSoundfx.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // intentionally empty; required to override
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // intentionally empty; required to override
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val progress = seekBar.progress
                Toast.makeText(applicationContext, "Sound FX volume: $progress", Toast.LENGTH_SHORT).show()
            }
        })

        binding.seekbarMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // intentionally empty; required to override
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // intentionally empty; required to override
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val progress = seekBar.progress
                Toast.makeText(applicationContext, "Music volume: $progress", Toast.LENGTH_SHORT).show()
            }
        })
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
                service.unmuteVolume()
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
            }
        }
        bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE)
    }
}