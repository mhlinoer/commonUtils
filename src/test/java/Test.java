import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linoer.app.model.User;
import com.linoer.app.model.db.JdbcModel;
import com.linoer.app.model.ftp.FtpConnection;
import com.linoer.app.utils.CommonHelper;
import com.linoer.app.utils.file.ftp.FtpUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Test {

    static void insertDataToOracle(JdbcModel jdbcModel, int dataNum) throws ClassNotFoundException, SQLException {

        Class.forName(jdbcModel.getDriver());
        Connection connection = DriverManager.getConnection(jdbcModel.getUrl(), jdbcModel.getUsername(), jdbcModel.getPassword());
        if (null == connection) {
            return;
        }
        connection.setAutoCommit(false);
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO index_test(TEXT1,TEXT2) VALUES (?,?)");
        for (int i = 0; i < dataNum; i++) {
            stmt.setString(1, "tom_" + UUID.randomUUID());
            stmt.setString(2, "jerry_" + UUID.randomUUID());
            stmt.addBatch();

            if (i % 50000 == 0) {
                stmt.executeBatch();
                connection.commit();
            }
        }

        stmt.executeBatch();
        connection.commit();

    }

    private static void mockInsertDataToOracle() {
        JdbcModel jdbcModel = new JdbcModel();
        jdbcModel.setDriver("oracle.jdbc.driver.OracleDriver");
        jdbcModel.setUrl("jdbc:oracle:thin:@127.0.0.1:1521:helowin");
        jdbcModel.setUsername("root");
        jdbcModel.setPassword("root");
        try {
            insertDataToOracle(jdbcModel, 4499999);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void test2() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"name\":\"tom\",\"myAge\":1,\"birthday\":\"2000-01-01 16:00:02\"}";
        try {
            User user = objectMapper.readValue(json, User.class);
            System.out.println(user.toString());
            user.setName("jerry");
            String userJson = objectMapper.writeValueAsString(user);
            System.out.println(userJson);
            List<User> users = objectMapper.readValue(json, new TypeReference<List<User>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        String s = "../../../../application.yml";
//        String base = "/home/ap/asap/flume";
//        System.out.println(CommonHelper.generatorRealPath(base, s));
////        String[] splitDir = base.split("/");
//        String[] splitDir = s.split("\\.\\./", -1);
//        System.out.println(splitDir.length);
//        for (String sp:splitDir){
//            System.out.println(sp);
//        }
//        FtpUtil ftpUtil = new FtpUtil();
//        ftpUtil.initFtp(new FtpConnection(
//                "muhenglv",
//                "10.211.55.3",
//                "lvmuheng"
//        ));
//        ftpUtil.test("/home/muhenglv", 4, 1);
        test2();
    }
}
