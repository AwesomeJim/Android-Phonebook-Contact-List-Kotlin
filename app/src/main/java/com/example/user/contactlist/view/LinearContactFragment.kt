package com.example.user.contactlist.view


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.user.contactlist.R
import com.example.user.contactlist.viewmodel.ContactViewModel


class LinearContactFragment : Fragment() {
    private val requestCode = 1
    internal var view: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_linear_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view = view

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPhoneContactsPermission(Manifest.permission.READ_CONTACTS))
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), requestCode)
            else
                init(view)
        } else {
            init(view)
        }

        val grid = view.findViewById<Button>(R.id.grid_btn)
        grid.setOnClickListener { listener!!.goToGrid() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            this.requestCode -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                init(view!!)
            else
                toast(getString(R.string.permission_denied))
        }
    }

    private fun hasPhoneContactsPermission(permission: String): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasPermission = ContextCompat.checkSelfPermission(context!!, permission)
            return hasPermission == PackageManager.PERMISSION_GRANTED
        } else
            return true
    }

    private fun init(view: View) {

        val recyclerView = view.findViewById<RecyclerView>(R.id.contact_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, LinearLayoutManager.HORIZONTAL))
        val adapter = ContactAdapter()

        val contactViewModel = ViewModelProvider(activity!!).get<ContactViewModel>(ContactViewModel::class.java)
        contactViewModel.setup()
        contactViewModel.setLiveDataString(getString(R.string.live_data_message))

        contactViewModel.contacts!!.observe(viewLifecycleOwner, Observer{ contacts ->
            recyclerView.adapter = adapter
            adapter.setContacts(contacts!!)
        })

        contactViewModel.getLiveDataString().observe(viewLifecycleOwner, Observer<String> { this.toast(it!!) })
    }

    private fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        private var listener: TransferInterface? = null
        fun newInstance(transferInterface: TransferInterface): LinearContactFragment {
            val args = Bundle()
            listener = transferInterface
            val fragment = LinearContactFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
