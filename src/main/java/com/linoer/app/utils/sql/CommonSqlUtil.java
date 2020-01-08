package com.linoer.app.utils.sql;

import com.linoer.app.model.db.JdbcModel;

import java.sql.*;

public class CommonSqlUtil {

    /**
     * 启用流式查询需要注意设置fetchSize为Integer.MIN_VALUE
     * @throws SQLException
     */
    public void doSteamQuery(JdbcModel jdbcModel, String sql) throws SQLException, ClassNotFoundException {
        Class.forName(jdbcModel.getDriver());
        Connection connection = DriverManager.getConnection(jdbcModel.getUrl(), jdbcModel.getUsername(), jdbcModel.getPassword());
        PreparedStatement statement;
        statement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        statement.setFetchSize(Integer.MIN_VALUE);

        long begin = System.currentTimeMillis();
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            //System.out.println(resultSet.getString(1));
        }

        long end = System.currentTimeMillis();
        System.out.println("selectStream span time=" + (end - begin) + "ms");
        resultSet.close();
        statement.close();
        connection.close();
    }

    public void doSteamQueryWithCursor(JdbcModel jdbcModel, String sql) throws SQLException, ClassNotFoundException {
        Class.forName(jdbcModel.getDriver());
        Connection connection = DriverManager.getConnection(jdbcModel.getUrl(), jdbcModel.getUsername(), jdbcModel.getPassword());
        PreparedStatement statement;
        statement = connection.prepareStatement(sql);
        statement.setFetchSize(5000);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            //System.out.println(resultSet.getString(1));
        }
        resultSet.close();
        statement.close();
        connection.close();
    }
}
