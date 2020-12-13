package com.example.service;

import java.util.Map;
import com.example.dto.PageDataBody;
import com.example.entity.AdminUser;

public interface AdminUserService {

	// 根据用户名获取后台用户
	AdminUser getByName(String name);
	
	// 根据ID获取后台用户
	AdminUser getById(Integer id);
	
	// 获取所有的后台用户
	PageDataBody<AdminUser> page(Integer pageNumber, Integer pageSize);

	// 获取所有的后台用户
	PageDataBody<AdminUser> pageRoles(Integer pageNumber, Integer pageSize);

	/**
	 * 新增后台用户
	 * @param username:用户名
	 * @param password:密码
	 * @param avatar:头像
	 */
	void save(String username,String password,String avatar);
	
	/**
	 * 更新后台用户
	 * @param id: 后台用户ID
	 * @param password: 密码
	 * @return 如果修改的是当前登录用户，则强制重新登录
	 */
	Map<String,Object> update(Integer id,String password,String avatar);
	
	// 删除后台用户
	void removeById(Integer id);
	
	// 统计所有后台用户
	int countAll();
}
