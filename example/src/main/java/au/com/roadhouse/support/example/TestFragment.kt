package au.com.roadhouse.support.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import au.com.roadhouse.support.ktrxnotify.Notification
import au.com.roadhouse.support.ktrxnotify.OnCompleteNotification
import au.com.roadhouse.support.ktrxnotify.RxFragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class TestFragment: RxFragment() {
    private var notificationCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.buttonSendToActivity.setOnClickListener {
            sendNotificationToActivity(OnCompleteNotification())
        }
        return view
    }

    override fun onActivityNotification(notification: Notification) {
        notificationCount++
        textViewActivityNotificationsReceived.setText(getString(R.string.format_activity_notifications_received, notificationCount))
    }
}