package dev.openfga.intellijplugin.util.notifications;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class ProjectNotifier implements Notifier {

    public static final String OPENFGA_NOTIFICATIONS_GROUP = "OpenFGA Notifications";
    private final Project project;

    public ProjectNotifier(Project project) {
        this.project = project;
    }

    public void notify(NotificationType notificationType, String content) {
        notify(notificationType, "", content);
    }

    public void notify(NotificationType notificationType, String title, String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(OPENFGA_NOTIFICATIONS_GROUP)
                .createNotification(title, content, notificationType)
                .notify(project);
    }
}
