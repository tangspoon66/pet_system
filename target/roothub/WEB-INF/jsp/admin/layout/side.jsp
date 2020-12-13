<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!-- 侧边栏 -->
<aside class="main-sidebar" style="position: fixed">
  <section class="sidebar">
    <ul class="sidebar-menu">
      <div class="user-panel">
        <div class="pull-left image">
          <img src="/resources/images/default-avatar.jpg" class="img-circle"
               alt="User Image" id="user-avatar">
        </div>
        <div class="pull-left info">
        <p id="user-name">admin</p>
          <a href="#"><i class="fa fa-circle text-success"></i>管理员</a>
        </div>
      </div>
      <li class="header">MAIN NAVIGATION</li>
        <li>
          <a href="/admin/index">
            <i class="fa fa-dashboard"></i>
            <span>仪表盘</span>
          </a>
        </li>
        <li>
          <a href="/admin/topic/list">
            <i class="fa fa-list"></i>
            <span>帖子管理</span>
          </a>
        </li>
        <li>
          <a href="/admin/reply/list">
            <i class="fa fa-comment"></i>
            <span>评论管理</span>
          </a>
        </li>
        <li>
          <a href="/admin/node/list">
            <i class="fa fa-tags"></i>
            <span>板块管理</span>
          </a>
        </li>
        <li>
          <a href="/admin/user/list">
            <i class="fa fa-user"></i>
            <span>用户管理</span>
          </a>
        </li>
        <li class="treeview">
          <a href="#">
            <i class="fa fa-server"></i> <span>权限管理</span>
            <span class="pull-right-container">
                <i class="fa fa-angle-left pull-right"></i>
              </span>
          </a>
          <ul class="treeview-menu">
              <li>
                <a href="/admin/admin_user/list">
                  <i class="fa fa-circle-o"></i>后台用户列表
                </a>
              </li>
          </ul>
        </li>
      <li>
      <a href="/admin/email">
      <i class="fa fa-envelope-o fa-fw"></i>
      <span>邮件发送</span>
      </a>
      </li>
        <li class="system-menu">
          <a href="#">
            <i class="fa fa-cogs"></i>
            <span>系统设置</span>
            <span class="pull-right-container">
                <i class="fa fa-angle-left pull-right"></i>
              </span>
          </a>
          <ul class="treeview-menu system-treeview-menu">
            <li>
              <a href="/admin/system/edit?pid=2&index=3">
                <i class="fa fa-circle-o"></i>上传设置
              </a>
            </li>
          </ul>
        </li>
      <li>
        <a href="/admin/logout">
          <i class="fa fa-sign-out"></i>
          <span>登出</span>
        </a>
      </li>
    </ul>
  </section>
  <!-- /.sidebar -->
  <script type="text/javascript">
  	$(function(){
  		$.ajax({
  			url: "/admin/user/info",
  			type: "get",
  			dataType: "json",
  			success: function(data){
  				if(data.success === true){
  					$("#user-name").text(data.data.username);
  					$("#user-avatar").attr("src",data.data.avatar);
  				}
  			},
  			error: function(data){

  			}
  		});
  	})
  </script>
</aside>