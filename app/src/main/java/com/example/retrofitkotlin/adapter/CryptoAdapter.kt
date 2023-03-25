package com.example.retrofitkotlin.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitkotlin.databinding.RowCryptoBinding
import com.example.retrofitkotlin.model.CryptoModel

class CryptoAdapter(var cryptoList:ArrayList<CryptoModel>,private val listener:Listener ) : RecyclerView.Adapter<CryptoAdapter.CryptoHolder>() {
    interface Listener{
        fun onItemClick(cryptoModel: CryptoModel)
    }


    val colors: Array<String> = arrayOf("#13bd27","#29c1e1","#b129e1","#d3df13","#f6bd0c","#a1fb93","#0d9de3","#ffe48f")

    class CryptoHolder(val binding:RowCryptoBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoHolder {
        var binding:RowCryptoBinding=RowCryptoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CryptoHolder(binding)
    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }

    override fun onBindViewHolder(holder: CryptoHolder, position: Int) {
        holder.itemView.setBackgroundColor(Color.parseColor(colors[position%8]))
        holder.itemView.setOnClickListener {
            listener.onItemClick(cryptoList[position])
        }

        holder.binding.currencyView.text=cryptoList[position].currency
        holder.binding.priceView.text=cryptoList[position].price
        //holder.itemView.setBackgroundColor(Color.parseColor(colors[position%7]))

    }

}