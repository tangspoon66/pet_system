package com.example.dao;

import java.util.List;

import com.example.entity.Tab;

/**
 * 父板块数据接口
 */
public interface TabDao {

	/**
	 * 查询所有板块
	 * @return
	 */
	List<Tab> selectAll();
}
