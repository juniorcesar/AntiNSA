/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.database;

import br.edu.utfpr.cm.antinsa.service.googledrive.DataFile;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author junior
 */
public class DaoDataFile {

    private TransactionManager tm;
    private Statement stmt;

    public DaoDataFile() throws ClassNotFoundException, SQLException {
        initTransactionManager();
    }

    public void insert(String name, long size, long date, String hash) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO DATAFILE (NAME,SIZE,DATE,HASH) "
                + "VALUES ('" + name + "', " + size + "," + date + ", '" + hash + "');";
        stmt.executeUpdate(sql);
    }

    public void update(String name, long size, long date, String hash) throws SQLException {
        String sql = "UPDATE DATAFILE SET NAME = '" + name + "',                     WHERE NAME LIKE " + name + ";";
        stmt.executeUpdate(sql);
    }

    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM DATAFILE;";
        stmt.executeUpdate(sql);
    }

    public DataFile getDataFile(String fileName) throws SQLException, ClassNotFoundException {
        DataFile dataFile = null;
        ResultSet rs = stmt.executeQuery("SELECT * FROM DATAFILE WHERE NAME LIKE '" + fileName + "';");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int size = rs.getInt("size");
            long date = rs.getInt("date");
            String hash = rs.getString("hash");
            dataFile = new DataFile(id, name, size, date, hash);
        }
        rs.close();
        return dataFile;
    }

    public List<DataFile> listAll() throws SQLException, ClassNotFoundException {
        List<DataFile> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM DATAFILE;");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int size = rs.getInt("size");
            long date = rs.getInt("date");
            String hash = rs.getString("hash");
            list.add(new DataFile(id, name, size, date, hash));
        }
        rs.close();
        return list;
    }

    private void initTransactionManager() throws SQLException, ClassNotFoundException {
        tm = new TransactionManager();
        stmt = tm.getStatement();
    }

    public TransactionManager getTransactionManager() {
        return tm;
    }

    public boolean dataFileExists(String fileName) throws SQLException, ClassNotFoundException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM DATAFILE WHERE NAME LIKE '" + fileName + "';");
        return rs.next();
    }
}
