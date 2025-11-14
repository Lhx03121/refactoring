package theater;

import java.util.List;

/**
 * Class representing an invoice for a customer.
 */
public class Invoice {

    private final String customer;
    private final List<Performance> performances;

    /**
     * Constructs an invoice with the given customer and performances.
     *
     * @param customer     the customer name
     * @param performances the list of performances in this invoice
     */
    public Invoice(String customer, List<Performance> performances) {
        this.customer = customer;
        this.performances = performances;
    }

    /**
     * Returns the name of the customer.
     *
     * @return the customer name
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * Returns the list of performances in this invoice.
     *
     * @return the list of performances
     */
    public List<Performance> getPerformances() {
        return performances;
    }
}