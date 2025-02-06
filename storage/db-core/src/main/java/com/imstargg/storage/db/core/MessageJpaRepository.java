package com.imstargg.storage.db.core;

import com.imstargg.core.enums.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MessageJpaRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByCode(String code);

    List<MessageEntity> findAllByCodeIn(Collection<String> codes);

    Optional<MessageEntity> findByCodeAndLang(String code, Language lang);
}
