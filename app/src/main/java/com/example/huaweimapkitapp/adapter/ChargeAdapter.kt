package com.example.huaweimapkitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.huaweimapkitapp.Connection
import com.example.huaweimapkitapp.databinding.RecyclerRowBinding

class ChargeAdapter(private val connectionList : ArrayList<Connection>) : RecyclerView.Adapter<ChargeAdapter.ChargeHolder>()  {

    class ChargeHolder(private val view:RecyclerRowBinding) : RecyclerView.ViewHolder(view.root){
        fun bind(connection : Connection){
            view.textView.text = connection.connectionType.title
            view.textView1.text = connection.connectionType.formalName
            view.textView3.text = connection.powerKW.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargeHolder {
        val view = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChargeHolder(view)
    }

    override fun getItemCount(): Int {
       return  connectionList.size
    }

    override fun onBindViewHolder(holder: ChargeHolder, position: Int) {
        val connection : Connection = connectionList[position]
        holder.bind(connection)
    }
}