import java.io.Serializable;

public class Message implements Serializable {
    private final String type;
    private final String[] args;

    public Message(String type, String[] args) {
        this.type = type;
        this.args = args;
    }

    public String getType() {
        return type;
    }

    public String[] getArgs() {
        return args;
    }

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
