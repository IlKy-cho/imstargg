package com.imstargg.batch.support;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractInMemoryCacheRepositoryTest {

    @Test
    void 캐시가_있을_경우_캐시에서_데이터를_가져온다() {
        // given
        TestRepository testRepository = new TestRepository();
        testRepository.save(new TestData(1L));
        testRepository.save(new TestData(2L));
        TestRepositoryInMemoryCache testRepositoryInMemoryCache = new TestRepositoryInMemoryCache(testRepository);

        // when
        Optional<TestData> result = testRepositoryInMemoryCache.find(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getValue()).isEqualTo("value-1");
        assertThat(testRepository.getFindCount()).isEqualTo(0);
        assertThat(testRepository.getSaveCount()).isEqualTo(2);
    }

    @Test
    void 캐시가_없을_경우_데이터를_로드_후_캐시에_넣고_반환한다() {
        // given
        TestRepository testRepository = new TestRepository();
        TestRepositoryInMemoryCache testRepositoryInMemoryCache = new TestRepositoryInMemoryCache(testRepository);
        testRepository.save(new TestData(1L));

        // when
        Optional<TestData> result = testRepositoryInMemoryCache.find(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getValue()).isEqualTo("value-1");
        assertThat(testRepository.getFindCount()).isEqualTo(1);
        assertThat(testRepository.getSaveCount()).isEqualTo(1);
    }

    @Test
    void 캐시도_없고_데이터도_존재하지_않을_경우_빈_값을_반환한다() {
        // given
        TestRepository testRepository = new TestRepository();
        TestRepositoryInMemoryCache testRepositoryInMemoryCache = new TestRepositoryInMemoryCache(testRepository);

        // when
        Optional<TestData> result = testRepositoryInMemoryCache.find(1L);

        // then
        assertThat(result).isEmpty();
        assertThat(testRepository.getFindCount()).isEqualTo(1);
        assertThat(testRepository.getSaveCount()).isEqualTo(0);
    }

    @Test
    void 데이터를_저장시_캐시에도_저장된다() {
        // given
        TestRepository testRepository = new TestRepository();
        TestRepositoryInMemoryCache testRepositoryInMemoryCache = new TestRepositoryInMemoryCache(testRepository);

        // when
        TestData testData = new TestData(1L);
        testRepositoryInMemoryCache.save(testData);
        Optional<TestData> foundData = testRepositoryInMemoryCache.find(1L);

        // then
        assertThat(testRepository.find(1L)).isPresent();
        assertThat(testRepository.getSaveCount()).isEqualTo(1);
        assertThat(foundData).isPresent();
        assertThat(foundData.get().getId()).isEqualTo(1L);
        assertThat(foundData.get().getValue()).isEqualTo("value-1");
        assertThat(testRepository.getFindCount()).isEqualTo(1);
    }

    static class TestRepositoryInMemoryCache extends AbstractInMemoryCacheRepository<Long, TestData> {

        private final TestRepository testRepository;

        TestRepositoryInMemoryCache(TestRepository testRepository) {
            super(testRepository::findAll);
            this.testRepository = testRepository;
        }

        @Override
        protected Long key(TestData value) {
            return value.getId();
        }

        @Override
        protected Optional<TestData> findData(Long key) {
            return testRepository.find(key);
        }

        @Override
        protected TestData saveData(TestData value) {
            return testRepository.save(value);
        }
    }

    static class TestRepository {

        private final ConcurrentHashMap<Long, TestData> data = new ConcurrentHashMap<>();
        private final AtomicInteger findCount = new AtomicInteger(0);
        private final AtomicInteger saveCount = new AtomicInteger(0);

        TestData save(TestData testData) {
            data.put(testData.getId(), testData);
            saveCount.incrementAndGet();
            return testData;
        }

        Optional<TestData> find(Long id) {
            findCount.incrementAndGet();
            return Optional.ofNullable(data.get(id));
        }

        List<TestData> findAll() {
            return List.copyOf(data.values());
        }

        int getFindCount() {
            return findCount.get();
        }

        int getSaveCount() {
            return saveCount.get();
        }
    }

    static class TestData {
        private final Long id;
        private final String value;

        TestData(Long id) {
            this.id = id;
            this.value = "value-" + id;
        }

        Long getId() {
            return id;
        }

        String getValue() {
            return value;
        }
    }
}