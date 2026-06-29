package com.mes.domain.common;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BaseEntityTest {

    @Test
    void constructor_setsIdAndCreatedAt() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        TestEntity entity = new TestEntity(id, now);

        assertEquals(id, entity.getId());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
        assertFalse(entity.isDeleted());
    }

    @Test
    void constructor_allowsNullIdForAutoIncrement() {
        // StockTransactionのtransactionId=auto-incrementのため、null idを許容
        TestEntity entity = new TestEntity(null, LocalDateTime.now());
        assertNull(entity.getId());
        assertNotNull(entity.getCreatedAt());
    }

    @Test
    void constructor_throwsOnNullCreatedAt() {
        assertThrows(NullPointerException.class, () -> new TestEntity(UUID.randomUUID(), null));
    }

    @Test
    void touchUpdatesUpdatedAt() {
        LocalDateTime original = LocalDateTime.of(2025, 1, 1, 0, 0);
        TestEntity entity = new TestEntity(UUID.randomUUID(), original);
        assertEquals(original, entity.getUpdatedAt());

        // Sleep briefly to ensure time difference
        try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        entity.touch();
        assertTrue(entity.getUpdatedAt().isAfter(original));
    }

    @Test
    void deleteSetsDeletedAt() {
        TestEntity entity = new TestEntity(UUID.randomUUID(), LocalDateTime.now());
        assertFalse(entity.isDeleted());

        entity.delete();
        assertTrue(entity.isDeleted());
        assertTrue(entity.getUpdatedAt().isAfter(entity.getCreatedAt()));
    }

    @Test
    void equalsAndHashCode() {
        UUID sameId = UUID.randomUUID();
        TestEntity a = new TestEntity(sameId, LocalDateTime.now());
        TestEntity b = new TestEntity(sameId, LocalDateTime.of(2025, 6, 1, 0, 0));

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, "not-an-entity");
    }

    @Test
    void copyReturnsNewEntityWithGivenId() {
        TestEntity original = new TestEntity(UUID.randomUUID(), LocalDateTime.of(2025, 1, 1, 0, 0));
        UUID newId = UUID.randomUUID();

        BaseEntity copied = original.copy(newId);
        assertEquals(newId, copied.getId());
        assertNotEquals(original.getId(), copied.getId());
    }

    /** Concrete test entity implementing abstract methods. */
    static class TestEntity extends BaseEntity {
        private final String name;

        public TestEntity(UUID id, LocalDateTime createdAt) {
            super(id, createdAt);
            this.name = "test";
        }

        @Override
        public BaseEntity copy(UUID newId) {
            return new TestEntity(newId, getCreatedAt());
        }
    }
}
