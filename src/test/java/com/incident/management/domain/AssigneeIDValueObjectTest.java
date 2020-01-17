package com.incident.management.domain;

import com.incident.management.utils.ValueObjectContractTest;

import static org.junit.jupiter.api.Assertions.*;

class AssigneeIDValueObjectTest extends ValueObjectContractTest {

    public AssigneeIDValueObjectTest() {
        super(AssigneeID.of("habip@live.com"),"habip@live.com");
    }

    public static Object[] equalsInstances() {
        return new Object[]{ AssigneeID.of("habip@live.com")};
    }


    public static Object[] nonEqualsInstances() {
        return new Object[]{ AssigneeID.of("hakan@live.com")};
    }

}