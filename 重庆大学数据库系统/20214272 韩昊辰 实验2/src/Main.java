import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void tableMenuPrint(){
        System.out.println("————输入数字以选择表————");
        System.out.println("1：employee");
        System.out.println("2：department");
        System.out.println("3：cheking_in");
        System.out.println("4：Manage");
    }
    public static void menu() throws SQLException, ClassNotFoundException {
        System.out.println(("————输入数字以选择功能————"));
        System.out.println(("1：增加记录"));
        System.out.println(("2：删除记录"));
        System.out.println(("3：修改记录"));
        System.out.println(("4：查询记录"));

        String str = Check.iocheck(1, 4);
        switch (str) {
            case "1" -> {
                addRecord_menu();
            }
            case "2" -> {
                deleteRecord_menu();
            }
            case "3" -> {
                changeRecord_menu();
            }
            case "4" -> {
                searchRecord_menu();
            }
            default -> {
                System.out.println("意外的错误");
            }
        }
    }

    public static void addRecord_menu() throws SQLException, ClassNotFoundException {
        System.out.println("————增加记录菜单————");
        tableMenuPrint();
        AddRecord.addRecordMenu();
    }

    public static void deleteRecord_menu() throws SQLException, ClassNotFoundException {
        System.out.println("————删除记录菜单————");
        tableMenuPrint();
        DeleteRecord.deleteRecordMenu();
    }

    public static void changeRecord_menu() throws SQLException, ClassNotFoundException {
        System.out.println("————修改记录菜单————");
        tableMenuPrint();
        ChangeRecord.changeRecordMenu();
    }

    public static void searchRecord_menu() {
        System.out.println("————查找记录菜单————");
        tableMenuPrint();
        SearchRecord.searchRecordMenu();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String str;
        do {
            menu();
            System.out.println("————还要继续修改请输入 1————\n" +
                                "————输入其他任意键即结束————");
            str = scanner.nextLine();
        } while (str.equals("1"));
        DBConnect.End_close();
        System.out.println("————程序已结束————");
    }
}
