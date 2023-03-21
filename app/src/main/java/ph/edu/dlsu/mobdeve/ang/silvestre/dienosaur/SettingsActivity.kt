package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivitySettingsBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentCredits
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentCustomize
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentHelp

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Hides action bar (bottom)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(binding.root)

        binding.settingsBack.setOnClickListener {
            finish()
        }

        binding.settingsCustomize.setOnClickListener{
            loadFragment(FragmentCustomize())
        }
        binding.settingsHelp.setOnClickListener{
            loadFragment(FragmentHelp())
        }
        binding.settingsCredits.setOnClickListener {
            loadFragment(FragmentCredits())
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



    private fun loadFragment(fragment: Fragment) {
        // create a FragmentManager
        val fm = supportFragmentManager

        // create a FragmentTransaction to begin the transaction and replace the Fragment
        val fragmentTransaction = fm.beginTransaction()

        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.settings_framelayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit() // save the changes

    }
}