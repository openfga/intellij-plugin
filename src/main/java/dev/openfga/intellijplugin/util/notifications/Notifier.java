package dev.openfga.intellijplugin.util.notifications;

import com.intellij.notification.NotificationType;

public interface Notifier {

    void notify(NotificationType notificationType, String content);

    void notify(NotificationType notificationType, String title, String content);

    default void notifyError(String content) {
        notify(NotificationType.ERROR, content);
    }

    default void notifyError(String title, String content) {
        notify(NotificationType.ERROR, title, content);
    }

    default void notifyError(Throwable throwable) {
        notifyError(throwable.getMessage());
    }

    default void notifyError(String title, Throwable throwable) {
        notifyError(title, throwable.getMessage());
    }
}
