package de.htwberlin.kba.gr7;

import java.sql.SQLException;
import java.util.List;

public interface Database {
    int connect();

    int disconnect();

    int executeQuery(String query) throws SQLException;

    List<Object> executeSelectQuery(String query) throws SQLException;
}
