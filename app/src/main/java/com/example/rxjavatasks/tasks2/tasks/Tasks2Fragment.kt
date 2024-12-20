package com.example.rxjavatasks.tasks2.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.rxjavatasks.R
import com.example.rxjavatasks.databinding.FragmentTasks2Binding
import com.example.rxjavatasks.tasks2.network.ApiClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class Tasks2Fragment : Fragment() {

    private var _binding: FragmentTasks2Binding? = null
    private val binding: FragmentTasks2Binding
        get() = _binding ?: throw RuntimeException("Tasks2Fragment is null")

    private val apiService = ApiClient.api
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasks2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTasks(arguments?.getString(BUTTON_ID))
    }

/**
* Задание сделать сетевой запрос и отобразить результат на экране
* */
    private fun makeNetworkRequest() {
        val result = apiService.getNumberOfCharacters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { goneProgressBar() }
            .subscribe(
                { success ->
                    binding.networkTextView.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.characters, success.info.count.toString())
                    }
                },
                { error ->
                    Toast.makeText(requireContext(), error.message.toString(), Toast.LENGTH_SHORT).show()
                }
            )
        disposable.add(result)
    }

    private fun initTasks(buttonId: String?) {
        when (buttonId) {
            NETWORK_REQUEST -> makeNetworkRequest()
            TIMER -> {}
            RECYCLER -> {}
            EDIT_TEXT -> {}
            TWO_SERVERS -> {}
        }
    }

    private fun goneProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable.dispose()
    }

    companion object {
        private const val BUTTON_ID = "button_id"
        const val NETWORK_REQUEST = "network_request"
        const val TIMER = "timer"
        const val RECYCLER = "recycler"
        const val EDIT_TEXT = "edit_text"
        const val TWO_SERVERS = "two_servers"

        @JvmStatic
        fun newInstance(buttonId: String) =
            Tasks2Fragment().apply {
                arguments = Bundle().apply {
                    putString(BUTTON_ID, buttonId)
                }
            }
    }
}