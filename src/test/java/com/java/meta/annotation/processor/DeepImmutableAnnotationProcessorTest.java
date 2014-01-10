package com.java.meta.annotation.processor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by никита on 10.01.14.
 */
public class DeepImmutableAnnotationProcessorTest {


    @Before
    public void setUp() throws Exception {

    }


    public class Allfinal_1 {
        final AllFinal_1_2 foo2 = new AllFinal_1_2();
    }

    public class AllFinal_1_2 {
        final JustFinalPrimitive_1_2_3 foo3 = new JustFinalPrimitive_1_2_3();
    }

    public class JustFinalPrimitive_1_2_3 {
        final int foo = 0;
    }



    @Test
    public void testClassValid_with_finals_List() throws Exception {
        DeepImmutableAnnotationProcessor deep = new DeepImmutableAnnotationProcessor();
        assertTrue(deep.classValid(DeepImmutableAnnotationProcessorTest_old.Allfinal_1.class));
    }
}
