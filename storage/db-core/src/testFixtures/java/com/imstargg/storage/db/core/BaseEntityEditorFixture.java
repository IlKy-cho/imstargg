package com.imstargg.storage.db.core;

import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BaseEntityEditorFixture {

    private final Object entity;

    public BaseEntityEditorFixture(Object entity) {
        this.entity = entity;
    }

    public void editId(Object id) {
        Class<?> entityClass = entity.getClass();
        
        Optional<Field> idField = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst();

        if (idField.isEmpty()) {
            throw new IllegalArgumentException(entityClass.getSimpleName() +
                    " 클래스에 @Id 어노테이션이 붙은 필드를 찾을 수 없습니다.");
        }

        Field field = idField.get();
        field.setAccessible(true);

        try {
            field.set(entity, id);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void editDeleted(boolean deleted) {
        Class<?> entityClass = entity.getClass();

        if (!BaseEntity.class.isAssignableFrom(entityClass)) {
            throw new IllegalArgumentException(entityClass.getSimpleName() + " 클래스는 BaseEntity를 상속해야 합니다.");
        }

        if (deleted) {
            ((BaseEntity) entity).delete();
        } else {
            ((BaseEntity) entity).restore();
        }
    }

    public void editCreatedAt(Object createdAt) {
        Class<?> entityClass = entity.getClass();

        List<Field> createdAtFields = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field ->
                        field.isAnnotationPresent(CreatedDate.class)
                                || field.isAnnotationPresent(CreationTimestamp.class)
                ).toList();

        if (createdAtFields.isEmpty()) {
            throw new IllegalArgumentException(entityClass.getSimpleName() +
                    " 클래스에 @CreatedDate 또는 @CreationTimestamp 어노테이션이 붙은 필드를 찾을 수 없습니다.");
        }

        for (Field field : createdAtFields) {
            field.setAccessible(true);
            try {
                field.set(entity, createdAt);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public void editUpdatedAt(Object updatedAt) {
        Class<?> entityClass = entity.getClass();

        List<Field> updatedAtFields = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field ->
                        field.isAnnotationPresent(LastModifiedDate.class)
                                || field.isAnnotationPresent(UpdateTimestamp.class)
                ).toList();

        if (updatedAtFields.isEmpty()) {
            throw new IllegalArgumentException(entityClass.getSimpleName() +
                    " 클래스에 @LastModifiedDate 또는 @UpdateTimestamp 어노테이션이 붙은 필드를 찾을 수 없습니다.");
        }

        for (Field field : updatedAtFields) {
            field.setAccessible(true);
            try {
                field.set(entity, updatedAt);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
