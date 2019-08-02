package com.hysuozh.servlet;

import com.hysuozh.util.CheckUtil;
import com.hysuozh.util.MessageUtil;
import org.dom4j.DocumentException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;


public class WeixinServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce");
        String echostr = req.getParameter("echostr");

        PrintWriter out = resp.getWriter();
        if (CheckUtil.checkSignature(signature,timestamp,nonce)){
            out.print(echostr);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out=resp.getWriter();
        try {
            Map<String,String> map = MessageUtil.xmlToMap(req);
            String fromUserName = map.get("FromUserName");
            String toUserName = map.get("ToUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");

            String message=null;

            switch (msgType){
                case MessageUtil.MESSAGE_TEXT:
                    if ("1".equals(content)){
                    message = MessageUtil.baseNewsMessage(toUserName,fromUserName,MessageUtil.w3cSchoolNews());
                    }else if ("2".equals(content)){
                        message = MessageUtil.baseNewsMessage(toUserName,fromUserName,MessageUtil.imukNews());
                    }else if ("3".equals(content)){
                        message = MessageUtil.baseNewsMessage(toUserName,fromUserName,MessageUtil.wangyiNews());
                    }else if ("4".equals(content)){
                        message = MessageUtil.baseNewsMessage(toUserName,fromUserName,MessageUtil.majiaNews());
                    }else if ("5".equals(content)){
                        message = MessageUtil.baseNewsMessage(toUserName,fromUserName,MessageUtil.openSource());
                    }else if ("6".equals(content)){
                        message = MessageUtil.initImageMessage(toUserName,fromUserName);
                    } else if ("7".equals(content)){
                        message = MessageUtil.initMusicMessage(toUserName,fromUserName);
                    } else if ("?".equals(content) || "？".equals(content)){
                        message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.menuText());
                    }else {
                        message = MessageUtil.initText(toUserName,fromUserName,content);
                    }
                    break;
                case MessageUtil.MESSAGE_IMAGE:
                    break;
                case MessageUtil.MESSAGE_VOICE:
                    break;
                case MessageUtil.MESSAGE_VIDEO:
                    break;
                case MessageUtil.MESSAGE_MUSIC:
                    break;
                case MessageUtil.MESSAGE_NEWS:
                    break;
                case MessageUtil.MESSAGE_EVENT:
                    String eventType = map.get("Event");//用于确定是哪个推送时间
                    if (MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
                        //message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.subscribeText());
                        message = MessageUtil.baseNewsMessage(toUserName,fromUserName,MessageUtil.subscriseNew());
                    }
                    break;
                    default:
                        System.out.println("不存在的情况竟然出现了");
            }
            System.out.println(message);
            out.print(message);
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally{
            out.close();
        }
    }
}
