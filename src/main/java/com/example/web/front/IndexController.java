package com.example.web.front;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.entity.Node;
import com.example.entity.Tag;
import com.example.entity.Topic;
import com.example.entity.User;
import com.example.exception.ApiAssert;
import com.example.util.CookieAndSessionUtil;
import com.example.util.bcrypt.BCryptPasswordEncoder;
import org.apache.commons.collections.map.HashedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dto.PageDataBody;
import com.example.dto.Result;
import com.example.dto.UserExecution;
import com.example.service.CollectService;
import com.example.service.FollowService;
import com.example.service.NodeService;
import com.example.service.NoticeService;
import com.example.service.ReplyService;
import com.example.service.NodeTabService;
import com.example.service.TopicService;
import com.example.service.UserService;

@Controller
public class IndexController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private NodeTabService nodeTabService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private CollectService collectDaoService;
	@Autowired
	private NodeService nodeService;

	@Autowired
	private FollowService followService;

	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	private String index(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "p", defaultValue = "1") Integer p,
			@RequestParam(value = "tab", defaultValue = "all") String tab,
			@RequestParam(value = "node", required = false) String node) {
		
		request.setAttribute("tab", tab);
		
		User user = getUser(request);
		
		if (user != null) {
			int countTopic = topicService.countByUserName(user.getUserName());
			int countCollect = collectDaoService.count(user.getUserId());
			int countFollow = followService.countByUid(user.getUserId());
			int countNotReadNotice = noticeService.countNotReadNotice(user.getUserName());
			int countScore = userService.countScore(user.getUserId());

			request.setAttribute("countTopic", countTopic);
			request.setAttribute("countCollect", countCollect);
			request.setAttribute("countFollow", countFollow);
			request.setAttribute("countNotReadNotice", countNotReadNotice);
			request.setAttribute("countScore", countScore);
		}
		
		if (StringUtils.isEmpty(node)) {
			Map<String, List<Topic>> map = new LinkedHashMap<>();
			map.put("热门帖子", topicService.findIndexHot(1, 5, "hot").getList());
			map.put("最新帖子", topicService.findIndexHot(1, 5, "newest").getList());
			map.put("等待回复", topicService.findIndexHot(1, 5, "noReply").getList());
			List<Node> nodeListForIndex = nodeService.listForIndex();
			nodeListForIndex.stream().forEach(n -> {
				map.put(n.getNodeTitle(), topicService.pageAllByTabAndNode(p, 10, tab, n.getNodeTitle()).getList());
			});
			request.setAttribute("map", map);
			request.setAttribute("nodeName", "index");
			return "index";
		} else {
			List<Node> hotNodeList = nodeService.findAll(0, 10); // 热门板块
			int countUserAll = userService.countUserAll(); // 注册会员的数量
			int countAllTopic = topicService.countAllTopic(null, null); // 帖子的数量
			int countAllReply = replyService.countAll(); // 评论的数量
			
			request.setAttribute("hotNodeList", hotNodeList);
			request.setAttribute("countUserAll", countUserAll);
			request.setAttribute("countAllTopic", countAllTopic);
			request.setAttribute("countAllReply", countAllReply);
			
			PageDataBody<Topic> page;
			
			if ("全部".equals(node)) {
				page = topicService.pageAllByTabAndNode(p, 25, tab, null);
			} else {
				page = topicService.pageAllByTabAndNode(p, 25, tab, node);
			}
			
			request.setAttribute("page", page);
			request.setAttribute("nodeName", node);
			return "node";
		}
	}

	/**
	 * 注册页面
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	private String register(HttpServletRequest request) {
		return "register";
	}

	/**
	 * 注册接口
	 * @param username
	 * @param password
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	private Result<UserExecution> register(@RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("email") String email,
			@RequestParam("userType") String userType, HttpServletRequest request, HttpServletResponse response) {
		ApiAssert.notEmpty(username, "请输入用户名");
		ApiAssert.notEmpty(password, "请输入密码");
		ApiAssert.notEmpty(email, "请输入邮箱");
		User user = userService.findByName(username);
		ApiAssert.isNull(user, "用户已存在");
		user = userService.findByEmail(email);
		ApiAssert.isNull(user, "邮箱已存在");
		UserExecution save = userService.createUser(username, password, email, userType);
		// 设置session
		CookieAndSessionUtil.setSession(request, "user", save.getUser());
		return new Result<UserExecution>(true, save);
	}

	/**
	 * 登录页面
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	private String login(HttpServletRequest request) {
		return "login";
	}

	/**
	 * 登录接口
	 * 
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	private Result<User> login(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response) {
		User user = userService.findByName(username);
		ApiAssert.notNull(user, "用户不存在");
		ApiAssert.isTrue(new BCryptPasswordEncoder().matches(password, user.getPassword()), "密码不正确");
		// 设置session
		CookieAndSessionUtil.setSession(request, "user", user);
		return new Result<User>(true, user);
	}

	/**
	 * 退出
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	private String logout(HttpServletRequest request, HttpServletResponse response) {
		// stringRedisTemplate.delete("user");
		CookieAndSessionUtil.removeSession(request, "user");
		return "redirect:/";
	}

	/**
	 * 标签页
	 * 
	 * @param request
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	private String tag(HttpServletRequest request, @RequestParam(value = "p", defaultValue = "1") Integer p) {
		PageDataBody<Tag> tag = topicService.findByTag(p, 50);
		request.setAttribute("tag", tag);
		return "tag/tag";
	}

	@RequestMapping(value = "/session", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, String> session(HttpServletRequest request) {
		User user = getUser(request);
		HashedMap map = new HashedMap();
		if (user != null) {
			map.put("success", true);
			map.put("user", user.getUserName());
			return map;
		} else {
			map.put("success", false);
			map.put("user", "");
			return map;
		}
	}

	/**
	 * 搜索
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	private String search(HttpServletRequest request, @RequestParam("s") String search,
			@RequestParam(value = "p", defaultValue = "1") Integer p) {
		if (search == null || search.equals("")) {
			return "search";
		}
		PageDataBody<Topic> pageLike = topicService.pageLike(p, 50, search);
		// BaseEntity baseEntity = new BaseEntity();
		// request.setAttribute("baseEntity", baseEntity);
		request.setAttribute("pageLike", pageLike);
		request.setAttribute("search", search);
		return "search/search";
	}

	/**
	 * Top100积分榜
	 * 
	 * @return
	 */
	@RequestMapping(value = "/top100")
	private String top100() {
		return "score/top100";
	}

	/**
	 * 关于
	 * 
	 * @return
	 */
	@RequestMapping(value = "/about")
	private String about() {
		return "foot/about";
	}

	/**
	 * faq
	 * 
	 * @return
	 */
	@RequestMapping(value = "/faq")
	private String faq() {
		return "foot/faq";
	}

	/**
	 * api
	 * 
	 * @return
	 */
	@RequestMapping(value = "/api")
	private String api() {
		return "foot/api";
	}

	/**
	 * mission
	 * 
	 * @return
	 */
	@RequestMapping(value = "/mission")
	private String mission() {
		return "foot/mission";
	}

	/**
	 * advertise
	 * 
	 * @return
	 */
	@RequestMapping(value = "/advertise")
	private String advertise() {
		return "foot/advertise";
	}

}
