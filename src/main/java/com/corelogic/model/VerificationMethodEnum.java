package com.corelogic.model;

public enum VerificationMethodEnum {
    PHONE("Phone"),
    SELF_EMPLOYED("Self Employed"),
    EMAIL("Email"),
    MAIL("Mail"),
    THIRD_PARTY("3rd Party"),
    TWN("TWN"),
    FAX("Fax"),
    MILITARY("Military"),
    INVALID("Invalid");

    private final String value;

    VerificationMethodEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static VerificationMethodEnum fromString(String value) {
        for (VerificationMethodEnum item : VerificationMethodEnum.values()) {
            if (item.value.equalsIgnoreCase(value)) {
                return item;
            }
        }
        return VerificationMethodEnum.INVALID;
    }
}
