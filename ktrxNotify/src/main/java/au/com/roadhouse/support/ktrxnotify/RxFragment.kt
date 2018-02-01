package au.com.roadhouse.support.ktrxnotify

import android.content.Context
import android.support.v4.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

open class RxFragment: Fragment() {
    private var activityObservable: PublishSubject<FragmentNotification>? = null
    private var notificationDisposable: Disposable? = null

    override fun onAttach(context: Context?) {
        if(context is RxActivity) {
            activityObservable = (activity as RxActivity).incomingObservable
        }
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        if(context is RxActivity) {
            activityObservable = (activity as RxActivity).incomingObservable
            notificationDisposable = (activity as RxActivity).outgoingObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy {
                        if (it.tag == null || it.tag == tag) {
                            onActivityNotification(it.notification)
                        }
                    }
        }
    }

    open fun onActivityNotification(notification: Notification){
        //To be overridden when required
    }

    fun sendNotificationToActivity(notification: Notification) {
        activityObservable?.onNext(FragmentNotification(fragment = this, notification = notification))
    }

    override fun onPause() {
        super.onPause()
        notificationDisposable?.dispose()
    }

    override fun onDetach() {
        activityObservable = null
        super.onDetach()
    }

}