package au.com.roadhouse.support.ktrxnotify

import androidx.fragment.app.Fragment

interface RxParentNotifications {
    fun onChildNotification(fragment: Fragment, notification: Notification)
    fun sendNotificationToChild(tag: String? = null, notification: Notification)
}