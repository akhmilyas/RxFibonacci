package com.akhmilyas.rxfibonacci

import io.reactivex.rxjava3.core.Observable

fun createFibonacciObservable(): Observable<String> = Observable.create { emitter ->
    var first = 0
    var second = 1
    var fib = first
    while (!emitter.isDisposed) {
        emitter.onNext(fib.toString())
        fib = first + second
        first = second
        second = fib
    }
}
