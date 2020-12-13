package com.example.web.admin;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.dto.TopicExecution;
import com.example.entity.AdminUser;
import com.example.entity.User;
import com.example.exception.ApiAssert;
import com.example.service.*;
import com.google.common.base.Joiner;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.dto.Result;
import com.sun.management.OperatingSystemMXBean;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/admin")
public class IndexAdminController {

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private UserService userService;
    @Autowired
    private NodeService nodeService;

    @Autowired
    private MailService mailService;

    // 后台首页
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        // 查询当天新增话题
        model.addAttribute("topic_count", topicService.countToday());
        // 查询当天新增评论
        model.addAttribute("comment_count", replyService.countToday());
        // 查询当天新增用户
        model.addAttribute("user_count", userService.countToday());
        //查询当天新增节点
        model.addAttribute("node_count", nodeService.countToday());
        // 获取操作系统的名字
        model.addAttribute("os_name", System.getProperty("os.name"));
        // 内存
        int kb = 1024;
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        // 总的物理内存（G）
        float totalMemorySize = (float) osmxb.getTotalPhysicalMemorySize() / kb / kb / kb;

        //已使用的物理内存（G）
        float usedMemory = (float) (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / kb / kb / kb;
        // 获取系统cpu负载
        double systemCpuLoad = osmxb.getSystemCpuLoad();
        // 获取jvm线程负载
        double processCpuLoad = osmxb.getProcessCpuLoad();

        DecimalFormat df = new DecimalFormat("0.0");
        model.addAttribute("totalMemorySize", df.format(totalMemorySize));
        model.addAttribute("usedMemory", df.format(usedMemory));
        model.addAttribute("systemCpuLoad", df.format(systemCpuLoad));
        model.addAttribute("processCpuLoad", df.format(processCpuLoad));

        return "/admin/index";
    }

    // 后台登录页面
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/admin/index";
        }
        return "/admin/login";
    }

    // 后台登录处理
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String username, String password, @RequestParam(defaultValue = "0") Boolean rememberMe, Model model) {
        try {
            // 添加用户认证信息
            //初始化
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {
                UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
                //进行验证，这里可以捕获异常，然后返回对应信息
                //登录，即身份校验，由通过Spring注入的UserRealm会自动校验输入的用户名和密码在数据库中是否有对应的值
                subject.login(token);
            }
        } catch (AuthenticationException e) {
            model.addAttribute("error", "用户名或密码错误");
            model.addAttribute("username", username);
            return "/admin/login";
        }
        return "redirect:/admin/index";
    }

    // 出错页面
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error() {
        return "admin/error/error";
    }

    /**
     * 获取后台登录用户的信息
     *
     * @return
     */
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    @ResponseBody
    public Result<AdminUser> getAdminUser() {
        AdminUser user = (AdminUser) SecurityUtils.getSubject().getPrincipal();
        return new Result<AdminUser>(true, user);
    }

    /**
     * 邮件发送
     *
     * @return
     */
    @RequestMapping("/email")
    public String email() {
        return "admin/email";
    }

    /**
     * 邮件发送
     *
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(value = "/email/send", method = RequestMethod.POST)
    @ResponseBody
    private Result<Object> save(String title, String content, HttpServletRequest request) {
        List<String> emailList = userService.findAllEmail();
        try {
            //把所有邮箱地址用，隔开
            String to = Joiner.on(",").join(emailList);
            mailService.sendMail(to, title, content);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<Object>(false, "发送失败");
        }
        return new Result<Object>(true, "发送成功");
    }
}
