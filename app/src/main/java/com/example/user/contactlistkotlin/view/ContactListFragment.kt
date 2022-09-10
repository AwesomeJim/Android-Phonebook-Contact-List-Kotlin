package com.example.user.contactlistkotlin.view


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.contactlistkotlin.R
import com.example.user.contactlistkotlin.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.fragment_linear_contact.*

class ContactListFragment : Fragment() {

    internal var view: View? = null
    private var isSearch: Boolean = false
    private lateinit var adapter: ContactAdapter

    // Single Permission Contract
    private val askContactPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                init()
                Log.e(TAG, "TAG Contract permission granted")
            } else {
                Log.e(TAG, "Contract Manifest.permission denied")
                Toast.makeText(
                    requireActivity(),
                    "You have disabled a contacts permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_linear_contact, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view = view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPhoneContactsPermission(Manifest.permission.READ_CONTACTS))
                askContactPermission.launch(Manifest.permission.READ_CONTACTS)
            else
                init()
        } else {
            init()
        }

        /*  val grid = view.findViewById<Button>(R.id.grid_btn)
          grid.setOnClickListener { listener!!.goToGrid() }*/
    }

    private fun hasPhoneContactsPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasPermission = ContextCompat.checkSelfPermission(requireContext(), permission)
            hasPermission == PackageManager.PERMISSION_GRANTED
        } else
            true
    }

    private fun init() {
        contact_recycler_view.layoutManager = LinearLayoutManager(contact_recycler_view.context)
        contact_recycler_view.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.HORIZONTAL
            )
        )
        adapter = ContactAdapter()
        val contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        contactViewModel.setup()
        contactViewModel.setLiveDataString(getString(R.string.live_data_message))
        contactViewModel.contactList.observe(viewLifecycleOwner) { contacts ->
            contact_recycler_view.adapter = adapter
            adapter.setContacts(contacts!!)
        }

        contactViewModel.getLiveDataString().observe(viewLifecycleOwner) { this.toast(it!!) }
        et_search.setOnClickListener {
            onClick(it)
        }
    }

    private fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private var listener: TransferInterface? = null
        fun newInstance(transferInterface: TransferInterface): ContactListFragment {
            val args = Bundle()
            listener = transferInterface
            val fragment = ContactListFragment()
            fragment.arguments = args
            return fragment
        }

        private const val TAG = "ContactListFragment"
    }

    private fun onClick(view: View) {
        val id = view.id
        if (id == R.id.et_search) {
            isSearch = true
            search_layout.visibility = View.INVISIBLE
            searchView.visibility = View.VISIBLE
            setUpSearch()
        }
    }

    /**
     * Set up search.
     */
    private fun setUpSearch() {
        searchView.isIconified = false
        searchView.queryHint = "Enter name"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                adapter.filter.filter(s)
                return true
            }
        })
        searchView.setOnCloseListener {
            closeSearch()
            true
        }
    }

    /**
     * Close Search
     */
    private fun closeSearch() {
        if (isSearch) {
            isSearch = false
            search_layout.visibility = View.VISIBLE
            searchView.visibility = View.INVISIBLE
        }
    }


}
