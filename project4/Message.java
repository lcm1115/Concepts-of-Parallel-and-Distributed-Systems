import java.io.Serializable;

/**
 * Class for encapsulating messages in a game of Shut The Box.
 *
 * @author Liam Morris (lcm1115)
 */
public class Message implements Serializable {
    /**
     * Represents the type of message being sent.
     */
    private final String type;

    /**
     * Arguments for the message.
     */
    private final String[] args;

    /**
     * Constructor for Message.
     *
     * @param type the message's type
     * @param args the arguments for the message
     */
    public Message(String type, String[] args) {
        this.type = type;
        this.args = args;
    }

    /**
     * Accessor method for type.
     *
     * @return the type of message
     */
    public String getType() {
        return type;
    }

    /**
     * Access method for args.
     *
     * @return the message arguments
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Converts the Message into a single String.
     *
     * @return string in form [type arg1 arg2 ...]
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        if (args != null) {
            for (int i = 0; i < args.length; ++i) {
                sb.append(" " + args[i]);
            }
        }
        return sb.toString();
    }
}
