package control;

import http.SendPostDataByJson;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Controller {

    private static String myIP;
    private static String myport;

    // 交易号、操作类型、标识、映射数据
    public static String transaction_id;
    public static String operation_type;
    public static String full_identity;
    public static String content;

    // couchdb地址
    public static String URL = "http://10.108.151.83:18056/_utils/#database/mychannel_identity/_all_docs";

//    public String control(JSONObject webJson) throws Exception {
////        myIP = InetAddress.getLocalHost().getHostAddress();
////        myport = "6011";
//        if (receiveWebRequestByJson(webJson)) {
//            String dhtIP = webJson.getString("dhtIP");
//            String dhtPort = webJson.getString("dhtPort");
//            if (operation_type.equals("insert") || operation_type.equals("modify")) {
//                String message = operation_type + "/" + full_identity + "/" + content;
//                return makeConnection(dhtIP, dhtPort, message);
//            } else {
//                String message = operation_type + "/" + full_identity;
//                return makeConnection(dhtIP, dhtPort, message);
//            }
//        }
//        return "[系统提示] 您没有 " + operation_type + " 权限！";
//    }

    // 从区块链查询是否有相应权限（增删改查）
    public static boolean receiveWebRequestByJson(JSONObject webJson){
        transaction_id = webJson.getString("transaction_id");
        operation_type = webJson.getString("operation_type");
        full_identity = webJson.getString("full_identity");

        // 查询couchdb
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("transaction_id", transaction_id);
        jsonObject.put("selector", jsonObject1);
        String received = SendPostDataByJson.sendPostDataByJson(URL, jsonObject.toString(), "utf-8");
        System.out.println("响应结果：(字符串格式)" + received);
        net.sf.json.JSONObject body = net.sf.json.JSONObject.fromObject(received);
        System.out.println("响应结果：(json格式)" + body);
        net.sf.json.JSONArray body1 = net.sf.json.JSONArray.fromObject(body.getString("docs"));
        Object[] object= body1.toArray();
        String top_identity="";
        String authority="0000";
        for (Object o : object) {
            top_identity = net.sf.json.JSONObject.fromObject(o).getString("top_identity");
            authority = net.sf.json.JSONObject.fromObject(o).getString("authority");
            System.out.println("交易号" + transaction_id + "对应标识的前缀是" + top_identity + "，权限为" + authority);
        }

//        top_identity = body.getString("top_identity"); // 前缀
//        authority = body.getString("authority"); // 权限
//        System.out.println("交易号" + transaction_id + "对应标识的前缀是" + top_identity + "，权限为" + authority);

        // 比较前缀和权限
        String[] tokens = full_identity.split("/");
        int type = -1;
        switch (operation_type){
            case "insert":
                type = 0;
            case "delete":
                type = 1;
            case "modify":
                type = 2;
            case "resolve":
                type = 3;
        }
        return tokens[0].equals(top_identity) && authority.charAt(type) == '1';
    }

    public static String makeConnection(String ip, String port, String message) throws Exception {
            Socket sendingSocket = new Socket(ip,Integer.parseInt(port));
            DataOutputStream out = new DataOutputStream(sendingSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sendingSocket.getInputStream()));

            // v2改：将.writeBytes(str)改为.write(str.getBytes()) --避免中文乱码
            out.write((message + "\n").getBytes());

            String result = inFromServer.readLine();

            out.close();
            inFromServer.close();
            sendingSocket.close();
            return result;
    }

    public static void main(String[] args) throws IOException {

        JSONObject webJson = new JSONObject();
        webJson.put("transaction_id","001");
        webJson.put("operation_type","insert");
        webJson.put("full_identity","fnl/123");
        webJson.put("content","www.bupt.edu.cn");

        System.out.println(Controller.receiveWebRequestByJson(webJson));

    }
}
