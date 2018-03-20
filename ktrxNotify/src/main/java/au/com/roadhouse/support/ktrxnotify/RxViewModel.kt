package au.com.roadhouse.support.ktrxnotify

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable

open class RxViewModel: ViewModel() {

    val notificationObservable: Observable<Notification>
        get() {
            return notificationSubject
        }

    private val notificationSubject = QueueSubject<Notification>()

    fun sendNotification(notification: Notification){
        notificationSubject.onNext(notification)
    }

    override fun onCleared() {
        notificationSubject.onComplete()
        super.onCleared()
    }
}
