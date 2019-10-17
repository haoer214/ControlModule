package bupt.fnl.dht.control;

import bupt.fnl.dht.node.Node;
import bupt.fnl.dht.utils.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Controller {

    private static List<Node> nodeList = new LinkedList<>();

    public static void main(String[] args) throws Exception {

        // 【测试信息】获取节点信息
        Message message0 = new Message();
        message0.setType("getNodeList");
        // 【测试信息】标识注册
        Message message1 = new Message();
        message1.setType("register");
        message1.setIdentity("bupt/123");
        message1.setMappingData("www.bupt.edu.cn");
        // 【测试信息】标识解析
        Message message2 = new Message();
        message2.setType("resolve");
        message2.setIdentity("bupt/123");

        InetAddress mIP = InetAddress.getLocalHost();
        // 本机IP地址
        String myIP = mIP.getHostAddress();
        System.out.println("本节点IP地址: " + myIP);
        // 端口
        int myPort = 30000;
        System.out.println("本节点Port: " + myPort);

        Message result = makeConnectionByObject(args[0],args[1],message0);
        switch (result.getType()) {
            // 获取节点信息
            case "getNodeList":
                nodeList = Arrays.asList(result.getNodeList());
                printNodeInfo();
                System.out.println(result.getFeedback());
                break;
            // 增
            case "register":
                System.out.println(result.getFeedback());
                break;
//          // 删
//            case "delete":
//                break;
            // 改
//            case "modify":
//                break;
            // 查
            case "resolve":
                System.out.println("映射数据: " + result.getMappingData());
                System.out.println(result.getFeedback());
                break;
        }
    }

    private static Message makeConnectionByObject(String ip, String port, Message message) throws Exception {

        try(
                Socket sendingSocket = new Socket(ip,Integer.parseInt(port));
                // 【注意】对于Object IO流，要先创建输出流对象，再创建输入流对象，不然程序会死锁
                ObjectOutputStream outToControllerOrOtherNodes = new ObjectOutputStream(sendingSocket.getOutputStream());
                ObjectInputStream inFromControllerOrOtherNodes = new ObjectInputStream(sendingSocket.getInputStream()))
            {
                outToControllerOrOtherNodes.writeObject(message);

                return (Message)inFromControllerOrOtherNodes.readObject();
            }
    }

    private synchronized static void printNodeInfo(){
        Iterator<Node> iterator = nodeList.iterator();
        String string;
        System.out.println("*****节点列表*****");
        if(nodeList.size()==0)
            System.out.println("列表为空！");
        while(iterator.hasNext()) {
            Node node = iterator.next();
            string="节点ID:"+node.getID()+"  IP地址："+node.getIP()+"  端口号： "+node.getPort()+" ";
            System.out.println(string);
        }
        System.out.println();
    }
}
