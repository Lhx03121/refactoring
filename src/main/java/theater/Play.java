package theater;

/**
 * Class representing a play.
 */
public class Play {
    public static final String TYPE_TRAGEDY = "tragedy";
    public static final String TYPE_COMEDY = "comedy";
    public static final String TYPE_HISTORY = "history";
    public static final String TYPE_PASTORAL = "pastoral";

    private final String name;
    private final String type;

    /**
     * Constructs a play with the given name and type.
     *
     * @param name the name of the play
     * @param type the type of the play (e.g., "tragedy", "comedy")
     */
    public Play(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the name of this play.
     *
     * @return the play name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the type of this play.
     *
     * @return the play type
     */
    public String type() {
        return type;
    }
}
