package dev.openfga.intellijplugin.util.notifications;

import com.intellij.notification.NotificationType;

public enum GlobalNotifier implements Notifier {

    INSTANCE;

    private final Notifier inner = new ProjectNotifier(null);

    @Override
    public void notify(NotificationType notificationType, String content) {
        inner.notify(notificationType, content);
    }

    @Override
    public void notify(NotificationType notificationType, String title, String content) {
        inner.notify(notificationType, title, content);
    }
}
