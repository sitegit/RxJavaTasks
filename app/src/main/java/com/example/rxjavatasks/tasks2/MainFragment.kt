package com.example.rxjavatasks.tasks2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rxjavatasks.R
import com.example.rxjavatasks.databinding.FragmentMainBinding
import com.example.rxjavatasks.databinding.FragmentTasks2Binding
import com.example.rxjavatasks.tasks2.tasks.Tasks2Fragment

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("FragmentMainBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.networkRequest.setOnClickListener {
            openTasks2Fragment(Tasks2Fragment.NETWORK_REQUEST)
        }
        binding.timer.setOnClickListener {
            openTasks2Fragment(Tasks2Fragment.TIMER)
        }
        binding.recycler.setOnClickListener {
            openTasks2Fragment(Tasks2Fragment.RECYCLER)
        }
        binding.editText.setOnClickListener {
            openTasks2Fragment(Tasks2Fragment.EDIT_TEXT)
        }
        binding.twoServers.setOnClickListener {
            openTasks2Fragment(Tasks2Fragment.TWO_SERVERS)
        }

    }

    private fun openTasks2Fragment(buttonId: String) {
        val fragment = Tasks2Fragment.newInstance(buttonId)

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}