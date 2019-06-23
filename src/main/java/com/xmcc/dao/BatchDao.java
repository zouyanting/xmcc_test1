package com.xmcc.dao;

import java.util.List;

//批量操作，减少向数据库操作的机会，避免资源浪费
public interface BatchDao<T>{
    void bacthInsert(List<T> list);
}
