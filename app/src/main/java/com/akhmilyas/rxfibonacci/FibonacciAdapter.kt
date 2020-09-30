package com.akhmilyas.rxfibonacci

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FibonacciAdapter(private val fibNumbers: ArrayList<String>) :
    RecyclerView.Adapter<FibonacciAdapter.FibonacciViewHolder>() {

    class FibonacciViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): FibonacciViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fib_number, parent, false) as TextView

        return FibonacciViewHolder(textView)
    }

    override fun onBindViewHolder(holder: FibonacciViewHolder, position: Int) {
        holder.textView.text = fibNumbers[position]
    }

    override fun getItemCount() = fibNumbers.size

}

