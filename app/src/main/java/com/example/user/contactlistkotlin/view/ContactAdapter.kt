package com.example.user.contactlistkotlin.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.user.contactlistkotlin.R
import com.example.user.contactlistkotlin.data.model.Contact
import java.util.*


class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(), Filterable {

    private var contacts: List<Contact>? = null

    // Allows to remember the last item shown on screen
    private var lastPosition = -1

    var filteredContactsList: List<Contact> = ArrayList()

    fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        this.filteredContactsList = contacts
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ContactViewHolder {

        val result = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_contact,
                viewGroup, false)
        return ContactViewHolder(result)
    }

    override fun onBindViewHolder(contactViewHolder: ContactViewHolder, i: Int) {
        contactViewHolder.onBind(filteredContactsList[i])
        // Here you apply the animation when the view is bound
        setAnimation(contactViewHolder.itemView, i)
    }

    override fun getItemCount(): Int {
        return filteredContactsList.size
    }


    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.contact_name)
        private val phoneNo: TextView = itemView.findViewById(R.id.contact_number)
        private val photo: ImageView = itemView.findViewById(R.id.contact_photo)
        private val drawableTextView: TextView = itemView.findViewById(R.id.drawableTextView)
        fun onBind(contact: Contact) {
            name.text = contact.name
            phoneNo.text = contact.phoneNumber
            if (contact.photoUri != "") {
                photo.visibility = View.VISIBLE
                drawableTextView.visibility = View.GONE
                Glide.with(itemView)
                        .load(contact.photoUri)
                        .apply(RequestOptions.circleCropTransform())
                        .into(photo)
            } else {
                photo.visibility = View.GONE
                drawableTextView.visibility = View.VISIBLE
                val serviceSubString: String = contact.name.substring(0, 2)
                drawableTextView.text = serviceSubString.uppercase(Locale.getDefault())
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                filteredContactsList = filterResults.values as List<Contact>
                notifyDataSetChanged()
            }
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val queryString = charSequence.toString().toLowerCase(Locale.ROOT)
                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    contacts
                else
                    contacts?.filter {
                        it.name.lowercase(Locale.getDefault()).contains(queryString)
                    }
                return filterResults
            }
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation =
                    AnimationUtils.loadAnimation(
                            viewToAnimate.context,
                            R.anim.item_animation_from_right
                    )
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

}
