package theater;

/**
 * Class representing a performance of a play.
 */
public class Performance {

    private final String playID;
    private final int audience;

    /**
     * Constructs a performance with the given play id and audience size.
     *
     * @param playID   identifier of the play
     * @param audience size of the audience
     */
    public Performance(String playID, int audience) {
        this.playID = playID;
        this.audience = audience;
    }

    /**
     * Returns the identifier of the play for this performance.
     *
     * @return the play identifier
     */
    public String playID() {
        return playID;
    }

    /**
     * Returns the audience size for this performance.
     *
     * @return the audience size
     */
    public int audience() {
        return audience;
    }
}
