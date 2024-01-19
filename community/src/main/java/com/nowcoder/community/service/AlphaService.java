package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author wsj
 * @description
 * @date 2024年01月18日 21:55
 */

@Service
public class AlphaService {

    @Autowired
     private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("实例化AlphaService");
    }
    @PostConstruct
    public void init() {
        System.out.println("初始化AlphaService");
    }
    @PreDestroy
    public void destroy() {
        System.out.println("销毁AlphaService");
    }
    public String find() {
        return alphaDao.select();

    }
}
