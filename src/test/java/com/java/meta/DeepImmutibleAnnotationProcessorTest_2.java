package com.java.meta;


import com.java.meta.annotation.processor.DeepImmutableAnnotationProcessor;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DeepImmutibleAnnotationProcessorTest_2 {

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
        assertTrue(deep.classValid(Allfinal_1.class));
    }




}
