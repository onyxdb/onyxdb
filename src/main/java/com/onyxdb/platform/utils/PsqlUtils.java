package com.onyxdb.platform.utils;

import java.util.function.Supplier;

import org.jooq.Index;
import org.jooq.Record;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

public class PsqlUtils {
    public static <RECORD extends Record, EXCEPTION extends Throwable> void handleDataAccessEx(
            Throwable e,
            TableImpl<RECORD> table,
            UniqueKey<RECORD> key,
            Supplier<? extends EXCEPTION> exceptionSupplier
    ) throws EXCEPTION {
        if (e.getCause() instanceof PSQLException psqlException) {
            ServerErrorMessage serverErrorMessage = psqlException.getServerErrorMessage();
            if (serverErrorMessage != null) {
                boolean tableMatches = table.getName().equals(serverErrorMessage.getTable());
                boolean constraintMatches = key.getName().equals(serverErrorMessage.getConstraint());

                if (tableMatches && constraintMatches) {
                    throw exceptionSupplier.get();
                }
            }
        }
    }

    public static <RECORD extends Record, EXCEPTION extends Throwable> void handleDataAccessEx(
            Throwable e,
            TableImpl<RECORD> table,
            Index index,
            Supplier<? extends EXCEPTION> exceptionSupplier
    ) throws EXCEPTION {
        if (e.getCause() instanceof PSQLException psqlException) {
            ServerErrorMessage serverErrorMessage = psqlException.getServerErrorMessage();
            if (serverErrorMessage != null) {
                boolean tableMatches = table.getName().equals(serverErrorMessage.getTable());
                boolean constraintMatches = index.getName().equals(serverErrorMessage.getConstraint());

                if (tableMatches && constraintMatches) {
                    throw exceptionSupplier.get();
                }
            }
        }
    }
}
