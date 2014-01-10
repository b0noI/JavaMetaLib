package com.java.meta.annotation.processor;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class DeepImmutableAnnotationProcessorTest_old {
    public static class Allfinal_1 {
         final AllFinal_1_2 foo2 = new AllFinal_1_2();
    }

    public static class AllFinal_1_2 {
        final JustFinalPrimitive_1_2_3 foo3 = new JustFinalPrimitive_1_2_3();
    }

    public static class JustFinalPrimitive_1_2_3 {
        final int foo = 0;
    }

    public static class InnerNotFinal_1 {
        HaveNotFinal_1_2 haveNotFinal = new HaveNotFinal_1_2();
    }

    public static class HaveNotFinal_1_2 {
        JustFinalPrimitive_1_2_3 justFinalPrimitive_1_2_3 = new JustFinalPrimitive_1_2_3();
    }

    public static class JustNonFinalPrimitive {
        int foo = 0;
    }

    public class FinalWithoutFields{
    }

    public class NonFinalWithoutFields{
    }

    public final class FinalHasFinal {
        final FinalWithoutFields finalWithoutFields = new FinalWithoutFields();
    }

    public final class FinalHasNoFinal{
        NonFinalWithoutFields nonFinalWithoutFields = new NonFinalWithoutFields();
    }

    public final class WithFinalList{
        final ArrayList<Allfinal_1> list = new ArrayList<>();
    }

    public final class WithNonFinalList{
        ArrayList<InnerNotFinal_1> list = new ArrayList<>();
    }

    DeepImmutableAnnotationProcessor deep = new DeepImmutableAnnotationProcessor();
    ArrayList<Allfinal_1> list = new ArrayList<>();


    @Test
    public void testClassValid_with_class_with_NON_final_List() throws Exception {
        assertFalse(deep.classValid(WithNonFinalList.class));
    }

        @Test
    public void testClassValid_with_class_with_final_List() throws Exception {
        assertTrue(deep.classValid(WithFinalList.class));
    }


    @Test
    public void testClassValid_with_class_with_primitive_final_fields() throws Exception {
        assertTrue(deep.classValid(JustFinalPrimitive_1_2_3.class));
    }

    @Test
    public void testClassValid_with_class_without_fields() throws Exception {
        assertTrue(deep.classValid(FinalWithoutFields.class));
    }


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
