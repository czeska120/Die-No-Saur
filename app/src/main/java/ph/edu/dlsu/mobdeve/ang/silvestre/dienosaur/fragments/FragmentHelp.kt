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
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.ViewPagerAdapter
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentHelpBinding

class FragmentHelp : Fragment() {
    private lateinit var binding: FragmentHelpBinding
    private var stepsList = mutableListOf<String>()
    private var imagesList = mutableListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHelpBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_help, container, false)
        val fragmentManager = requireActivity().supportFragmentManager

        // Finding IDs
        val backBtn = rootView.findViewById<TextView>(R.id.help_back)
        val vp = rootView.findViewById<ViewPager2>(R.id.help_viewpager)
        val indicator = rootView.findViewById<CircleIndicator3>(R.id.help_indicator)

        // Listeners
        backBtn.setOnClickListener{
            fragmentManager.beginTransaction().remove(this).commit()
        }

        // ViewPager setup
        addToList("Tilt left and right to control your dinosaur", R.drawable.nico)
        addToList("Avoid the falling meteors", R.drawable.nico)
        addToList("You have 3 lives to run as long as you can!", R.drawable.nico)
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