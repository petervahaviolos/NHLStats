package com.example.myfirstapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

class PlayerAdapter(private val players: MutableList<Player>) : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = players[position]
        holder.playerName.text = player.name
        player.picture.into(holder.playerPicture)
        holder.playerPosition.text = player.position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val playerName: TextView = itemView.textViewPlayerName
        val playerPicture: ImageView = itemView.imageViewPlayerPicture
        val playerPosition: TextView = itemView.textViewPlayerPosition
    }
}
