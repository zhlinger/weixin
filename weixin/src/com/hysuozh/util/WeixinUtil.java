package com.hysuozh.util;

import com.hysuozh.menu.Button;
import com.hysuozh.menu.ClickButton;
import com.hysuozh.menu.Menu;
import com.hysuozh.menu.ViewButton;
import com.hysuozh.pojo.AccessToken;
import com.sun.org.apache.bcel.internal.generic.NEW;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import sun.net.www.http.HttpClient;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * create by SGOD
 */
public class WeixinUtil {

    private static final String APPID = "wxa8985b6a25035682";
    private static final String APPSECRET = "9296ef9e6f141fe3af28a403dc79c00f";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    public static JSONObject doGet(String url){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;

        try {
            HttpResponse resonse = httpClient.execute(httpGet);
            HttpEntity entity = resonse.getEntity();
            if (entity != null){
                String result = EntityUtils.toString(entity,"UTF-8");
                jsonObject = JSONObject.fromObject(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject doPost(String url,String outStr){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;

        try {
            httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            jsonObject = jsonObject.fromObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 获取ACCESS_TOKEN
     * @return
     */
    public static AccessToken getAccexxToken(){
        String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        JSONObject jsonObject = doGet(url);
        AccessToken accessToken = new AccessToken();
        if (jsonObject != null ){
            System.out.println("access_token原始数据："+jsonObject.toString());
            accessToken.setToken(jsonObject.getString("access_token"));
            accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
        }
        return accessToken;
    }

    /**
     * 图片上传
     * @param filePath
     * @param accessToken
     * @param type
     * @return
     * @throws IOException
     */
    public static String upload(String filePath,String accessToken,String type) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()){
            throw new IOException("文件不存在");
        }
        String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);

        URL urlObject = new URL(url);

        HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();

        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);

        //设置请求头信息
        con.setRequestProperty("Connection","Keep-Alive");
        con.setRequestProperty("Charset","UTF-8");

        //设置边界
        String BOUNDARY = "--------------"+System.currentTimeMillis();
        con.setRequestProperty("Content-Type","multipart/form-data;boundary="+BOUNDARY);

        StringBuffer sb = new StringBuffer();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head = sb.toString().getBytes("UTF-8");

        //获得输出流
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        //输出表头
        out.write(head);

        //文件正文部分
        //把文件以流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream((file)));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while((bytes = in.read(bufferOut)) != -1){
            out.write(bufferOut,0,bytes);
        }
        in.close();

        //结尾部分
        byte[] foot = ("\r\n--"+ BOUNDARY + "--\r\n").getBytes("UTF-8");

        out.write(foot);

        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;

        try{
            //定义BufferedReader 输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null ){
                buffer.append(line);
            }
            if (result == null){
                result = buffer.toString();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (reader != null){
                reader.close();
            }
        }

        JSONObject jsonObject = JSONObject.fromObject(result);
        System.out.println("获取media_id返回json对象"+jsonObject);
        String typeName = "media_id";
        if (!"image".equals(typeName)){
            typeName = type + "_media_id";
        }
        String media_id = jsonObject.getString(typeName);
        return media_id;

    }

    /**
     * 组装菜单
     * @return
     */
    public static Menu initMenu(){
        Menu menu = new Menu();

        ClickButton button11 = new ClickButton();
        button11.setName("click菜单");
        button11.setType("click");
        button11.setKey("11");

        ViewButton button21 = new ViewButton();
        button21.setName("view菜单");
        button21.setType("view");
        button21.setUrl("http://www.imooc.com");

        ClickButton button31 = new ClickButton();
        button31.setName("扫码堆事件");
        button31.setType("scancode_push");
        button31.setKey("31");

        ClickButton button32 = new ClickButton();
        button32.setName("地理位置");
        button32.setType("location_select");
        button32.setKey("32");

        Button button = new Button();
        button.setName("菜单");
        button.setSub_button(new Button[]{button31,button32});

        menu.setButton(new Button[]{button11,button21,button});
        return menu;
    }

    public static int createMenu(String token,String menu){
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN",token);
        JSONObject jsonObject = doPost(url, menu);
        int result=0;
        if (jsonObject != null){
            result = jsonObject.getInt("errcode");
        }
        return result;

    }
}
