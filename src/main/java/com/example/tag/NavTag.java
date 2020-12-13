package com.example.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.example.entity.Node;
import com.example.service.NodeService;
import com.example.util.ApplicationContextUtil;

/**
 * 导航标签
 */
public class NavTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) super.getJspContext();
		ServletRequest request = pageContext.getRequest();
		Object nodeName = request.getAttribute("nodeName");
		NodeService nodeService = ApplicationContextUtil.getBean(NodeService.class);
		List<Node> nodes = nodeService.listForNav();
		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"tab-nav\" style=\"overflow-x: auto;\">");
		sb.append("\t\n");
		sb.append("<div class=\"container\" style=\"height: 45px;\">");
		sb.append("\t\n");
		sb.append("<ul style=\"padding-left: 0px\">");
		sb.append("\t\n");
		if ("index".equals(nodeName)) {
			sb.append("<li class=\"li-cate\"><a href=\"/\" class=\"li-cate-active\" style=\"padding-left: 0px;\">首页</a></li>");
		} else {
			sb.append("<li class=\"li-cate\"><a href=\"/\" class=\"li-cate-a\" style=\"padding-left: 0px;\">首页</a></li>");
		}
		if ("全部".equals(nodeName)) {
			sb.append("<li class=\"li-cate\"><a href=\"/?node=全部\" class=\"li-cate-active\">全部</a></li>");
		} else {
			sb.append("<li class=\"li-cate\"><a href=\"/?node=全部\" class=\"li-cate-a\">全部</a></li>");
		}
		sb.append("\t\n");
		for (Node node : nodes) {
			if (node.getNodeTitle().equals(nodeName)) {
				sb.append("<li class=\"li-cate\"><a href=\"/?node="+ node.getNodeTitle() +"\" class=\"li-cate-active\">"+ node.getNodeTitle() +"</a></li>");
			} else {
				sb.append("<li class=\"li-cate\"><a href=\"/?node="+ node.getNodeTitle() +"\" class=\"li-cate-a\">"+ node.getNodeTitle() +"</a></li>");
			}
			sb.append("\t\n");
		}
		sb.append("</ul>");
		sb.append("\t\n");
		sb.append("</div>");
		sb.append("\t\n");
		sb.append("</div>");
		sb.append("\t\n");
		super.getJspContext().getOut().append(sb.toString());
	}
	
}
