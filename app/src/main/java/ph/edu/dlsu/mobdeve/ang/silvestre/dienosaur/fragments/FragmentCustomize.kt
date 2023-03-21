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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentCustomize.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentCustomize : Fragment() {
    private lateinit var binding: FragmentCustomizeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCustomizeBinding.inflate(layoutInflater)
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_customize, container, false)

        val buttonClick = AlphaAnimation(1F, 0.8F);

        // Plugging data
        val characterArray = ArrayList<Int>()
        var bgArray = arrayOf("FALL","SPRING","WINTER")
        var bgImgArray = ArrayList<Int>()

        characterArray.add(R.drawable.nico)
        characterArray.add(R.drawable.doux)
        characterArray.add(R.drawable.kira)
        characterArray.add(R.drawable.kuro)
        characterArray.add(R.drawable.mono)
        characterArray.add(R.drawable.olaf)

        bgImgArray.add(R.drawable.dark_fall)
        bgImgArray.add(R.drawable.dark_spring)
        bgImgArray.add(R.drawable.dark_winter)


        // Finding IDs
        val backBtn = rootView.findViewById<TextView>(R.id.customize_back)
        val saveBtn = rootView.findViewById<ImageButton>(R.id.customize_save)
        val charaPrev = rootView.findViewById<ImageButton>(R.id.character_prev)
        val charaNext = rootView.findViewById<ImageButton>(R.id.character_next)
        val icon = rootView.findViewById<ImageView>(R.id.customize_dino)
        val bgPrev = rootView.findViewById<ImageButton>(R.id.bg_prev)
        val bgNext = rootView.findViewById<ImageButton>(R.id.bg_next)
        val bgText = rootView.findViewById<TextOutline>(R.id.customize_bgbody)
        val bgImg = rootView.findViewById<ImageView>(R.id.customize_bg)

        // Listeners
        backBtn.setOnClickListener{
            getActivity()?.onBackPressed()
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentCustomize.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentCustomize().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}