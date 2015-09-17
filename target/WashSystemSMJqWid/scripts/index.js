

$(document).ready(function () {
               
            $("#username").jqxInput({theme: themeConstant, height: 25, width: 200, minLength: 1});
           
            $("#pass").jqxInput({theme: themeConstant,  height: 25, width: 200, minLength: 1});

            
            $("#loginWinSubmit").jqxButton({theme: themeConstant,  width: '50'});
            $("#loginWinCancel").jqxButton({theme: themeConstant,  width: '50'});
                
            $('#loginWin').jqxWindow({
            				theme: 'darkblue',
            				isModal: true,
            				resizable : false,
                    height: 200, width: 500,
                    initContent: function () {
                        
                    }
                });
            $('#loginWin').jqxWindow('open');
            
            $('#loginWin').bind('close', function(){
            	$('#loginWin').jqxWindow('open');
            });
            
            $('#loginWinSubmit').bind('click', function(){
            	$.ajax({    
        	        type:'get',        
        	        url:'user/userauth?username=' + $('#username').val() + '&password=' + $('#pass').val(),    
        	        data:{},    
        	        cache:false,    
        	        dataType:'json',    
        	        success:function(data){  
        	        	//console.log("return data is: " + data);
        	        	if (data.code == '1') {
        	        		//console.log("login success");
        	        		window.location.href='./calc.html';
        	        	} else {
        	        		alert("登录错误，请重新登录！");
        	        	}
        	        },
        	        error : function() {    
        	            alert("登录异常，请检查网络连接或服务器状态！");    
        	       }    
        	    });    
            });
            
            $('#loginWinCancel').bind('click', function(){
            	$("#username").val('');
            	$("#pass").val('');
            });
            
            
});
