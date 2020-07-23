package com.example.user.contactlistkotlin.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.user.contactlistkotlin.R
import com.example.user.contactlistkotlin.data.model.Contact
import java.util.*


class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var contacts: List<Contact>? = null

    fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ContactViewHolder {

        val result = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_contact,
                viewGroup, false)
        return ContactViewHolder(result)
    }

    override fun onBindViewHolder(contactViewHolder: ContactViewHolder, i: Int) {
        contactViewHolder.onBind(contacts!![i])

    }

    override fun getItemCount(): Int {
        return if (contacts != null) contacts!!.size else 0
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
                drawableTextView.text = serviceSubString.toUpperCase(Locale.getDefault())
            }
        }
    }

    /**
     * @return a random color which is used a background by
     * services initial letters
     */
    private fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}
