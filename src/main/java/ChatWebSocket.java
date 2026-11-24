import io.quarkus.websockets.next.*;
import jakarta.inject.Inject;

@WebSocket(path = "/chat/{username}")
public class ChatWebSocket {

    /**
     * Es gibt nur drei Arten von Nachrichten: Benutzer hat sich verbunden, Benutzer hat sich getrennt, Chat-Nachricht
     */
    public enum MessageType {USER_JOINED, USER_LEFT, CHAT_MESSAGE}

    /**
     * Fasst die verschiedenen Nachrichtenarten zusammen, die im ChatWebSocket verwendet werden können.
     * @param type Der Typ der Nachricht
     * @param from Von wem die Nachricht kommt
     * @param message Der Inhalt der Nachricht
     */
    public record ChatMessage(MessageType type, String from, String message) {
    }

    @Inject
    WebSocketConnection connection;

    /**
     * Wird aufgerufen, wenn ein Benutzer die Verbindung zum ChatWebSocket herstellt.
     * @return Eine ChatMessage, die den Typ USER_JOINED und den Benutzernamen enthält.
     */
    @OnOpen(broadcast = true)
    public ChatMessage onOpen(){
      return new ChatMessage(MessageType.USER_JOINED, connection.pathParam("username"), null);
    }

    /**
     * Wird aufgerufen, wenn ein Benutzer die Verbindung zum ChatWebSocket trennt.
     */
    @OnClose
    public void onClose(){
        ChatMessage abgang = new ChatMessage(MessageType.USER_LEFT, connection.pathParam("username"), null);
        connection.broadcast().sendTextAndAwait(abgang);
    }

    /**
     * Wird aufgerufen, wenn ein Benutzer eine Nachricht sendet.
     * @param message Die empfangene ChatMessage
     * @return Die empfangene ChatMessage
     */
    @OnTextMessage(broadcast = true)
    public ChatMessage onMessage(ChatMessage message){
        return new ChatMessage(
                MessageType.CHAT_MESSAGE,
                "Server",
                "Empfangen: " + message.message()
        );
    }


}
