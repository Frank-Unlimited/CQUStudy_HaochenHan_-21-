import java.awt.desktop.SystemSleepEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SearchRecord {
    static Scanner scanner = new Scanner(System.in);
    public static void searchRecordMenu() {
        String str = Check.iocheck(1, 4);
        switch (str) {
            case "1" -> searchEmployee();
            case "2" -> searchDepartment();
            case "3" -> searchCheking_in();
            case "4" -> searchManage();
            default -> System.out.println("意外的错误");
        }
    }

    public static void searchEmployee(){
        System.out.println("————查找员工信息————");
        System.out.println("""
                            ————输入 1 查询全部员工信息————
                            ————输入 2 精确查询员工信息————""");
        String SQL = "SELECT * from employee";
        ResultSet resultSet = null;
        String str = Check.iocheck(1,2);
        try {
            switch (str) {
                case "1" -> {
                    resultSet = DBConnect.HandleSQL(SQL,2);
                }
                case "2" -> {
                    System.out.println("————输入对应的员工编号————");
                    String E_id = scanner.nextLine();
                    SQL += " where E_id='" + E_id + "'";
                    resultSet = DBConnect.HandleSQL(SQL,2);
                }
                default -> {
                    System.out.println("意外的错误");
                }
            }
            while (resultSet.next()) {
                String id  = resultSet.getString("E_id");
                String name = resultSet.getString("E_name");
                String gender = resultSet.getString("gender");
                String phone = resultSet.getString("phone");

                System.out.println("ID: " + id);
                System.out.println("名字: " + name);
                System.out.println("性别: " + gender);
                System.out.println("电话号码: " + phone + "\n");
            }
        } catch (SQLException s){
            s.printStackTrace();
        }
    }

    public static void searchDepartment(){
        System.out.println("————查找部门信息————");
        System.out.println("""
                            ————输入 1 查询全部部门信息————
                            ————输入 2 精确查询部门信息————""");
        String SQL = "SELECT * from department";
        ResultSet resultSet = null;
        String str = Check.iocheck(1,2);
        try {
            switch (str) {
                case "1" -> {
                    resultSet = DBConnect.HandleSQL(SQL,2);
                }
                case "2" -> {
                    System.out.println("————输入对应的部门编号————");
                    String E_id = scanner.nextLine();
                    SQL += " where D_id='" + E_id + "'";
                    resultSet = DBConnect.HandleSQL(SQL,2);
                }
                default -> {
                    System.out.println("意外的错误");
                }
            }
            while (resultSet.next()) {
                String id  = resultSet.getString("D_id");
                String name = resultSet.getString("D_name");

                System.out.println("ID: " + id);
                System.out.println("名称: " + name);
            }
        } catch (SQLException s){
            s.printStackTrace();
        }
    }

    public static void searchCheking_in(){
        System.out.println("————查找考勤信息————");
        System.out.println("""
                            ————输入 1 查询全部考勤信息————
                            ————输入 2 精确查询考勤信息————""");
        String SQL = "SELECT * from cheking_in";
        ResultSet resultSet = null;
        String str = Check.iocheck(1,2);
        try {
            switch (str) {
                case "1" -> {
                    resultSet = DBConnect.HandleSQL(SQL,2);
                }
                case "2" -> {
                    System.out.println("————输入对应的员工编号————");
                    String E_id = scanner.nextLine();
                    SQL += " where c_id='" + E_id + "'";
                    resultSet = DBConnect.HandleSQL(SQL,2);
                }
                default -> {
                    System.out.println("意外的错误");
                }
            }
            while (resultSet.next()) {
                String id  = resultSet.getString("c_id");
                String name = resultSet.getString("type");
                String gender = resultSet.getString("reason");
                String phone = resultSet.getString("date");

                System.out.println("ID: " + id);
                System.out.println("类型: " + name);
                System.out.println("原因: " + gender);
                System.out.println("日期: " + phone + "\n");
            }
        } catch (SQLException s){
            s.printStackTrace();
        }
    }
    public static void searchManage(){
        System.out.println("————查找管理信息————");
        System.out.println("""
                            ————输入 1 查询全部管理信息————
                            ————输入 2 精确查询管理信息————""");
        String SQL = "SELECT * from Manage";
        ResultSet resultSet = null;
        String str = Check.iocheck(1,2);
        try {
            switch (str) {
                case "1" -> {
                    resultSet = DBConnect.HandleSQL(SQL,2);
                }
                case "2" -> {
                    System.out.println("————输入管理者编号————");
                    String E_id = scanner.nextLine();
                    System.out.println("————输入查询考勤编号————");
                    String c_id = scanner.nextLine();
                    SQL += " where E_id='" + E_id + "'and c_id='\" + c_id +\"' ";//
                    resultSet = DBConnect.HandleSQL(SQL,2);
                }
                default -> {
                    System.out.println("意外的错误");
                }
            }
            while (resultSet.next()) {
                String id  = resultSet.getString("e_id");
                String name = resultSet.getString("c_id");
                String date = resultSet.getString("m_date");

                System.out.println("管理者ID: " + id);
                System.out.println("考勤ID: " + name);
                System.out.println("管理日期: " + date);
            }
        } catch (SQLException s){
            s.printStackTrace();
        }
    }

}
