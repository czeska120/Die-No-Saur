package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R
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

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCustomizeBinding.inflate(layoutInflater)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_customize, container, false)

        val characterArray = ArrayList<Int>()
        characterArray.add(R.drawable.nico)
        characterArray.add(R.drawable.doux)
        characterArray.add(R.drawable.kira)
        characterArray.add(R.drawable.kuro)
        characterArray.add(R.drawable.mono)
        characterArray.add(R.drawable.olaf)


        var i = 0
        binding.characterNext.setOnClickListener {
            if(i < 6) {
                binding.customizeDino.setImageResource(characterArray[i])
                i++
            }
            else{
                i = 0
            }
            Toast.makeText(context, "Next pressed!", Toast.LENGTH_SHORT).show()
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