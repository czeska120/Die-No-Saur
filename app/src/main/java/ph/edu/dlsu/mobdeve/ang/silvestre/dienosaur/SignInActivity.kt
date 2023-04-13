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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivitySignInBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentBottomBtns
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.BGs
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.Dinos

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
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
        binding = ActivitySignInBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        soundPoolManager = SoundPoolManager.getInstance(applicationContext)
        serviceIntent =  Intent(applicationContext, MusicService::class.java)

        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Hides action bar (bottom)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        setContentView(binding.root)

        binding.signinBtnRegister.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_button)
            val email = binding.signinEtEmail.text.toString()
            val password = binding.signinEtPassword.text.toString()
            val confirmpass = binding.signinEtConfirmpass.text.toString()

            // prevent double clicking
            binding.signinBtnRegister.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                binding.signinBtnRegister.isEnabled = true
            }, 5000) // re-enable after 5 seconds

            if(password == confirmpass){
                createUser(email, password)
            }
            else{
                Toast.makeText(applicationContext,"Passwords do not match.", Toast.LENGTH_SHORT).show()
            }

        }
        binding.signinTvLoginPrompt.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_text)
            val logIn = Intent(this, LoginActivity::class.java)
            startActivity(logIn)
            finish()
        }

        val frame = R.id.signin_framelayout
        loadFragment(frame, FragmentBottomBtns())
        loadData()
    }

    private fun createUser(email: String, password: String){
        if(TextUtils.isEmpty(email)){
            binding.signinEtEmail.setError("Email cannot be empty")
            binding.signinEtEmail.requestFocus()
        }else if(TextUtils.isEmpty(password)){
            binding.signinEtPassword.setError("Password cannot be empty")
            binding.signinEtPassword.requestFocus()
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(applicationContext,"User registered!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainLoggedInActivity::class.java))
                    finishAffinity()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext,"Registration error: "+ task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadFragment(frame:Int, fragment: Fragment) {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()

        fragmentTransaction.replace(frame, fragment)
        fragmentTransaction.commit()
    }
    fun loadData(){
        var sharedPreferences : SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        chosenBG = sharedPreferences.getInt("bgKey", 0)
        serviceStatus = sharedPreferences.getInt("isMuted", 0)

        binding.signinBg.setImageResource(BGs[chosenBG].dark)
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