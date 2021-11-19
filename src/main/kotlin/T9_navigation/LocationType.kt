package T9_navigation

enum class LocationType(val code: Int) {
    UNKNOWN(0),
    LOCATION(1),
    GAS_STATION(2),
    CHARGE_STATION(4),
}