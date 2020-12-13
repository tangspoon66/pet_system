package com.example.service;

import java.util.List;

import com.example.entity.Tab;

public interface TabService {

	/**
	 * 查询所有板块
	 * @return
	 */
	List<Tab> selectAll();
}
