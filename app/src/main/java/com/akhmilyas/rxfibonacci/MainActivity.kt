package com.akhmilyas.rxfibonacci

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

const val DELAY = 500L

class MainActivity : AppCompatActivity() {

    lateinit var inputDisposable: Disposable
    private val disposableBag: CompositeDisposable = CompositeDisposable()
    private var fibonacciNumbers: ArrayList<String> = arrayListOf()
    private val adapter = FibonacciAdapter(fibonacciNumbers)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fib_output.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        inputDisposable = fib_input
            .textChanges()
            .debounce(300, TimeUnit.MILLISECONDS)
            .map {
                if (it.isEmpty() || !it.isDigitsOnly()) 0L
                else it.toString().toLong()
            }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { amountOfFibonacci ->
                if (amountOfFibonacci in 1..30) {
                    fib_button.isEnabled = true
                    fib_input.isEnabled = true

                    fib_button.setOnClickListener {
                        fib_input.isEnabled = false
                        fib_button.isEnabled = false

                        fibonacciNumbers.clear()
                        disposableBag.clear()
                        fib_input.hideKeyboard()

                        val disposable = createFibonacciChain(amountOfFibonacci)
                        disposableBag.add(disposable)
                    }
                } else {
                    fib_button.isEnabled = false
                }
            }
    }

    private fun createFibonacciChain(amountOfFibonacci: Long): Disposable {
        return createFibonacciObservable()
            .subscribeOn(Schedulers.computation())
            .take(amountOfFibonacci)
            .delayEach(DELAY, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { number ->
                    fibonacciNumbers.add(number)
                    adapter.notifyDataSetChanged()
                },
                { error ->
                    Log.e("FIBONACCI", error.toString())
                },
                {
                    fib_button.isEnabled = true
                    fib_input.isEnabled = true
                    Toast.makeText(this, "All numbers are displayed", Toast.LENGTH_LONG).show()
                })
    }

    private fun createFibonacciObservable(): Observable<String> {
        return Observable.create { emitter ->
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
    }

    override fun onStop() {
        super.onStop()
        disposableBag.clear()
        inputDisposable.dispose()
    }
}