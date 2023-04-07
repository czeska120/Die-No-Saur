package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        soundPoolManager = SoundPoolManager.getInstance(applicationContext)
        val buttonClick = AlphaAnimation(1F, 0.8F)

        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //Hides action bar (bottom)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        setContentView(binding.root)

        binding.loginBtnLogin.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_confirm)
            binding.loginBtnLogin.startAnimation(buttonClick)
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

        binding.loginBg.setImageResource(BGs[chosenBG].dark)
    }
}