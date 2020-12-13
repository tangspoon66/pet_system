<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-md-3 hidden-sm hidden-xs">
  	<c:if test="${sessionScope.user != null}">
					<div class="panel panel-default" id="session">
						<div class="panel-body">
							<div class="media">
								<div class="media-left">
									<a href="/user/${sessionScope.user.userName}"> <img
										src="${sessionScope.user.avatar}" title=""
										class="user-avatar img-circle">
									</a>
								</div>
								<div class="media-body">
									<div class="media-heading">
										<strong><a href="/user/${sessionScope.user.userName}">${sessionScope.user.userName}</a></strong>
										<div style="color: #7A7A7A; font-size: 12px; margin-top: 5px;">
											<i>${sessionScope.user.signature}</i>
										</div>
									</div>
								</div>
								<div style="margin-top: 15px;">
									<a href="/topic/create?statusCd=1000" style="font-size: 14px;"><button
											class="btn btn-success">发布帖子</button></a>
								</div>
							</div>
							<div class="sep10" style="height: 10px;"></div>
							<table cellpadding="0" cellspacing="0" border="0" width="100%"
								class="table_fade" style="font-size: 14px;">
								<tbody>
									<tr>
										<td width="33%" align="center"><a href="/user/topics"
											class="dark" style="display: block;"><span class="bigger">${sessionScope.countTopic}</span>
												<div class="sep3"></div> <span class="fade">我的主题</span></a></td>
										<td width="34%"
											style="border-left: 1px solid rgba(100, 100, 100, 0.4); border-right: 1px solid rgba(100, 100, 100, 0.4);"
											align="center"><a href="/collect/topics" class="dark"
											style="display: block;"><span class="bigger">${sessionScope.countCollect}</span>
												<div class="sep3"></div> <span class="fade">我的收藏</span></a></td>
										<td width="33%" align="center"><a href="/follow/topics"
											class="dark" style="display: block;"><span class="bigger">${sessionScope.countFollow}</span>
												<div class="sep3"></div> <span class="fade">特别关注</span></a></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="panel-footer" style="background-color: white">
							<div class="row">
								<span class="col-md-6"><a href="/notification/list"><span
										id="n_count">${sessionScope.countNotReadNotice}</span> 条未读消息</a></span> <span class="col-md-6 text-right">积分：<a
									href="/top100">${sessionScope.countScore}</a></span>
							</div>
						</div>
					</div>
				</c:if>
				
				<!-- 今日热议主题 -->
    <div class="panel panel-default">
      <div class="panel-heading"><span style="color: #ccc;">今日热议主题</span></div>
      <table class="table" style="font-size: 14px;">
        <tbody>
        <c:forEach var="item" items="${findHot}">
          <tr>
          <c:if test="${fn:length(item.avatar) > 0}">
          <td width="24" valign="middle" align="center">
                <%-- <a href="/user/${item.author}"> --%><img src="${item.avatar}" class="avatar img-circle" border="0" align="default" style="max-width: 24px; max-height: 24px;"><!-- </a> -->
            </td>
            </c:if>
            <td>
            	<c:choose>
                	<c:when test="${item.url != null}">
                		<a href="${item.url}">${item.title}</a>
                	</c:when>
                	<c:otherwise>
                		<a href="/topic/${item.topicId}">${item.title}</a>
                	</c:otherwise>
                </c:choose>
            </td>
          </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <!-- 今日等待回复的主题 -->
    <div class="panel panel-default">
      <div class="panel-heading"><span style="color: #ccc;">今日等待回复主题</span></div>
      <table class="table" style="font-size: 14px;">
        <tbody>
        <c:forEach var="item" items="${findTodayNoReply}">
          <tr>
          <td width="24" valign="middle" align="center">
                <a href="/user/${item.author}"><img src="${item.avatar}" class="avatar img-circle" border="0" align="default" style="max-width: 24px; max-height: 24px;"></a>
            </td>
            <td>
            	<c:choose>
                	<c:when test="${item.url != null}">
                		<a href="${item.url}">${item.title}</a>
                	</c:when>
                	<c:otherwise>
                		<a href="/topic/${item.topicId}">${item.title}</a>
                	</c:otherwise>
                </c:choose>
            </td>
          </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <!-- 积分榜 -->
    <div class="panel panel-default">
    <div class="panel-heading"><span style="color: #ccc;">积分榜  <a class="dark" href="/top100">TOP 100 &gt;&gt;</a></span></div>
    <div class="panel-body">
      <div class="row">
      <div class="inner top100"></div>
      </div>
    </div>
  </div>
    <!-- 最热标签 -->
    <div class="panel panel-default">
    <div class="panel-heading"><span style="color: #ccc;">热门节点</span></div>
    <div class="panel-body">
      <div class="row">
      <c:forEach var="item" items="${nodeList2}">
          <div class="col-md-6" style="margin-bottom: 10px; padding-left: 10px;">
            <%-- <a href="${item.url}">
              <span>n/${item.nodeTitle}</span>
            </a> --%>
            <%-- <span class="text-muted">x ${item.number}</span> --%>
            <a href="${item.url}"><span class="label label-warning text-muted">${item.nodeTitle}</span></a>
            <small class="excerpt text-muted" style="display: block; margin-top: 10px;"></small>
          </div>
          </c:forEach>
      </div>
    </div>
  </div>
    <!-- 社区运行状况 -->
    <div class="panel panel-default">
    <div class="panel-heading"><span style="color: #ccc;">社区运行状况</span></div>
    <div class="cell">
    	<table cellpadding="5" cellspacing="0" border="0" width="100%">
            <tbody style="font-size: 14px;">
            <tr>
                <td width="80" align="right"><span class="gray">注册会员：</span></td>
                <td width="auto" align="left"><strong>${countUserAll}</strong></td>
            </tr>
            <tr>
                <td width="80" align="right" style="font-size: 14px;"><span class="gray">主题：</span></td>
                <td width="auto" align="left"><strong>${countAllTopic}</strong></td>
            </tr>
            <tr>
                <td width="80" align="right"><span class="gray">回复：</span></td>
                <td width="auto" align="left"><strong>${countAllReply}</strong></td>
            </tr>
           </tbody>
        </table>
      </div>
  </div>
</div>