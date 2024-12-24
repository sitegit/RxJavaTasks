package com.example.rxjavatasks.tasks2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.rxjavatasks.R
import com.example.rxjavatasks.databinding.FragmentTasks2Binding
import com.example.rxjavatasks.tasks2.other.TaskType
import com.example.rxjavatasks.tasks2.other.adapter.CardsAdapter
import com.example.rxjavatasks.tasks2.other.adapter.CharactersAdapter
import com.example.rxjavatasks.tasks2.other.network.ApiClient
import com.example.rxjavatasks.tasks2.other.network.CardsSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Все выполненные задания перечислены в теле фрагмента
 * */
class Tasks2Fragment : Fragment() {

    private var _binding: FragmentTasks2Binding? = null
    private val binding: FragmentTasks2Binding
        get() = _binding ?: throw RuntimeException("Tasks2Fragment is null")

    private val apiService = ApiClient.api
    private val compositeDisposable = CompositeDisposable()
    private val charactersAdapter by lazy { CharactersAdapter() }
    private val cardsAdapter by lazy { CardsAdapter() }
    private val taskType by lazy {
        arguments?.getString(BUTTON_ID)?.let { TaskType.valueOf(it) }
    }
    private val cardsSource = CardsSource()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasks2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTasks(taskType)
    }

    /**
    * Сделать сетевой запрос и отобразить результат на экране
    * */
    private fun makeNetworkRequest() {
        compositeDisposable.add(
            apiService.getCharacters()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { changeShowProgressBar(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { changeShowProgressBar(false) }
                .subscribe({ response ->
                    binding.networkTextView.visibility = View.VISIBLE
                    binding.networkTextView.text = getString(R.string.characters, response.info.count.toString())
                }, { throwable ->
                    showToast(throwable.message.toString())
                })
        )
    }

    /**
    * Сделайте таймер. TextView которая раз в секунду меняется (timer)
    * */
    private fun startTimer() {
        compositeDisposable.add(
            Observable.interval(1, TimeUnit.SECONDS)
                .doOnSubscribe { changeShowProgressBar(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (it == 0L) {
                        changeShowProgressBar(false)
                        binding.timerTextView.visibility = View.VISIBLE
                    }
                }
                .subscribe {
                    binding.timerTextView.text = it.toString()
                }
        )
    }

    /**
     * Сделайте ресайклер. По нажатию на элемент передавайте его позицию во фрагмент.
     * и во фрагменте этот номер отображайте в тосте. (Subject)
     * */
    private fun showElementPosition() {
        initCharactersAdapter()
        compositeDisposable.add(
            charactersAdapter.clickSubject
                .subscribe { position ->
                    showToast(
                        getString(R.string.position_characters, position.toString())
                    )
                }
        )
    }

    /**
     * Сделайте EditText. При наборе текста выводите в лог содержимое EditText всегда,
     * когда пользователь 3 секунды что-то не вводил (debounce)
     * */
    private fun observeEditTextWithLogging() {
        compositeDisposable.add(
            Observable
                .create { emitter ->
                    emitTextChanges { emitter.onNext(it) }
                }
                .debounce(3, TimeUnit.SECONDS)
                .filter { it.isNotBlank() }
                .doOnSubscribe { binding.editText.visibility = View.VISIBLE }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { text ->
                    Log.d("EditTextInput", "Введено: $text")
                }
        )
    }

    /**
     * Есть 2 сервера на которых лежат скидочные карты. Нужно получить эти данные и вывести
     * в единый список. (zip и тд)
     *
     * а) Если 1 из запросов падает, то все равно выводить (найти метод RX для такого, чтоб самому
     * не прописывать логику)
     * */
    private fun getDiscountCardsA() {
        compositeDisposable.add(
            Single.zip(
                cardsSource.server1Cards.subscribeOn(Schedulers.io()).onErrorReturn { emptyList() },
                cardsSource.server2Cards.subscribeOn(Schedulers.io()).onErrorReturn { emptyList() }
            ) { cards1, cards2 -> cards1 + cards2 }
                .doOnSubscribe { changeShowProgressBar(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { changeShowProgressBar(false) }
                .subscribe { cards ->
                    Log.i("MyTag", "$cards")
                    cardsAdapter.submitList(cards)
                }
        )
    }
    /**
     * б) Если 1 из запросов падает, то не выводить ничего (найти метод RX)
     * */
    private fun getDiscountCardsB() {
        compositeDisposable.add(
            Single.zip(
                cardsSource.server1Cards,
                cardsSource.server2Cards
            ) { cards1, cards2 -> cards1 + cards2 }
                .subscribeOn(Schedulers.io())
                .onErrorReturn { emptyList() }
                .doOnSubscribe { changeShowProgressBar(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { changeShowProgressBar(false) }
                .subscribe { cards ->
                    cardsAdapter.submitList(cards)
                }
        )
    }

    private fun initTasks(buttonId: TaskType?) {
        when (buttonId) {
            TaskType.NETWORK_REQUEST -> makeNetworkRequest()
            TaskType.TIMER -> startTimer()
            TaskType.RECYCLER -> showElementPosition()
            TaskType.EDIT_TEXT -> observeEditTextWithLogging()
            TaskType.TWO_SERVERS -> {
                initCardsAdapter()
                getDiscountCardsA()
                //getDiscountCardsB()
            }
            else -> throw RuntimeException("Unknown task type")
        }
    }

    private fun emitTextChanges(onChanged: (String) -> Unit) {
        binding.editText.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
                onChanged(s.toString())
            }
        )
    }

    private fun initCharactersAdapter() {
        binding.recyclerCharacters.adapter = charactersAdapter
        binding.recyclerCharacters.visibility = View.VISIBLE

        compositeDisposable.add(
            apiService.getCharacters()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { changeShowProgressBar(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { changeShowProgressBar(false) }
                .subscribe({ response ->
                    charactersAdapter.submitList(response.results)
                }, { throwable ->
                    showToast(throwable.message.toString())
                })
        )
    }

    private fun initCardsAdapter() {
        binding.recyclerCards.adapter = cardsAdapter
        binding.recyclerCards.visibility = View.VISIBLE
    }

    private fun changeShowProgressBar(isVisible: Boolean) {
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.dispose()
    }

    companion object {
        private const val BUTTON_ID = "button_id"

        @JvmStatic
        fun newInstance(buttonId: TaskType) =
            Tasks2Fragment().apply {
                arguments = Bundle().apply {
                    putString(BUTTON_ID, buttonId.name)
                }
            }
    }
}