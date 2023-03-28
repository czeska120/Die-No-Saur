package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityGameOverBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentBottomBtns

class GameOverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameOverBinding
    private lateinit var user: FirebaseUser
    private lateinit var dbreference: DatabaseReference
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        val frame2 = R.id.settings_framelayout2
        loadFragment(frame2, FragmentBottomBtns())

        val data = intent.extras
        val score = data?.getString("score")
        binding.playerScore.text = score

        val currentUser = mAuth.currentUser
        dbreference = FirebaseDatabase.getInstance().reference

//        if(user != null){
        if (currentUser != null) {
            dbreference.child("Score").child(currentUser.uid).child("result").setValue(score)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Score added", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
//        }


        binding.btnPlayAgain.setOnClickListener{
            val startGame = Intent(this, GameActivity::class.java)
            startActivity(startGame)
            finish()
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