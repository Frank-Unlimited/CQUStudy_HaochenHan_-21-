package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.List;




/*
 * GUI部分没有什么好讲的，主要新增了两个进程
 * 一个是读消息队列进程，当窗口建立后开启，一直循环检测是否读取到新信息，直到点击退出
 * 还有一个是刷新进程，实现原理很简单，就是每隔一段时间10s重新获取在线客户名单并显示
 * GUI中所有事件监测的操作都和event.java有关，其中封装了所有的事件
 * ******************亮点*******************
 * 1. 为了方便用户名管理，采用注册功能，将用户名转为唯一的整型ID
 * 2. 有两种方法刷新在线用户列表，一是重新关闭窗口再重新点开，手动刷新，二是线程自动刷新，每隔10s刷新一次
 */

public class clientGUI {
    private static event event = new event();
    private static int id = -1; //登录后获取ID
    private static String passwords = "";
    private static boolean isFile = false;
    private static File selectFile = null;
    //线程异步终止
    private static volatile boolean exit = false;
    private static volatile boolean exit1 = false;
    private static volatile boolean exit2 = false;
    public static HashMap<Integer, String> nameHashMap = new HashMap<>();

    private static int isRegister = 0;
    static Set<Integer> ls_group=new HashSet<>();

    private static int groupid = 0;
    private static String groupremark = "";

    private static int isRegisterwindow = 1;
    private static String userid = "";

    private static String  username = "";
    private static String titleresource = "src\\resources\\title2.png";
    public static void runGUI() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("登录");
            frame.setSize(500, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);// 设置居中显示

            // 设置背景颜色为白色
            frame.getContentPane().setBackground(Color.WHITE);


            ImageIcon icon = new ImageIcon(titleresource);
            frame.setIconImage(icon.getImage());// 给窗体设置图标方法


            // 创建一个面板，使用GridBagLayout布局管理器
            JPanel panel = new JPanel(new GridBagLayout());
            frame.add(panel);
            panel.setBackground(Color.WHITE);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(10, 10, 10, 10); // 设置组件之间的间距

            // 创建图片面板，使用默认的FlowLayout布局管理器
            JPanel panel1 = new JPanel();
            panel1.setBackground(Color.WHITE);
            // 添加图片到图片面板
            ImageIcon imageIcon = new ImageIcon("src/resources/title1.png");
            Image image = imageIcon.getImage().getScaledInstance(300, -1, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(image);
            JLabel imageLabel = new JLabel(scaledImageIcon);
            constraints.gridx = 0;
            constraints.gridy = 0;
            panel1.add(imageLabel,constraints);
            // 文本欢迎登录
            JLabel welcomeLabel = new JLabel("欢迎登录");
            constraints.gridx = 0;
            constraints.gridy = 1;
            panel.add(welcomeLabel, constraints);


            // 将图片面板添加到主面板
            constraints.gridx = 0;
            constraints.gridy = 0;
            panel.add(panel1, constraints);





            // 创建图片面板2，使用默认的FlowLayout布局管理器
            JPanel panel2 = new JPanel();
            panel2.setBackground(Color.WHITE);
            // 添加用户名标签和输入框
            JLabel usernameLabel = new JLabel("用户名:");
            constraints.gridx = 0;
            constraints.gridy = 0;
            panel2.add(usernameLabel, constraints);
            if(!String.valueOf(isRegister).equals("0")){
                userid = String.valueOf(isRegister);
            }

            // 设置默认用户名为 "12345678"
            JTextField usernameField = new JTextField(userid, 20);
            constraints.gridx = 1;
            constraints.gridy = 0;
            panel2.add(usernameField, constraints);
            // 将图片面板2添加到主面板
            constraints.gridx = 0;
            constraints.gridy = 2;
            panel.add(panel2, constraints);



            // 创建图片面板3，使用默认的FlowLayout布局管理器
            JPanel panel3 = new JPanel();
            panel3.setBackground(Color.WHITE);
            // 添加密码标签和输入框
            JLabel passwordLabel = new JLabel("    密码:");
            constraints.gridx = 0;
            constraints.gridy = 0;
            panel3.add(passwordLabel, constraints);

            JPasswordField passwordField = new JPasswordField(20);
            constraints.gridx = 1;
            constraints.gridy = 0;
            panel3.add(passwordField, constraints);
            // 将图片面板3添加到主面板
            constraints.gridx = 0;
            constraints.gridy = 3;
            panel.add(panel3, constraints);


            // 创建面板4，使用默认的FlowLayout布局管理器
            JPanel panel4 = new JPanel();
            panel4.setBackground(Color.WHITE);
            // 添加登录按钮
            JButton loginButton = new JButton("登录");
            // 登录事件监测
            loginButton.addActionListener(e->{
                // 检查用户名和密码是否为空
                if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "用户名和密码不能为空！", "输入错误", JOptionPane.WARNING_MESSAGE);
                } else {
                    boolean isLogin = event.login(Integer.parseInt(usernameField.getText()), String.valueOf(passwordField.getPassword()));

                    System.out.println(isLogin);
                    if(isLogin){
                        frame.dispose();
                        id = Integer.parseInt(usernameField.getText());
                        passwords = String.valueOf(passwordField.getPassword());
                        mainWindow();
                    }else{
                        JOptionPane.showMessageDialog(null, "登陆失败！请检测ID和密码", "登录失败", JOptionPane.ERROR_MESSAGE);
                    }
                }

            });

            constraints.gridx = 0;
            constraints.gridy = 0;
            panel4.add(loginButton, constraints);

            // 添加注册按钮
            JButton registerButton = new JButton("注册");
            // 点击注册生成注册界面
            registerButton.addActionListener(e1->{
                if(isRegisterwindow == 1){
                    frame.dispose();
                    registerWindow();
                }else{
                    JOptionPane.showMessageDialog(null, "您已经注册过了", "输入错误", JOptionPane.WARNING_MESSAGE);
                }


            });
            constraints.gridx = 1;
            constraints.gridy = 0;
            panel4.add(registerButton, constraints);
            // 将面板4添加到主面板
            constraints.gridx = 0;
            constraints.gridy = 4;
            panel.add(panel4, constraints);


            // 设置frame可见
            frame.setVisible(true);
        });
    }
    public static void registerWindow() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("注册");
            frame.setSize(500, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);// 设置居中显示

            // 设置背景颜色为白色
            frame.getContentPane().setBackground(Color.WHITE);

            ImageIcon icon = new ImageIcon(titleresource);
            frame.setIconImage(icon.getImage());// 给窗体设置图标方法
            // 创建一个面板，使用GridBagLayout布局管理器
            JPanel panel = new JPanel(new GridBagLayout());
            frame.add(panel);
            panel.setBackground(Color.WHITE);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(10, 10, 10, 10); // 设置组件之间的间距

            // 添加图片到图片面板
            ImageIcon imageIcon = new ImageIcon("src/resources/title1.png");
            Image image = imageIcon.getImage().getScaledInstance(300, -1, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(image);
            JLabel imageLabel = new JLabel(scaledImageIcon);
            constraints.gridx = 0;
            constraints.gridy = 0;
            panel.add(imageLabel,constraints);

            // 文本欢迎登录
            JLabel welcomeLabel = new JLabel("欢迎注册");
            constraints.gridx = 0;
            constraints.gridy = 1;
            panel.add(welcomeLabel, constraints);




            // 创建图片面板2，使用默认的FlowLayout布局管理器
            JPanel panel2 = new JPanel();
            panel2.setBackground(Color.WHITE);
            // 添加用户名标签和输入框
            JLabel usernameLabel = new JLabel("用户名:");
            constraints.gridx = 0;
            constraints.gridy = 0;
            panel2.add(usernameLabel, constraints);


            // 设置默认用户名为 "12345678"
            JTextField usernameField = new JTextField( 20);
            constraints.gridx = 1;
            constraints.gridy = 0;
            panel2.add(usernameField, constraints);
            // 将图片面板2添加到主面板
            constraints.gridx = 0;
            constraints.gridy = 2;
            panel.add(panel2, constraints);



            // 创建图片面板3，使用默认的FlowLayout布局管理器
            JPanel panel3 = new JPanel();
            panel3.setBackground(Color.WHITE);
            // 添加密码标签和输入框
            JLabel passwordLabel = new JLabel("    密码:");
            constraints.gridx = 0;
            constraints.gridy = 0;
            panel3.add(passwordLabel, constraints);

            JPasswordField passwordField = new JPasswordField(20);
            constraints.gridx = 1;
            constraints.gridy = 0;
            panel3.add(passwordField, constraints);
            // 将图片面板3添加到主面板
            constraints.gridx = 0;
            constraints.gridy = 3;
            panel.add(panel3, constraints);


            // 创建面板4，使用默认的FlowLayout布局管理器
            JPanel panel4 = new JPanel();
            panel4.setBackground(Color.WHITE);
            // 添加注册按钮
            JButton registerButton = new JButton("注册");
            // 点击注册成功返回登录界面
            registerButton.addActionListener(e1->{
                // 检查用户名和密码是否为空
                if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "用户名和密码不能为空！", "输入错误", JOptionPane.WARNING_MESSAGE);
                } else {
                    isRegister = event.register(usernameField.getText(),passwordField.getText());
                    if (isRegister>0) {
                        JOptionPane.showInternalMessageDialog(null, "您对应的ID为"+String.valueOf(isRegister),"注册成功", JOptionPane.INFORMATION_MESSAGE);
                        isRegisterwindow = 0;
                        username = usernameField.getText();
                        frame.dispose();
                        runGUI();
                    } else {
                        JOptionPane.showMessageDialog(null, "注册失败", "注册失败", JOptionPane.ERROR_MESSAGE);
                        frame.dispose();
                    }
                }


            });
            constraints.gridx = 0;
            constraints.gridy = 0;
            panel4.add(registerButton, constraints);

            // 添加返回按钮
            JButton backButton = new JButton("返回");
            // 点击注册生成注册界面
            backButton.addActionListener(e1->{
                frame.dispose();
                runGUI();
            });
            constraints.gridx = 1;
            constraints.gridy = 0;
            panel4.add(backButton, constraints);


            // 将面板4添加到主面板
            constraints.gridx = 0;
            constraints.gridy = 4;
            panel.add(panel4, constraints);


            // 设置frame可见
            frame.setVisible(true);
        });
    }



    public static void mainWindow(){

        JFrame jFrame = new JFrame("CQU聊天系统");
        jFrame.setSize(900, 600);
        jFrame.setLocationRelativeTo(null);// 设置居中显示
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
        jFrame.setBackground(Color.white);
        jFrame.setResizable(true);




        ImageIcon icon = new ImageIcon(titleresource);
        jFrame.setIconImage(icon.getImage());// 给窗体设置图标方法

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10); // 设置组件之间的间距


        //关闭事件
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                exit = true;
                //发送注销信息
                event.unlogin(id,passwords);
            }
        });



        // 创建图片面板1，使用默认的FlowLayout布局管理器
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.WHITE);


        //接收窗口
        JPanel jPanel2 = new JPanel();
        jPanel2.setBackground(Color.WHITE);
        JTextArea output = new JTextArea(12,30);
        output.setFont(new Font("标楷体", Font.BOLD, 14));
        output.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(output);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel1.add(jPanel2, constraints);

        //提示窗口
        JPanel jPanelsend = new JPanel();
        JLabel jLabelsend = new JLabel("发送窗口:                                                                                                                ");

        constraints.gridx = 0;
        constraints.gridy = 0;
        jPanelsend.add(jLabelsend, constraints);


        jPanelsend.setBackground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel1.add(jPanelsend, constraints);

        //发送窗口
        JPanel jPanel1 = new JPanel();
        JTextArea input = new JTextArea(5,26);
        jPanel1.setBackground(Color.WHITE);
        input.setFont(new Font("标楷体", Font.BOLD, 14));
        jPanel1.add(new JScrollPane(input));
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel1.add(jPanel1, constraints);

        //提示窗口
        JPanel jPanelsend1 = new JPanel();
        JLabel jLabelsend1 = new JLabel("                                                                                                                ");
        jPanelsend1.setBackground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 0;
        jPanelsend1.add(jLabelsend1, constraints);


        //添加文件和图片
        JButton jButton2 = new JButton("选择文件");
        jButton2.addActionListener(e2 -> {
            JFileChooser jfc=new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
            jfc.showDialog(new JLabel(), "选择");
            File file=jfc.getSelectedFile();
            if(file!=null) {
                input.setText(file.getAbsolutePath());
                //锁定输入框，置文件位
                input.setEditable(false);
                isFile = true;
                selectFile = file;
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 0;
        jPanelsend1.add(jButton2, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        panel1.add(jPanelsend1, constraints);

        //读接受到消息队列线程开启
        Thread readQueueThread = new Thread(new Runnable(){
            public void run() {
                try {
                    while (true) {
                        if(exit){
                            return;
                        }
                        //一秒读取一次，减轻CPU负载
                        Thread.sleep(1000);
                        //监听type=11的协议，如果存在，则接受
                        while (clientSocket.queue.isEmtry(11)) {
                            if(exit)
                            {
                                return;
                            }
                        };
                        clientProtocol message = clientSocket.queue.read(11);
                        if (message != null && message.getData()!=null) {
                            System.out.println("接收到文字" + message.getDataTypeString());
                            if (message.getDataTypeString().equals("0")) {
                                output.append("接收到 id = " + Integer.toString(message.getFromID()) +"用户：" +  nameHashMap.get(message.getFromID()));
                                output.append(" 发送的文字：\n  " +new String(message.getData(),"utf-8"));
                                output.append("\n");
                            }else if(message.getDataTypeString().equalsIgnoreCase("jpg") ||
                                    message.getDataTypeString().equalsIgnoreCase("png") ||
                                    message.getDataTypeString().equalsIgnoreCase("jpeg") ||
                                    message.getDataTypeString().equalsIgnoreCase("bmp")){
                                output.append("接收到 id = " + Integer.toString(message.getFromID()));
                                output.append(" 发送的图片:\n  图片已保存到D:\\recFile文件夹中" );
                                String fileName = Integer.toString(message.getFromID()) + "Rec" +
                                        Long.toString(System.currentTimeMillis()) +"."+ message.getDataTypeString();
                                File file = new File("D:\\recFile\\"+fileName);
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                fileOutputStream.write(message.getData());
                                output.append("\n");
                                fileOutputStream.close();
                            }else{
                                output.append("接收到 id = " + Integer.toString(message.getFromID()));
                                output.append(" 发送的文件:\n  文件已保存到D:\\recFile文件夹中" );
                                output.append("\n");
                                String fileName = Integer.toString(message.getFromID()) + "Rec" +
                                        Long.toString(System.currentTimeMillis()) +"."+ message.getDataTypeString();
                                File file = new File("D:\\recFile\\"+fileName);
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                fileOutputStream.write(message.getData());
                                fileOutputStream.close();
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        readQueueThread.start();

        //  读被拉进群聊消息队列线程开启
        Thread readInvitedThread3 = new Thread(new Runnable(){
            public void run() {
                try {
                    while (true) {
                        if(exit){
                            return;
                        }
                        //一秒读取一次，减轻CPU负载
                        Thread.sleep(1000);
                        //监听type=11的协议，如果存在，则接受
                        while (clientSocket.queue.isEmtry(72)) {
                            if(exit)
                            {
                                exit=false;
                                return;
                            }
                        };
                        clientProtocol message = clientSocket.queue.read(72);
                        int gid = message.getFromID();
                        System.out.println("被拉进"+gid+"群");
                        ls_group.add(gid);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        readInvitedThread3.start();

        //下拉框自动定位到最后一行
        jScrollPane.getViewport().setViewPosition(new Point(0, jScrollPane.getVerticalScrollBar().getMaximum()));
        jPanel2.add(jScrollPane);


        //按钮板3
        JPanel jPanel3 = new JPanel();
        jPanel3.setBackground(Color.WHITE);
        JButton jButton1 = new JButton("发送");
        //单击按钮执行的方法
        jButton1.addActionListener(e -> {
            //创建新的窗口
            JFrame frame = new JFrame("请选择对象");
            frame.setSize(200, 500);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);// 设置居中显示

            // 设置背景颜色为白色
            frame.getContentPane().setBackground(Color.WHITE);


            ImageIcon icon1 = new ImageIcon(titleresource);
            frame.setIconImage(icon1.getImage());// 给窗体设置图标方法

            JPanel panelselect = new JPanel();
            //根据在线人信息填充复选框
            List<Map<Integer,String>> listx = event.getOnlineUsers(id);
            int onlineUserConut = listx.size();
            panelselect.setLayout(new GridLayout(onlineUserConut+1,1,100,20));
            JCheckBox[] jCheckBoxes = new JCheckBox[onlineUserConut];

            int j = 0;
            for (Map<Integer,String> map:listx){
                for(Map.Entry<Integer,String> entry:map.entrySet()){
                    jCheckBoxes[j] = new JCheckBox(entry.getValue()+" "+Integer.toString(entry.getKey()));
                    panelselect.add(jCheckBoxes[j]);
                }
                j++;
            }

            //传输按钮
            JButton jButton = new JButton("发送");

            jButton.addActionListener(e1 ->{
                try {
                    byte[] bytes = null;
                    //读取 input 的值
                    String inputTest = input.getText();
                    //判断传输类型，生成byte
                    String type = "0";
                    if (isFile) {
                        if (selectFile == null) {
                            //不存在文件
                            throw new RuntimeException();
                        } else {
                            int index = selectFile.getName().lastIndexOf(".");
                            if (index == -1) {
                                type = "";
                            }
                            String houZhui = selectFile.getName().substring(index + 1);
                            if (houZhui.equalsIgnoreCase("jpg") ||
                                    houZhui.equalsIgnoreCase("png") ||
                                    houZhui.equalsIgnoreCase("jpeg") ||
                                    houZhui.equalsIgnoreCase("bmp")) {
                                type = houZhui;
                            } else {
                                type = houZhui;
                            }
                            bytes = Files.readAllBytes(selectFile.toPath());
                        }
                    }else {
                        bytes = inputTest.getBytes("utf-8");
                    }
                    //获取接收对象

                    List<Integer> toLDlist = new ArrayList<Integer>();
                    //读取复选框的值
                    for (int i = 0; i < jCheckBoxes.length; i++) {
                        if (jCheckBoxes[i].isSelected()) {
                            //复选框内容：name id
                            toLDlist.add(Integer.parseInt(jCheckBoxes[i].getText().split(" ")[1]));
                        }
                        ;
                    }
                    //封装为数组
                    int[] toID = new int[toLDlist.size()];
                    int lentoID = 0;
                    for (Integer integer : toLDlist) {
                        toID[lentoID] = integer;
                        lentoID++;
                        System.out.println("发送对象ID为： " + integer);
                    }

                    if(lentoID<=0){ throw new RuntimeException(); }

                    //调用Event.transfor传输数据
                    event.transfor(id,lentoID,toID,type,bytes);

                    //关闭子jFrame
                    frame.dispose();
                    //output 显示 发送成功
                    output.append("发送");
                    if(type.equals("0")){
                        output.append("文字");
                    }else if(type.equalsIgnoreCase("jpg") ||
                            type.equalsIgnoreCase("png") ||
                            type.equalsIgnoreCase("jpeg") ||
                            type.equalsIgnoreCase("bmp")){
                        output.append("图片");
                    }else{
                        output.append("文件");
                    }
                    output.append("成功\n");
                    input.setText("");
                    jScrollPane.getViewport().setViewPosition(new Point(0, jScrollPane.getVerticalScrollBar().getMaximum()));
                    input.setEditable(true);
                    isFile = false;
                    selectFile = null;
                }catch (Exception e5){
                    e5.printStackTrace();
                    frame.dispose();
                    output.append("发送失败\n");
                    input.setText("");
                    input.setEditable(true);
                    isFile = false;
                    selectFile = null;
                }
            });

            panelselect.add(jButton);
            frame.add(new JScrollPane(panelselect));
            frame.setLocation(320, 162);

            frame.setVisible(true);
        });
        jPanel3.add(jButton1);


        //加群申请
        JButton jButton_joinG = new JButton("加入群聊");
        jButton_joinG.addActionListener(e -> {
            joingroup();
        });
        jPanel3.add(jButton_joinG);




        JButton jButton3 = new JButton("世界");
        //单击按钮执行的方法
        jButton3.addActionListener(e -> {

            wordWindow();
        });
        jPanel3.add(jButton3);

        JPanel jPanel4 = new JPanel(new GridBagLayout());
        jPanel4.setBackground(Color.WHITE);
        JLabel listname = new JLabel("用户列表                                       群聊列表");
        constraints.gridx = 0;
        constraints.gridy = 0;
        jPanel4.add(listname,constraints);
        //显示好友列表窗口
        JPanel jPanel5 = new JPanel();
        jPanel5.setBackground(Color.WHITE);
        JTextArea online5 = new JTextArea(20,12);
        online5.setEditable(true);
        //获取在线用户列表
        online5.setText("——————————————————————————————————————————\n");
        online5.setFont(new Font("标楷体", Font.BOLD, 15));
        online5.setEditable(false);
        List<Map<Integer,String>> list = event.getOnlineUsers(id);
        if(list.size() > 0) {
            for (Map<Integer, String> map : list) {
                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    online5.append("用户： " + entry.getValue());
                    online5.append("\n——————————————————————————————————————————\n");
                }
            }
        }
        Thread flushThread = new Thread(new Runnable(){
            public void run() {
                try {
                    while (true) {
                        if (exit) { return; }
                        Thread.sleep(10000);//减轻CPU负载
                        //获取在线用户列表
                        online5.setText("——————————————————————————————————————————\n");
                        online5.setFont(new Font("标楷体", Font.BOLD, 15));
                        online5.setEditable(false);
                        List<Map<Integer,String>> list = event.getOnlineUsers(id);
                        if(list.size()>0) {
                            for (Map<Integer, String> map : list) {
                                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                                    nameHashMap.putIfAbsent(entry.getKey(), entry.getValue());
                                    System.out.println(entry.getValue()+"已经加入hashname表");
                                    online5.append("用户： " + entry.getValue());
                                    online5.append("\n——————————————————————————————————————————\n");
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        flushThread.start();
        jPanel5.add(new JScrollPane(online5,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        constraints.gridx = 0;
        constraints.gridy = 1;
        jPanel4.add(jPanel5,constraints);



        //显示群列表窗口
        JPanel jPanel6 = new JPanel();
        jPanel6.setBackground(Color.WHITE);
        //显示群列表窗口
        JPanel jPanel7 = new JPanel();
        jPanel7.setBackground(Color.WHITE);
        JTextArea online6 = new JTextArea(20,12);
        online6.setEditable(true);
        //获取群列表
        online6.setText("——————————————————————————————————————————\n");
        online6.setFont(new Font("标楷体", Font.BOLD, 15));
        online6.setEditable(false);

        if(ls_group!=null && ls_group.size() > 0) {
            for (Integer gid: ls_group) {
                    online6.append("群聊id ：" + gid);
                    online6.append("\n——————————————————————————————————————————\n");
            }
        }
        Thread flushThread1 = new Thread(new Runnable(){
            public void run() {
                try {
                    while (true) {
                        if (exit) { return; }
                        Thread.sleep(10000);//减轻CPU负载
                        //获取在线用户列表
                        //获取群列表
                        online6.setText("——————————————————————————————————————————\n");
                        online6.setFont(new Font("标楷体", Font.BOLD, 15));
                        online6.setEditable(false);

                        if(ls_group!=null && ls_group.size() > 0) {
                            for (Integer gid: ls_group) {
                                online6.append("群聊id ：" + gid);
                                online6.append("\n——————————————————————————————————————————\n");
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        flushThread1.start();
        jPanel5.add(new JScrollPane(online6,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));


        jPanel6.add(jPanel7);



        // 获取当前系统时间
        LocalTime currentTime = LocalTime.now();

        // 设置时间范围
        LocalTime morningStart = LocalTime.of(0, 0);
        LocalTime noonStart = LocalTime.of(12, 0);
        LocalTime afternoonStart = LocalTime.of(14, 0);
        LocalTime eveningStart = LocalTime.of(19, 0);

        // 定义问候语
        String greeting;

        // 根据当前时间设置问候语
        if (currentTime.isAfter(morningStart) && currentTime.isBefore(noonStart)) {
            greeting = "早上好！";
        } else if (currentTime.isAfter(noonStart) && currentTime.isBefore(afternoonStart)) {
            greeting = "中午好！";
        } else if (currentTime.isAfter(afternoonStart) && currentTime.isBefore(eveningStart)) {
            greeting = "下午好！";
        } else {
            greeting = "晚上好！";
        }

        //显示窗口
        JPanel jPanel8 = new JPanel();
        jPanel8.setBackground(Color.decode("#0b5495"));


        // 添加图片到图片面板
        ImageIcon imageIcon = new ImageIcon("src/resources/title3.png");
        Image image = imageIcon.getImage().getScaledInstance(400, -1, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(scaledImageIcon);
        constraints.gridx = 0;
        constraints.gridy = 0;
        jPanel8.add(imageLabel,constraints);

        JLabel jlabel9 = new JLabel(getCurrentDate());
        jlabel9.setForeground(Color.WHITE); // 设置前景色为白色
        constraints.gridx = 0;
        constraints.gridy = 1;
        jPanel8.add(jlabel9, constraints);

        JLabel jlabel10 = new JLabel(greeting + username + "    ");
        jlabel10.setForeground(Color.WHITE); // 设置前景色为白色
        constraints.gridx = 1;
        constraints.gridy = 1;
        jPanel8.add(jlabel10, constraints);

        JLabel jlabel11 = new JLabel("id:" + isRegister);
        jlabel11.setForeground(Color.WHITE); // 设置前景色为白色
        constraints.gridx = 2;
        constraints.gridy = 1;
        jPanel8.add(jlabel11, constraints);





        JPanel jPane3 = new JPanel();
        jPane3.setLayout(new BorderLayout());


        jPane3.add(jPanel8,BorderLayout.NORTH); //按钮板
        jPane3.add(jPanel3,BorderLayout.SOUTH); //按钮板
        jPane3.add(jPanel4,BorderLayout.WEST); //在线列表
        jPane3.add(panel1,BorderLayout.CENTER);//接受发送窗口
        jPane3.add(jPanel6,BorderLayout.EAST); //在线群列表

        jFrame.add(jPane3);
        jFrame.setVisible(true);
    }
    // 获取当前系统日期的方法
    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日  E", Locale.CHINESE);
        return dateFormat.format(new Date());
    }
    public static void joingroup(){
        exit2=false;
        JFrame frame1 = new JFrame();
        frame1.setTitle("申请加群");//标题
        frame1.setSize(280,180);//窗体大小
        frame1.setResizable(true);//设置大小可变
        frame1.setLocationRelativeTo(null);//设置居中显示

        ImageIcon icon = new ImageIcon(titleresource);
        frame1.setIconImage(icon.getImage());// 给窗体设置图标方法

        JPanel s1 = new JPanel();
        JPanel z1 = new JPanel();
        JPanel x1 = new JPanel();

        JLabel user1 = new JLabel("群号:");
        JLabel password1 =  new JLabel("备注:");

        JTextField userTF1 = new JTextField(10);
        JTextField pwdTF1 = new JTextField(10);

        JButton okB1 =  new JButton("申请加群");

        //布局习惯：窗体中布局面板，面板中布局组件
        frame1.setLayout(new BorderLayout());
        frame1.add(s1,BorderLayout.NORTH );
        frame1.add(z1, BorderLayout.CENTER );
        frame1.add(x1,BorderLayout.SOUTH);

        s1.add(user1);
        s1.add(userTF1);
        z1.add( password1);
        z1.add(pwdTF1);
        x1.add(okB1);

        //设置窗体可见性
        frame1.setVisible(true);
        okB1.addActionListener(e2-> {
            groupid = Integer.parseInt(userTF1.getText());
            boolean isJoinGroup = event.joinGroup(isRegister, groupid, "data".getBytes());
            System.out.println(groupid);
            if(isJoinGroup){
                groupremark = pwdTF1.getText();
                System.out.println("加群成功");
                ls_group.add(groupid);
                frame1.dispose();
                groupWindow();
            }
        });
    }

    public static void groupWindow(){
        exit2=false;
        JFrame jFrame = new JFrame("群聊");
        jFrame.setSize(900, 600);
        jFrame.setLocationRelativeTo(null);// 设置居中显示
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
        jFrame.setResizable(true);
        ImageIcon icon = new ImageIcon(titleresource);
        jFrame.setIconImage(icon.getImage());// 给窗体设置图标方法
        //关闭事件
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                //终止readThread，writeThread，readQueueThread线程
                //结束消息队列的监听
                exit2 = true;

            }
        });
        // 创建一个面板，使用GridBagLayout布局管理器
        JPanel panel = new JPanel(new GridBagLayout());
        jFrame.add(panel);
        panel.setBackground(Color.WHITE);

        GridBagConstraints constraints = new GridBagConstraints();

        // 创建图片面板1，使用默认的FlowLayout布局管理器
        JPanel panel1 = new JPanel(new GridBagLayout());

        panel1.setBackground(Color.WHITE);

        //接收窗口
        JPanel jPanel2 = new JPanel();
        jPanel2.setBackground(Color.WHITE);
        JTextArea output = new JTextArea(12,40);
        output.setFont(new Font("标楷体", Font.BOLD, 14));
        output.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(output);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel1.add(jPanel2, constraints);

        //提示窗口
        JPanel jPanelsend = new JPanel();
        JLabel jLabelsend = new JLabel("发送窗口:                                                                                                                                       ");

        constraints.gridx = 0;
        constraints.gridy = 0;
        jPanelsend.add(jLabelsend, constraints);


        jPanelsend.setBackground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 1;
        // 设置上一个格子和下一个格子的间距为0
        constraints.insets = new Insets(10, 10, 0, 10);
        panel1.add(jPanelsend, constraints);

        //发送窗口
        JPanel jPanel1 = new JPanel();
        JTextArea input = new JTextArea(5,35);
        jPanel1.setBackground(Color.WHITE);
        input.setFont(new Font("标楷体", Font.BOLD, 14));
        jPanel1.add(new JScrollPane(input));
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel1.add(jPanel1, constraints);

        //提示窗口
        JPanel jPanelsend1 = new JPanel();
        JLabel jLabelsend1 = new JLabel("                                                                                                          ");
        jPanelsend1.setBackground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 0;
        jPanelsend1.add(jLabelsend1, constraints);

        JButton jButton1 = new JButton("发送");
        jButton1.addActionListener(e -> {
            try {
                byte[] bytes = null;
                //读取 input 的值
                String inputTest = input.getText();
                //判断传输类型，生成byte
                String type = "0";
                if (isFile) {
                    if (selectFile == null) {
                        //不存在文件
                        throw new RuntimeException();
                    } else {
                        int index = selectFile.getName().lastIndexOf(".");
                        if (index == -1) {
                            type = "";
                        }
                        String houZhui = selectFile.getName().substring(index + 1);
                        if (houZhui.equalsIgnoreCase("jpg") ||
                                houZhui.equalsIgnoreCase("png") ||
                                houZhui.equalsIgnoreCase("jpeg") ||
                                houZhui.equalsIgnoreCase("bmp")) {
                            type = houZhui;
                        } else {
                            type = houZhui;
                        }
                        bytes = Files.readAllBytes(selectFile.toPath());
                    }
                }else {
                    bytes = inputTest.getBytes("utf-8");
                }

                //调用Event.transfor传输数据
                int[] toID = new int[1];

                event.transfor_group(id, groupid, type,bytes);

                output.append("\n");
                input.setText("");
                jScrollPane.getViewport().setViewPosition(new Point(0, jScrollPane.getVerticalScrollBar().getMaximum()));
                input.setEditable(true);
                isFile = false;
                selectFile = null;
            }catch (Exception e5){
                e5.printStackTrace();
                output.append("发送失败\n");
                input.setText("");
                input.setEditable(true);
                isFile = false;
                selectFile = null;
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 0;
        jPanelsend1.add(jButton1, constraints);
        //添加文件和图片
        JButton jButton2 = new JButton("选择文件");
        jButton2.addActionListener(e2 -> {
            JFileChooser jfc=new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
            jfc.showDialog(new JLabel(), "选择");
            File file=jfc.getSelectedFile();
            if(file!=null) {
                input.setText(file.getAbsolutePath());
                //锁定输入框，置文件位
                input.setEditable(false);
                isFile = true;
                selectFile = file;
            }
        });
        constraints.gridx = 2;
        constraints.gridy = 0;
        jPanelsend1.add(jButton2, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        panel1.add(jPanelsend1, constraints);









        //读接受到消息队列线程开启
        Thread readQueueThread1 = new Thread(new Runnable(){
            public void run() {
                try {
                    while (true)
                    {
                        if(exit2)
                        {
                            return;
                        }
                        //一秒读取一次，减轻CPU负载
                        Thread.sleep(1000);
                        //监听type=99的协议，如果存在，则接受
                        while (clientSocket.queue.isEmtry(166)) {
                            if(exit2)
                            {
                                return;
                            }
                        };

                        clientProtocol message = clientSocket.queue.read(166);
                        if (message != null && message.getData()!=null)
                        {
                            String currentTime = TimeUtil.getCurrentTime();
                            output.append("时间: " + currentTime);
                            System.out.println( "++++++++++++++++++"+ message.getDatatype()+"-----------------");
                            System.out.println("检查上一行");
                            if (message.getDataTypeString().equals("0"))
                            {
                                output.append("     id:" + message.getFromID()+"     用户:" + nameHashMap.get(message.getFromID()));
                                output.append("     广播的文字：\n  " +new String(message.getData(),"utf-8"));
                                output.append("\n");
                            }
                            else if(message.getDataTypeString().equalsIgnoreCase("jpg") ||
                                    message.getDataTypeString().equalsIgnoreCase("png") ||
                                    message.getDataTypeString().equalsIgnoreCase("jpeg") ||
                                    message.getDataTypeString().equalsIgnoreCase("bmp"))
                            {
                                output.append("id = " + Integer.toString(message.getFromID()));
                                output.append(" 广播的图片:\n  图片已保存到D:\\recFile文件夹中" );
                                String fileName = Integer.toString(message.getFromID()) + "Rec" +
                                        Long.toString(System.currentTimeMillis()) +"."+ message.getDataTypeString();
                                File file = new File("D:\\recFile\\"+fileName);
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                fileOutputStream.write(message.getData());
                                output.append("\n");
                                fileOutputStream.close();
                            }else
                            {
                                output.append("接收到 id = " + Integer.toString(message.getFromID()));
                                output.append(" 广播的文件:\n  文件已保存到D:\\recFile文件夹中" );
                                output.append("\n");
                                String fileName = Integer.toString(message.getFromID()) + "Rec" +
                                        Long.toString(System.currentTimeMillis()) +"."+ message.getDataTypeString();
                                File file = new File("D:\\recFile\\"+fileName);
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                fileOutputStream.write(message.getData());
                                fileOutputStream.close();
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        readQueueThread1.start();

        //下拉框自动定位到最后一行
        jScrollPane.getViewport().setViewPosition(new Point(0, jScrollPane.getVerticalScrollBar().getMaximum()));
        jPanel2.add(jScrollPane);

        //按钮板3
        JPanel jPanel3 = new JPanel();
        jPanel3.setBackground(Color.white);
        JButton jButton4 = new JButton("拉好友");
        jButton4.addActionListener(e -> {
            //创建新的窗口
            JFrame frame = new JFrame("请选择对象");
            frame.setSize(200, 500);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);// 设置居中显示

            // 设置背景颜色为白色
            frame.getContentPane().setBackground(Color.WHITE);


            ImageIcon icon1 = new ImageIcon(titleresource);
            frame.setIconImage(icon1.getImage());// 给窗体设置图标方法
            JPanel panelselect = new JPanel();
            //根据在线人信息填充复选框
            List<Map<Integer,String>> listx = event.getOnlineUsers(id);
            int onlineUserConut = listx.size();
            panelselect.setLayout(new GridLayout(onlineUserConut+1,1,100,20));
            JCheckBox[] jCheckBoxes = new JCheckBox[onlineUserConut];

            int j = 0;
            for (Map<Integer,String> map:listx){
                for(Map.Entry<Integer,String> entry:map.entrySet()){
                    jCheckBoxes[j] = new JCheckBox(entry.getValue()+" "+Integer.toString(entry.getKey()));
                    panelselect.add(jCheckBoxes[j]);
                }
                j++;
            }

            //传输按钮
            JButton jButton = new JButton("拉好友");

            jButton.addActionListener(e1 ->{
                try {
                    byte[] bytes = null;
                    //读取 input 的值
                    String inputTest = input.getText();
                    //判断传输类型，生成byte
                    String type = "0";
                    if (isFile) {
                        if (selectFile == null) {
                            //不存在文件
                            throw new RuntimeException();
                        } else {
                            int index = selectFile.getName().lastIndexOf(".");
                            if (index == -1) {
                                type = "";
                            }
                            String houZhui = selectFile.getName().substring(index + 1);
                            if (houZhui.equalsIgnoreCase("jpg") ||
                                    houZhui.equalsIgnoreCase("png") ||
                                    houZhui.equalsIgnoreCase("jpeg") ||
                                    houZhui.equalsIgnoreCase("bmp")) {
                                type = houZhui;
                            } else {
                                type = houZhui;
                            }
                            bytes = Files.readAllBytes(selectFile.toPath());
                        }
                    }else {
                        bytes = inputTest.getBytes("utf-8");
                    }
                    //获取接收对象

                    List<Integer> toLDlist = new ArrayList<Integer>();
                    //读取 复选框的值
                    for (int i = 0; i < jCheckBoxes.length; i++) {
                        if (jCheckBoxes[i].isSelected()) {
                            //复选框内容：name id
                            toLDlist.add(Integer.parseInt(jCheckBoxes[i].getText().split(" ")[1]));
                        }
                        ;
                    }
                    //封装为数组
                    int[] toID = new int[toLDlist.size()];
                    int lentoID = 0;
                    for (Integer integer : toLDlist) {
                        toID[lentoID] = integer;
                        lentoID++;
                        System.out.println("发送对象ID为： " + integer);
                    }
                    event.inviteGroup( isRegister, lentoID, toID ,String.valueOf(groupid).getBytes() );
                }catch (Exception e5){
                    e5.printStackTrace();
                    frame.dispose();
                    output.append("拉好友失败\n");
                    input.setText("");
                    input.setEditable(true);
                    isFile = false;
                    selectFile = null;
                }
            });

            panelselect.add(jButton);
            frame.add(new JScrollPane(panelselect));
            frame.setLocation(320, 162);
            frame.setVisible(true);
        });
        jPanel3.add(jButton4);



        //添加文件和图片
        JButton jButton3 = new JButton("退出群聊");
        jButton3.addActionListener(e2 -> {
            boolean isQuitgroup= event.quitGroup(isRegister, groupid,"".getBytes());
            System.out.println(groupid);
            System.out.println(isRegister);
            if(isQuitgroup)
            {
                jFrame.dispose();
                ls_group.remove(groupid);
                exit2 = true;
            }
        });
        jPanel3.add(jButton3);


        // 创建图片面板1，使用默认的FlowLayout布局管理器
        JPanel panel6 = new JPanel();
        panel6.setBackground(Color.decode("#0b5495"));
        // 添加图片到图片面板
        ImageIcon imageIcon = new ImageIcon("src/resources/title3.png");
        Image image = imageIcon.getImage().getScaledInstance(400, -1, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(scaledImageIcon);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel6.add(imageLabel,constraints);
        // 添加用户名标签和输入框
        JLabel groupidLabel = new JLabel("群号:"+ groupid);
        groupidLabel.setForeground(Color.WHITE); // 设置前景色为白色
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel6.add(groupidLabel, constraints);
        JLabel groupremarkLabel = new JLabel("          备注:"+groupremark);
        groupremarkLabel.setForeground(Color.WHITE); // 设置前景色为白色
        constraints.gridx = 2;
        constraints.gridy = 0;
        panel6.add(groupremarkLabel, constraints);





        JPanel jPane3 = new JPanel();
        jPane3.setLayout(new BorderLayout());

        jPane3.add(panel6,BorderLayout.NORTH);
        jPane3.add(panel1,BorderLayout.CENTER);//接受发送窗口
        jPane3.add(jPanel3,BorderLayout.SOUTH); //按钮板


        jFrame.add(jPane3);
        jFrame.setVisible(true);
    }


    public static void wordWindow(){
        exit1=false;
        JFrame jFrame = new JFrame("世界");
        jFrame.setSize(900, 600);
        jFrame.setLocationRelativeTo(null);// 设置居中显示
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
        jFrame.setResizable(true);
        // 创建一个面板，使用GridBagLayout布局管理器
        JPanel panel = new JPanel(new GridBagLayout());
        jFrame.add(panel);
        panel.setBackground(Color.WHITE);
        ImageIcon icon = new ImageIcon(titleresource);
        jFrame.setIconImage(icon.getImage());// 给窗体设置图标方法
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10); // 设置组件之间的间距


        //关闭事件
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                //终止readThread，writeThread，readQueueThread线程
                //结束消息队列的监听
                exit1 = true;

            }
        });



        // 创建图片面板1，使用默认的FlowLayout布局管理器
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.WHITE);

        // 添加图片到图片面板
        ImageIcon imageIcon = new ImageIcon("src/resources/title1.png");
        Image image = imageIcon.getImage().getScaledInstance(400, -1, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(scaledImageIcon);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel1.add(imageLabel,constraints);

        //接收窗口
        JPanel jPanel2 = new JPanel();
        jPanel2.setBackground(Color.WHITE);
        JTextArea output = new JTextArea(10,40);
        output.setFont(new Font("标楷体", Font.BOLD, 14));
        output.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(output);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel1.add(jPanel2, constraints);

        //提示窗口
        JPanel jPanelsend = new JPanel();
        JLabel jLabelsend = new JLabel("发送窗口:                                                                                                                                       ");

        constraints.gridx = 0;
        constraints.gridy = 0;
        jPanelsend.add(jLabelsend, constraints);


        jPanelsend.setBackground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel1.add(jPanelsend, constraints);

        //发送窗口
        JPanel jPanel1 = new JPanel();
        JTextArea input = new JTextArea(4,35);
        jPanel1.setBackground(Color.WHITE);
        input.setFont(new Font("标楷体", Font.BOLD, 14));
        jPanel1.add(new JScrollPane(input));
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel1.add(jPanel1, constraints);

        //提示窗口
        JPanel jPanelsend1 = new JPanel();
        JLabel jLabelsend1 = new JLabel("                                                                                                          ");
        jPanelsend1.setBackground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 0;
        jPanelsend1.add(jLabelsend1, constraints);

        JButton jButton50 = new JButton("发送");
        jButton50.addActionListener(e -> {
            try {
                byte[] bytes = null;
                //读取 input 的值
                String inputTest = input.getText();
                //判断传输类型，生成byte
                String type = "0";
                if (isFile) {
                    if (selectFile == null) {
                        //不存在文件
                        throw new RuntimeException();
                    } else {
                        int index = selectFile.getName().lastIndexOf(".");
                        if (index == -1) {
                            type = "";
                        }
                        String houZhui = selectFile.getName().substring(index + 1);
                        if (houZhui.equalsIgnoreCase("jpg") ||
                                houZhui.equalsIgnoreCase("png") ||
                                houZhui.equalsIgnoreCase("jpeg") ||
                                houZhui.equalsIgnoreCase("bmp")) {
                            type = houZhui;
                        } else {
                            type = houZhui;
                        }
                        bytes = Files.readAllBytes(selectFile.toPath());
                    }
                }else {
                    bytes = inputTest.getBytes("utf-8");
                }

                //调用Event.transfor传输数据
                int[] toID = new int[1];

                event.transfor_broad(id,1, toID, type, bytes);
                output.append("\n");
                input.setText("");
                jScrollPane.getViewport().setViewPosition(new Point(0, jScrollPane.getVerticalScrollBar().getMaximum()));
                input.setEditable(true);
                isFile = false;
                selectFile = null;
            }catch (Exception e5){
                e5.printStackTrace();
                output.append("发送失败\n");
                input.setText("");
                input.setEditable(true);
                isFile = false;
                selectFile = null;
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 0;
        jPanelsend1.add(jButton50, constraints);
        //添加文件和图片
        JButton jButton2 = new JButton("选择文件");
        jButton2.addActionListener(e2 -> {
            JFileChooser jfc=new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
            jfc.showDialog(new JLabel(), "选择");
            File file=jfc.getSelectedFile();
            if(file!=null) {
                input.setText(file.getAbsolutePath());
                //锁定输入框，置文件位
                input.setEditable(false);
                isFile = true;
                selectFile = file;
            }
        });
        constraints.gridx = 2;
        constraints.gridy = 0;
        jPanelsend1.add(jButton2, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        panel1.add(jPanelsend1, constraints);

        //读接受到消息队列线程开启
        Thread readQueueThread1 = new Thread(new Runnable(){
            public void run() {
                try {
                    while (true)
                    {
                        if(exit1)
                        {
                            return;
                        }
                        //一秒读取一次，减轻CPU负载
                        Thread.sleep(1000);
                        //监听type=99的协议，如果存在，则接受
                        while (clientSocket.queue.isEmtry(99)) {
                            if(exit1)
                            {
                                return;
                            }
                        };

                        clientProtocol message = clientSocket.queue.read(99);
                        if (message != null && message.getData()!=null)
                        {
                            String currentTime = TimeUtil.getCurrentTime();
                            output.append("时间: " + currentTime);
                            System.out.println( "++++++++++++++++++"+ message.getDatatype()+"-----------------");
                            System.out.println("检查上一行");
                            if (message.getDataTypeString().equals("0"))
                            {
                                output.append("     id:" + message.getFromID()+"     用户:" + nameHashMap.get(message.getFromID()));
                                output.append("     广播的文字：\n  " +new String(message.getData(),"utf-8"));
                                output.append("\n");
                            }
                            else if(message.getDataTypeString().equalsIgnoreCase("jpg") ||
                                    message.getDataTypeString().equalsIgnoreCase("png") ||
                                    message.getDataTypeString().equalsIgnoreCase("jpeg") ||
                                    message.getDataTypeString().equalsIgnoreCase("bmp"))
                            {
                                output.append("id = " + Integer.toString(message.getFromID()));
                                output.append(" 广播的图片:\n  图片已保存到D:\\recFile文件夹中" );
                                String fileName = Integer.toString(message.getFromID()) + "Rec" +
                                        Long.toString(System.currentTimeMillis()) +"."+ message.getDataTypeString();
                                File file = new File("D:\\recFile\\"+fileName);
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                fileOutputStream.write(message.getData());
                                output.append("\n");
                                fileOutputStream.close();
                            }else
                            {
                                output.append("接收到 id = " + Integer.toString(message.getFromID()));
                                output.append(" 广播的文件:\n  文件已保存到D:\\recFile文件夹中" );
                                output.append("\n");
                                String fileName = Integer.toString(message.getFromID()) + "Rec" +
                                        Long.toString(System.currentTimeMillis()) +"."+ message.getDataTypeString();
                                File file = new File("D:\\recFile\\"+fileName);
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                fileOutputStream.write(message.getData());
                                fileOutputStream.close();
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        readQueueThread1.start();

        //下拉框自动定位到最后一行
        jScrollPane.getViewport().setViewPosition(new Point(0, jScrollPane.getVerticalScrollBar().getMaximum()));
        jPanel2.add(jScrollPane);


        JPanel jPane3 = new JPanel();
        jPane3.setLayout(new BorderLayout());


        jPane3.add(panel1,BorderLayout.CENTER);//接受发送窗口



        jFrame.add(jPane3);
        jFrame.setVisible(true);
    }

}
