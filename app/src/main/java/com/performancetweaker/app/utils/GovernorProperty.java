package com.performancetweaker.app.utils;

public class GovernorProperty {

    private String GovernorProperty;
    private String GovernorPropertyValue;

    public GovernorProperty(String governorProperty, String governorPropertyValue) {
        this.GovernorProperty = governorProperty;
        this.GovernorPropertyValue = governorPropertyValue;
    }

    public String getGovernorProperty() {
        return GovernorProperty;
    }

    public String getGovernorPropertyValue() {
        return GovernorPropertyValue;
    }
}
