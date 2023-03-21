package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator
import me.relex.circleindicator.CircleIndicator3
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.ViewPagerAdapter
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.FragmentHelp1Binding
import pl.droidsonroids.gif.GifImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentHelp1.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentHelp1 : Fragment() {
    private lateinit var binding: FragmentHelp1Binding
    private var stepsList = mutableListOf<String>()
    private var imagesList = mutableListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHelp1Binding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_help1, container, false)
        val backBtn = rootView.findViewById<TextView>(R.id.help1_back)

        backBtn.setOnClickListener{
            getActivity()?.onBackPressed()
        }

        addToList("Tilt left and right to control your dinosaur", R.drawable.nico)
        addToList("Avoid the falling meteors", R.drawable.nico)
        addToList("You have 3 lives to run as long as you can!", R.drawable.nico)
        val vp = rootView.findViewById<ViewPager2>(R.id.help1_viewpager)
        vp.adapter = ViewPagerAdapter(stepsList,imagesList)

        val indicator = rootView.findViewById<CircleIndicator3>(R.id.help1_indicator)
        indicator.setViewPager(vp)

        // Inflate the layout for this fragment
        return rootView
    }

    private fun addToList(step: String, image: Int){
        stepsList.add(step)
        imagesList.add(image)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentHelp1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentHelp1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}