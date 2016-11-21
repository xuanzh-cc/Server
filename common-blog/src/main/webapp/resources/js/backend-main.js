/**
 * 判断是否支持 localStorage
 * @returns {boolean}
 */
function localStorageSupport(){
    return "localStorage" in window && null!==window.localStorage;
}

function SmoothlyMenu() {
    if($("body").hasClass("mini-navbar")){
        if($("body").hasClass("fixed-sidebar")){
            $("#side-menu").hide();
            setTimeout(function() {
                $("#side-menu").fadeIn(500)
            },300);
        } else {
            $("#side-menu").removeAttr("style");
        }
    } else {
        $("#side-menu").hide();
        setTimeout(function() {
            $("#side-menu").fadeIn(500)
        },100);
    }
}

function NavToggle() {
    $(".navbar-minimalize").trigger("click");
}

$(document).ready(function() {

    function e() {
        var e = $("body > #wrapper").height() - 61;
        $(".sidebard-panel").css("min-height", e + "px")
    }

    $("#side-menu").metisMenu(),

        $(function() {
            $(".sidebar-collapse").slimScroll({
                height: "100%",
                railOpacity: .9,
                alwaysVisible: !1
            })
        }),

        $(".navbar-minimalize").click(function() {
            $("body").toggleClass("mini-navbar"),
                SmoothlyMenu()
        }),

        e(),

        $(window).bind("load resize click scroll",function() {
            $("body").hasClass("body-small") || e()
        }),

        $(window).scroll(function() {
            $(window).scrollTop() > 0 && !$("body").hasClass("fixed-nav") ? $("#right-sidebar").addClass("sidebar-top") : $("#right-sidebar").removeClass("sidebar-top")
        }),

        $(".full-height-scroll").slimScroll({
            height: "100%"
        }),

        $("#side-menu>li").click(function() {
            $("body").hasClass("mini-navbar") && NavToggle()
        }),

        $("#side-menu>li li a").click(function() {
            $(window).width() < 769 && NavToggle()
        }),

        $(".nav-close").click(NavToggle),

    /(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent) && $("#content-main").css("overflow-y", "auto");


    /**
     * 菜单点击
     * @returns {boolean}
     */
    function menuItemClickHander() {
        var hrefAttr = $(this).attr("href");

        if (hrefAttr == undefined || $.trim(hrefAttr).length == 0) {
            return false;
        }

        $("#content-main").empty();

        var iframe = '<iframe class="J_iframe" width="100%" height="100%" src="' + hrefAttr + '" frameborder="0" seamless></iframe>';
        $("#content-main").append(iframe);
        return false
    }
    /* 为菜单链接 绑定 点击事件 */
    $(".menuItem").on("click", menuItemClickHander);

    $('#password-modify').on('click', function(){
        $("#password-modify-modal").modal({
            backdrop: 'static',
            keyboard: false,
        });
    });

    $('#password-modify-confirm-btn').on('click', function(){
        var oldPassword = $('#old-password').val().trim();
        var newPassword = $('#new-password').val().trim();
        var newPasswordRepeated = $('#new-password-repeated').val().trim();

        if(oldPassword == null || '' == oldPassword){
            swal("修改失败！", "原密码不能为空！", "error");
            return ;
        }
        if(oldPassword == null || '' == oldPassword){
            swal("修改失败！", "新密码不能为空！", "error");
            return ;
        }
        if(oldPassword == null || '' == oldPassword){
            swal("修改失败！", "确认密码不能为空！", "error");
            return ;
        }
        if(newPassword != newPasswordRepeated){
            swal("修改失败！", "两次密码输入不一致！", "error");
            return ;
        }

        doAjax({password : newPassword, oldPassword: oldPassword}, 'post', '/blog/user/admin/passwordModify')
            .then(function (data) {
                swal({
                    title: data.success ? '密码修改成功!' : '密码修改失败！',
                    text: data.success ? '' : data.msg,
                    type: data.success ? "success" : "error",
                    showCancelButton: false,
                    confirmButtonColor:  "#ec4758",
                    confirmButtonText: "确定",
                    closeOnConfirm: true,
                }, function () {
                    if(data.success) {
                        $("#password-modify-modal").modal('hide');
                        $('#old-password').val('');
                        $('#new-password').val('');
                        $('#new-password-repeated').val('');
                    }
                });
            }, function (data) {
                swal("服务器内部错误！", data.msg, "error");
            });
    });

});

$(window).bind("load resize",function() {
    $(this).width() < 769 && ($("body").addClass("mini-navbar"), $(".navbar-static-side").fadeIn())
});

/**
 * 发送ajax请求
 * formData 要发送的数据
 * reqestType 请求方式
 * url 请求地址
 */
function doAjax(formData, requestType, url){
    return new Promise(function(resolve, reject){
        $.ajax({
            type: requestType,
            url: url,
            data: formData,
            success: function(data){resolve(data);},
            error: function(data){reject(data);}
        });
    });
}

/**
 * 获取编辑器中的纯文本内容,没有段落格式
 * @method getContentTxt
 * @return { String } 编辑器不带段落格式的纯文本内容字符串
 * @example
 * ```javascript
 * //编辑器html内容:<p><strong>1</strong></p><p><strong>2</strong></p>
 * console.log(editor.getPlainTxt()); //输出:"12
 * ```
 */
function getContentTxt(contentHtml) {
    var agent = navigator.userAgent.toLowerCase();
    var ie = /(msie\s|trident.*rv:)([\w.]+)/.test(agent);
    var fillChar = ie && browser.version == '6' ? '\ufeff' : '\u200B';

    var reg = new RegExp(fillChar, 'g');
    //取出来的空格会有c2a0会变成乱码，处理这种情况\u00a0
    return contentHtml.replace(reg, '').replace(/\u00a0/g, ' ');
}

/**
 * 得到编辑器的纯文本内容，但会保留段落格式
 * @method getPlainTxt
 * @return { String } 编辑器带段落格式的纯文本内容字符串
 * @example
 * ```javascript
 * //编辑器html内容:<p><strong>1</strong></p><p><strong>2</strong></p>
 * console.log(editor.getPlainTxt()); //输出:"1\n2\n
 * ```
 */
function getPlainTxt(contentHtml) {

    var agent = navigator.userAgent.toLowerCase();
    var ie = /(msie\s|trident.*rv:)([\w.]+)/.test(agent);
    var fillChar = ie && browser.version == '6' ? '\ufeff' : '\u200B';

    var reg = new RegExp(fillChar, 'g');
    var html = contentHtml.replace(/[\n\r]/g, '');//ie要先去了\n在处理

    html = html.replace(/<(p|div)[^>]*>(<br\/?>|&nbsp;)<\/\1>/gi, '\n')
        .replace(/<br\/?>/gi, '\n')
        .replace(/<[^>/]+>/g, '')
        .replace(/(\n)?<\/([^>]+)>/g, function (a, b, c) {
            return dtd.$block[c] ? '\n' : b ? b : '';
        });
    //取出来的空格会有c2a0会变成乱码，处理这种情况\u00a0
    return html.replace(reg, '').replace(/\u00a0/g, ' ').replace(/&nbsp;/g, ' ');
};



$(document).ready(function() {
    //登陆页面
    $("#admin-login-btn").click(function () {
        var username = $("#username").val();
        var password = $("#password").val();
        doAjax({username: username, password: password}, "post", "/blog/user/admin/login").then(function (data) {
            var success = data['success'];
            if(success == true) {
                window.location.href = '/blog/admin/index';
            } else {
                alert(data['msg']);
            }
        }, function (data) {
        });
    });
});

/** 文章详情页面 */
$(document).ready(function(){

    /** 返回按钮 */
    $("#back-to-last-url-btn").on("click", function(){
        window.history.go(-1);
    });

    /** 文章删除按钮 */
    $("#article-delete-btn").on("click", function(){
        var self = $(this);
        swal({
                title: "确定要删该文章吗",
                text: "删除后将无法恢复，请谨慎操作！",
                type: "warning",
                showCancelButton: true,
                cancelButtonText : '取消',
                confirmButtonColor: "#ec4758",
                confirmButtonText: "确定",
                closeOnConfirm: false,
            },
            function() {
                var articleId = self.data("articleId");
                doAjax({}, "post", '/blog/article/admin/' + articleId + '/delete')
                    .then(function(data){
                        if(data.success){
                            swal({
                                title: "删除成功！",
                                type: "success",
                                showCancelButton: false,
                                confirmButtonColor: "#ec4758",
                                confirmButtonText: "确定",
                                closeOnConfirm: false,
                            }, function () {
                                window.location.href = "/blog/article/admin/list";
                            });
                        } else {
                            swal("删除失败！", data.msg, "error");
                        }
                    }, function () {
                        swal("删除失败！", "服务器内部错误！", "error");
                    });
            });
    });
});

/** 文章发布 和 编辑页面 */
function initSimditor(editContentInfo) {
    //编辑器配置
    var config = {
        textarea: $("#editor"),

        placeholder: "尽情发挥吧，少年....",

        toolbar: ['title','bold','underline','strikethrough','hr','|','fontScale','color','|','ol','ul','|','blockquote','table','link','|','code','image','|','indent','outdent','alignment','|','emoji','mark','html', '|', ],

        upload: {
            url: '/blog/picture/admin/upload',
            params: {uploadType : 1,},
            fileKey: 'upload_file',
            connectionCount: 3,
            leaveConfirm: '图片正在上传中，确定离开吗？',
        },

        defaultImage: '/blog/resources/images/editor/default.png',

        allowedTags: ['br', 'span', 'a', 'img', 'b', 'strong', 'i', 'strike', 'u', 'font', 'p', 'ul', 'ol', 'li', 'blockquote', 'pre', 'code', 'h1', 'h2', 'h3', 'h4', 'hr'],

        codeLanguages:[
            { name: 'Java', value: 'java' },
            { name: 'JavaScript', value: 'js' },
            { name: 'Bash', value: 'bash' },
            { name: 'SQL', value: 'sql'},
            { name: 'Python', value: 'python' },
            { name: 'HTML,XML', value: 'html' },
            { name: 'CSS', value: 'css' },
        ],
        //扩展 - emoji
        emoji: {
            imagePath: '/blog/resources/images/editor/emoji',
            images:[ 'smile', 'smiley', 'laughing', 'blush', 'heart_eyes', 'smirk', 'flushed', 'satisfied', 'grin', 'wink', 'stuck_out_tongue_winking_eye', 'stuck_out_tongue', 'sleeping', 'worried', 'expressionless', 'sweat_smile', 'cold_sweat', 'joy', 'sob', 'angry', 'mask', 'scream', 'sunglasses', 'heart', 'broken_heart', 'star', 'anger', 'exclamation', 'question', 'zzz', 'thumbsup', 'thumbsdown', 'ok_hand', 'punch', 'v', 'clap', 'muscle', 'pray', 'skull', 'trollface' ],
        },
    };

    var editor = new Simditor(config);

    var submitLi = $('<li class="li-simditor-icon-submit"><a class="toolbar-item toolbar-item-submit" href="javascript:;" id="article-post-btn"><span class="simditor-icon simditor-icon-submit"></span></a></li>')
    $('.simditor-toolbar > ul').append(submitLi);

    //不是新创建的文章
    if(editContentInfo.isNewCreate != true){
        var submitLi = $('<li class="li-simditor-icon-cancel"><a class="toolbar-item toolbar-item-cancel" href="javascript:;" id="article-cancel-btn"><span class="simditor-icon simditor-icon-cancel"></span></a></li>')
        $('.simditor-toolbar > ul').append(submitLi);
        $("#article-cancel-btn").click(function(){
            window.history.go(-1);
        });

        $("#article-title").val(editContentInfo.atricleDto.title);
        var articleCategoryId =  editContentInfo.atricleDto.category.categoryId;
        $('#article-categoryId option[value="' + articleCategoryId +'"]').attr("selected","true")
        editor.setValue(editContentInfo.atricleDto.content);

        var articleId = editContentInfo.atricleDto.articleId;
    }

    //文章发布按钮
    $("#article-post-btn").click(function () {
        var title = $("#article-title").val();
        // TODO 前端验证
        if(title == null || title == '') {

        }
        var categoryId = $("#article-categoryId").val();
        var contentHtml = editor.getValue();
        var contentPlain = getPlainTxt(contentHtml);
        var reqParam = {title: title, categoryId: categoryId, contentHtml: contentHtml, contentPlain: contentPlain, tags: [1, 2, 2, 3], };

        //新创建的文章
        var url;
        var message;
        var jumpUrl;

        if(editContentInfo.isNewCreate == true){
            url = '/blog/article/admin/post'
            message = "发布成功";
            var autoSave = editor.autosave;
            autoSave.storage.remove(autoSave.path);
            jumpUrl = '/blog/article/admin/list';
        } else {
            var articleId = editContentInfo.atricleDto.articleId;
            url = '/blog/article/admin/' + articleId + '/update';
            message = "更新成功";
            var autoSave = editor.autosave;
            autoSave.storage.remove(autoSave.path);
            jumpUrl = '/blog/article/admin/' + articleId + '/detail';
        }

        doAjax(reqParam, "post", url).then(function (data) {
            if(data.success){
                swal({
                    title: message,
                    type: "success",
                    showCancelButton: false,
                    confirmButtonColor: "#ec4758",
                    confirmButtonText: "确定",
                    closeOnConfirm: false,
                }, function () {
                    window.location.href = jumpUrl;
                });
            } else {
                swal("操作失败！", data.msg, "error");
            }
        }, function (data) {
            swal("服务器内部错误！", data.msg, "error");
        });
    });
};

/* 类别页面 */
$(document).ready(function () {
    // 新建类别按钮(用来弹出新建类别模态框）
    $('#category-create-modal-btn').on('click', function () {
        $("#category-create-modal").modal({
            backdrop: 'static',
            keyboard: false,
        });
    });

    // 新建类别模态框弹出后 输入框聚焦
    $('#category-create-modal').on('shown.bs.modal', function (e) {
        $("#category-create-modal-input-name").focus();
    })

    // 类别添加模态框 -> 确认添加按钮
    $("#category-create-confirm-btn").on('click', function(){
        var categoryName = $("#category-create-modal-input-name").val();
        var priority = $("#category-create-modal-input-priority").val();
        if(isNaN(priority)) {
            swal("操作失败！", '优先级必须为数字', "error");
            return ;
        }
        // TODO 增加前端验证
        doAjax({categoryName: categoryName, priority: priority}, 'post', '/blog/category/admin/add')
            .then(function (data) {
                if(data.success){
                    swal({
                        title: '类别添加成功',
                        type: "success",
                        showCancelButton: false,
                        confirmButtonColor: "#ec4758",
                        confirmButtonText: "确定",
                        closeOnConfirm: true,
                    }, function () {
                        window.location.href = '/blog/category/admin/list';
                    });
                } else {
                    swal("操作失败！", data.msg, "error");
                }
            }, function (data) {
                swal("服务器内部错误！", data.msg, "error");
            });
    });

    /** 类别删除按钮 */
    $(".category-delete-btn").on("click", function(){
        var self = $(this);
        swal({
                title: "确定要删该类别吗",
                text: "同时会删除该类别下的全部文章，请谨慎操作！",
                type: "warning",
                showCancelButton: true,
                cancelButtonText : '取消',
                confirmButtonColor: "#ec4758",
                confirmButtonText: "确定",
                closeOnConfirm: false,
            },
            function() {
                var categoryId = self.data("categoryId");
                doAjax({}, "post", '/blog/category/admin/' + categoryId + '/delete')
                    .then(function(data){
                        if(data.success){
                            swal({
                                title: "删除成功！",
                                type: "success",
                                showCancelButton: false,
                                confirmButtonColor: "#ec4758",
                                confirmButtonText: "确定",
                                closeOnConfirm: true,
                            }, function () {
                                window.location.href = "/blog/category/admin/list";
                            });
                        } else {
                            swal("删除失败！", data.msg, "error");
                        }
                    }, function () {
                        swal("删除失败！", "服务器内部错误！", "error");
                    });
            });
    });

    // 类别编辑按钮(用来弹出新建类别模态框）
    $(".category-edit-btn").on("click", function(){
        var self = $(this);
        var categoryId = self.data("categoryId");
        var categoryName = self.data("categoryName");
        var priority = self.data("categoryPriority");

        //弹出类别修改模态框
        $("#category-edit-modal").modal({
            backdrop: 'static',
            keyboard: false,
        });

        $('#category-edit-modal-input-name').val(categoryName);
        $('#category-edit-modal-input-priority').val(priority);

        //将 类别ID 数据存储到按钮
        $('#category-edit-confirm-btn').data('categoryId', categoryId);
    });

    // 类别修改模态框弹出后，输入框聚焦
    $('#category-edit-modal').on('shown.bs.modal', function (e) {
        $('#category-edit-modal-input-name').focus();
    });

    // 类别修改模态框关闭后，移除数据缓存
    $('#category-edit-modal').on('hide.bs.modal', function (e) {
        $('#category-edit-confirm-btn').removeData("categoryId");
    });

    //类别修改模态框 提交按钮点击事件
    $("#category-edit-confirm-btn").on('click', function(){
        var self = $(this);
        var categoryId = self.data("categoryId");
        var categoryName = $('#category-edit-modal-input-name').val();
        var priority = $("#category-edit-modal-input-priority").val();
        if(isNaN(priority)) {
            swal("操作失败！", '优先级必须为数字', "error");
            return ;
        }
        // TODO 增加前端验证
        doAjax({categoryName: categoryName, priority: priority}, 'post', '/blog/category/admin/' + categoryId + '/update')
            .then(function (data) {
                if(data.success){
                    swal({
                        title: '类别修改成功',
                        type: "success",
                        showCancelButton: false,
                        confirmButtonColor: "#ec4758",
                        confirmButtonText: "确定",
                        closeOnConfirm: false,
                    }, function () {
                        window.location.href = '/blog/category/admin/list';
                    });
                } else {
                    swal("操作失败！", data.msg, "error");
                }
            }, function (data) {
                swal("服务器内部错误！", data.msg, "error");
            });
    });



});






















