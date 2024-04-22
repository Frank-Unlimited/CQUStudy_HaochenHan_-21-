import java.sql.SQLException;
import java.util.Scanner;

public class ChangeRecord{
    static Scanner scanner = new Scanner(System.in);

    public static void changeRecordMenu() throws SQLException, ClassNotFoundException {
        String str = Check.iocheck(1, 4);
        switch (str) {
            case "1" -> changeEmployee();
            case "2" -> changeDepartment();
            case "3" -> changeChking_in();
            case "4" -> changeManage();
            default -> System.out.println("意外的错误");
        }
    }

    public static void changeEmployee() throws SQLException, ClassNotFoundException {
        System.out.println("————修改员工信息————\n");
        String SQL = "update employee set ";

        System.out.println("————输入要修改的员工信息对应的员工编号————");
        String E_id = scanner.nextLine();

        System.out.println("""
                ————是否修改员工名字————
                ————输入y或者n————""");
        if (scanner.nextLine().equals("Y") || scanner.nextLine().equals("y")) {
            System.out.println("————输入修改后的员工名字————");
            String E_name = scanner.nextLine();
            SQL += "E_name'"+E_name+"',";
        }

        System.out.println("""
                ————是否修改员工性别————
                ————输入y或者n————""");
        if (scanner.nextLine().equals("Y") || scanner.nextLine().equals("y")) {
            System.out.println("————输入修改后的员工性别————");
            String gender = scanner.nextLine();
            SQL += "gender'"+gender+"',";
        }

        System.out.println("""
                ————是否修改员工电话号码————
                ————输入y或者n————""");
        if (scanner.nextLine().equals("Y") || scanner.nextLine().equals("y")) {
            System.out.println("————输入修改后的员工电话号码————");
            String phone = scanner.nextLine();
            SQL += "phone'"+phone+"',";
        }

        SQL = SQL.substring(0, SQL.length()-1) + "where E_id=" + E_id;
        DBConnect.HandleSQL(SQL,1);
    }

    public static void changeDepartment() throws SQLException, ClassNotFoundException {
        System.out.println("————修改部门信息————\n");
        String SQL = "update department set ";

        System.out.println("————输入要修改的部门信息对应的部门编号————");
        String D_id = scanner.nextLine();

        System.out.println("""
                ————是否修改部门名称————
                ————输入y或者n————""");
        if (scanner.nextLine().equals("Y") || scanner.nextLine().equals("y")) {
            System.out.println("————输入修改后的部门名称————");
            String D_name = scanner.nextLine();
            SQL += "D_name='"+D_name+"',";
        }

        SQL = SQL.substring(0, SQL.length()-1) + "where D_id=" + D_id;
        DBConnect.HandleSQL(SQL,1);
    }

    public static void changeChking_in() throws SQLException, ClassNotFoundException {
        System.out.println("————修改考勤信息————\n");
        String SQL = "update cheking_in set ";

        System.out.println("————输入要修改的考勤信息对应的考勤编号————");
        String D_id = scanner.nextLine();

        System.out.println("""
                ————是否修改考勤类型————
                ————输入y或者n————""");
        if (scanner.nextLine().equals("Y") || scanner.nextLine().equals("y")) {
            System.out.println("————输入修改后的考勤类型————");
            String D_name = scanner.nextLine();
            SQL += "type='"+D_name+"',";
        }

        System.out.println("""
                ————是否修改考勤原因————
                ————输入y或者n————""");
        if (scanner.nextLine().equals("Y") || scanner.nextLine().equals("y")) {
            System.out.println("————输入修改后的考勤原因————");
            String D_name = scanner.nextLine();
            SQL += "reason='"+D_name+"',";
        }

        System.out.println("""
                ————是否修改考勤日期————
                ————输入y或者n————""");
        if (scanner.nextLine().equals("Y") || scanner.nextLine().equals("y")) {
            System.out.println("————输入修改后的考勤类型————");
            String D_name = scanner.nextLine();
            SQL += "date='"+D_name+"',";
        }

        SQL = SQL.substring(0, SQL.length()-1) + "where c_id=" + D_id;
        DBConnect.HandleSQL(SQL,1);
    }

    public static void changeManage() throws SQLException, ClassNotFoundException {
        System.out.println("————修改管理信息————\n");
        String SQL = "update department set ";

        System.out.println("————输入要修改的管理信息对应的管理者编号————");
        String E_id = scanner.nextLine();
        System.out.println("————输入要修改的管理信息对应的考勤编号————");
        String c_id = scanner.nextLine();

        System.out.println("""
                ————是否修改管理日期————
                ————输入y或者n————""");
        if (scanner.nextLine().equals("Y") || scanner.nextLine().equals("y")) {
            System.out.println("————输入修改后的管理日期————");
            String D_name = scanner.nextLine();
            SQL += "M_date='"+D_name+"',";
        }

        SQL = SQL.substring(0, SQL.length()-1) + "where E_id=" + E_id + "and c_id="+c_id;
        DBConnect.HandleSQL(SQL,1);
    }


}
