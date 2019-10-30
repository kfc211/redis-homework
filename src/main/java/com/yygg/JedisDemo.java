package com.yygg;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

public class JedisDemo {
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
        String uptitle="123";
        Article article2=updateTitle(articleId,jedis,uptitle);
        System.out.println("修改题目成功"+article2.getTitle());
        delArticle(articleId,jedis);
        System.out.println("删除成功");
    }
    public static Long saveArticle(Jedis jedis,Article article){
        String articleJson=JSON.toJSONString(article);
        Long articleId=jedis.incr("post");
        jedis.set("article"+articleId+"Data",articleJson);
        return articleId;
    }

    public static Article findArticle(Long articleId,Jedis jedis){
        String articleJson=jedis.get("article"+articleId+"Data");
        Article article=JSON.parseObject(articleJson,Article.class);
        return article;
    }

    public static Article updateTitle(Long articleId,Jedis jedis,String title){
        Article article=findArticle(articleId,jedis);
        article.setTitle(title);
        String articleJson=JSON.toJSONString(article);
        jedis.set("article"+articleId+"Data",articleJson);
        return article;
    }

    public static void delArticle(Long articleId,Jedis jedis){
        jedis.del("article"+articleId+"Data");
    }
}
