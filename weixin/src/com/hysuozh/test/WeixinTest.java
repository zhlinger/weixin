package com.hysuozh.test;

import com.hysuozh.pojo.AccessToken;
import com.hysuozh.util.WeixinUtil;
import net.sf.json.JSONObject;


/**
 * create by SGOD
 */
public class WeixinTest {
    public static void main(String[] args) {

        try {
            AccessToken token = WeixinUtil.getAccexxToken();
            System.out.println("票据："+token.getToken());
            System.out.println("有效时间:"+token.getExpiresIn());

            String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
            int result = WeixinUtil.createMenu(token.getToken(), menu);
            if (result == 0 ){
                System.out.println("创建菜单成功");
            }else {
                System.out.println("错误码："+result);
            }

            /*String media_id = WeixinUtil.upload(path, token.getToken(), "thumb");
            System.out.println(media_id);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        //String path="D://7.jpg";
    }
}
