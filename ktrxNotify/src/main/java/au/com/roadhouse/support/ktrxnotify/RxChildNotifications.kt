package au.com.roadhouse.support.ktrxnotify

interface RxChildNotifications {
    fun onParentNotification(notification: Notification)
    fun sendNotificationToParent(notification: Notification)
}