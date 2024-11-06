package demo.HotelManagement.entities;

public enum Gender {
    Female("Female"),
    Male("Male"),
    Prefer_Not_To_Say("Prefer_Not_To_Say");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
