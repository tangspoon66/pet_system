package com.example.service;

import com.example.dto.DMLExecution;
import com.example.dto.PageDataBody;
import com.example.entity.User;
import com.example.entity.Visit;


public interface VisitService {

	/**
	 * 分页查询访问记录
	 * @param vid 被访问者ID
	 * @param pageNumber 当前页
	 * @param pageSize 每页显示的数据量
	 * @return
	 */
	PageDataBody<User> page(Integer vid,Integer pageNumber,Integer pageSize);
	
	/**
	 * 添加访问记录
	 * @param visit
	 * @return
	 */
	DMLExecution save(Visit visit);
	
	/**
	 * 被访问的次数
	 * @param vid
	 * @return
	 */
	int count(Integer vid);
	
}
