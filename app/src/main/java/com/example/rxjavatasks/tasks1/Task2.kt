package com.example.rxjavatasks.tasks1

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Задача 2. Какой результат будет в логе?
 *
 * В данном примере подписчик регистрируется после того, как все элементы были отправлены,
 * поэтому он ничего не получит, соответственно лог будет пустым. Для вывода элементов необходимо
 * переместить вызов subscribe перед вызовами onNext, либо воспользоваться ReplaySubject, который
 * сохраняет все отправленные элементы до подписки и воспроизводит их для новых подписчиков.
 * */

@SuppressLint("CheckResult")
fun task2() {
    val subject = PublishSubject.create<String>()
    subject.onNext("1")
    subject.onNext("2")
    subject.onNext("3")

    subject.subscribe { Log.d("HAHAHA", it) }
}