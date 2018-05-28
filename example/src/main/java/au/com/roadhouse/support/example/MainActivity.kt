package au.com.roadhouse.support.example

import android.os.Bundle
import au.com.roadhouse.support.ktrxnotify.Notification
import au.com.roadhouse.support.ktrxnotify.OnCompleteNotification
import au.com.roadhouse.support.ktrxnotify.RxActivity
import au.com.roadhouse.support.ktrxnotify.RxFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RxActivity() {

    private var notificationsReceived = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                    .add(frameLayoutPanel.id, TestFragment(), "Test")
                    .add(frameLayoutPanelTwo.id, TestFragmentTwo(), "Test2")
                    .commit()

        }
        buttonSendToFragment.setOnClickListener {
            sendNotificationToChild("Test", OnCompleteNotification())
        }

        buttonSendToFragmentTwo.setOnClickListener {
            sendNotificationToChild("Test2", OnCompleteNotification())
        }

        buttonSendToFragmentBoth.setOnClickListener {
            sendNotificationToChild(notification = OnCompleteNotification())
        }
    }

    override fun onChildNotification(fragment: RxFragment, notification: Notification) {
        notificationsReceived++
        textViewFragmentNotificationsReceived.setText(getString(R.string.format_fragment_notifications_received, notificationsReceived))
    }
}
