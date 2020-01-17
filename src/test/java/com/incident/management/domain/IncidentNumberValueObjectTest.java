package com.incident.management.domain;

import com.incident.management.utils.ValueObjectContractTest;

public class IncidentNumberValueObjectTest extends ValueObjectContractTest {

    public IncidentNumberValueObjectTest() {
        super(IncidentNumber.of("789"), "789");
    }

    public static Object[] equalsInstances() {
        return new Object[]{ IncidentNumber.of("789"),IncidentNumber.of("789")};
    }


    public static Object[] nonEqualsInstances() {
        return new Object[]{ IncidentNumber.of("897"), IncidentNumber.of("656")};
    }

}
