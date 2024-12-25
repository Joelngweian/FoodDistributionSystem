package src;

public class DonationItem {
    private String category;
    private String details;
    private int quantity;

    public DonationItem(String category, String details, int quantity) {
        this.category = category;
        this.details = details;
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getDetails() {
        return details;
    }

    public int getQuantity() {
        return quantity;
    }
}

