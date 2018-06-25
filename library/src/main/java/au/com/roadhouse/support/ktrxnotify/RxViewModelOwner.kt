package au.com.roadhouse.support.ktrxnotify

interface RxViewModelOwner<V: RxViewModel> {
    fun onCreateViewModel(): V
    fun onViewModelNotification(notification: Notification)
}