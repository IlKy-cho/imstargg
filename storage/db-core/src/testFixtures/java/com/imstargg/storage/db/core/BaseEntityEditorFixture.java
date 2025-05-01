package com.imstargg.storage.db.core;

import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class BaseEntityEditorFixture {

    private final Object entity;

    public BaseEntityEditorFixture(Object entity) {
        this.entity = entity;
    }

    public void editDeleted(boolean deleted) {
        if (!BaseEntity.class.isAssignableFrom(entity.getClass())) {
            throw new IllegalArgumentException(entity.getClass().getName() + " 클래스는 BaseEntity를 상속해야 합니다.");
        }

        if (deleted) {
            ((BaseEntity) entity).delete();
        } else {
            ((BaseEntity) entity).restore();
        }
    }

    public void editId(Object id) {
        List<Field> idFields = findFieldByAnnotation(Id.class);

        if (idFields.isEmpty()) {
            throw new IllegalArgumentException(entity.getClass().getName() +
                    " 클래스에 @Id 어노테이션이 붙은 필드를 찾을 수 없습니다.");
        }

        for (Field field : idFields) {
            setFieldValue(field, id);
        }
    }

    public void editCreatedAt(Object createdAt) {
        List<Field> createdAtFields = Stream.of(
                        findFieldByAnnotation(CreationTimestamp.class), findFieldByAnnotation(CreatedDate.class)
                ).flatMap(List::stream).distinct().toList();

        if (createdAtFields.isEmpty()) {
            throw new IllegalArgumentException(entity.getClass().getName() +
                    " 클래스에 @CreationTimestamp 또는 @CreatedDate 어노테이션이 붙은 필드를 찾을 수 없습니다.");
        }

        for (Field field : createdAtFields) {
            setFieldValue(field, createdAt);
        }
    }

    public void editUpdatedAt(Object updatedAt) {
        List<Field> updatedAtFields = Stream.of(
                        findFieldByAnnotation(UpdateTimestamp.class), findFieldByAnnotation(LastModifiedDate.class)
        ).flatMap(List::stream).distinct().toList();

        if (updatedAtFields.isEmpty()) {
            throw new IllegalArgumentException(entity.getClass().getName() +
                    " 클래스에 @UpdateTimestamp 또는 @LastModifiedDate 어노테이션이 붙은 필드를 찾을 수 없습니다.");
        }

        for (Field field : updatedAtFields) {
            setFieldValue(field, updatedAt);
        }
    }

    private <T extends Annotation> List<Field> findFieldByAnnotation(Class<T> annotationClass) {
        List<Field> foundFields = new ArrayList<>();
        Class<?> currentClass = entity.getClass();

        while (currentClass != null && currentClass != Object.class) {
            List<Field> fields = Arrays.stream(currentClass.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(annotationClass))
                    .toList();

            foundFields.addAll(fields);

            currentClass = currentClass.getSuperclass();
        }

        return foundFields.stream().toList();
    }

    private void setFieldValue(Field field, Object value) {
        field.setAccessible(true); // private 필드에 접근 가능하도록 설정

        try {
            field.set(entity, value); // 필드 값 변경
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("필드에 접근할 수 없습니다: " + e.getMessage(), e);
        }
    }
}
