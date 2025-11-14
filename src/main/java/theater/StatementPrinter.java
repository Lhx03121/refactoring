package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a formatted statement for a given invoice of performances.
 */
public class StatementPrinter {

    private final Invoice invoice;
    private final Map<String, Play> plays;

    /**
     * Construct a StatementPrinter for a specific invoice and play collection.
     *
     * @param invoice the invoice to print
     * @param plays   the map of play id to play
     */
    public StatementPrinter(final Invoice invoice, final Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns a formatted statement for the invoice associated with this printer.
     *
     * @return the formatted statement string
     * @throws RuntimeException if a play id is unknown or a play type is unknown
     */
    public String statement() {
        final StringBuilder result = new StringBuilder(
                "Statement for " + invoice.getCustomer() + System.lineSeparator()
        );
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);

        int totalAmount = 0;
        int totalVolumeCredits = 0;

        for (final Performance performance : invoice.getPerformances()) {
            final Play play = getPlay(performance);
            final int thisAmount = getAmount(performance);
            final int thisCredits = getVolumeCredits(performance);

            totalAmount += thisAmount;
            totalVolumeCredits += thisCredits;

            result.append(String.format(
                    "  %s: %s (%s seats)%n",
                    play.name(),
                    formatter.format(thisAmount / (double) Constants.PERCENT_FACTOR),
                    performance.audience()
            ));
        }

        result.append(String.format(
                "Amount owed is %s%n",
                formatter.format(totalAmount / (double) Constants.PERCENT_FACTOR)
        ));
        result.append(String.format(
                "You earned %s credits%n",
                totalVolumeCredits
        ));

        return result.toString();
    }

    /**
     * Compute the amount (in cents) for a single performance.
     *
     * @param performance the performance
     * @return the amount in cents
     * @throws RuntimeException if the play type is unknown
     */
    private int getAmount(final Performance performance) {
        final Play play = getPlay(performance);
        final String type = play.type();
        final int audience = performance.audience();

        int amount = 0;

        if (Play.TYPE_TRAGEDY.equals(type)) {
            amount = Constants.TRAGEDY_BASE_AMOUNT;
            if (audience > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                amount += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                        * (audience - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
            }
        }
        else if (Play.TYPE_COMEDY.equals(type)) {
            amount = Constants.COMEDY_BASE_AMOUNT;
            if (audience > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                amount += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                        + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                        * (audience - Constants.COMEDY_AUDIENCE_THRESHOLD);
            }
            amount += Constants.COMEDY_AMOUNT_PER_AUDIENCE * audience;
        }
        else if (Play.TYPE_HISTORY.equals(type)) {
            amount = Constants.HISTORY_BASE_AMOUNT;
            if (audience > Constants.HISTORY_AUDIENCE_THRESHOLD) {
                amount += Constants.HISTORY_OVER_BASE_CAPACITY_PER_PERSON
                        * (audience - Constants.HISTORY_AUDIENCE_THRESHOLD);
            }
        }
        else if (Play.TYPE_PASTORAL.equals(type)) {
            amount = Constants.PASTORAL_BASE_AMOUNT;
            if (audience > Constants.PASTORAL_AUDIENCE_THRESHOLD) {
                amount += Constants.PASTORAL_OVER_BASE_CAPACITY_PER_PERSON
                        * (audience - Constants.PASTORAL_AUDIENCE_THRESHOLD);
            }
        }
        else {
            throw new RuntimeException(String.format("unknown type: %s", type));
        }

        return amount;
    }

    /**
     * Compute the volume credits for a single performance.
     *
     * @param performance the performance
     * @return the number of credits earned
     * @throws RuntimeException if the play type is unknown
     */
    private int getVolumeCredits(final Performance performance) {
        final Play play = getPlay(performance);
        final String type = play.type();
        final int audience = performance.audience();

        int credits = 0;

        if (Play.TYPE_TRAGEDY.equals(type) || Play.TYPE_COMEDY.equals(type)) {
            credits += Math.max(audience - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        }
        else if (Play.TYPE_HISTORY.equals(type)) {
            credits += Math.max(audience - Constants.HISTORY_VOLUME_CREDIT_THRESHOLD, 0);
        }
        else if (Play.TYPE_PASTORAL.equals(type)) {
            credits += Math.max(audience - Constants.PASTORAL_VOLUME_CREDIT_THRESHOLD, 0);
        }
        else {
            throw new RuntimeException(String.format("unknown type: %s", type));
        }

        if (Play.TYPE_COMEDY.equals(type)) {
            credits += audience / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }

        return credits;
    }

    /**
     * Lookup helper for plays map.
     *
     * @param performance the performance
     * @return the corresponding play
     * @throws RuntimeException if the play id is unknown
     */
    private Play getPlay(final Performance performance) {
        final Play play = plays.get(performance.playID());
        if (play == null) {
            throw new RuntimeException(
                    String.format("unknown playID: %s", performance.playID())
            );
        }
        return play;
    }
}
