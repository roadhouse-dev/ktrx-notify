package au.com.roadhouse.support.ktrxnotify

import android.app.Activity
import android.os.Bundle
import kotlin.reflect.KClass

/**
 * Represents a notification for communicating between a RxActivity, and a RxFragment or vice-verser.
 * Custom notifications can be created by extending this interface
 */
interface Notification

/**
 * A wrapper class used by a RxFragment when sending notifications to its parentNotificationSubject RxActivity. This should
 * not be used directly.
 *
 * @property fragment The fragment that sent the notification. Due to the nature of fragments consider
 * the reference to this fragment only valid within the method.
 */
data class ChildNotification(val fragment: RxFragment, val notification: Notification)

/**
 * A wrapper class used by RxActivity when sending notifications to a child fragment. This should not
 * be used directly.
 *
 * @property tag The tag of the fragment that this notification should be sent to. A null value
 * will cause the notification to be sent to all attached fragments that are in a resumed state
 */
data class ParentNotification(val tag: String?, val notification: Notification)

/**
 * A notification that is typically sent by a RxFragment to its parentNotificationSubject RxActivity to indicate that
 * the Fragment has finished it's purpose successfully.
 */
class OnCompleteNotification: Notification

/**
 * A notification that is typically sent by a RxFragment to its parentNotificationSubject RxActivity to indicate that
 * the fragment has been cancelled, either by the back button or some other view element
 */
class OnCanceledNotification: Notification

/**
 * A notification that is typically sent to an RxFragment from it's viewModel to indicate that an
 * alert should be displayed to the user
 *
 * @property title The title to display in the dialog
 * @property content The content to display in the dialog
 * @property onDismiss A lambda expression to invoke if the dialog is dismissed
 * @property iconResId The resourceId of an icon to display in the dialog
 */
class AlertNotification(val title: String?,
                        val content: String,
                        val onDismiss: (() -> Unit)? = null,
                        val iconResId: Int? = null): Notification

/**
 * A notification that is typically sent to an RxFragment from it's viewModel to indicate than a
 * confirmation dialog should be displayed to the user
 *
 * @property title The title to display in the confirmation dialog
 * @property content The content to display in the dialog
 * @property positiveLabel The text for the positive button
 * @property negativeLabel The text for the negative button
 * @property onPositive A lambda expression to invoke when the postive button has been selected
 * @property onNegative A lambda expression to invoke when a negative button has been selected
 */
class ConfirmationNotification(val title: String,
                               val content: String,
                               val positiveLabel: String,
                               val negativeLabel: String,
                               val onPositive: (() -> Unit),
                               val onNegative: (() -> Unit)? = null): Notification

/**
 * A notification that is typically sent to an RxFragment from it's viewModel to indicate that a
 * background loading task is taking place, for which the UI should display some loading indicator
 *
 * @property isComplete A boolean flag which indicates if the background task is still running or complete
 */
class LoadingNotification(val isComplete: Boolean): Notification

/**
 * A notification that is typically sent to an RxFragment from it's viewModel to indicate that an
 * additional activity is required to be launched
 *
 * @property activity The class of the activity to be launched
 * @property extras Any extras that are required to launch the application
 */
class StartActivityNotification(
        val activity: KClass<out Activity>,
        val extras: Bundle = Bundle.EMPTY): Notification