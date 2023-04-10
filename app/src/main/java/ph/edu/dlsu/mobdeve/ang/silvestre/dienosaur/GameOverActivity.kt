package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
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
    private lateinit var dbreference: DatabaseReference
    private lateinit var mAuth : FirebaseAuth
    private var SHARED_PREFS = "sharedPrefs"
    private var chosenBG = 0
    private var chosenDino = 0
    private lateinit var soundPoolManager: SoundPoolManager
    private lateinit var serviceIntent: Intent
    private lateinit var service: MusicService
    private lateinit var serviceConn: ServiceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        soundPoolManager = SoundPoolManager.getInstance(applicationContext)
        serviceIntent =  Intent(this, MusicService::class.java)
        setContentView(binding.root)

        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Hides action bar (bottom)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        val frame2 = R.id.settings_framelayout2
        loadFragment(frame2, FragmentBottomBtns())

        val data = intent.extras
        val score = data?.getString("score")
        binding.playerScore.text = score

        val handler = Handler(Looper.getMainLooper())

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
            soundPoolManager.playSound(R.raw.sfx_button)
            val startGame = Intent(this, GameActivity::class.java)
            startActivity(startGame)
            handler.postDelayed({ finish() }, 1000)
        }

        binding.btnLeaderboard.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_button)
            val goToLeaderboard = Intent(this, LeaderboardActivity::class.java)
            startActivity(goToLeaderboard)
        }
        loadData()
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

    fun loadData(){
        var sharedPreferences : SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        chosenBG = sharedPreferences.getInt("bgKey", 0)
        chosenDino = sharedPreferences.getInt("dinoKey", 0)
        binding.gameOver.background = ContextCompat.getDrawable(applicationContext, BGs[chosenBG].dark)
        binding.dinoDead.setImageResource(Dinos[chosenDino].dead)
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