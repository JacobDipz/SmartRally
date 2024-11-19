package com.example.badmintonai.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.badmintonai.MainActivity
import com.example.badmintonai.R
import com.example.badmintonai.adapter.LogAdapter
import com.example.badmintonai.databinding.FragmentAddLogBinding
import com.example.badmintonai.model.Log
import com.example.badmintonai.viewmodel.LogViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [AddLogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddLogFragment : Fragment(R.layout.fragment_add_log), MenuProvider {

    private var addLogBinding: FragmentAddLogBinding? = null
    private val binding get() = addLogBinding!!

    private lateinit var logViewModel: LogViewModel
    private lateinit var addLogView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addLogBinding = FragmentAddLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        logViewModel = (activity as MainActivity).logViewModel
        addLogView = view

    }

    private fun saveLog(view: View){
        val logTitle = binding.addLogTitle.text.toString().trim()
        val logDesc = binding.addLogDesc.text.toString().trim()

        if (logTitle.isNotEmpty()){
            val log = Log(0, logTitle, logDesc)
            logViewModel.addLog(log)

            Toast.makeText(addLogView.context, "Log Saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(addLogView.context, "Please enter log title", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_log, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu -> {
                saveLog(addLogView)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addLogBinding = null
    }
}