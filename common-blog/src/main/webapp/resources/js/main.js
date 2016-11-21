/**
 * Created by Administrator on 2016/9/14.
 */

function initFooterPosition(){
    var footer = document.getElementById("page-footer");
    var scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
    footer.style.top = scrollHeight + "px";
}

var goTop = function(){
    var backTopBtn = document.getElementById("backToTop");
    var timer = null;
    window.onscroll = function(){
        var topPos = document.documentElement.scrollTop || document.body.scrollTop;
        //if(topPos <= clientHeight)
        if(topPos <= 0){
            backTopBtn.style.display = "none";
        }
        else{
            backTopBtn.style.display = "block";
        }
    };

    var isUpBtnClick = false;

    backTopBtn.onclick = function(){
        if(isUpBtnClick) return;
        timer = setInterval(function(){
            isUpBtnClick = true;
            var topPos = document.documentElement.scrollTop || document.body.scrollTop;
            var offset = Math.floor(-topPos/6);
            document.documentElement.scrollTop = document.body.scrollTop = topPos + offset;
            if(topPos <= 0){
                isUpBtnClick = false;
                clearInterval(timer);
            }
        },30);
    };
};



$(document).ready(function(){
    $('.nav-toggle').on('click', function(){
        var self = $(this);
        var menu = $('.nav-collapse')[0];
        if(menu.className.indexOf('hidden') != -1){
            menu.className = menu.className.replace(/\s*hidden\s*/, '');
            self.addClass('active');
            initFooterPosition();
        } else {
            menu.className += ' hidden';
            self.removeClass('active');
            initFooterPosition();
        }
    });

    goTop();

    initFooterPosition();

    $('#nav-search-form').submit(function(){
        var keyword = $('.nav-search-input').val();
        if(keyword == null || '' == keyword.trim()){
            return false;
        }
        return true;
    });
});
