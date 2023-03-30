package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        val buttonClick = AlphaAnimation(1F, 0.8F);
        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Hides action bar (bottom)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(binding.root)

        val frame1 = R.id.settings_framelayout
        val frame2 = R.id.settings_framelayout2

        binding.settingsCustomize.setOnClickListener{
            binding.settingsCustomize.startAnimation(buttonClick)
            loadFragment(frame1, FragmentCustomize())
        }
        binding.settingsHelp.setOnClickListener{
            binding.settingsHelp.startAnimation(buttonClick)
            loadFragment(frame1, FragmentHelp())
        }
        binding.settingsCredits.setOnClickListener {
            binding.settingsCredits.startAnimation(buttonClick)
            loadFragment(frame1, FragmentCredits())
        }
        loadFragment(frame2, FragmentBottomBtns())

        binding.settingsSaveBtn.setOnClickListener {
            binding.settingsSaveBtn.startAnimation(buttonClick)
            val goToHome = Intent(this, MainActivity::class.java)
            startActivity(goToHome)
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
}