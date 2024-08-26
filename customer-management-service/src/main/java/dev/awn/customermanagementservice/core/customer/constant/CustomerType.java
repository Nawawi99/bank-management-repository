package dev.awn.customermanagementservice.core.customer.constant;

public enum CustomerType {
    RETAIL,
    CORPORATE,
    INVESTMENT;

    public static boolean isValid(String type) {
        for (CustomerType t : CustomerType.values()) {
            if (t.name().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
