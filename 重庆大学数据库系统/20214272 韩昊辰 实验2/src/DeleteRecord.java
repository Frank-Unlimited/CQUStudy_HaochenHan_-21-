import java.sql.SQLException;
import java.util.Scanner;

public class DeleteRecord {
    static Scanner scanner = new Scanner(System.in);

    public static void deleteRecordMenu() throws SQLException, ClassNotFoundException {
        String str = Check.iocheck(1, 4);
        switch (str) {
            case "1" -> deleteEmployee();
            case "2" -> deleteDepartment();
            case "3" -> deleteCheking_in();
            case "4" -> deleteManage();
            default -> System.out.println("意外的错误");
        }
    }

    public static void deleteEmployee() throws SQLException, ClassNotFoundException {
        System.out.println("————删除员工信息————");
        System.out.println("————输入要删去的员工的编号————");
        String E_id = scanner.nextLine();

        String SQL = "delete from employee where E_id='"+E_id+"'";
        DBConnect.HandleSQL(SQL,1);
    }

    public static void deleteDepartment() throws SQLException, ClassNotFoundException {
        System.out.println("————删除部门信息————");
        System.out.println("————输入要删去的部门的编号————");
        String D_id = scanner.nextLine();

        String SQL = "delete from department where D_id='"+D_id+"'";
        DBConnect.HandleSQL(SQL,1);
    }

    public static void deleteCheking_in() {
        System.out.println("————删除考勤信息————");
        System.out.println("————输入要删去的考勤的编号————");
        String P_id = scanner.nextLine();

        String SQL = "delete from cheking_in where c_id='"+P_id+"'";
        DBConnect.HandleSQL(SQL,1);
    }

    public static void deleteManage() throws SQLException, ClassNotFoundException {
        System.out.println("————删除管理信息————");
        System.out.println("————输入要删去的管理信息对应管理者编号————");
        String E_id = scanner.nextLine();

        System.out.println("————输入要删去的管理信息对应考勤编号————");
        String c_id = scanner.nextLine();

        String SQL = "delete from Manage where E_id='"+E_id+"' and c_id='"+c_id+"'";
        DBConnect.HandleSQL(SQL,1);
    }
}
