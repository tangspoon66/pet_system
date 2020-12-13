package com.example.web.admin;

import java.util.Date;

import com.example.config.SiteConfig;
import com.example.entity.Topic;
import com.example.exception.ApiAssert;
import com.example.service.NodeService;
import com.example.service.ReplyService;
import com.example.service.TopicService;
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
@RequestMapping("/admin/topic")
public class TopicAdminController {

	@Autowired
	private TopicService topicService;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private ReplyService replyService;
	
	/**
	 * 话题列表
	 * @param startDate
	 * @param endDate
	 * @param author
	 * @param model
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public String list(String startDate, String endDate,String author, Model model,@RequestParam(defaultValue = "1") Integer p) {
		if (StringUtils.isEmpty(startDate)) startDate = null;
	    if (StringUtils.isEmpty(endDate)) endDate = null;
	    if (StringUtils.isEmpty(author)) author = null;
		PageDataBody<Topic> page = topicService.pageForAdmin(author, startDate, endDate, p, 25);
		model.addAttribute("page", page);
	    model.addAttribute("startDate", startDate);
	    model.addAttribute("endDate", endDate);
	    model.addAttribute("author", author);
	    return "admin/topic/list";
	}
	
	/**
	 * 置顶或者取消置顶
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/top",method = RequestMethod.GET)
	@ResponseBody
	public Result<String> top(@RequestParam("id") Integer id){
		Topic topic = topicService.findById(id);
		topic.setTop(!topic.getTop());
		topic.setUpdateDate(new Date());
		topicService.updateTopic(topic);
		return new Result<>(true, "更新成功！");
	}
	
	/**
	 * 加精或者取消加精
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/good",method = RequestMethod.GET)
	@ResponseBody
	public Result<String> good(@RequestParam("id") Integer id){
		Topic topic = topicService.findById(id);
		topic.setGood(!topic.getGood());
		topic.setUpdateDate(new Date());
		topicService.updateTopic(topic);
		return new Result<>(true, "更新成功！");
	}
	
	/**
	 * 删除话题
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@ResponseBody
	public Result<String> delete(@RequestParam("id") Integer id){
		topicService.deleteByTopicId(id);
		// 删除关联的评论
		replyService.deleteByTopicId(id);
		return new Result<>(true, "删除成功！");
	}
	
	/**
	 * 编辑话题页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam("id") Integer id,Model model) {
		model.addAttribute("topic", topicService.findById(id));
		model.addAttribute("nodes", nodeService.findAll(null, null));
		model.addAttribute("vEnter", "\n");
		return "/admin/topic/edit";
	}
	
	/**
	 * 编辑话题接口
	 * @param id
	 * @param title
	 * @param content
	 * @param nodeTitle
	 * @return
	 */
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	@ResponseBody
	public Result<String> edit(@RequestParam("id") Integer id,@RequestParam("title") String title,
					  		   @RequestParam("content") String content,@RequestParam("nodeTitle") String nodeTitle){
		ApiAssert.notEmpty(title, "标题不能为空");
		ApiAssert.notEmpty(nodeTitle, "节点不能为空");
		Topic topic = topicService.findById(id);
		topic.setTitle(title);
		topic.setContent(content);
		topic.setNodeTitle(nodeTitle);
		topic.setUpdateDate(new Date());
		topicService.updateTopic(topic);
		return new Result<>(true, "编辑成功！");
	}
}
