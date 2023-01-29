package br.com.calves.cookspringboot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by clezio on 12/08/16.
 */
public class DatabaseMysql implements IDatabase {

    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private String url;

    private Connection con;

    private String user;

    private String password;

    public DatabaseMysql(String host, Integer port, String dbName, String user, String password) {
        this.url = String.format("jdbc:mysql://%s:%d/%s?reconnect=true&useSSL=false", host, port, dbName);
        this.user = user;
        this.password = password;
    }

    public void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        con = DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        return con;
    }

    public void closeConnection() {
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            ;
        }
    }
}
