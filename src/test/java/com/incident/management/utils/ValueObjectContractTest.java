package com.incident.management.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ValueObjectContractTest {
    protected Object instance;
    protected String expectedInstanceString;

    public ValueObjectContractTest(Object instance, String expectedInstanceString) {
        this.instance = instance;
        this.expectedInstanceString = expectedInstanceString;
    }

    @Test
    public  void should_have_descriptive_textual_representation(){
        assertEquals(expectedInstanceString,instance.toString());
    }

    @ParameterizedTest
    @MethodSource("equalsInstances")
    public void should_be_equals_if_has_same_values(Object equalsInstance){
        //given:
        final Object some=instance;

        //when;
        boolean areEqual=some.equals(equalsInstance);

        //then:
        assertTrue(areEqual);

    }
    @ParameterizedTest
    @MethodSource("nonEqualsInstances")
    public void should_be_equals_if_different_values(Object nonEqual){
        //given:
        final Object some=instance;

        //when;
        boolean result=some.equals(nonEqual);

        //then:
        assertFalse(result);

    }
    @Test
    public void should_not_be_equals_if_null_passed(){
        //given:
        final Object some=instance;

        //when;
        boolean result= (some == null);

        //then:
        assertFalse(result);

    }
    @ParameterizedTest
    @MethodSource("equalsInstances")
    public void should_have_some_hash_codes_if_objects_equals(Object equalsInstance){
        //given:
        final Object some=instance;

        //when;
        boolean areEqual=some.hashCode()==equalsInstance.hashCode();

        //then:
        assertTrue(areEqual);

    }
    @ParameterizedTest
    @MethodSource("nonEqualsInstances")
    public void should_have_different_hash_codes_if_non_equals_instances(Object nonEqualInstance){
        //given:
        final Object some=instance;

        //when;
        boolean areEqual=some.hashCode()==nonEqualInstance.hashCode();

        //then:
        assertFalse(areEqual);

    }


}
