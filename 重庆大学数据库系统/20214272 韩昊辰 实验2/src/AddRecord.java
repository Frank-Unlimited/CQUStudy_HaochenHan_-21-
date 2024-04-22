import java.sql.SQLException;
import java.util.Scanner;


public class AddRecord {
    static Scanner scanner = new Scanner(System.in);

    public static void addRecordMenu() throws SQLException, ClassNotFoundException {
        String str = Check.iocheck(1, 4);
        switch (str) {
            case "1" -> addEmployee();
            case "2" -> addDepartment();
            case "3" -> addCheking_in();
            case "4" -> addManage();
            default -> System.out.println("意外的错误");
        }
    }

    public static void addEmployee() throws SQLException, ClassNotFoundException {
        System.out.println("————添加员工信息————");
        System.out.println("————输入员工编号————");
        String E_id = scanner.nextLine();
        System.out.println("————输入员工名字————");
        String E_name = scanner.nextLine();
        System.out.println("————输入员工性别————");
        String gender = scanner.nextLine();
        System.out.println("————输入员工电话号码————");
        String phone = scanner.nextLine();

        String SQL = "insert into employee values" +
                " ('"+E_id+"','" +E_name+"','"+gender+"','"+phone+"');";
        DBConnect.HandleSQL(SQL,1);

    }

    public static void addDepartment() throws SQLException, ClassNotFoundException {
        System.out.println("————添加部门信息————");
        System.out.println("————输入部门编号————");
        String D_id = scanner.nextLine();
        System.out.println("————输入部门名称————");
        String D_name = scanner.nextLine();

        String SQL = "insert into department values" +
                " ('"+D_id+"','"+D_name+"');";
        DBConnect.HandleSQL(SQL,1);
    }

    public static void addCheking_in() throws SQLException, ClassNotFoundException {
        System.out.println("————添加考勤信息————");
        System.out.println("————输入考勤编号————");
        String c_id = scanner.nextLine();
        System.out.println("————输入考勤类型————");
        String type = scanner.nextLine();
        System.out.println("————输入考勤原因————");
        String reason = scanner.nextLine();
        System.out.println("————输入考勤日期————");
        String date = scanner.nextLine();

        String SQL = "insert into cheking_in values" +
                " ('"+c_id+"','"+type+"','"+reason+"','"+date+"')";
        DBConnect.HandleSQL(SQL,1);
    }

    public static void addManage() throws SQLException, ClassNotFoundException {
        System.out.println("————添加管理信息————");
        System.out.println("————输入管理者编号————");
        String E_id = scanner.nextLine();
        System.out.println("————输入管理考勤编号————");
        String c_id = scanner.nextLine();
        System.out.println("————输入管理日期————");
        String M_date = scanner.nextLine();

        String SQL = "insert into Manage values" +
                " ('"+E_id+"','"+c_id+"','"+M_date+"')";
        DBConnect.HandleSQL(SQL,1);
    }
}
