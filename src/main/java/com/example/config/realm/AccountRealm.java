package com.example.config.realm;

import com.example.entity.AdminUser;
import com.example.service.AdminUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class AccountRealm extends AuthorizingRealm {

	private Logger log = LoggerFactory.getLogger(AuthorizingRealm.class);
	
	@Autowired
	private AdminUserService adminUserService;
	

	
	//权限校验
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 获取 principal
		AdminUser principal = (AdminUser)principals.getPrimaryPrincipal();
		// 获取用户
		AdminUser adminUser = adminUserService.getByName(principal.getUsername());
		if(adminUser != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			return info;
		}
		return null;
	}

	//身份校验
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 1. 把 AuthenticationToken 转换为 UsernamePasswordToken 
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		
		// 2. 从 UsernamePasswordToken 中来获取 username
		String username = upToken.getUsername();
				
		log.debug("用户：{} 正在登录...", username);
		
		// 3.从数据库中查询 username 对应的用户记录
		AdminUser adminUser = adminUserService.getByName(username);
		
		// 4.如果用户不存在，则抛出未知用户的异常
		if(adminUser == null) throw new UnknownAccountException("用户不存在!");
		
		// 5.根据用户的情况, 来构建 AuthenticationInfo 对象并返回，通常使用的实现类为: SimpleAuthenticationInfo
		
		/**
		 * 5.1 principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象. 
		 * 可以通过 SecurityUtils.getSubject().getPrincipal() 拿到 principal，如果有多个，则随机返回其中的一个
		 * 也可以通过 PrincipalCollection.getPrimaryPrincipal() 拿到 principal，如果有多个，则随机返回其中的一个
		 * 也可以通过 PrincipalCollection.asSet() 拿到所有的 principal，返回的是 set 集合
		 */
		// Object principal = username;
		AdminUser principal = new AdminUser();
		principal.setAdminUserId(adminUser.getAdminUserId());
		principal.setUsername(username);
		principal.setAvatar(adminUser.getAvatar());
		
		// 5.2 credentials: 密码
		Object credentials = adminUser.getPassword();
		
		// 5.3 realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
		String realmName = getName();
		
		// 5.4 盐值加密
		ByteSource credentialsSalt = ByteSource.Util.bytes(username);
		
		return new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
	}

	//测试
	public static void main(String[] args) {
		String hashAlgorithmName = "MD5";
		Object credentials = "123";
		Object salt = ByteSource.Util.bytes("admin");
		int hashIterations = 1024;
		Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
		System.out.println(result);
	}

}
