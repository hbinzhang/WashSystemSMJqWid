<%@ page language="java" pageEncoding="UTF-8"%>  
<%@ page contentType="text/html;charset=UTF-8"%> 
<html lang="en">
<head>
	<meta charset="UTF-8">
    <title id='Description'>光伏板清洗管理系统</title>
    <script type="text/javascript" src="../scripts/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">
    var count = 5;
        $(document).ready(function () {   
        	var goback = function() {
        		if (count > 0) {
        			$("#timebackdiv").html("上传失败，" + count + " 秒后返回数据页面。");
        			count--;
        		} else  {
	        		window.location.href='../calc.html';
        		}
        	};
        	setInterval(goback,1000);
        });
    </script>
</head>
<body class='default' >
<div id="timebackdiv"></div></br>
<div>请检查Excel文件中每一行是否都存在："日期"、"清洗电量"、"折算率指数"三列数据。</div>
</body>
</html>
