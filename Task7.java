import java.util.Map;
import java.util.stream.Collectors;

interface DeliveryChannelStrategy {
    boolean send(String recipient, String content);
}

class EmailDelivery implements DeliveryChannelStrategy {
    public boolean send(String recipient, String content) {
        System.out.println("[EMAIL -> " + recipient + "] " + content);
        return true;
    }
}

class SmsDelivery implements DeliveryChannelStrategy {
    public boolean send(String recipient, String content) {
        System.out.println("[SMS -> " + recipient + "] " + content.substring(0, Math.min(160, content.length())));
        return true;
    }
}

class PushNotificationDelivery implements DeliveryChannelStrategy {
    public boolean send(String recipient, String content) {
        System.out.println("[PUSH -> " + recipient + "] " + content);
        return true;
    }
}

class UserPreferenceStore {
    String getContact(String userId) { return "user_" + userId + "@example.com"; }
}

class TemplateFormatter {
    String render(String key, Map<String, Object> payload) {
        String body = payload.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", "));
        return "[" + key.toUpperCase() + "] " + body;
    }
}

class DeliveryLogger {
    void log(String userId, String key, boolean ok) {
        System.out.println("LOG: user=" + userId + ", key=" + key + ", status=" + (ok ? "OK" : "FAIL"));
    }
}

class NotificationFacade {
    private DeliveryChannelStrategy strategy;
    private final UserPreferenceStore prefs = new UserPreferenceStore();
    private final TemplateFormatter formatter = new TemplateFormatter();
    private final DeliveryLogger logger = new DeliveryLogger();

    NotificationFacade(DeliveryChannelStrategy strategy) { this.strategy = strategy; }
    void setStrategy(DeliveryChannelStrategy strategy) { this.strategy = strategy; }

    boolean sendAlert(String userId, String messageKey, Map<String, Object> payload) {
        String contact = prefs.getContact(userId);
        String msg = formatter.render(messageKey, payload);
        boolean sent = strategy.send(contact, msg);
        logger.log(userId, messageKey, sent);
        return sent;
    }
}