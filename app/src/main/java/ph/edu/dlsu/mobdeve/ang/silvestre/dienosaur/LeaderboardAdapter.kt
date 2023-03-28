package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models.User

class LeaderboardAdapter(private var context: Context, private var users: ArrayList<User>): RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {
    inner class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.item_name)
        val score: TextView = itemView.findViewById(R.id.item_score)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false)
        return LeaderboardViewHolder(v)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val user: User = users[position]
        holder.name.text = user.name
        holder.score.text = user.score + " "

    }
}