<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>宠物信息服务平台-发布帖子</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="/resources/css/app.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="/resources/wangEditor/wangEditor.min.css">
        <link rel="shortcut icon" href="/resources/images/favicon.ico">
        <script src="/resources/js/logout.js"></script>
    </head>
    <body>
        <div class="wrapper">
            <jsp:include page="../components/head.jsp"></jsp:include>
            <div class="row">
                <div class="col-md-9">
                    <div class="box box-warning">
                        <div class="box-header with-border">
                            <a href="/">首页</a> / 发布帖子
                        </div>
                        <div class="box-body">
                            <form id="form">
                                <div class="form-group">
                                    <label for="title">标题</label>
                                    <input type="text" class="form-control" id="title" name="title"
                                           placeholder="请输入话题标题，如果标题能够表达完整内容，则正文可以为空">
                                </div>
                                <div class="form-group">
                                    <label for="editor">内容</label>
                                    <input type="hidden" id="commentId" value="">
                                    <p class="hidden" id="replyP">回复<span id="replyAuthor"></span>: <a
                                            href="javascript:cancelReply();">取消</a></p>
                                    <div id="editor" style="margin-bottom: 10px;"></div>
                                </div>
                                <%-- <div class="form-group">
                                  <label for="tab">板块</label>
                                  <select id="tab" class="form-control" name="tab" onchange="getNode()">
                                   <c:forEach var="item" items="${tabList}" varStatus="status">
                                   <c:if test="${item.tabName != 'all' && item.tabName != 'member'}">
                                   <option value="${item.tabName}">${item.tabDesc}</option>
                                 </c:if>
                               </c:forEach>
                             </select>
                           </div> --%>

                                <div class="form-group">
                                    <label for="node">板块</label>
                                    <select id="node" class="form-control" name="node">
                                        <c:forEach var="item" items="${nodeList}" varStatus="status">
                                            <option value="${item.nodeTitle}">${item.nodeTitle}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group" style="display: none;">
                                    <div class="form-group">
                                        <label for="title">标签</label>
                                        <input type="text" class="form-control" id="tag" name="title"
                                               placeholder="请为你的主题选择一个标签。恰当的归类会让你发布的信息更加有用">
                                    </div>
                                </div>
                                <button type="button" id="btn" class="btn btn-warning">发布</button>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 hidden-sm hidden-xs">
                    <div class="box box-warning">
                        <div class="box-header with-border">
                            <b>帖子发布指南</b>
                        </div>
                        <div class="box-body">
                            <p>• 在标题中描述内容要点。如果一件事情在标题的长度内就已经可以说清楚，那就没有必要写正文了。</p>
                            <p>• 保持对陌生人的友善。用知识去帮助别人。</p>
                            <p>• 如果是转载的文章，请务必只填上原文的URL，内容就不用复制过来了。</p>
                            <p>• 请为你的帖子选择一个板块。恰当的归类会让你发布的信息更加有用。</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </div>
        <jsp:include page="../components/foot.jsp"></jsp:include>
        <script src="/resources/js/jquery.js"></script>
        <script src="/resources/js/bootstrap.min.js"></script>
        <script src="/resources/wangEditor/wangEditor.js"></script>
        <script src="/resources/js/emotions.js"></script>
        <!-- <script src="/resources/js/topic/node.js"></script> -->
        <script type="text/javascript">

            $(function () {
                var E = window.wangEditor;
                var editor = new E('#editor');
                editor.customConfig.uploadFileName = 'file';
                editor.customConfig.uploadImgServer = '/common/upload';
                // 将图片大小限制为 5MB
                editor.customConfig.uploadImgMaxSize = 1024 * 1024 * 1024;
                // 限制一次最多上传 5 张图片
                editor.customConfig.uploadImgMaxLength = 10;
                // 将 timeout 时间改为 10s
                editor.customConfig.uploadImgTimeout = 10000;
                editor.customConfig.menus = [
                    'head',  // 标题
                    'bold',  // 粗体
                    'fontSize',  // 字号
                    'fontName',  // 字体
                    'italic',  // 斜体
                    'underline',  // 下划线
                    'strikeThrough',  // 删除线
                    'foreColor',  // 文字颜色
                    'backColor',  // 背景颜色
                    'link',  // 插入链接
                    'list',  // 列表
                    'justify',  // 对齐方式
                    'quote',  // 引用
                    'emoticon',  // 表情
                    'image',  // 插入图片
                    'table',  // 表格
                    'video',  // 插入视频
                    'code',  // 插入代码
                    'undo',  // 撤销
                    'redo'  // 重复
                ];
                editor.customConfig.emotions = [
                    {
                        title: '默认',
                        type: 'image',
                        content: defaultEmotions
                    },
                    {
                        title: '动漫',
                        type: 'image',
                        content: animeEmotions
                    },
                    {
                        title: 'GIF',
                        type: 'image',
                        content: gifEmotions
                    },
                    {
                        title: '其他',
                        type: 'image',
                        content: otherEmotions
                    }
                ];

                // 视频上传
                editor.customConfig.uploadVideoServer = '/common/upload'; // 上传接口
                editor.customConfig.uploadVideoHooks = { // 上传完成处理方法
                    customInsert: function (insertVideo, result) {
                        console.log(result);
                        if (result.errno === 0) {
                            $.each(result.data,function(index,value){
                                insertVideo(value);
                            });
                        } else {
                            alert('上传失败');
                        }
                    }
                };

                editor.create();


                function cancelReply() {
                    $("#replyAuthor").text("");
                    $("#commentId").val("");
                    $("#replyP").addClass("hidden");
                }

                $("#btn").click(function () {
                    // 标题
                    var title = $("#title").val();
                    // html 格式的内容
                    var contentHtml = editor.txt.html();
                    // 普通格式的内容
                    var contentText = editor.txt.text();

                    // var tab = $("#tab option:selected").val();
                    // var nodeCode = $("#node option:selected").val();
                    // alert(contentHtml);

                    var node = "${node}";
                    var statusCd = "${statusCd}";

                    // 节点
                    var nodeTitle = node ? node : $("#node option:selected").val();
                    // 标签
                    var tag = $("#tag").val();
                    // var avatar = $("#editor").find("img:first").attr("src");
                    if (!title || title.length > 120) {
                        alert('请输入标题，且最大长度在120个字符以内');
                        return false;
                    } else if (!nodeTitle) {
                        alert('请选择一个合适的板块');
                        return false;
                    } else {
                        $.ajax({
                            url: '/topic/save',
                            type: 'post',
                            async: false,
                            cache: false,
                            dataType: 'json',
                            data: {
                                title: title,
                                content: contentHtml,
                                statusCd: statusCd,
                                // nodeCode:nodeCode,
                                nodeTitle: nodeTitle,
                                tag: tag
                            },
                            success: function (data) {
                                //console.log(JSON.stringify(data));
                                if (data.success != null && data.success == true) {
                                    window.location.href = "/topic/" + data.data.topic.topicId;
                                } else {
                                    alert(data.error);
                                }
                            },
                            error: function (data) {
                                console.log(data);
                                //alert(data.error);
                            }
                        })
                    }
                });
            })



        </script>
    </body>
</html>