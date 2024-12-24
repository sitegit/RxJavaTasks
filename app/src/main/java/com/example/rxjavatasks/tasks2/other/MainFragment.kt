package com.example.rxjavatasks.tasks2.other

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rxjavatasks.R
import com.example.rxjavatasks.databinding.FragmentMainBinding
import com.example.rxjavatasks.tasks2.Tasks2Fragment

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
            openTasks2Fragment(TaskType.NETWORK_REQUEST)
        }
        binding.timer.setOnClickListener {
            openTasks2Fragment(TaskType.TIMER)
        }
        binding.recycler.setOnClickListener {
            openTasks2Fragment(TaskType.RECYCLER)
        }
        binding.editText.setOnClickListener {
            openTasks2Fragment(TaskType.EDIT_TEXT)
        }
        binding.twoServers.setOnClickListener {
            openTasks2Fragment(TaskType.TWO_SERVERS)
        }

    }

    private fun openTasks2Fragment(buttonId: TaskType) {
        val fragment = Tasks2Fragment.newInstance(buttonId)

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}