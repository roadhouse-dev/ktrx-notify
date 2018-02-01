package au.com.roadhouse.support.ktrxnotify

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

open class RxActivity: AppCompatActivity() {

    internal var incomingObservable: PublishSubject<FragmentNotification> = PublishSubject.create()
    internal var outgoingObservable: PublishSubject<ActivityNotification> = PublishSubject.create()
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposable = incomingObservable
                .subscribeBy {
                    onFragmentNotification(it.fragment, it.notification)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        disposable = incomingObservable
                .subscribeBy {
                    onFragmentNotification(it.fragment, it.notification)
                }
    }

    open fun onFragmentNotification(fragment: RxFragment, notification: Notification){
        //Implement in child if required
    }

    fun sendNotificationToFragment(tag: String? = null, notification: Notification) {
        outgoingObservable.onNext(ActivityNotification(tag = tag, notification = notification))
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}