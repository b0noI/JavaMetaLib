package com.java.meta.annotation.processor;

import com.java.meta.annotation.DeepImmutable;
import com.java.meta.annotation.DefaultVisibilityLevelForTesting;
import org.kohsuke.MetaInfServices;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import java.lang.reflect.Modifier;;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import static javax.lang.model.element.Modifier.FINAL;

/**
 * Created by b0noI on 08/01/2014.
 */
@MetaInfServices(javax.annotation.processing.Processor.class)
@SupportedAnnotationTypes("com.java.meta.annotation.DeepImmutable")
public class DeepImmutableAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DeepImmutable.class);
        for(Element e : elements) {
            if (!e.getModifiers().contains(FINAL))
                failElement(e);
            if(!classValid(convertToClass(e.asType())))
                failElement(e);
        }
        return true;
    }

    // Private section

    private static final String FAIL_STRING = "@DeepImmutable annotated fields must be with deep final fields only";

    private static final Set<Class> VALID_CLASSES = new CopyOnWriteArraySet<>();

    private static final Set<Class> INVALID_CLASSES = new CopyOnWriteArraySet<>();

    private static Class<?> convertToClass(TypeMirror typeMirror) {
        try {
            return Class.forName(typeMirror.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    @DefaultVisibilityLevelForTesting
    static boolean classValid(final Class<?> classForCheck) {
        return classValid(classForCheck, new HashSet<Class<?>>());
    }

    private static boolean classValid(final Class<?> classForCheck, final Set<Class<?>> checkedClasses) {

        if (classForCheck == null || classForCheck.isPrimitive())
            return true;

        if (VALID_CLASSES.contains(classForCheck))
            return true;

        if (INVALID_CLASSES.contains(classForCheck))
            return false;

        for (Field declareField : classForCheck.getDeclaredFields()) {
            final int declareFieldModifiers = declareField.getModifiers();
            if (Modifier.isTransient(declareFieldModifiers)) {
                continue;
            } else if (Modifier.isFinal(declareFieldModifiers)) {
                if (declareField.getType().isPrimitive())
                    return true;
                Class<?> nextClass = declareField.getClass();
                if (checkedClasses.contains(nextClass))
                    return true;
                checkedClasses.add(nextClass);
                if (!classValid(nextClass, checkedClasses))
                    return false;
            } else {
                INVALID_CLASSES.add(classForCheck);
                return false;
            }
        }

        VALID_CLASSES.add(classForCheck);
        return true;
    }

    private void failElement(final Element element) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                FAIL_STRING, element);
    }

}
