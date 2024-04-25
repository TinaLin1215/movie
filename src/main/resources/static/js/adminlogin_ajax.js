$(function() {
    $("form").submit(function() {
        var username = $("input[name='username']").val();
        var password = $("input[name='password']").val();
        $.get("/admin/doAdminLogin", {
            "username" : username,
            "password" : password
        }, function(result) {
            if(result=="登录成功！"){
                window.location.href="/admin/movieIndex";
            }else {
                alert(result)
            }
        });
        return false;
    })
})
