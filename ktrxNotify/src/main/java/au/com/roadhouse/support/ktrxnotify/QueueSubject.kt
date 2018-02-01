package au.com.roadhouse.support.ktrxnotify

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.subjects.Subject
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

class QueueSubject<T> : Subject<T>() {

    private val observerList = Collections.synchronizedList(ArrayList<QueueDisposable<T>>())
    private val itemQueue = ConcurrentLinkedQueue<T>()
    private var error: Throwable? = null
    private var isTerminated = AtomicBoolean(false)

    override fun hasObservers(): Boolean {
        return !observerList.isEmpty()
    }

    override fun onNext(t: T) {
        itemQueue.add(t)
        drainQueue()
    }

    private fun drainQueue() {
        synchronized(this) {
            if (observerList.isEmpty()) {
                return
            }

            while (itemQueue.peek() != null) {
                val data = itemQueue.remove()
                for (disposable in observerList) {
                    disposable.onNext(data)
                }
            }
        }
    }

    fun peekQueuedItem(index: Int): T {
        return itemQueue.elementAt(index)
    }

    fun queueCount(): Int {
        return itemQueue.size
    }

    override fun getThrowable(): Throwable? {
        return error
    }

    override fun subscribeActual(observer: Observer<in T>) {
        val queueDisposable = QueueDisposable(observer, this)
        observerList.add(queueDisposable)
        observer.onSubscribe(queueDisposable)
        if (queueDisposable.isDisposed) {
            remove(queueDisposable)
        } else {
            drainQueue()
        }
    }

    override fun onComplete() {
        if (isTerminated.get()) {
            return
        }
        isTerminated.set(true)
        for (disposable in observerList) {
            disposable.onComplete()
        }
    }

    override fun onSubscribe(d: Disposable) {
        if (isTerminated.get()) {
            d.dispose()
        }
    }


    override fun hasComplete(): Boolean {
        return isTerminated.get()
    }

    override fun hasThrowable(): Boolean {
        return error != null
    }

    override fun onError(e: Throwable) {
        if (isTerminated.getAndSet(true)) {
            RxJavaPlugins.onError(e);
            return
        }

        error = e

        for (disposable in observerList) {
            disposable.onError(e)
        }
    }

    private fun remove(queueDisposable: QueueDisposable<T>) {
        observerList.remove(queueDisposable)
    }

    class QueueDisposable<T>(val observer: Observer<in T>, val parent: QueueSubject<T>) : AtomicBoolean(), Disposable {

        fun onNext(t: T) {
            if (!get()) {
                observer.onNext(t)
            }
        }

        fun onComplete() {
            if (!get()) {
                observer.onComplete()
            }
        }

        fun onError(t: Throwable) {
            if (get()) {
                RxJavaPlugins.onError(t)
            } else {
                observer.onError(t)
            }
        }

        override fun isDisposed(): Boolean {
            return get()
        }

        override fun dispose() {
            compareAndSet(false, true)
            parent.remove(this)
        }
    }


}