package au.com.roadhouse.support.ktrxnotify

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

open class RxActivity: AppCompatActivity(), RxParentNotifications {

    internal var notificationSubject: PublishSubject<Any> = PublishSubject.create()
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposable = notificationSubject
                .subscribeBy {
                    onNotification(it)
                }
    }

    private fun onNotification(it: Any){
        if(it is ChildNotification){
            onChildNotification(it.fragment, it.notification)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        disposable = notificationSubject
                .subscribeBy {
                    onNotification(it)
                }
    }

    override fun onChildNotification(fragment: Fragment, notification: Notification){
        //Implement in child if required
    }

    override fun sendNotificationToChild(tag: String?, notification: Notification) {
        notificationSubject.onNext(ParentNotification(tag = tag, notification = notification))
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}