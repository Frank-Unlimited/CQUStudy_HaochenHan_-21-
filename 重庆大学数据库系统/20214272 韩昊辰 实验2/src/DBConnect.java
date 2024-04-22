import java.sql.*;

public class DBConnect {
    static String JDBC_DRIVER = "com.huawei.opengauss.jdbc.Driver";
    static String DB_URL = "jdbc:opengauss://110.41.126.115:8000/CQU_DB2021?ApplicationName=app1";

    static String USER = "db_user2021_10";
    static String PASS = "db_user@123";

    public static ResultSet HandleSQL(String args, int type) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);


            if(type==1){    //更新相关操作
                stmt = conn.createStatement();
                stmt.executeUpdate(args);
            }
            else {      //查询相关操作
                try {
                    stmt = conn.createStatement();
                    return stmt.executeQuery(args);
                }catch (SQLException sANDe){
                      sANDe.printStackTrace();
                }
            }
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        return null;
    }

    public static void End_close() {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();


        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }

//    public static void HandleSQL(String SQL) throws SQLException, ClassNotFoundException {
//        Connection connection;
//        Statement statement;
//
//
//            Class.forName(JDBC_DRIVER);
//            connection = DriverManager.getConnection(DB_URL, userName, passWord);
//            statement = connection.createStatement();
//
//            statement.executeUpdate(SQL);
//
//    }
//
//
//    public static ResultSet returnInfoOperation(String SQL) {
//        Connection connection;
//        Statement statement;
//
//        try {
//            Class.forName(JDBC_DRIVER);
//            connection = DriverManager.getConnection(DB_URL, userName, passWord);
//            statement = connection.createStatement();
//
//            return statement.executeQuery(SQL);
//        }catch (SQLException | ClassNotFoundException sANDe){
//            sANDe.printStackTrace();
//        }
//        return null;
//    }
}
