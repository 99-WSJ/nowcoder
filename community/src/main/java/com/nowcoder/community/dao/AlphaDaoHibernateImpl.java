package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author wsj
 * @description
 * @date 2024年01月18日 19:40
 */

@Repository
public class AlphaDaoHibernateImpl implements AlphaDao {
    @Override
    public String select() {
        return "hibernate";
    }
}
