package com.example.user.contactlistkotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.user.contactlistkotlin.R
import com.example.user.contactlistkotlin.viewmodel.ContactViewModel


class GridContactFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_grid_contact, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {

        val recyclerView = view.findViewById<RecyclerView>(R.id.grid_contact_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 2)
        val adapter = ContactAdapter()

        val contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java!!)

        contactViewModel.contacts!!.observe(viewLifecycleOwner, { contacts ->
            recyclerView.adapter = adapter
            adapter.setContacts(contacts!!)
        })

        contactViewModel.liveDataString.observe(viewLifecycleOwner, { s -> Toast.makeText(context, s, Toast.LENGTH_SHORT).show() })

    }

    companion object {

        fun newInstance(): GridContactFragment {
            val fragment = GridContactFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
