package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator3
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.SoundPoolManager
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.ViewPagerAdapter
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentHelpBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.Dinos

class FragmentHelp : Fragment() {
    private lateinit var binding: FragmentHelpBinding
    private var stepsList = mutableListOf<String>()
    private var imagesList = mutableListOf<Int>()
    private lateinit var soundPoolManager: SoundPoolManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHelpBinding.inflate(layoutInflater)
        soundPoolManager = SoundPoolManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = binding.root
        val fragmentManager = requireActivity().supportFragmentManager

        // Finding IDs
        val backBtn = binding.helpBack
        val vp = binding.helpViewpager
        val indicator = binding.helpIndicator

        // Listeners
        backBtn.setOnClickListener{
            soundPoolManager.playSound(R.raw.sfx_tick)
            fragmentManager.beginTransaction().remove(this).commit()
        }

        // ViewPager setup
        addToList("Tilt left and right to control your dinosaur", Dinos[0].walk)
        addToList("Avoid the falling meteors", Dinos[0].walk)
        addToList("You have 3 lives to run as long as you can!", Dinos[0].walk)
        vp.adapter = ViewPagerAdapter(stepsList,imagesList)
        indicator.setViewPager(vp)

        // Inflate the layout for this fragment
        return rootView
    }

    private fun addToList(step: String, image: Int){
        stepsList.add(step)
        imagesList.add(image)
    }
}