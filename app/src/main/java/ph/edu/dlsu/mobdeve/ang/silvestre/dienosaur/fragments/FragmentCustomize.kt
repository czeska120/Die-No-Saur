package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.SettingsActivity
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.SoundPoolManager
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentCustomizeBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.BGs
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.Dinos

class FragmentCustomize : Fragment() {
    private lateinit var binding: FragmentCustomizeBinding
    private lateinit var soundPoolManager: SoundPoolManager
    private var SHARED_PREFS = "sharedPrefs"
    private var chosenBG = 0
    private var chosenDino = 0
    private var i = 0
    private var j = 0
    private val characterArray = ArrayList<Int>()
    private var bgArray = arrayOf("FALL","SPRING","WINTER")
    private var bgImgArray = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCustomizeBinding.inflate(layoutInflater)
        soundPoolManager = SoundPoolManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = binding.root
        val fragmentManager = requireActivity().supportFragmentManager
        val buttonClick = AlphaAnimation(1F, 0.8F)

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
            soundPoolManager.playSound(R.raw.sfx_tick)
            charaPrev.startAnimation(buttonClick)
            i--
            if(i >= 0) {
                dino.setImageResource(characterArray[i])
            }
            else{
                i = 5
                dino.setImageResource(characterArray[i])
            }
            chosenDino = i
        }
        charaNext.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_tick)
            charaNext.startAnimation(buttonClick)
            i++
            if(i < 6) {
                dino.setImageResource(characterArray[i])
            }
            else{
                i = 0
                dino.setImageResource(characterArray[i])
            }
            chosenDino = i
        }

        bgPrev.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_tick)
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
            chosenBG = j
        }
        bgNext.setOnClickListener {
            soundPoolManager.playSound(R.raw.sfx_tick)
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
            chosenBG = j
        }
        Log.d("TESTING","BEFORE SAVE $chosenDino, $i")
        Log.d("TESTING","BEFORE SAVE $chosenBG, $j")

        backBtn.setOnClickListener{
            soundPoolManager.playSound(R.raw.sfx_tick)
            fragmentManager.beginTransaction().remove(this).commit()
        }

        saveBtn.setOnClickListener{
            soundPoolManager.playSound(R.raw.sfx_button)
            saveBtn.startAnimation(buttonClick)
            Log.d("TESTING","INSIDE SAVE BTN $chosenDino, $chosenBG")
            saveData()
        }
        loadData()
        // Inflate the layout for this fragment
        return rootView
    }

    fun saveData(){
        var sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, AppCompatActivity.MODE_PRIVATE)
        var sharedPrefEdit = sharedPreferences.edit()
        // store blank new, get to be saved
        Log.d("TESTING","INSIDE SAVE DATA MISMO BG: $chosenBG")
        sharedPrefEdit.putInt("bgKey",chosenBG)
        sharedPrefEdit.putInt("dinoKey",chosenDino)
        sharedPrefEdit.commit()
        // close fragment
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction().remove(this).commit()
    }
    fun loadData(){
        var sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, AppCompatActivity.MODE_PRIVATE)
        chosenBG = sharedPreferences.getInt("bgKey", 0)
        chosenDino = sharedPreferences.getInt("dinoKey", 0)

        binding.customizeBg.setImageResource(BGs[chosenBG].dark)
        binding.customizeDino.setImageResource(Dinos[chosenDino].walk)
        binding.customizeBgbody.text = bgArray[chosenBG]

        Log.d("TESTING","INSIDE SETTINGS BG: $chosenBG")
    }
    override fun onDetach() {
        super.onDetach()
        val activity = requireActivity() as SettingsActivity
        activity.loadData()
    }
}