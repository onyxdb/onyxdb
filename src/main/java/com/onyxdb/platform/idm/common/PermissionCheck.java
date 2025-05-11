package com.onyxdb.platform.idm.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author ArtemFed
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionCheck {
    String entity();         // Название сущности (например, "account/product")

    String action();         // Действие (например, "create/get/edit/delete")

    String resourceId() default ""; // SpEL-выражение для получения resourceId (например, "#productId")
}
