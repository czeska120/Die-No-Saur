package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.SettingsActivity
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.TextOutline
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivitySettingsBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentCustomizeBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.BGs
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.Dinos

class FragmentCustomize : Fragment() {
    private lateinit var binding: FragmentCustomizeBinding

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
        val icon = binding.customizeDino
        val bgPrev = binding.bgPrev
        val bgNext = binding.bgNext
        val bgText = binding.customizeBgbody
        val bgImg = binding.customizeBg

        // Listeners
        backBtn.setOnClickListener{
            fragmentManager.beginTransaction().remove(this).commit()
        }

        saveBtn.setOnClickListener{
            saveBtn.startAnimation(buttonClick)
        }

        var i = 0
        charaPrev.setOnClickListener {

            charaPrev.startAnimation(buttonClick)
            i--
            if(i >= 0) {
                icon.setImageResource(characterArray[i])
            }
            else{
                i = 5
                icon.setImageResource(characterArray[i])
            }
        }
        charaNext.setOnClickListener {
            charaNext.startAnimation(buttonClick)
            i++
            if(i < 6) {
                icon.setImageResource(characterArray[i])
            }
            else{
                i = 0
                icon.setImageResource(characterArray[i])
            }
        }

        var j=0
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
        }
        // Inflate the layout for this fragment
        return rootView
    }

    companion object {

    }
}