package com.xixi.dao;


import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class baseDao {
    private static  String driver;
    private static  String url;
    private static String username;
    private static  String password;
    static {
        InputStream is = baseDao.class.getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        driver = properties.getProperty("driverClass");
        url = properties.getProperty("url");
         username = properties.getProperty("username");
         password = properties.getProperty("password");
    }
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection=null;
         Class.forName(driver);
       connection = DriverManager.getConnection(url,username,password);
        return connection;
    }
    public static ResultSet execute(Connection connection,String sql,Object[] prams,PreparedStatement preparedStatement,ResultSet resultSet) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i=0;i<prams.length;i++){
            preparedStatement.setObject(i+1,prams[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public static int  execute(Connection connection,String sql,Object[] prams,PreparedStatement preparedStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i=0;i<prams.length;i++){
            preparedStatement.setObject(i+1,prams[i]);
        }
        int row = preparedStatement.executeUpdate();
        return row;
    }
    public static void close(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet) throws SQLException {
        if (connection!=null){
            connection.close();
        }
        if (preparedStatement!=null){
            preparedStatement.close();
        }
        if (resultSet!=null){
            resultSet.close();
        }

    }
}
