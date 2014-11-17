package org.jon.ivmark.footballcoupons.application.game.domain.event;

import com.google.common.base.Predicate;
import org.jon.ivmark.footballcoupons.application.game.infrastructure.event.EventLogException;
import org.jon.ivmark.footballcoupons.application.game.infrastructure.event.Replayble;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import static com.google.common.collect.Sets.filter;
import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withParameters;

public abstract class ReflectionsEventHandler<T extends DomainEvent> implements EventHandler<T> {
    @Override
    public void replayEvent(Object event) {

        //noinspection unchecked
        Set<Method> methods = filter(getAllMethods(getClass(), withParameters(event.getClass())),
                                     isReplayble());
        for (Method method : methods) {
            invokeEventHandlerMethod(event, method);
        }
    }

    private void invokeEventHandlerMethod(Object event, Method method) {
        try {
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EventLogException("Failed to replay event", e);
        }
    }

    private Predicate<? super Method> isReplayble() {
        return new Predicate<Method>() {
            @Override
            public boolean apply(Method method) {
                return method.isAnnotationPresent(Replayble.class);
            }
        };
    }
}
