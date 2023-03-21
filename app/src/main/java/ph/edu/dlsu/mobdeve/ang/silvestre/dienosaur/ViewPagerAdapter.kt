package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R.layout.step_page

class ViewPagerAdapter(private var steps: List<String>, private var images: List<Int>): RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(stepView: View): RecyclerView.ViewHolder(stepView){
        val stepTitle: TextView = stepView.findViewById(R.id.help1_body)
        val stepImage: ImageView = stepView.findViewById(R.id.help1_dino)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerAdapter.Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(step_page, parent, false))
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {
        holder.stepTitle.text = steps[position]
        holder.stepImage.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return steps.size
    }
}