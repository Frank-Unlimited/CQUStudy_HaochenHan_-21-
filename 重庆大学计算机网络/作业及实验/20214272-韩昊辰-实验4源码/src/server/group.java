package server;

import java.util.HashSet;
import java.util.Set;

public class group {
    private int gid;
    private String gname;
    private Set<clients> clients; // 存放群聊中的client
    // 构造函数
    public group(int gid, String gname) {
        this.gid = gid;
        this.gname = gname;
        this.clients = new HashSet<>();
    }
    public int getClientNum(){ return clients.size(); }

    // 获取群聊ID
    public int getGid() {
        return gid;
    }

    // 获取群聊名字
    public String getGname() {
        return gname;
    }

    // 获取群聊中的client集合
    public Set<clients> getClients() {
        return clients;
    }

    // 向群聊中添加client
    public void addClient(clients client) {
        clients.add(client);
    }

    // 从群聊中移除client
    public void removeClient(clients client) {
        clients.remove(client);
    }

    public boolean contains(server.clients trueUser) {
        return clients.contains(trueUser);
    }
}