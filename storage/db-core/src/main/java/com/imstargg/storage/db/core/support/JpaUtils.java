package com.imstargg.storage.db.core.support;

import jakarta.persistence.Table;

import java.util.Optional;

public abstract class JpaUtils {

    private JpaUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> String getTableName(Class<T> entityType) {
        return Optional.ofNullable(
                        entityType.getAnnotation(Table.class)
                ).map(Table::name)
                .filter(name -> !name.isBlank())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Entity[" + entityType.getName() + "] does not have @Table annotation or name attribute is empty."
                ));
    }
}
