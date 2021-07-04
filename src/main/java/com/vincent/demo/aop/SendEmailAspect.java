package com.vincent.demo.aop;

import com.vincent.demo.auth.UserIdentity;
import com.vincent.demo.service.MailService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@Component
@Aspect
public class SendEmailAspect {

    @Autowired
    private UserIdentity userIdentity;

    @Autowired
    private MailService mailService;

    private static final Map<ActionType, String> SUBJECT_TEMPLATE_MAP;
    private static final Map<ActionType, String> MESSAGE_TEMPLATE_MAP;

    static {
        SUBJECT_TEMPLATE_MAP = new EnumMap<>(ActionType.class);
        SUBJECT_TEMPLATE_MAP.put(ActionType.CREATE, "New %s");
        SUBJECT_TEMPLATE_MAP.put(ActionType.UPDATE, "Update %s");
        SUBJECT_TEMPLATE_MAP.put(ActionType.DELETE, "Delete %s");

        MESSAGE_TEMPLATE_MAP = new EnumMap<>(ActionType.class);
        MESSAGE_TEMPLATE_MAP.put(ActionType.CREATE, "Hi, %s. There's a new %s (%s) created.");
        MESSAGE_TEMPLATE_MAP.put(ActionType.UPDATE, "Hi, %s. There's a %s (%s) updated.");
        MESSAGE_TEMPLATE_MAP.put(ActionType.DELETE, "Hi, %s. A %s (%s) is just deleted.");
    }

    @Pointcut("@annotation(com.vincent.demo.aop.SendEmail)")
    public void pointcut() {
    }

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void sendEmail(JoinPoint joinPoint, Object result) {
        if (userIdentity.isAnonymous()) {
            return;
        }

        SendEmail annotation = getAnnotation(joinPoint);
        String subject = composeSubject(annotation);
        String message = composeMessage(annotation, joinPoint, result);

        mailService.sendMail(subject, message,
                Collections.singletonList(userIdentity.getEmail()));
    }

    private SendEmail getAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(SendEmail.class);
    }

    private String composeSubject(SendEmail annotation) {
        String template = SUBJECT_TEMPLATE_MAP.get(annotation.action());
        return String.format(template, annotation.entity());
    }

    private String composeMessage(SendEmail annotation, JoinPoint joinPoint, Object entity) {
        String template = MESSAGE_TEMPLATE_MAP.get(annotation.action());

        int idParamIndex = annotation.idParamIndex();
        String entityId = idParamIndex == -1
                ? getEntityId(entity)
                : (String) joinPoint.getArgs()[idParamIndex];

        return String.format(template,
                userIdentity.getName(), annotation.entity(), entityId);
    }

    private String getEntityId(Object obj) {
        try {
            Field field = obj.getClass().getDeclaredField("id");
            field.setAccessible(true);
            return (String) field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return "";
        }
    }
}
