/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.database;

import br.edu.utfpr.cm.antinsa.configuration.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 *
 * @author junior
 */
public class TransactionManager {

    private Connection connection = null;
    private Statement stmt;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection == null) {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + Config.STORE_CONFIG + "/antinsa.db");
            stmt = (Statement) connection.createStatement();
        }
        return connection;
    }

    public void createTable() throws ClassNotFoundException, SQLException {
        getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS DATAFILE "
                + "(ID INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,"
                + " NAME           TEXT UNIQUE   NOT NULL, "
                + " SIZE            INT     NOT NULL, "
                + " DATE            INT     NOT NULL, "
                + " HASH_LOCAL        TEXT NOT NULL, "
                + " HASH_CLOUD        TEXT NOT NULL)";
        stmt.executeUpdate(sql);
        close();
    }

    public Statement getStatement() throws ClassNotFoundException, SQLException {
        if (stmt == null) {
            stmt = getConnection().createStatement();
        }
        return stmt;
    }

    public void close() throws SQLException {
        stmt.close();
        connection.close();
    }

    public void commit() throws SQLException {
        connection.commit();
        close();
    }
}
