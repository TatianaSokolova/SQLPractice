package com.practics.sqlitefirstpractice

import android.view.ViewGroup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RVAdapter(
    private val context: Context,
    private val items: ArrayList<PersonData>,
    private val listener: RVListener
 ) : RecyclerView.Adapter<RVAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.items_row, parent, false))
    }

    //binds each item in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.name.text = item.name
        holder.email.text = item.email
        /**
         * можно (лучше?) использовать метод bind во вьюхолдере,
         * где и проводить обновление полей
         */
        holder.deleteBtn.setOnClickListener {
            listener.deleteRecord(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.text_name)
        val email: TextView = view.findViewById(R.id.text_email)
        val changeBtn: ImageButton = view.findViewById(R.id.change_button)
        val deleteBtn: ImageButton = view.findViewById(R.id.delete_button)
    }
}



