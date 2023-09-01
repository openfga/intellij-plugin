package com.github.le_yams.openfga4intellij;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class Notifier {

    public static void notifyError(String content) {
        notifyError((Project) null, content);
    }

    public static void notifyError(Project project, String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("OpenFGA Notifications")
                .createNotification(content, NotificationType.ERROR)
                .notify(project);
    }
    public static void notifyError(String title, String content) {
        notifyError(null, title, content);
    }

    public static void notifyError(Project project, String title, String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("OpenFGA Notifications")
                .createNotification(title, content, NotificationType.ERROR)
                .notify(project);
    }
}
