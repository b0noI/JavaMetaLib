package com.java.meta.annotation.processor;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by никита on 08.01.14.
 */
public class DeepImmutableAnnotationProcessorTest {



    public class Allfinal_1 {
         final AllFinal_1_2 foo2 = new AllFinal_1_2();
    }

    public class AllFinal_1_2 {
        final JustFinalPrimitive_1_2_3 foo3 = new JustFinalPrimitive_1_2_3();
    }

    public class JustFinalPrimitive_1_2_3 {
        final int foo = 0;
    }

    public class InnerNotFinal_1 {
        HaveNotFinal_1_2 haveNotFinal = new HaveNotFinal_1_2();
    }


    public class HaveNotFinal_1_2 {
        JustFinalPrimitive_1_2_3 justFinalPrimitive_1_2_3 = new JustFinalPrimitive_1_2_3();
    }

    public class JustNonFinalPrimitive {
        int foo = 0;
    }

    public class FinalWithoutFields{

    }

    public class NonFinalWithoutFields{

    }

    public class FinalHasFinal {
        final FinalWithoutFields finalWithoutFields = new FinalWithoutFields();
    }

    public final class FinalHasNoFinal{
        NonFinalWithoutFields nonFinalWithoutFields = new NonFinalWithoutFields();
    }

    final Allfinal_1 foo1 = new Allfinal_1();
    AllFinal_1_2 foo2 = new AllFinal_1_2();
    JustFinalPrimitive_1_2_3 foo3 = new JustFinalPrimitive_1_2_3();


    DeepImmutableAnnotationProcessor deep = new DeepImmutableAnnotationProcessor();


    @Test
    public void testClassValid_with_finals_1() throws Exception {

        assertTrue(deep.classValid(Allfinal_1.class));
    }

    @Test
    public void testClassValid_with_finals_2() throws Exception {
        assertTrue(deep.classValid(AllFinal_1_2.class));
    }

    @Test
    public void testClassValid_with_finals_3() throws Exception {
        assertTrue(deep.classValid(FinalHasFinal.class));
    }

    @Test
    public void testClassValid_wihtout_finals1() throws Exception {
        assertFalse(deep.classValid(InnerNotFinal_1.class));
    }

    @Test
    public void testClassValid_without_finals_2() throws Exception {
        assertFalse(deep.classValid(HaveNotFinal_1_2.class));
    }

    @Test
    public void testClassValid_without_finals_3() throws Exception {
        assertFalse(deep.classValid(FinalHasNoFinal.class));
    }




}
