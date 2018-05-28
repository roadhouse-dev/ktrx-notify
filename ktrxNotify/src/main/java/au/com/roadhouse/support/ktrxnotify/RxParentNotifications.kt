package au.com.roadhouse.support.ktrxnotify

interface RxParentNotifications {
    fun onChildNotification(fragment: RxFragment, notification: Notification)
    fun sendNotificationToChild(tag: String? = null, notification: Notification)
}