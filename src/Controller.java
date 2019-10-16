import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Controller {

    private static String myIP;
    private static int myport;
//    private Socket connection;
    private static Message[] messages = new Message[3];
    private static List<Node> nodeList = new LinkedList<>();

//    public Controller(Socket s){
//        this.connection = s;
//    }

    public static void main(String[] args) throws Exception {

        Message message0 = new Message();
        message0.setType("getNodeList");
        Message message1 = new Message();
        message1.setType("register");
        message1.setIdentity("bupt/123");
        message1.setMappingData("www.bupt.edu.cn");
        Message message2 = new Message();
        message2.setType("resolve");
        message2.setIdentity("bupt/123");
        messages[0] = message0;
        messages[1] = message1;
        messages[2] = message2;

        InetAddress mIP = InetAddress.getLocalHost();
        myIP = mIP.getHostAddress();
        System.out.println("本节点IP地址: " + myIP );
        myport = 30000;
        System.out.println("本节点Port: " + myport );

        Message result = makeConnectionByObject(args[0],args[1],message0);
        switch (result.getType()) {
            case "getNodeList":
                nodeList = Arrays.asList(result.getNodeList());
                printNodeInfo();
                System.out.println(result.getFeedback());
                break;
            case "register":
                System.out.println(result.getFeedback());
                break;
//            case "delete":
//                break;
//            case "modify":
//                break;
            case "resolve":
                System.out.println("映射数据: " + result.getMappingData());
                System.out.println(result.getFeedback());
                break;
        }

//        ServerSocket serverSocket = new ServerSocket(myport);
//        while (true) {
//            Socket socket = serverSocket.accept();
//            Runnable runnable = new Controller(socket);
//            Thread thread = new Thread(runnable);
//            thread.start();
//        }

    }

//    @Override
//    public void run() {
//        try (
//                // 【注意】对于Object IO流，要先创建输出流对象，再创建输入流对象，不然程序会死锁
//                ObjectOutputStream outToControllerOrOtherNodes = new ObjectOutputStream(connection.getOutputStream());
//                ObjectInputStream inFromControllerOrOtherNodes = new ObjectInputStream(connection.getInputStream()))
//        {
//            outToControllerOrOtherNodes.writeObject(messages[0]);
//
//            Message received_message = (Message)inFromControllerOrOtherNodes.readObject();
//
//        } catch (Exception e) {
//            System.out.println("[系统提示]:"+"线程无法服务连接");
//            //e.printStackTrace();
//        }
//    }
    public static Message makeConnectionByObject(String ip, String port, Message message) throws Exception {

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
    public synchronized static void printNodeInfo(){
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
