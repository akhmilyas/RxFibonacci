package com.akhmilyas.rxfibonacci

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

const val DELAY = 300L

class MainActivity : AppCompatActivity() {

    lateinit var disposable: Disposable
    private var fibonacciNumbers: ArrayList<String> = arrayListOf()
    private val adapter = FibonacciAdapter(fibonacciNumbers)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fib_output.adapter = adapter

        fib_button.isEnabled = true
        fib_button.setOnClickListener {
            fib_button.isEnabled = false
            fibonacciNumbers.clear()
            disposable = createFibonacciObservable()
                .subscribeOn(Schedulers.computation())
                .take(30)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { number ->
                        Log.d("FIBONACCI", number)
//                        Log.d("FIBONACCI", Thread.currentThread().name)
                        fibonacciNumbers.add(number)
                        adapter.notifyDataSetChanged()
                    },
                    { error ->
                        Log.e("FIBONACCI", error.toString())
                    },
                    {
                        fib_button.isEnabled = true
                    })
        }
    }

    private fun createFibonacciObservable(): Observable<String> {
        return Observable.create { observer ->
            var first = 0
            var second = 1
            var fib = first + second
            while (!observer.isDisposed) {
//                Log.d("IN OBSERVABLE FIBONACCI", fib.toString())
//                Log.d("IN OBSERVABLE FIBONACCI", Thread.currentThread().name)
                observer.onNext(fib.toString())
                fib = first + second
                first = second
                second = fib
                SystemClock.sleep(DELAY)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }
}