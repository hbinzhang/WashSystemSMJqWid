

$(document).ready(function () {
               
            $("#username").jqxInput({theme: themeConstant, height: 25, width: 200, minLength: 1});
           
            $("#pass").jqxInput({theme: themeConstant,  height: 25, width: 200, minLength: 1});

            
            $("#loginWinSubmit").jqxButton({theme: 'darkblue',  width: '50'});
            $("#loginWinCancel").jqxButton({theme: 'darkblue',  width: '50'});
                
            $('#loginWin').jqxWindow({
            				theme: 'darkblue',
            				isModal: false,
            				resizable : false,
            				position: { x: 600, y: 305 },
                    height: 200, width: 500,
                    initContent: function () {
                        
                    }
                });
            $('#loginWin').jqxWindow('open');
            
            $('#loginWin').bind('close', function(){
            	$('#loginWin').jqxWindow('open');
            });
            
            $('#loginWinSubmit').bind('click', function(){
            	loginAct();
            });
            
            $('#loginWinCancel').bind('click', function(){
            	$("#username").val('');
            	$("#pass").val('');
            });
            
            $('#pass').keydown(function(e){
            	if(e.keyCode==13){
            		loginAct();
            	}
            }); 
            
            var loginAct = function() {
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
            };
});
