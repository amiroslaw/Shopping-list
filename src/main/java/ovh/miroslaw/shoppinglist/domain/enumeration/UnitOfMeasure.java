package ovh.miroslaw.shoppinglist.domain.enumeration;

/**
 * The enum Unit of measure.
 */
public enum UnitOfMeasure {
    CUP("cup"), TABLESPOON("tablespoon"), TEASPOON("teaspoon"), PINCH("pinch"), PIECE("piece"), LITER("liter"), ML(
        "ml"), KG("kg"), GRAMS("grams");

    private final String unit;

    /**
     * Gets unit of measure.
     *
     * @return the measure unit
     */
    public String getUnit() {
        return unit;
    }

    UnitOfMeasure(String unit) {
        this.unit = unit;
    }
}
