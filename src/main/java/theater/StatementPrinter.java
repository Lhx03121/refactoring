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
     * @throws RuntimeException if a play type is unknown
     */
    public String statement() {
        final StringBuilder result = new StringBuilder(
                "Statement for " + invoice.getCustomer() + System.lineSeparator());

        for (final Performance performance : invoice.getPerformances()) {
            result.append(String.format(
                    "  %s: %s (%s seats)%n",
                    getPlay(performance).name(),
                    usd(getAmount(performance)),
                    performance.audience()
            ));
        }

        final int totalAmount = getTotalAmount();

        final int totalVolumeCredits = getTotalVolumeCredits();

        result.append(String.format(
                "Amount owed is %s%n",
                usd(totalAmount)
        ));
        result.append(String.format(
                "You earned %s credits%n",
                totalVolumeCredits
        ));

        return result.toString();
    }

    /**
     * Lookup helper for plays map.
     *
     * @param performance the performance
     * @return the corresponding play
     */
    private Play getPlay(final Performance performance) {
        return plays.get(performance.playID());
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

        int amount;

        if ("tragedy".equals(type)) {
            amount = Constants.TRAGEDY_BASE_AMOUNT;
            if (audience > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                amount = amount + Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                        * (audience - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
            }
        }
        else if ("comedy".equals(type)) {
            amount = Constants.COMEDY_BASE_AMOUNT;
            if (audience > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                amount = amount + Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                        + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                        * (audience - Constants.COMEDY_AUDIENCE_THRESHOLD);
            }
            amount = amount + Constants.COMEDY_AMOUNT_PER_AUDIENCE * audience;
        }
        else {
            final String message = String.format("unknown type: %s", type);
            throw new RuntimeException(message);
        }

        return amount;
    }

    /**
     * Compute the volume credits for a single performance.
     *
     * @param performance the performance
     * @return the number of credits earned
     */
    private int getVolumeCredits(final Performance performance) {
        final Play play = getPlay(performance);
        final String type = play.type();
        final int audience = performance.audience();

        int credits = 0;

        if (audience > Constants.BASE_VOLUME_CREDIT_THRESHOLD) {
            credits = credits + audience - Constants.BASE_VOLUME_CREDIT_THRESHOLD;
        }

        if ("comedy".equals(type)) {
            credits = credits + audience / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }

        return credits;
    }

    /**
     * Compute the total amount for the invoice.
     *
     * @return total amount in cents
     */
    private int getTotalAmount() {
        int total = 0;
        for (final Performance performance : invoice.getPerformances()) {
            total = total + getAmount(performance);
        }
        return total;
    }

    /**
     * Compute the total volume credits for the invoice.
     *
     * @return total volume credits
     */
    private int getTotalVolumeCredits() {
        int totalCredits = 0;
        for (final Performance performance : invoice.getPerformances()) {
            totalCredits = totalCredits + getVolumeCredits(performance);
        }
        return totalCredits;
    }

    /**
     * Format the given amount (in cents) as US currency.
     *
     * @param amountInCents the amount in cents
     * @return formatted amount as a String
     */
    private String usd(final int amountInCents) {
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        final double amountInDollars =
                amountInCents / (double) Constants.PERCENT_FACTOR;
        return formatter.format(amountInDollars);
    }
}
