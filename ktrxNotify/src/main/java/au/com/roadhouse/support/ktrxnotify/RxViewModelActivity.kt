package au.com.roadhouse.support.ktrxnotify

import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

abstract class RxViewModelActivity<V: RxViewModel>: RxActivity(), RxViewModelOwner<V> {

    private lateinit var notificationDisposable: CompositeDisposable
    private lateinit var viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = onCreateViewModel()
    }

    override fun onResume() {
        super.onResume()
        notificationDisposable = CompositeDisposable()
        viewModel.notificationObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    onViewModelNotification(it)
                }
                .addTo(notificationDisposable)
    }

    override fun onPause() {
        notificationDisposable.dispose()
        super.onPause()
    }
}