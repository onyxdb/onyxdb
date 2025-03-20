package com.onyxdb.mdb.common;

import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * TransactionTemplate is a thread-safe class, but it contains settings entire life.
 * MemoriousTransactionTemplate helps to change settings temporarily for one execution
 * and then restore them to the previous state.
 * <p/>
 * <a href="https://docs.spring.io/spring-framework/reference/data-access/transaction/programmatic.html#tx-prog-template-settings">Docs</a>
 *
 * @author foxleren
 */
public class MemoriousTransactionTemplate extends TransactionTemplate {
    private volatile int prevIsolation = getIsolationLevel();

    public MemoriousTransactionTemplate(PlatformTransactionManager transactionManager) {
        super(transactionManager);
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> T executeWithIsolation(
            @NonNull TransactionCallback<T> action,
            int isolation
    ) throws TransactionException {
        setCachedIsolation(isolation);
        var result = execute(action);
        restoreSettings();
        return result;
    }

    private synchronized void setCachedIsolation(int isolation) {
        prevIsolation = getIsolationLevel();
        setIsolationLevel(isolation);
    }

    private synchronized void restoreSettings() {
        setIsolationLevel(prevIsolation);
    }
}
