package com.longtran.a7minuteworkout

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.longtran.a7minuteworkout.databinding.ItemHistoryRowBinding

class HistoryAdapter(val context: Context,val items: ArrayList<String>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemHistoryRowBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryRowBinding
            .inflate(LayoutInflater.from(parent.context),parent ,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date : String = items.get(position)
        holder.binding.tvPosition.text =  (position+1).toString()
        holder.binding.tvItem.text = date

        if(position % 2 == 0){
            holder.binding.llHistoryItemMain.setBackgroundColor(
                Color.parseColor("#EBEBEB")
            )
        }else{
            holder.binding.llHistoryItemMain.setBackgroundColor(
                Color.parseColor("#FFFFFF")
            )
        }
    }

    override fun getItemCount(): Int {
    return items.size
    }


}