$(function() {
	$("form").submit(function() {

		var username = $("input[name='username']").val();
		var password = $("input[name='password']").val();

		// // 添加密码非空检查
		// if (!password) {
		// 	alert("密码不能为空");
		// 	return false; // 阻止表单提交
		// }

		$.get("/index/doLoginNew", {
			"username" : username,
			"password" : password
		}, function(result) {
			console.log('result', result)
			if(result == "登录成功！") {
				window.location.href = "/index";
			} else if(result=="密码不能为空！"){
				alert("密码不能为空！");
			}else{
				alert("登录失败！");
			}
		});

		return false;
	});
});
