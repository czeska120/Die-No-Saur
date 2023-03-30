package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityGameOverBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentBottomBtns
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.BGs
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.Dinos
import java.text.SimpleDateFormat
import java.util.*

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

        val handler = Handler(Looper.getMainLooper())

        // customize based on user preferences
        //binding.gameOver.background = ContextCompat.getDrawable(applicationContext, BGs[1].dark)
        //binding.dinoDead.setImageResource(Dinos[1].dead)

        val currentUser = mAuth.currentUser
        dbreference = FirebaseDatabase.getInstance().reference

        if (currentUser != null) {
            val username = currentUser?.email.toString()
            val index = username.indexOf('@')
            val name = username.substring(0,index)


            val check = dbreference.child("Scores").child(currentUser.uid)
            check.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.hasChild("score")){
                        //compare existing score
                        val oldScoreString = dataSnapshot.child("score").value.toString()
                        val newScoreString = score
                        val finalString = compare(oldScoreString, newScoreString!!)

                        dbreference.child("Scores").child(currentUser.uid).child("name").setValue(name)
                        dbreference.child("Scores").child(currentUser.uid).child("score").setValue(finalString)
                    }
                    else{
                        dbreference.child("Scores").child(currentUser.uid).child("name").setValue(name)
                        dbreference.child("Scores").child(currentUser.uid).child("score").setValue(score)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors
                }
            })

        }

        binding.btnPlayAgain.setOnClickListener{
            val startGame = Intent(this, GameActivity::class.java)
            startActivity(startGame)
            handler.postDelayed({ finish() }, 1000)
        }

        binding.btnLeaderboard.setOnClickListener {
            val goToLeaderboard = Intent(this, LeaderboardActivity::class.java)
            startActivity(goToLeaderboard)
            finish()
        }
    }

    private fun compare(oldScore: String, newScore: String): String{
        val sdf = SimpleDateFormat("mm:ss.SS")
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val time1 = sdf.parse(oldScore).time
        val time2 = sdf.parse(newScore).time

        val longerTime = if (time1 > time2) time1 else time2

        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.timeInMillis = longerTime

        val resultString = sdf.format(cal.time)

        return resultString
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