package com.shiro.ehcache_test;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;

import java.io.InputStream;

/**
 * @author xukai
 * @create 2022-10-18 10:49
 */
public class TestEH {

    public static void main(String[] args) {
        //1.获取编译目录下的ehcache.xml 配置文件的流对象
        InputStream input = TestEH.class.getClassLoader().getResourceAsStream("ehcache.xml");
        //2.创建Ehcache的缓存管理对象
        CacheManager cacheManager = new CacheManager(input);
        //3.获取缓存对象
        Cache cache = cacheManager.getCache("HelloWorldCache");
        //4.创建缓存数据
        Element element = new Element("name", "xukai");
        //5.存入缓存
        cache.put(element);
        //6.从缓存中获取数据
        Element element1 = cache.get("name");
        System.out.println(element1.getObjectValue());
    }

}
