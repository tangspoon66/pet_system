package com.example.web.front;

import javax.servlet.http.HttpServletRequest;

import com.example.config.SiteConfig;
import com.example.entity.User;
import com.example.util.CookieAndSessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Controller;

import com.example.service.CollectService;
import com.example.service.NoticeService;
import com.example.service.ReplyService;
import com.example.service.NodeTabService;
import com.example.service.TopicService;
import com.example.service.UserService;

@Controller
public class BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService rootUserService;
	@Autowired
	private TopicService rootTopicService;
	@Autowired
	private NodeTabService rootSectionService;
	@Autowired
	private NoticeService rootNoticeService;
	@Autowired
	private ReplyService rootReplyService;
	@Autowired
	private CollectService collectDaoService;

	@Autowired
	private SiteConfig siteConfig;


	/**
	 * 获取登录用户的信息
	 *
	 * @param request
	 * @return
	 */
	public User getUser(HttpServletRequest request) {
		return CookieAndSessionUtil.getSession(request, "user");
	}

	public String isLogin(HttpServletRequest request, String errorPage, String suesscePage) {
		User user = getUser(request);
		if (user == null) {
			return errorPage;
		}
		return suesscePage;
	}

	/**
	 * 未读通知的数量
	 * 
	 * @param request
	 * @return
	 */
	public int getNotReadNotice(HttpServletRequest request) {
		int notReadNotice = 0;
		notReadNotice = rootNoticeService.countNotReadNotice(getUser(request).getUserName());
		return notReadNotice;
	}

	/**
	 * 发布的主题的数量
	 * 
	 * @param request
	 * @return
	 */
	public int getCountTopicByUserName(HttpServletRequest request) {
		int countTopicByUserName = 0;
		countTopicByUserName = rootTopicService.countByUserName(getUser(request).getUserName());
		return countTopicByUserName;
	}

	/**
	 * 收藏话题的数量
	 * 
	 * @param request
	 * @return
	 */
	public int getCountCollect(HttpServletRequest request) {
		int countCollect = 0;
		collectDaoService.count(getUser(request).getUserId());
		return countCollect;
	}
}
