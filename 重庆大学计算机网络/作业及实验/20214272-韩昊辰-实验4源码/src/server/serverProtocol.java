package server;

import client.clientProtocol;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

/*
 * 读线程读到协议后，会到该模块对协议进行解析
 */

//服务端消息解析
public class serverProtocol {
	// 在线用户管理和总用户管理
    private static online online = new online();
    private static manager manager = new manager();
    private static group_manager group_manager = new group_manager();


    //解析消息，并处理消息，返回待转发的消息列表
    public static List<clientProtocol> Parsing(clientProtocol message) throws UnsupportedEncodingException {
        int type = message.getType();
        //将协议解析后将返回消息列表
        List<clientProtocol> list = new ArrayList<clientProtocol>();
        /*****************************登录消息*****************************/
        if(type == 0) {
        	//获取ID信息
            int id = message.getFromID();
            //获取密码信息
            String password = new String(message.getData(), "utf-8");
            //验证是否存在该用户
            clients trueUser = manager.select(id);
            //如果不存在此用户，返回noUser
            if (trueUser == null) {
                /*
                 * 登录回复事件封装成协议，字段说明如下
                 * @ 3: 登录回复事件标识字段
                 * @ 0: 源ID为0，说明是服务端发送的信息
                 * @ 1: 目的端只有一个
                 * @ new int[]{id}: 目的端为id
                 * @ 0: data类型为文本
                 * @ data: 具体封装的数据，此处为"noUser"
                 */
                list.add(new clientProtocol(3, 0, 1, new int[]{id}, 0, "noUser".getBytes("UTF-8")));
                return list;
            }
            //验证密码
            if (trueUser.getPassword().equals(password)) {
            	//若密码正确则将该用户加入在线用户管理
                online.add(id);
                /*
                 * 除了具体数据不同外，其余同上
                 */
                list.add(new clientProtocol(3, 0, 1, new int[]{id}, 0, "success".getBytes("UTF-8")));// 封装回复协议
                return list;
            } else {
                list.add(new clientProtocol(3, 0, 1, new int[]{id}, 0, "passwordError".getBytes("UTF-8")));
                return list;
            }
            /*****************************注销消息*****************************/
        }else if(type == 1) {
            int id = message.getFromID();
            clients trueUser = manager.select(id);
            //不存在此用户
            if (trueUser == null) {
                list.add(new clientProtocol(4, 0, 1, new int[]{id}, 0, "noUser".getBytes("UTF-8")));
                return list;
            }
            //注销成功，从在线管理中移除
            online.remove(id);
            /*
             * 注销回复事件封装成协议，字段说明如下
             * @ 4: 注销回复事件标识字段
             * @ 0: 源ID为0，说明是服务端发送的信息
             * @ 1: 目的端只有一个
             * @ new int[]{id}: 目的端为id
             * @ 0: data类型为文本
             * @ data: 具体封装的数据，此处为"success"
             */
            list.add(new clientProtocol(4, 0, 1, new int[]{id}, 0, "success".getBytes("UTF-8")));// 封装回复协议
            return list;
            /*****************************注册消息*****************************/
        } else if(type == 2) {
            int id;
            do {
                id = new Random().nextInt(90000000) + 100000000;
            } while (manager.select(id) != null);
            String data = new String(message.getData(), "utf-8");
            //从协议data中分离出用户名和密码
            String[] datas = data.split(",");
            String password = datas[1];
            String name = datas[0];
            //生成新用户，并加入用户管理模块
            clients user = new clients();
            user.setId(id);
            user.setName(name);
            user.setPassword(password);
            manager.insert(user);
            //封装回复协议
            /*
             * 注册回复事件封装成协议，字段说明如下
             * @ 5: 注册回复事件标识字段
             * @ 0: 源ID为0，说明是服务端发送的信息
             * @ 1: 目的端只有一个
             * @ new int[]{id}: 目的端为id
             * @ 0: data类型为文本
             * @ data: 具体封装的数据，此处为"success"
             */
            list.add(new clientProtocol(5, 0, 1, new int[]{id}, 0, "success".getBytes("UTF-8")));
            return list;
            /*****************************加群消息*****************************/
        } else if(type == 33) {
            //获取用户id
            int id = message.getFromID();
            //查找用户
            clients trueUser = manager.select(id);
            //获取群聊id
            int gid = message.getToID()[0];

            //获取群聊
            group trueGroup = group_manager.select(gid);
            if(trueGroup==null){
                //创建group，加到manager里
                group newg = new group(gid,"默认群名");
                trueGroup = newg;
            }
            //插入用户
            trueGroup.addClient(trueUser);
            group_manager.insert(trueGroup);
            System.out.println("groupManager的size"+group_manager.getSize());
            list.add(new clientProtocol(44, 0, 1, new int[]{id}, 0, "success".getBytes("UTF-8")));
            return list;
            /*****************************退群消息*****************************/
        } else if(type == 55) {
            //获取用户id
            int id = message.getFromID();
            //查找用户
            clients trueUser = manager.select(id);
            //获取群聊id
            int gid = message.getToID()[0];
            //获取群聊
            group trueGroup = group_manager.select(gid);
            if(trueGroup==null){
                //不存在该群聊
                list.add(new clientProtocol(77, 0, 1, new int[]{id}, 404, "noGroup".getBytes("UTF-8")));
                return list;
            }
            //删除用户
            trueGroup.removeClient(trueUser);
            group_manager.update(trueGroup);
            list.add(new clientProtocol(77, 0, 1, new int[]{id}, 0, "success".getBytes("UTF-8")));
            return list;
            /*****************************发送消息*****************************/
        } else if(type == 10){
            int     id = message.getFromID();
            int  toLen = message.getLenToID();
            int[] toID = message.getToID();
            //多点转单点
            for(int i = 0; i < toLen; i++){
            	/*
                 * 发送事件封装成协议，字段说明如下
                 * @ 11: 发送事件标识字段
                 * @ id: 源ID为客户端发送方
                 * @ 1: 目的端只有一个，这里将id列表中的客户端分开发送
                 * @ new int[]{toID[i]}: 目的端为其中一个
                 * @ message.getDatatype(): data类型
                 * @ message.getData(): 具体封装的数据
                 */
                list.add(new clientProtocol(11, id, 1, new int[]{toID[i]}, message.getDatatype(),message.getData()));
            }
            return list;
        }
        else if(type == 88){
            int id = message.getFromID();

            Set<Integer> set  = online.get();
            int ls[] = new int[set.size()];
            int i=0;
            for (Integer integer:set){
                ls[i++]=integer;
            }
            list.add(new clientProtocol(99, id, set.size(), ls, message.getDatatype(),message.getData()));
            return list;
        }
        /*****************************发送群聊消息*****************************/
        else if(type == 66){
            int     id = message.getFromID();
            Integer gid = message.getToID()[0];
            System.out.println("check gid : "+gid);
            //获取群聊
            group trueGroup = group_manager.select(gid);
            System.out.println("查到的group：   ");
            group_manager.getInfo();
            if(trueGroup==null){
                //不存在该群聊
                list.add(new clientProtocol(166, 0, 1, new int[]{id}, 404, "noGroup".getBytes("UTF-8")));
                return list;
            }
            System.out.println("群在线人数 "+trueGroup.getClientNum());
            Set<clients> toLen = trueGroup.getClients();

            for(clients cl: toLen){
                list.add(new clientProtocol(166, id, 1, new int[]{cl.getId()}, message.getDatatype(),message.getData()));
            }
            return list;
            /*****************************获取在线列表消息*****************************/
        }else if(type == 12){
            /*
             * 此时dataType为在线用户个数，data存储在线用户ID和姓名，每个用户通过,分隔，eg: A B,C D,E F
             * 具体格式为ID1 User1, ID2 User2, ID3 User3以此类推
             */
            StringBuffer data = new StringBuffer();
            Set<Integer> set  = online.get();
            for (Integer integer:set){
                data.append(integer);
                data.append(" ");
                data.append(manager.select(integer).getName());
                data.append(",");
            }
            data.deleteCharAt(data.length()-1);
            /*
             * 在线列表事件封装成协议，字段说明如下
             * @ 13: 在线列表事件标识字段
             * @ 0: 源ID为服务端
             * @ 1: 目的端只有一个，即请求在线列表的
             * @ new int[]{message.getFromID()}: 目的端
             * @ set.size(): data类型，这里具体含义为在线人数
             * @ data.toString().getBytes("utf-8"): 在线用户具体信息
             */
            list.add(new clientProtocol(13, 0, 1, new int[]{message.getFromID()}, set.size(),data.toString().getBytes("utf-8")));
            return list;
        } else if(type == 71){
            /*
             拉人协议 71
             */
            int     id = message.getFromID();
            String temp = new String(message.getData(),"utf-8");
            Integer gid = Integer.parseInt(temp);
            int to_id[] = message.getToID();
            System.out.println("拉人启动！！！！！");
            System.out.println("check gid : "+gid);
            //获取群聊
            group trueGroup = group_manager.select(gid);
            System.out.println("查到的group :   ");

            System.out.println("群在线人数 "+trueGroup.getClientNum());
            Set<clients> toLen = new HashSet<>();

            for (int idx :to_id)
            {
                clients trueUser = manager.select(idx);
                if(trueGroup.contains(trueUser)) {
                    ;
                }
                else {
                    trueGroup.addClient(trueUser);
                    toLen.add(trueUser);
                }
            }

            for(clients cl: toLen){
                list.add(new clientProtocol(72, gid, 1, new int[]{cl.getId()}, message.getDatatype(),message.getData()));
            }
            return list;
        }

        //协议解析静态扩展
        return null;
    }
}
