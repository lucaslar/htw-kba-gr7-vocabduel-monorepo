package de.htwberlin.kba.gr7.vocabduel.game_administration.assets;

import javax.persistence.EntityTransaction;

public class EntityTransactionMock implements EntityTransaction {
    @Override
    public void begin() {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void setRollbackOnly() {

    }

    @Override
    public boolean getRollbackOnly() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }
}
