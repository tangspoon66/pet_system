<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="layout/header.jsp" %>
<!-- 内容主体区域 -->
<div class="content-wrapper" style="padding: 50px 0 40px;">
    <section class="content-header">
        <h1>
            首页 <small>邮件发送</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i>
                首页</a></li>
            <li class="active">邮件发送</li>
        </ol>
    </section>
    <section class="content">

        <div class="box box-warning">
            <div class="box-header with-border">
				发送邮件
            </div>
            <div class="box-body">
                <form id="form">
                    <div class="form-group">
                        <label for="title">邮件标题</label>
                        <input type="text" class="form-control" id="title" name="title"
                               placeholder="请输入邮件标题">
                    </div>
                    <div class="form-group">
                        <label for="editor">邮件内容</label>
                        <div id="editor" style="margin-bottom: 10px;"></div>
                    </div>

                    <button type="button" id="btn" class="btn btn-warning">发送</button>
                </form>
            </div>
        </div>
        <!-- ./col -->
    </section>
	<script src="/resources/js/jquery.js"></script>
	<script src="/resources/js/bootstrap.min.js"></script>
	<script src="/resources/wangEditor/wangEditor.js"></script>
	<script src="/resources/js/emotions.js"></script>
    <script type="text/javascript">
        $(function () {
            $(".sidebar-menu li:eq(1)").addClass("active");
        });
    </script>
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


            editor.create();


            $("#btn").click(function () {
                // 标题
                var title = $("#title").val();
                // html 格式的内容
                var contentHtml = editor.txt.html();
                // 普通格式的内容
                var contentText = editor.txt.text();

                // var avatar = $("#editor").find("img:first").attr("src");
                if (!title || title.length > 120) {
                    alert('请输入标题，且最大长度在120个字符以内');
                    return false;
                }  else {
                    $.ajax({
                        url: '/admin/email/send',
                        type: 'post',
                        async: false,
                        cache: false,
                        dataType: 'json',
                        data: {
                            title: title,
                            content: contentHtml,
                        },
                        success: function (data) {
                            //console.log(JSON.stringify(data));
                            if (data.success != null && data.success == true) {
								alert('发送成功');
								$("#title").val('');
								editor.txt.html('')
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
</div>
<%@ include file="layout/footer.jsp" %>