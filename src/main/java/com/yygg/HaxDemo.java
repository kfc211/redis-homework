package com.yygg;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HaxDemo {
    public static void main(String[] args){
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");
        Article article=new Article();
        article.setTitle("t1");
        article.setContent("c1");
        Long articleId=saveArticle(jedis,article);
        System.out.println("保存成功"+"article"+articleId+"Data");
        Article article1=findArticle(articleId,jedis);
        System.out.println("查询成功"+article1.getTitle());
        String title="123";
        updateTitle(articleId,jedis,title);
        System.out.println("修改题目成功");
        List list=pagePostId(jedis,2,1);
        System.out.println(list);
        delArticle(articleId,jedis);
    }
    public static Long saveArticle(Jedis jedis, Article article){
        Long articleId=jedis.incr("post");
        Map<String,String> map=new HashMap<String, String>();
        map.put("Title",article.getTitle());
        map.put("Content",article.getContent());
        jedis.hmset("article"+articleId+"Data",map);
        jedis.rpush("articleList","article"+articleId+"Data");
        return articleId;
    }
    public static Article findArticle(Long articleId,Jedis jedis){
        Map<String,String> map=jedis.hgetAll("article"+articleId+"Data");
        Article article=new Article();
        article.setTitle(map.get("Title"));
        article.setContent(map.get("Content"));
        return article;
    }

    public static List pagePostId(Jedis jedis,Integer size,Integer now){
        List list=jedis.lrange("articleList",(now-1)*size,now*size-1);
        return list;
    }
    public static void updateTitle(Long articleId,Jedis jedis,String title){
//        Article article=findArticle(articleId,jedis);
//        article.setTitle(title);
//        Map<String,String> map=new HashMap<String, String>();
//        map.put("Title",article.getTitle());
//        jedis.hmset("article"+articleId+"Data",map);
        jedis.hset("article"+articleId+"Data","Title",title);
    }

    public static void delArticle(Long articleId,Jedis jedis){
        jedis.lrem("articleList",1,"article"+articleId+"Data");
        jedis.del("article"+articleId+"Data");
    }

}
