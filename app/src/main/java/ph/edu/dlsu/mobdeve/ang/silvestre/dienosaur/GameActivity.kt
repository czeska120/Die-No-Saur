package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class GameActivity : AppCompatActivity() {
    //private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(GameView(this))
    }
}