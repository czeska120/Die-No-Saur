package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityLoginBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentBottomBtns
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.BGs

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private var SHARED_PREFS = "sharedPrefs"
    private var chosenBG = 0
    private lateinit var soundPoolManager: SoundPoolManager
    private lateinit var serviceIntent: Intent
    private lateinit var service: MusicService
    private lateinit var serviceConn: ServiceConnection
    private var serviceStatus: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        soundPoolManager = SoundPoolManager.getInstance(applicationContext)
        serviceIntent =  Intent(applicationContext, MusicService::class.java)
        val buttonClick = AlphaAnimation(1F, 0.8F)

        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Hides action bar (bottom)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        setContentView(binding.root)

        binding.loginBtnLogin.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_button)
            binding.loginBtnLogin.startAnimation(buttonClick)

            // prevent double clicking
            binding.loginBtnLogin.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                binding.loginBtnLogin.isEnabled = true
            }, 5000) // re-enable after 5 seconds

            loginUser()
        }
        binding.loginTvSigninPrompt.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_text)
            val signin = Intent(this, SignInActivity::class.java)
            startActivity(signin)
            finish()
        }

        val frame = R.id.login_framelayout
        loadFragment(frame, FragmentBottomBtns())
        loadData()
    }

    private fun loginUser(){
        val email = binding.loginEtEmail.text.toString()
        val password = binding.loginEtPassword.text.toString()

        if(TextUtils.isEmpty(email)){
            binding.loginEtEmail.setError("Email cannot be empty")
            binding.loginEtEmail.requestFocus()
        }else if(TextUtils.isEmpty(password)){
            binding.loginEtPassword.setError("Password cannot be empty")
            binding.loginEtPassword.requestFocus()
        }else{
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(applicationContext,"User logged in!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainLoggedInActivity::class.java))
                        finishAffinity()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(applicationContext,"Login error: "+ task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
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
        serviceStatus = sharedPreferences.getInt("isMuted", 0)

        binding.loginBg.setImageResource(BGs[chosenBG].dark)
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