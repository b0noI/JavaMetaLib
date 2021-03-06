package com.java.meta.annotation.processor;

import com.java.meta.annotation.DeepImmutable;
import com.java.meta.annotation.DefaultVisibilityLevelForTesting;
import org.kohsuke.MetaInfServices;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import java.lang.reflect.Modifier;;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import static javax.lang.model.element.Modifier.FINAL;

/**
 * Processor for processing DeepImmutable annotation
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

    private static final Set<Class<?>> EXCEPTION = new HashSet<Class<?>>(){{
        add(String.class);
    }};

    private static final String FAIL_STRING = "@DeepImmutable annotated fields must be with deep final fields only";

    private static final Set<Class<?>> VALID_CLASSES = new CopyOnWriteArraySet<>(EXCEPTION);

    private static final Set<Class<?>> INVALID_CLASSES = new CopyOnWriteArraySet<>();

    private static Class<?> convertToClass(final TypeMirror typeMirror) {
        try {
            return Class.forName(typeMirror.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    @DefaultVisibilityLevelForTesting
    public static boolean classValid(final Class<?> classForCheck) {
        return classValid(classForCheck, new HashSet<Class<?>>());
    }

    private static boolean classValid(final Class<?> inputClass, final Set<Class<?>> checkedClasses) {

        Class<?> classForCheck = inputClass;

        if (classForCheck == null || classForCheck.isPrimitive())
            return true;

        if (classForCheck.isArray())
            classForCheck = classForCheck.getComponentType();

        if (!checkGeneric(classForCheck, checkedClasses)) {
            INVALID_CLASSES.add(classForCheck);
            return false;
        }

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
                declareField.getGenericType();
                Class<?> nextClass = declareField.getType();
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

    private static boolean checkGeneric(final Class<?> classForCheck, final Set<Class<?>> checkedClasses) {
        Type genericType = classForCheck.getGenericSuperclass();
        if (!(genericType instanceof ParameterizedType))
            return true;
        ParameterizedType t = (ParameterizedType) genericType;

        if (t.getActualTypeArguments().length == 0)
            return false;

        for (Type type : t.getActualTypeArguments()) {
            if (type instanceof TypeVariableImpl)
                return false;
            final Class<?> typeClass = (Class<?>) type;
            if (!classValid(typeClass, checkedClasses))
                return false;
        }
        return true;
    }

    private void failElement(final Element element) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                FAIL_STRING, element);
    }

}
