package com.example.badmintonai.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.badmintonai.MainActivity
import com.example.badmintonai.R
import com.example.badmintonai.databinding.FragmentEditLogBinding
import com.example.badmintonai.model.Log
import com.example.badmintonai.viewmodel.LogViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [EditLogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditLogFragment : Fragment(R.layout.fragment_edit_log), MenuProvider {

    private var editLogBinding: FragmentEditLogBinding? = null
    private val binding get() = editLogBinding!!

    private lateinit var logViewModel: LogViewModel
    private lateinit var currentLog: Log

    private val args: EditLogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editLogBinding = FragmentEditLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoView = view.findViewById<VideoView>(R.id.simpleVideoView)
        val packageName = "android.resource://" + requireContext().packageName + "/"+ R.raw.example_video
        val uri = Uri.parse(packageName)
        videoView.setVideoURI(uri)
        videoView.start()

        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        logViewModel = (activity as MainActivity).logViewModel
        currentLog = args.log!!

        binding.editLogTitle.setText(currentLog.logTitle)
        binding.editLogDesc.setText(currentLog.logDesc)

        binding.editLogFab.setOnClickListener{
            val logTitle = binding.editLogTitle.text.toString().trim()
            //val logDesc = binding.editLogDesc.text.toString().trim()

            if(logTitle.isNotEmpty()){
                val log = Log(currentLog.id, logTitle, currentLog.logDesc)
                logViewModel.updateLog(log)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Please enter title", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun deleteLog(){
        AlertDialog.Builder(activity).apply{
            setTitle("Delete Log")
            setMessage("Do you want to delete this log?")
            setPositiveButton("Delete"){_,_ ->
                logViewModel.deleteLog(currentLog)
                Toast.makeText(context, "Log Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_log, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.deleteMenu -> {
                deleteLog()
                true
            } else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editLogBinding = null
    }
}