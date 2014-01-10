package com.java.meta.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checking immutability of field. Type of field must bee DeepImmutable
 *
 * DeepImmutable type is next type:
 * - all filed must or final or transient
 * - all field types must be DeepImmutable
 * - if type is Generic, parametrise type must be set:
 *  - List<?> - incorrect type
 *  - List - incorrect type
 *  - List<Integer> - correct type
 * - arrays must be with DeepImmutable types
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface DeepImmutable {
}
