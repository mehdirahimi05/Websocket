public class ChatMessage {
    private String from;
    private String message;
    private String roomName;

    public ChatMessage() {
        this.from = "";
        this.message = "";
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
