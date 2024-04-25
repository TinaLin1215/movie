$(function(){
    $.ajax({
        type: "GET",
        url: "/movie?id="+id,
        success: function(response) {
            // 处理成功的情况

            window.location.href = "/movie?id="+id;
        },
        error: function(jqXHR) {
            // 处理错误的情况
            if (jqXHR.status === 403) {
                // 如果是 403 错误，表示无权限访问
                alert("本电影为VIP用户专享！");
            } else {
                // 其他错误处理
                alert("发生错误：" + jqXHR.statusText);
            }
        }
    });
});
