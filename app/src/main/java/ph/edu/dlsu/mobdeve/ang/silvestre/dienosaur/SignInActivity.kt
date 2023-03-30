package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivitySignInBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentBottomBtns

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()

        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //Hides action bar (bottom)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        setContentView(binding.root)

        binding.signinBtnRegister.setOnClickListener {
            val email = binding.signinEtEmail.text.toString()
            val password = binding.signinEtPassword.text.toString()
            val confirmpass = binding.signinEtConfirmpass.text.toString()

            if(password == confirmpass){
                createUser(email, password)
            }
            else{
                Toast.makeText(applicationContext,"Passwords do not match.", Toast.LENGTH_SHORT).show()
            }

        }
        binding.signinTvLoginPrompt.setOnClickListener {
            val logIn = Intent(this, LoginActivity::class.java)
            startActivity(logIn)
            finish()
        }

        val frame = R.id.signin_framelayout
        loadFragment(frame, FragmentBottomBtns())
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
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext,"Registration error: "+ task.exception!!.message, Toast.LENGTH_SHORT).show()
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
}