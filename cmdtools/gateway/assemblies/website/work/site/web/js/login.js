$(document).ready(function(){
	$('.buttons a[login]').on('click',function(e){
		$('.form span[error]').remove();
		e.stopPropagation();
		e.preventDefault();
		var the=$(this);
		var user=$('.login-form ul li[user] input').val();
		if(user==''){
			user.after("<span error style='color:red;'>用户名为空</span>");
			return;
		}
		var pwd=$('.login-form ul li[pwd] input').val();
		if(pwd==''){
			the.after("<span error style='color:red;'>密码为空</span>");
			return;
		}
		$.post('../public/auth.service',{user:user,pwd:pwd},function(data){
				window.location.href='../';
		}).error(function(e){
			the.parent('.buttons').siblings('.form').append("<li error style='width:300px;height:20px;color:red;overflow:hidden;text-overflow:ellipsis;text-align:left;'><span style='display:inline-block;' error>认证失败,原因："+e.responseText+"</span></li>");
		})
	});
	$('.login-form ul li[pwd] input').keyup(function(e){
		if(e.keyCode==13){
			$('.buttons a[login]').trigger('click');
		}
	});
})