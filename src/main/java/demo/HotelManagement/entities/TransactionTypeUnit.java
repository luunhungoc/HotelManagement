package demo.HotelManagement.entities;

public enum TransactionTypeUnit {

    night("night"),
    pcs("pcs"),
    pack("pack"),
    way("way");

    private final String displayName;

    TransactionTypeUnit(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
