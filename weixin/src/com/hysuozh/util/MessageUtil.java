package com.hysuozh.util;

import com.hysuozh.pojo.*;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * xml转map集合
 */
public class MessageUtil {

    /**
     * 消息类型常量
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_CLICK = "CLICK";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_NEWS = "news";
    public static final String MESSAGE_MUSIC = "music";

    //XML转MAP
    public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> map = new HashMap<String,String>();
        SAXReader reader = new SAXReader();

        ServletInputStream ins = request.getInputStream();
        Document doc = reader.read(ins);

        Element root = doc.getRootElement();
        List<Element> list = root.elements();
        for (Element e : list) {
            map.put(e.getName(),e.getText());
        }

        ins.close();
        return map;
    }

    //text转XML
    public static String textMessageToXml(TextMessage textMessage) {
        XStream xStream = new XStream();
        xStream.alias("xml",textMessage.getClass());
        return xStream.toXML(textMessage);
    }

    //关注事件
    public static String subscribeText(){
        StringBuffer sb = new StringBuffer();
        sb.append("欢迎您的关注! \n\n");
        sb.append(menuText());
        return sb.toString();
    }

    //主菜单
    public static String menuText(){
        StringBuffer sb = new StringBuffer();
        sb.append("请按照菜单提示进行操作： \n\n");
        sb.append("1.W3CSchool介绍 \n");
        sb.append("2.慕课网介绍 \n");
        sb.append("3.网易云课堂介绍 \n");
        sb.append("4.码家学院介绍 \n");
        sb.append("5.开源中国介绍 \n\n");
        sb.append("回复？调出此菜单。");
        return sb.toString();
    }

    //拼接文本消息
    public static String initText(String toUserName,String fromUserName,String content){
        TextMessage text = new TextMessage();
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMsgType(MessageUtil.MESSAGE_TEXT);
        text.setCreateTime(Long.toString(new Date().getTime()));
        text.setContent(content);
        return textMessageToXml(text);
    }

    //自动回复 1
    public static News w3cSchoolNews(){
        News news = new News();
        news.setTitle("W3CSchool介绍");
        news.setDescription("W3Cschool是一个专业的编程入门学习及技术文档查询应用，提供包括HTML，CSS，Javascript，jQuery，C，PHP，Java，Python，Sql，Mysql等编程语言和开源技术的在线教程及使用手册，是类似国外w3schools的学习社区及菜鸟编程平台。");
        news.setPicUrl("http://homelinger.free.idcfengye.com/weixin/image/1.jpg");
        news.setUrl("https://www.w3cschool.cn/");
        return news;
    }

    //自动回复 2
    public static News imukNews(){
        News news = new News();
        news.setTitle("慕课网介绍");
        news.setDescription("慕课网是垂直的互联网IT技能免费学习网站。以独家视频教程、在线编程工具、学习计划、问答社区为核心特色。在这里，你可以找到最好的互联网技术牛人，也可以通过免费的在线公开视频课程学习国内领先的互联网IT技术。");
        news.setPicUrl("http://homelinger.free.idcfengye.com/weixin/image/2.jpg");
        news.setUrl("http://www.imooc.com");
        return news;
    }

    //自动回复3
    public static News wangyiNews(){
        News news = new News();
        news.setTitle("网易云课堂介绍");
        news.setDescription("网易云课堂立足于实用性的要求，网易云课堂与多家教育、培训机构建立合作，课程数量已达4100+，课时总数超50000,涵盖实用软件、IT与互联网、外语学习、生活家居、兴趣爱好、职场技能、金融管理、考试认证、中小学、亲子教育等十余大门类。");
        news.setPicUrl("http://homelinger.free.idcfengye.com/weixin/image/3.jpg");
        news.setUrl("https://study.163.com/");
        return news;
    }

    //自动回复4
    public static News majiaNews(){
        News news = new News();
        news.setTitle("码家学院介绍");
        news.setDescription("里面的内容，大致是都是来源于蚂蚁课堂，其次码家学院是翻录蚂蚁课堂视频~");
        news.setPicUrl("http://homelinger.free.idcfengye.com/weixin/image/4.jpg");
        news.setUrl("https://www.majiaxueyuan.com/");
        return news;
    }

    //被关注回复信息
    public static News subscriseNew(){
        News news = new News();
        news.setTitle("中国石油机关服务中心欢迎您");
        news.setDescription("点击跳转机关服务");
        news.setPicUrl("http://homelinger.free.idcfengye.com/weixin/image/0.jpg");
        news.setUrl("https://beidou.cnpc.com.cn/wechat/index.htm");
        return news;
    }

    //自动回复5
    public static News openSource(){
        News news = new News();
        news.setTitle("开源中国介绍");
        news.setDescription("开源中国成立于2008年8月，是目前国内最大的开源技术社区，拥有超过200万会员，形成了由开源软件库、代码分享、资讯、协作翻译、码云、众包、招聘等几大模块内容，为IT开发者提供了一个发现、使用、并交流开源技术的平台。");
        news.setPicUrl("http://homelinger.free.idcfengye.com/weixin/image/5.jpg");
        news.setUrl("https://www.oschina.net/");
        return news;
    }

    //图文消息转换XML
    public static String newsToXML(NewsMessage newsMessage){
        XStream xStream = new XStream();
        xStream.alias("xml",newsMessage.getClass());
        xStream.alias("item",new News().getClass());
        return xStream.toXML(newsMessage);
    }

    public static String initNewsMessage(String toUserName,String fromUserName){
        NewsMessage newsMessage = new NewsMessage();
        ArrayList<News> list = new ArrayList<>();

        News news = new News();
        news.setTitle("慕课网介绍");
        news.setDescription("慕课网是垂直的互联网IT技能免费学习网站。以独家视频教程、在线编程工具、学习计划、问答社区为核心特色。在这里，你可以找到最好的互联网技术牛人，也可以通过免费的在线公开视频课程学习国内领先的互联网IT技术。");
        news.setPicUrl("http://homelinger.free.idcfengye.com/weixin/image/rb.jpg");
        news.setUrl("http://www.imooc.com");

        list.add(news);
        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(Long.toString(new Date().getTime()));
        newsMessage.setMsgType(MessageUtil.MESSAGE_NEWS);
        newsMessage.setArticleCount(list.size());
        newsMessage.setArticles(list);

        return newsToXML(newsMessage);
    }

    public static String baseNewsMessage(String toUserName,String fromUserName,News news){
        NewsMessage newsMessage = new NewsMessage();
        ArrayList<News> list = new ArrayList<>();
        list.add(news);
        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(Long.toString(new Date().getTime()));
        newsMessage.setMsgType(MessageUtil.MESSAGE_NEWS);
        newsMessage.setArticleCount(list.size());
        newsMessage.setArticles(list);
        return newsToXML(newsMessage);
    }

    public static String initImageMessage(String toUserName,String fromUserName){
        Image image = new Image();
        image.setMediaId("-hJoXQOtKad0KlLGcoSj_FHcbI09xtkpLqZrboTkAcQ-UX1R77a_-lW-e8BTYRMa");
        //image.setMediaId("YvPGGnZPWwlhxaH33oe3VM428q8zDzskCfZ5QMrXM59OO90Pw0HL0AToKqTLrfg6");
        ImageMessage imageMessage = new ImageMessage();
        imageMessage.setToUserName(fromUserName);
        imageMessage.setFromUserName(toUserName);
        imageMessage.setMsgType(MESSAGE_IMAGE);
        imageMessage.setCreateTime(Long.toString(new Date().getTime()));
        imageMessage.setImage(image);
        return imageMessageToXML(imageMessage);
    }

    //图片转XML
    public static String imageMessageToXML(ImageMessage imageMessage){
        XStream xStream = new XStream();
        xStream.alias("xml",imageMessage.getClass());
        return xStream.toXML(imageMessage);
    }

    //音乐对象转XML
    public static String musicMessageToXML(MusicMessage musicMessage){
        XStream xStream = new XStream();
        xStream.alias("xml",musicMessage.getClass());
        return xStream.toXML(musicMessage);
    }

    //拼装音乐对象
    public static String initMusicMessage(String toUserName,String fromUserName){
        Music music = new Music();

        music.setTitle("17 岁");
        music.setDescription("安安静静 单曲循环");
        music.setMusicUrl("https://y.qq.com/n/yqq/song/234277322_num.html?ADTAG=h5_playsong&no_redirect=1");
        music.setHQMusicUrl("https://y.qq.com/n/yqq/song/234277322_num.html?ADTAG=h5_playsong&no_redirect=1-BTqqAMS91EFifFqAATipVksB61G1");
        music.setThumbMediaId("YvPGGnZPWwlhxaH33oe3VM428q8zDzskCfZ5QMrXM59OO90Pw0HL0AToKqTLrfg6");

        MusicMessage musicMessage = new MusicMessage();
        musicMessage.setFromUserName(toUserName);
        musicMessage.setToUserName(fromUserName);
        musicMessage.setMsgType(MESSAGE_MUSIC);
        musicMessage.setCreateTime(Long.toString(new Date().getTime()));
        musicMessage.setMusic(music);

        return musicMessageToXML(musicMessage);
    }

    public static void main(String[] args) {
        TextMessage text = new TextMessage();
        text.setFromUserName("1");
        text.setToUserName("2");
        text.setMsgType("text");
        text.setCreateTime(Long.toString(new Date().getTime()));
        text.setContent("您发送的消息是："+"hello");
        System.out.println(textMessageToXml(text));
    }

}
















