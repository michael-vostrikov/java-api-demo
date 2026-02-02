package com.app.common.system;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import java.lang.InterruptedException;
import java.util.function.Supplier;

@Service
public class LockService {
    @Autowired
    private EntityManager entityManager;

    public <T> T withLock(String entityName, int id, int timeoutMs, Supplier<T> actionCallback) {
        lock(entityName, id, timeoutMs);
        try {
            return actionCallback.get();
        } finally {
            unlock(entityName, id);
        }
    }

    public void lock(String entityName, int id, int timeoutMs) {
        var delayMs = 20;
        var startTime = System.nanoTime();
        var currentTime = 0L;
        var waitingTime = 0L;

        while (waitingTime < 1_000_000L * timeoutMs) {
            Query query = entityManager.createNativeQuery("SELECT pg_try_advisory_lock(:key1, :key2)")
                .setParameter("key1", entityName.hashCode())
                .setParameter("key2", id);

            var isLocked = (Boolean) query.getSingleResult();
            if (isLocked) {
                return;
            }

            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                break;
            }

            currentTime = System.nanoTime();
            waitingTime = currentTime - startTime;
        }

        throw new RuntimeException("Cannot lock resource: " + entityName + ":" + id);
    }

    public void unlock(String entityName, int id) {
        Query query = entityManager.createNativeQuery("SELECT pg_advisory_unlock(:key1, :key2)")
            .setParameter("key1", entityName.hashCode())
            .setParameter("key2", id);

        var isUnlocked = (Boolean) query.getSingleResult();
        if (!isUnlocked) {
            var message = "Cannot unlock resource: " + entityName + ":" + id + ". Probably number of lock/unlock calls is unequal.";
            throw new RuntimeException(message);
        }
    }
}
