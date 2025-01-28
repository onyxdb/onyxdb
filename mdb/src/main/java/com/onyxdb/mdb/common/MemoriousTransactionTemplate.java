package com.onyxdb.mdb.common;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * TransactionTemplate is a thread-safe class, but it contains settings entire life.
 * MemoriousTransactionTemplate helps to change settings temporarily for one execution
 * and then restore them to the previous state.
 * Docs: <a href="https://docs.spring.io/spring-framework/reference/data-access/transaction/programmatic.html#tx-prog-template-settings">...</a>
 *
 * @author foxleren
 */
@Component
public class MemoriousTransactionTemplate extends TransactionTemplate {
    private volatile int previousPropagation;

    public MemoriousTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.previousPropagation = transactionTemplate.getPropagationBehavior();
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> T execute(@NonNull TransactionCallback<T> action, int propagation) throws TransactionException {
        setPropagation(propagation);
        var result = execute(action);
        restoreSettings();
        return result;
    }

    private synchronized void setPropagation(int propagation) {
        previousPropagation = getPropagationBehavior();
        setPropagationBehavior(propagation);
    }

    private synchronized void restoreSettings() {
        setPropagationBehavior(previousPropagation);
    }
}
