package com.example.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.NodeDao;
import com.example.dto.PageDataBody;
import com.example.entity.Node;
import com.example.service.NodeService;
import com.example.service.TopicService;


@Service
public class NodeServiceImpl implements NodeService{

	@Autowired
	private NodeDao nodeDao;
	
	@Autowired
	private TopicService topicService;
	
	private List<Node> nodeIndexCache;
	
	private List<Node> nodeNavCache;
	
	//根据板块查询节点
	@Override
	public List<Node> findAllByTab(String tabName, Integer pageNumber, Integer pageSize) {
		return nodeDao.selectAllByTab(tabName, pageNumber, pageSize);
	}

	@Override
	public Node findByTitle(String title) {
		return nodeDao.selectByTitle(title);
	}
	
	@Override
	public Node findById(Integer id) {
		return nodeDao.selectById(id);
	}

	//子节点
	@Override
	public List<Node> findChildrenNode(String nodeTitle, Integer pageNumber, Integer pageSize) {
		return nodeDao.selectChildrenNode(nodeTitle, pageNumber, pageSize);
	}

	//相邻节点
	@Override
	public List<Node> adjacencyNode(Node node) {
		if(node.getParentNodeCode() != null) {
			return nodeDao.selectAtherNode(node.getNodeCode(), node.getParentNodeCode(), null, null);
		}else {
			return nodeDao.selectAtherParentNode(node.getNodeCode(), node.getTabId(), null, null);
		}
	}

	//查询全部节点
	@Override
	public List<Node> findAll(Integer pageNumber, Integer pageSize) {
		return nodeDao.selectAll(pageNumber, pageSize);
	}

	// 后台节点列表
	@Override
	public PageDataBody<Node> pageForAdmin(String nodeTitle, Integer pageNumber, Integer pageSize) {
		List<Node> list = nodeDao.listForAdmin(nodeTitle, (pageNumber - 1) * pageSize, pageSize);
		return new PageDataBody<Node>(list, pageNumber, pageSize, count(nodeTitle));
	}

	// 统计节点列表
	@Override
	public int count(String nodeTitle) {
		return nodeDao.count(nodeTitle);
	}

	@Transactional
	@Override
	public void update(Integer nodeId, String nodeTitle, String avatarNormal, String avatarLarge, String nodeDesc, Integer sort) {
		Node node = findById(nodeId);
		// 先更新话题的节点名称
		if(!nodeTitle.equals(node.getNodeTitle())) {
			topicService.updateNodeTitile(node.getNodeTitle(), nodeTitle);
		}
		node.setNodeTitle(nodeTitle);
		node.setAvatarNormal(avatarNormal);
		node.setAvatarLarge(avatarLarge);
		node.setNodeDesc(nodeDesc);
		node.setUrl("/n/" + nodeTitle);
		node.setUpdateDate(new Date());
		node.setSort(sort);
		// 最后在更新节点
		nodeDao.update(node);
		nodeIndexCache = null;
		nodeNavCache = null;
	}

	/**
	 * 删除节点
	 */
	@Transactional
	@Override
	public void deleteById(Integer id) {
		Node node = findById(id);
		// 先将话题的节点设置为 null
		topicService.updateNodeTitile(node.getNodeTitle(), null);
		// 然后在删除节点
		nodeDao.deleteById(id);
	}

	/**
	 * 统计当天的节点数量
	 */
	@Override
	public int countToday() {
		return nodeDao.countToday();
	}

	@Override
	public void save(Integer nodeId, String nodeTitle, String avatarNormal, String avatarLarge, String nodeDesc, Integer sort) {
		Node node = new Node();
		node.setNodeTitle(nodeTitle);
		node.setAvatarNormal(avatarNormal);
		node.setAvatarLarge(avatarLarge);
		node.setNodeDesc(nodeDesc);
		node.setNodeCode(nodeTitle);
		node.setUrl("/n/" + nodeTitle);
		node.setCreateDate(new Date());
		node.setSort(sort);
		nodeDao.insert(node);
	}

	@Override
	public void update(Node node) {
		nodeIndexCache = null;
		nodeNavCache = null;
		nodeDao.update(node);
	}

	@Override
	public List<Node> listForIndex() {
		if (nodeIndexCache != null) {
			return nodeIndexCache;
		}
		nodeIndexCache = nodeDao.listForIndex();
		return nodeIndexCache;
	}

	@Override
	public List<Node> listForNav() {
		if (nodeNavCache != null) {
			return nodeNavCache;
		}
		nodeNavCache = nodeDao.listForNav();
		return nodeNavCache;
	}
	
}
