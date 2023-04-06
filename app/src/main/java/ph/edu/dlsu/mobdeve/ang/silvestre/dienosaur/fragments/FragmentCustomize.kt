package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.SettingsActivity
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.TextOutline
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivitySettingsBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentCustomizeBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.BGs
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.Dinos

class FragmentCustomize : Fragment() {
    private lateinit var binding: FragmentCustomizeBinding
    private var SHARED_PREFS = "sharedPrefs"
    private var bgKey = "bgKey"
    private var dinoKey = "dinoKey"
    private var chosenBG = 0
    private var chosenDino = 0
    private var i = 0
    private var j = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCustomizeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = binding.root
        val fragmentManager = requireActivity().supportFragmentManager

        val buttonClick = AlphaAnimation(1F, 0.8F)

        // Plugging data
        val characterArray = ArrayList<Int>()
        var bgArray = arrayOf("FALL","SPRING","WINTER")
        var bgImgArray = ArrayList<Int>()

        characterArray.add(Dinos[0].walk)
        characterArray.add(Dinos[1].walk)
        characterArray.add(Dinos[2].walk)
        characterArray.add(Dinos[3].walk)
        characterArray.add(Dinos[4].walk)
        characterArray.add(Dinos[5].walk)

        bgImgArray.add(BGs[0].dark)
        bgImgArray.add(BGs[1].dark)
        bgImgArray.add(BGs[2].dark)


        // Finding IDs
        val backBtn = binding.customizeBack
        val saveBtn = binding.customizeSave
        val charaPrev = binding.characterPrev
        val charaNext = binding.characterNext
        val dino = binding.customizeDino
        val bgPrev = binding.bgPrev
        val bgNext = binding.bgNext
        val bgText = binding.customizeBgbody
        val bgImg = binding.customizeBg

        // Listeners
        charaPrev.setOnClickListener {

            charaPrev.startAnimation(buttonClick)
            i--
            if(i >= 0) {
                dino.setImageResource(characterArray[i])
            }
            else{
                i = 5
                dino.setImageResource(characterArray[i])
            }
            Toast.makeText(requireActivity(), "Sa letrang I: $i", Toast.LENGTH_SHORT).show()
            chosenDino = i
        }
        charaNext.setOnClickListener {
            charaNext.startAnimation(buttonClick)
            i++
            if(i < 6) {
                dino.setImageResource(characterArray[i])
            }
            else{
                i = 0
                dino.setImageResource(characterArray[i])
            }
            Toast.makeText(requireActivity(), "Sa letrang I: $i", Toast.LENGTH_SHORT).show()
            chosenDino = i
        }

        bgPrev.setOnClickListener {
            bgPrev.startAnimation(buttonClick)
            j--
            if(j >= 0) {
                bgText.text = bgArray[j]
                bgImg.setImageResource(bgImgArray[j])
            }
            else{
                j = 2
                bgText.text = bgArray[j]
                bgImg.setImageResource(bgImgArray[j])
            }
            Toast.makeText(requireActivity(), "Sa letrang J: $j", Toast.LENGTH_SHORT).show()
            chosenBG = j
        }
        bgNext.setOnClickListener {
            bgNext.startAnimation(buttonClick)
            j++
            if(j < 3) {
                bgText.text = bgArray[j]
                bgImg.setImageResource(bgImgArray[j])
            }
            else{
                j = 0
                bgText.text = bgArray[j]
                bgImg.setImageResource(bgImgArray[j])
            }
            Toast.makeText(requireActivity(), "Sa letrang J: $j", Toast.LENGTH_SHORT).show()
            chosenBG = j
        }
        Log.d("TESTING","BEFORE SAVE $chosenDino, $i")
        Log.d("TESTING","BEFORE SAVE $chosenBG, $j")

        backBtn.setOnClickListener{
            fragmentManager.beginTransaction().remove(this).commit()
        }

        saveBtn.setOnClickListener{
            saveBtn.startAnimation(buttonClick)
            Log.d("TESTING","INSIDE SAVE BTN $chosenDino, $chosenBG")
            saveData()
        }
        // Inflate the layout for this fragment
        return rootView
    }

    fun saveData(){
        var sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, AppCompatActivity.MODE_MULTI_PROCESS)
        var sharedPrefEdit = sharedPreferences.edit()
        // store blank new, get to be saved
        Log.d("TESTING","INSIDE SAVE DATA MISMO $chosenBG")
        sharedPrefEdit.putInt(bgKey,chosenDino)
        sharedPrefEdit.putInt(dinoKey,chosenBG)
        sharedPrefEdit.commit()
        // close fragment
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction().remove(this).commit()
    }
    companion object {

    }
}