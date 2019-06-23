package com.xmcc.dao.impl;

import com.xmcc.dao.BatchDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class BatchDaoImpl<T> implements BatchDao<T> {
    //批量处理的实体管理
    @PersistenceContext
    private EntityManager em;

    @Override
    public void bacthInsert(List<T> list) {
        //获取批量处理的长度
        int size=list.size();
        //循环存入缓冲区
        for(int i=0;i<size;i++){
            //存入缓冲区
            em.persist(list.get(i));
            //每100条写入数据库，如果不足100条，就直接将全部数据写入数据库
            if(i%100==0||i==size-1){
                em.flush();
                em.clear();
            }
        }
    }
}
