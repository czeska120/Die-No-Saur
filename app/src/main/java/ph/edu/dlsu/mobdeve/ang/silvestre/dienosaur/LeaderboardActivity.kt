package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityLeaderboardBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentBottomBtns
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentHelp
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.User
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding
    private lateinit var rv: RecyclerView
    private lateinit var adapter: LeaderboardAdapter
    private lateinit var list: ArrayList<User>
    private lateinit var dbreference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)

        // Hides title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //Hides action bar (bottom)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        setContentView(binding.root)
        rv = findViewById(R.id.leaderboard_recyclerview)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        list = ArrayList<User>()
        adapter = LeaderboardAdapter(this, list)
        rv.adapter = adapter

        dbreference = FirebaseDatabase.getInstance().getReference("Scores")

        dbreference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(dataSnapshot in snapshot.children){
                    val user: User = dataSnapshot.getValue(User::class.java)!!
                    list.add(user)
                }

                Collections.sort(list, object : Comparator<User> {
                    override fun compare(o1: User, o2: User): Int {
                        return o2.score.compareTo(o1.score)
                    }
                })

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors
            }
        })




        val bottomFrame = R.id.leaderboardframelayout
        loadFragment(bottomFrame, FragmentBottomBtns())
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