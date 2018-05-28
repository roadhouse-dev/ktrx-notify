package au.com.roadhouse.support.ktrxnotify

import android.support.v4.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

open class RxFragment : Fragment(), RxChildNotifications, RxParentNotifications {

    private var notificationSubject: PublishSubject<Any> = PublishSubject.create()
    internal var parentNotificationSubject:PublishSubject<Any>? = null
    private var disposables = CompositeDisposable()

    override fun onResume() {
        super.onResume()
        if (parentFragment != null) {
            (parentFragment as RxFragment?)?.let { rxParent ->
                parentNotificationSubject = rxParent.notificationSubject

                rxParent.notificationSubject
                        .observeOn(Schedulers.io())
                        .subscribe {
                            onReceivedNotification(it)
                        }
                        .addTo(disposables)
            }
        } else if (activity is RxActivity){
            (activity as RxActivity?)?.let {rxActivity ->
                parentNotificationSubject = rxActivity.notificationSubject

                 rxActivity.notificationSubject
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy {
                            onReceivedNotification(it)
                        }
                         .addTo(disposables)

            }
        }

        notificationSubject
                .observeOn(Schedulers.io())
                .subscribe {
                    onReceivedNotification(it)
                }
                .addTo(disposables)

    }

    private fun onReceivedNotification(notification: Any) {
        if (notification is ParentNotification && this.tag == notification.tag) {
            onParentNotification(notification.notification)
        } else if (notification is ChildNotification) {
            onChildNotification(notification.fragment, notification.notification)
        }
    }

    override fun onParentNotification(notification: Notification) {
        //Override were required
    }

    override fun onChildNotification(fragment: RxFragment, notification: Notification) {
        //Override were required
    }

    override fun sendNotificationToChild(tag: String?, notification: Notification) {
        notificationSubject.onNext(ParentNotification(tag, notification))
    }

    override fun sendNotificationToParent(notification: Notification) {
        parentNotificationSubject?.onNext(ChildNotification(this, notification))
    }

    override fun onPause() {
        disposables.dispose()
        disposables = CompositeDisposable()
        super.onPause()
    }


}