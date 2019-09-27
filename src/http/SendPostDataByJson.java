package http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SendPostDataByJson {

    public static String sendPostDataByJson(String url, String json, String encoding) {
        String result = "";
        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        // 设置参数到请求对象中
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());// 返回json格式：
            // 释放链接
            response.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
