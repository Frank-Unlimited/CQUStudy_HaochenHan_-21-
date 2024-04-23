package server;

import java.util.HashMap;
import java.util.Map;

//用户信息管理，用于增删改查用户id，用户名和密码
public class group_manager {
    private static Map<Integer, group> map = new HashMap<Integer, group>();
    public group select(int id){ return map.get(id); }
    public void update(group g){ map.remove(g.getGid()); map.put(g.getGid(),g); }

    public void delete(int id){ map.remove(id); }
    public void insert(group g){ map.putIfAbsent(g.getGid(), g); }
    public int getSize(){return map.size();}
    public void getInfo(){
        System.out.println("************************gm.getinfo");
        for (Map.Entry<Integer, group> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }
}