package T9_navigation

enum class LocationType(val description: String, val code: Int) {
    GAS_STATION("Gas station", 2),
    CHARGE_STATION("Charge station", 4),
    LOCATION("Location", 1),
    UNKNOWN("Unknown", 0),
}