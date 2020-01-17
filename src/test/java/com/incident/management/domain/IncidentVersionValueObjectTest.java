package com.incident.management.domain;

import com.incident.management.utils.ValueObjectContractTest;

public class IncidentVersionValueObjectTest extends ValueObjectContractTest {

    public IncidentVersionValueObjectTest() {
        super(IncidentVersion.of("bug","1.0"),"bug-1.0");
    }

    public static Object[] equalsInstances() {
        return new Object[]{ IncidentVersion.of("bug","1.0")
                ,IncidentVersion.of("bug","1.0")};
    }


    public static Object[] nonEqualsInstances() {
        return new Object[]{ IncidentVersion.of("down","1.0")
                ,IncidentVersion.of("down","2.0")};
    }

}
