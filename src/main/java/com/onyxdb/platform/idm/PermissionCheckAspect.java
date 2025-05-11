package com.onyxdb.platform.idm;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.idm.common.PermissionCheck;
import com.onyxdb.platform.idm.common.jwt.SecurityContextUtils;
import com.onyxdb.platform.idm.models.exceptions.ForbiddenException;


/**
 * @author ArtemFed
 */
@Aspect
@Component
public class PermissionCheckAspect {

    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(permissionCheck)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, PermissionCheck permissionCheck) throws Throwable {
        String entity = permissionCheck.entity();
        String action = permissionCheck.action();
        String resourceIdExpr = permissionCheck.resourceId();

        // Получение resourceId через SpEL (если указано)
        UUID resourceId = parseResourceId(joinPoint, resourceIdExpr);

        // Генерация ключей для проверки
        List<String> permissionKeys = generatePermissionKeys(entity, action, resourceId);

        // Получение текущих разрешений
        Map<String, Optional<Map<String, Object>>> permissions = SecurityContextUtils.getCurrentPermissions();

        // Проверка наличия хотя бы одного разрешения
        boolean hasAccess = permissionKeys.stream()
                .anyMatch(permissions::containsKey);

        if (!hasAccess) {
            if (resourceId == null) {
                throw new ForbiddenException(
                        String.format("Access Forbidden. Your token has no access to %s-%s", entity, action));
            }
            throw new ForbiddenException(
                    String.format("Access Forbidden. Your token has no access to %s-%s-%s", entity, action, resourceId));
        }

        return joinPoint.proceed();
    }

    private UUID parseResourceId(ProceedingJoinPoint joinPoint, String resourceIdExpr) {
        if (resourceIdExpr.isEmpty()) return null;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();

        // Создание контекста для SpEL
        EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        try {
            Expression exp = parser.parseExpression(resourceIdExpr);
            Object value = exp.getValue(context);
            return convertToUUID(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid resourceId expression: " + resourceIdExpr, e);
        }
    }

    private UUID convertToUUID(Object value) {
        if (value instanceof UUID) return (UUID) value;
        if (value instanceof String) return UUID.fromString((String) value);
        throw new IllegalArgumentException("ResourceId must be UUID or String");
    }

    private List<String> generatePermissionKeys(String entity, String action, UUID resourceId) {
        List<String> keys = new ArrayList<>();

        if (resourceId != null) {
            keys.add(String.format("%s-%s-%s", entity, action, resourceId));
            keys.add(String.format("%s-%s-any", entity, action));
        }

        keys.addAll(List.of(
                String.format("%s-%s", entity, action),
                String.format("%s-any", entity),
                String.format("global-%s", action),
                "global-any"
        ));

        return keys;
    }
}