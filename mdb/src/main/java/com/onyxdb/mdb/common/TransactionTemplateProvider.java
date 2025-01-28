package com.onyxdb.mdb.common;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * TransactionTemplate is a thread-safe class, but it contains settings entire life.
 * So TransactionTemplateProvider helps to change settings temporarily for one execution
 * and then restore them to the previous state.
 * @author foxleren
 */
@Component
public class TransactionTemplateProvider implements TransactionOperations {
    private final TransactionTemplate transactionTemplate;

    private volatile int previousPropagation;

    public TransactionTemplateProvider(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
        this.previousPropagation = transactionTemplate.getPropagationBehavior();
    }

    public <T> T execute(@NonNull TransactionCallback<T> action) throws TransactionException {
        return transactionTemplate.execute(action);
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> T execute(@NonNull TransactionCallback<T> action, int propagation) throws TransactionException {
        setPropagation(propagation);
        var result = execute(action);
        restoreSettings();
        return result;
    }

    private synchronized void setPropagation(int propagation) {
        previousPropagation = transactionTemplate.getPropagationBehavior();
        transactionTemplate.setPropagationBehavior(propagation);
    }

    private synchronized void restoreSettings() {
        transactionTemplate.setPropagationBehavior(previousPropagation);
    }
}
