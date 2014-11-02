package org.jon.ivmark.footballcoupons.application.coupons.domain.valueobjects;

public abstract class Id<T> extends ValueObject {

    private final T value;

    protected Id(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
