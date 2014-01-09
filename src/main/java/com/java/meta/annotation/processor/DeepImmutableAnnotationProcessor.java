package com.java.meta.annotation.processor;

import com.java.meta.annotation.DeepImmutable;
import com.java.meta.annotation.DefaultVisibilityLevelForTesting;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by b0noI on 08/01/2014.
 */
//@MetaInfServices(javax.annotation.processing.Processor.class)
@SupportedAnnotationTypes("com.java.meta.annotation.DeepImmutable")
public class DeepImmutableAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DeepImmutable.class);
        for(Element e : elements){
            if (!e.getModifiers().contains(Modifier.FINAL))
                failElement(e);
            if(!classValid(e.getClass()))
                failElement(e);
        }
        return true;
    }

    // Private section

    private static final String FAIL_STRING = "@DeepImmutable annotated fields must be with deep final fields only";

    private static final Set<Class> VALID_CLASSES = new CopyOnWriteArraySet<>();

    private static final Set<Class> INVALID_CLASSES = new CopyOnWriteArraySet<>();

    private void failElement(final Element element) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                FAIL_STRING, element);
    }


    @DefaultVisibilityLevelForTesting
    boolean classValid(final Class<?> classForCheck) {

        if (VALID_CLASSES.contains(classForCheck))
            return true;

        if (INVALID_CLASSES.contains(classForCheck))
            return false;

        final int transientModifier = java.lang.reflect.Modifier.TRANSIENT;
        final int finalModifier = java.lang.reflect.Modifier.FINAL;

        for (Field declareField : classForCheck.getDeclaredFields()) {
            if ((declareField.getModifiers() & transientModifier) == transientModifier) {
                continue;
            } else if ((declareField.getModifiers() & finalModifier) == finalModifier) {
                if (declareField.getType().isPrimitive())
                    return true;
                if (!classValid(declareField.getClass()))
                    return false;
            } else {
                INVALID_CLASSES.add(classForCheck);
                return false;
            }
        }

        VALID_CLASSES.add(classForCheck);
        return true;
    }

}
