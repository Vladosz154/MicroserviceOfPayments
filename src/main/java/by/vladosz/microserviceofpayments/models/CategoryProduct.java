package by.vladosz.microserviceofpayments.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CategoryProduct {
    ЭЛЕКТРОНИКА("ЭЛЕКТРОНИКА"),
    ОДЕЖДА("ОДЕЖДА"),
    КНИГИ("КНИГИ"),
    ИГРУШКИ("ИГРУШКИ"),
    ПРОДУКТЫ("ПРОДУКТЫ"),
    КОСМЕТИКА("КОСМЕТИКА"),
    СПОРТ("СПОРТ"),
    БЫТОВАЯ_ТЕХНИКА("БЫТОВАЯ ТЕХНИКА");

    private final String displayName;


    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static CategoryProduct fromValue(String value) {
        for (CategoryProduct product : CategoryProduct.values()) {
            if (product.getDisplayName().equalsIgnoreCase(value)) {
                product.name().equalsIgnoreCase(value.replace(" ", "_"));
                return product;
            }
        }
        throw new IllegalArgumentException(value);
    }

}
