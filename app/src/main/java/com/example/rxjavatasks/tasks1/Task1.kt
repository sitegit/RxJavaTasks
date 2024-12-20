package com.example.rxjavatasks.tasks1

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/** Задача 1. Какой результат будет в логе?
 *
 * 1. В map у нас будет выведено имя потока используещего "Schedulers.newThread()",
 * так как на этапе подписки мы переключаемся на него, в случае если бы мы убрали из параметра функции
 * timer "Schedulers.newThread()", то использовался бы "Schedulers.computation()" так как он по умолчанию
 * используется в параметре функции.
 *
 * 2. В doOnSubscribe будет выведено имя потока для "Schedulers.computation()", так как этот оператор
 * отрабатывает на этапе подписки, где подписка происходит снизу вверх по цепочке
 *
 * 3. Лог в теле flatMap выведет имя для "Schedulers.single()", так как при эмиссии observeOn
 * воздействует на операторы, следующих за ним
 *
 * 4. В теле subscribe выведет имя потока для "Schedulers.io()", так как на этапе эмиссии во flatMap
 * мы создали новый поток данных, в котором переключились на пулл-потоков io.
 */

@SuppressLint("CheckResult")
fun task1() {
    Observable.timer(10, TimeUnit.MILLISECONDS, Schedulers.newThread())
        .subscribeOn(Schedulers.io())
        .map {
            Log.d("HAHAHA", "mapThread: ${Thread.currentThread().name}") // newThread
        }
        .doOnSubscribe {
            Log.d("HAHAHA", "onSubscribeThread: ${Thread.currentThread().name}") // computation
        }
        .subscribeOn(Schedulers.computation())
        .observeOn(Schedulers.single())
        .flatMap {
            Log.d("HAHAHA", "flatMapThread: ${Thread.currentThread().name}") // single
            Observable.just(it)
                .subscribeOn(Schedulers.io())
        }
        .subscribe {
            Log.d("HAHAHA", "subscribeThread: ${Thread.currentThread().name}") // io
        }
}