package com.example.web.admin;

import java.util.Map;


import com.example.entity.AdminUser;
import com.example.exception.ApiAssert;
import com.example.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.dto.PageDataBody;
import com.example.dto.Result;



@Controller
@RequestMapping("/admin/admin_user")
public class AdminUserAdminController {

	@Autowired
	private AdminUserService adminUserService;



	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model modle, @RequestParam(defaultValue = "1") Integer p) {
		PageDataBody<AdminUser> page = adminUserService.pageRoles(p, 25);
		modle.addAttribute("page", page);
		modle.addAttribute("p", p);
		return "admin/admin_user/list";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) {
		return "admin/admin_user/add";
	}

	/**
	 * 添加后台用户
	 * @param username:用户名
	 * @param password:密码
	 * @param avatar:头像
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> add(String username, String password, String avatar) {
		ApiAssert.notNull(username, "用户名不能为空");
		ApiAssert.notNull(password, "密码不能为空");
		if(StringUtils.isEmpty(avatar)) avatar = "/resources/images/default-avatar.jpg";
		adminUserService.save(username, password, avatar);
		return new Result<>(true, "添加用户成功");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Model model, Integer id) {
		AdminUser adminUser = adminUserService.getById(id);
		model.addAttribute("adminUser", adminUser);
		return "admin/admin_user/edit";
	}

	/**
	 * 编辑后台用户
	 * 如果 password 为空，则不更新 password
	 * 如果 avatar 为空，则设置 avatar 为默认值
	 * 如果修改的是当前登录用户，则强制重新登录
	 * @param id:后台用户ID
	 * @param password:密码
	 * @param avatar:头像
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> edit(Integer id, String password, String avatar) {
		// ApiAssert.notNull(username, "用户名不能为空");
		if(StringUtils.isEmpty(avatar)) avatar = "/resources/images/default-avatar.jpg";
		// 更新用户
		Map<String, Object> map = adminUserService.update(id, password, avatar);
		return new Result<Map<String,Object>>(true, map);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(Integer id) {
		adminUserService.removeById(id);
		return "redirect: /admin/admin_user/list";
	}

}
