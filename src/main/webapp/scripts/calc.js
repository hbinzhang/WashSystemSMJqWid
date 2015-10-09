var currentuser = "";
var currentuserDispName = "";
var currentuserCompName = "";
var currentuserCompEs = "";
var calcByDay = true;
var needloadgriddata = true;

var selectedStation = '';
var stationSelectionData = [];
var url;
selectedStation = stationSelectionData[0];
var source;

$(document).ready(function () {
	var aj = $.ajax( {    
	    url:'user/getCurrentUser',    
	    type:'get',    
	    async : false,
	    cache:false,    
	    dataType:'json',
	    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	    success:function(data) {    
	    	currentuser = data.userName;
	    	currentuserDispName = data.displayName;
	    	currentuserCompName = data.companyName;
	    	currentuserCompEs = data.companyEs;
	     },    
	     error : function() {
	    	 //alert(data);
	     }    
	});  
	
	// Modify logo and title
	var logoUrl = "url('./images/logo/" + currentuserCompEs + ".png')";
	var banerUrl = "url('./images/logo/" + currentuserCompEs + "_header.jpg')";
	var rooterUrl = "url('./images/logo/" + currentuserCompEs + "_rooter.jpg')";
	
	if (currentuser == "gjdw") {
		$("#logoDiv").css({"float": "left","background-image":logoUrl, "background-repeat":"no-repeat", "background-position": "center left"});
		$("#compNameLabel").html("<font size='6px' color='#408080'>" + "光伏板清洗管理系统</font>");
	} else if(currentuser == "nfdw"|| currentuser == "zghn"|| currentuser == "zgdt"){
		$("#logoDiv").html("");
		$("#compNameLabel").html("");
	} else {
		$("#compNameLabel").html("");
		$("#logoDiv").html("<font size='5px' color='#ffffff'>光伏板清洗管理系统</font>");
	}
	
	$("#titleLabel").css({ "width":"1010px" ,"background-image":banerUrl, "background-size":"100% 100%"});
	$("#rooter").css({"width":"1010px","background-image":rooterUrl, "background-size":"100% 100%"});
	
	//$("#compNameLabel").html("<font size='6px'>" + currentuserCompName + "光伏板清洗管理系统</font>");
	
	$("#rooterCompname").html("<font size='2px'>" + currentuserCompName + "版权所有</font>");
	
	if (currentuser == "") {
		alert("未登陆，请从新登陆！");
		window.location.href="./index.html";
	}
//	if (currentuserCompEs == "nfdw"){
//		themeConstant = "darkblue";
//	}
	$("#hellolabel").text('您好，' + currentuserDispName);
//	$("#hellolabel").text('');
    //$('#splitContainer').jqxSplitter({ theme: themeConstant, height: 750, width: '100%', disabled: true, orientation: 'horizontal', panels: [{ size: 60 }, { size: 800 }] });
    $('#splitter').jqxSplitter({ splitBarSize: 5, theme: themeConstant, height: 750, width: 1010,  panels: [{ size: 550,collapsible: false }, { size: 550, collapsed : true}] });
    $("#tabswidget").jqxTabs({ theme: themeConstant,  height: '100%', width: '100%', animationType: 'fade' });
    
    var date2str = function (yy, mm, dd,n) {
        var s, d, t, t2;
        t = Date.UTC(yy, mm, dd);
        t2 = n * 1000 * 3600 * 24;
        t+= t2;
        d = new Date(t);
        s = d.getUTCFullYear() + "/";
        s += ("00"+(d.getUTCMonth()+1)).slice(-2) + "/";
        s += ("00"+d.getUTCDate()).slice(-2);
        return s;
    };
    
    var DateAddDay = function(str,n){   
    	  var   dd, mm, yy;   
    	  var   reg = /^(\d{4})\/(\d{1,2})\/(\d{1,2})$/;
    	  if (arr = str.match(reg)) {
    	    yy = Number(arr[1]);
    	    mm = Number(arr[2])-1;
    	    dd = Number(arr[3]);
    	  } else {
    	    var d = new Date();
    	    yy = d.getUTCFullYear();
    	    mm = ("00"+(d.getUTCMonth())).slice(-2);
    	    dd = ("00"+d.getUTCDate()).slice(-2);
    	  }
    	  return date2str(yy, mm, dd,n);
    };
    
    var setStartTime1 = function() {
    	var destStartDate = DateAddDay(startDate, -1);
    	var tmp = $("#jqxgrid").jqxGrid('getcellvalue', 1, 'item_2');
    	
    	$("#staticGrid").jqxGrid('setcellvalue', 1, 'item_2', tmp);
    	$("#staticGrid").jqxGrid('setcellvalue', 2, 'item_2', destStartDate);
    	$("#staticGrid").jqxGrid('setcellvalue', 3, 'item_2', endDate);
    };
    var setStartTime2 = function() {
    	var bastPeriodTmp = $("#staticGrid").jqxGrid('getcellvalue', 9, 'item_2');
    	if (bastPeriodTmp === undefined || bastPeriodTmp == null || bastPeriodTmp.length == 0) {
    		alert("请先在统计计算结果页面完成计算！");
    		return;
    	}
    	var tmp = $("#jqxgrid").jqxGrid('getcellvalue', 1, 'item_2');
    	$("#deduceGrid").jqxGrid('setcellvalue', 1, 'item_2', tmp);
    	
    	tmp = $("#staticGrid").jqxGrid('getcellvalue', 2, 'item_2');
    	$("#deduceGrid").jqxGrid('setcellvalue', 2, 'item_2', tmp);
    	
    	tmp = $("#staticGrid").jqxGrid('getcellvalue', 3, 'item_2');
    	$("#deduceGrid").jqxGrid('setcellvalue', 3, 'item_2', tmp);
    	
    	tmp = $("#staticGrid").jqxGrid('getcellvalue', 9, 'item_2');
    	$("#deduceGrid").jqxGrid('setcellvalue', 5, 'item_2', tmp);
    	
    	tmp = $("#staticGrid").jqxGrid('getcellvalue', 10, 'item_2');
    	$("#deduceGrid").jqxGrid('setcellvalue', 6, 'item_2', tmp);
    	
    	tmp = $("#staticGrid").jqxGrid('getcellvalue', 11, 'item_2');
    	$("#deduceGrid").jqxGrid('setcellvalue', 7, 'item_2', tmp);
    	
    	tmp = $("#staticGrid").jqxGrid('getcellvalue', 12, 'item_2');
    	$("#deduceGrid").jqxGrid('setcellvalue', 8, 'item_2', tmp);
    };
    $('#tabswidget').on('tabclick', function (event) { 
    	var clickedItem = event.args.item;
    	if (clickedItem == 1) {
    		setTimeout(setStartTime1, 1000); 
    	}
    	if (clickedItem == 2) {
    		setTimeout(setStartTime2, 1000); 
    	}
    }); 
    $("#leftPanel").jqxPanel({theme: themeConstant, width: '100%', height: '100%'});
    
//    $(window).resize(function(){ 
//    	alert($(window).width());
//    	alert($("splitContainer").outerWidth());
//        $("#splitContainer").css( 
//            "margin-left", ($(window).width() - $("splitContainer").outerWidth())/2
//        );        
//    }); 
    
    //$("#titleLabel").jqxDockPanel({ width: '100%', height: 89});
    //$("#titleLabel > div > div").css({ width: '250px' });
    //$('#first').attr('dock', 'left');
    //$('#second').attr('dock', 'left');
    //$('#third').attr('dock', 'left');
    ////$('#fourth').attr('dock', 'left');
    var startDate = '';
    var endDate = '';
    
    
    $('#titleLabel').jqxDockPanel('render');   
    // prepare the data
            var data = [{
            	item_1 : '1',
            	item_2 : '20',
            	item_3 : '-'}
            	];
            
            var selectionHtmlVar = '<select onchange="selectStation(this)">';
            
            var aj = $.ajax( {    
        	    url:'data/stations',// 跳转到 action    
        	    type:'get',    
        	    async : false,
        	    contentType : 'application/json',
        	    cache:false,    
        	    dataType:'json',    
        	    success:function(data) { 
        	    	var ss = data.data;
        	        for (var i = 0; i < ss.length; i++) {
        	        	stationSelectionData.push(ss[i].name);
        	        	selectionHtmlVar += '<option value ="'+ss[i].name+'">'+ ss[i].name +'</option>';
        	        }
        	     },    
        	     error : function() {    
        	          alert("获取电站信息失败！");
        	     }    
        	});  
            selectionHtmlVar += '</select">';
            if (stationSelectionData.length > 0) {
            	selectedStation = stationSelectionData[0];
            }
            
            url = "./data/getStationData?stationName="+encodeURIComponent(stationSelectionData[0]);
            source = 
            {
                //localdata: data,
                datatype: "json",
                url: url,
                updaterow: function (rowid, rowdata, commit) {
                    // synchronize with the server - send update command
                    // call commit with parameter true if the synchronization with the server is successful 
                    // and with parameter false if the synchronization failder.
                    commit(true);
                },
                datafields:
                [
                   { name: 'item_1', type: 'string'},
            	   { name: 'item_2', type: 'string'},
            	   { name: 'item_3', type: 'string'}
                ]
            };
            var dataAdapter = new $.jqx.dataAdapter(source, {
            	loadComplete: function (data) { 
                    var dataLine = $('#jqxgrid').jqxGrid('getrowdata', 10);
                    startDate = dataLine.item_1;
                    var rows = $('#jqxgrid').jqxGrid('getrows');
                    var lastLine = $('#jqxgrid').jqxGrid('getrowdata', rows.length - 1);
                    endDate = lastLine.item_1;
            	}
            });
            // initialize jqxGrid
            $("#jqxgrid").jqxGrid(
            {
            	theme: themeConstant,
                width: 1000,
                height: 710,
                source: dataAdapter,
                editable: true,
                enabletooltips: true,
                selectionmode: 'multiplecellsadvanced',
                columnsresize: true,
                columns: [
                  { text: '', editable : false, columntype: 'textbox', datafield: 'item_1', width: 360},
                  { text: '光伏原始数据', editable : false, columntype: 'textbox', datafield: 'item_2', width: 320 },
                  { text: '', editable : false, columntype: 'textbox', datafield: 'item_3', width: 320 }
                ],
                showtoolbar: true,
                toolbarheight : 40,
                rendertoolbar: function (statusbar) {
                    // appends buttons to the status bar.
                    var container = $("<div style='overflow: hidden; position: relative; margin: 2px;'></div>");
                    var stationSelection = $("<div id = 'stationSelection' style='float: left; margin-left: 10px;margin-top: 5px;'>"+selectionHtmlVar+"</div>");
                    var downldButton = $("<div style='float: left; margin-left: 5px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/arrowdown.gif'/><span style='margin-left: 4px; position: relative; top: -3px;'>下载记录</span></div>");

                    container.append(stationSelection);
                    container.append(downldButton);
                    statusbar.append(container);
                    
//                    stationSelection.jqxDropDownList({
//                    	autoOpen: true, 
//                    	source: stationSelectionData, 
//                    	selectedIndex: 1, 
//                    	placeHolder: "请选择电站:",
//                    	width: '200', 
//                    	height: '25'});
//                    if (stationSelectionData.length > 0) {
//                    	stationSelection.jqxDropDownList('selectItem', stationSelectionData[0] );
//                    }
                    
                    downldButton.jqxButton({  theme: themeConstant,width: 100, height: 20 });
                    
//                    stationSelection.on('select', function (event)
//                    		{
//                    		    var args = event.args;
//                    		    if (args) {
//                    		    // index represents the item's index.                
//                    		    var index = args.index;
//                    		    var item = args.item;
//                    		    // get item's label and value.
//                    		    var label = item.label;
//                    		    var value = item.value;
//                    		    // update table data
//                    		    source.url = "./data/getStationData?stationName="+encodeURIComponent(value);
//                    		    selectedStation = value;
//                    		    $('#jqxgrid').jqxGrid('updatebounddata');
//                    		}                        
//                    });
                    
                    downldButton.click(function (event) {
                    	var rows = $('#jqxgrid').jqxGrid('getrows');
                    	var jsonpara = JSON.stringify({"data":rows});
                    	window.location.href='./data/user/' + currentuser+ '/' + encodeURIComponent("电站原始数据") + '/' + encodeURIComponent(selectedStation) + '.xls';
                    	/*var aj = $.ajax( {    
                    	    url:'data/download', 
                    	    type:'post',    
                    	    async : false,
                    	    contentType : 'application/json',
                    	    cache:false,    
                    	    data: jsonpara,
                    	    dataType:'json',    
                    	    success:function(data) {
                    	        if(data.code == '1'){
                    	        	window.location.href='./data/user/' + currentuser+ '/' + item.value + '.xls';
                    	        }else{
                    	        	alert("下载异常！" + data.message);
                    	        }  
                    	     },    
                    	     error : function() {    
                    	          alert("下载失败！");
                    	     }    
                    	});  */
                    });
                    
                }
            });

             
            
            // events
//            $("#jqxgrid").on('cellbeginedit', function (event) {
                //var args = event.args;
                //$("#cellbegineditevent").text("Event Type: cellbeginedit, Column: " + args.datafield + ", Row: " + (1 + args.rowindex) + ", Value: " + args.value);
//            });

//            $("#jqxgrid").on('cellendedit', function (event) {
//            	// row's bound index.
//                var rowBoundIndex = event.args.rowindex;
//                var dataField = event.args.datafield;
//                var rowBoundIndex = event.args.rowindex;
//                var value = args.value;
//                $("#jqxgrid").jqxGrid('setcellvalue', rowBoundIndex, dataField, value);  
//                calcLineData(rowBoundIndex);
//            });
            
            var dataSta = [
            	];
            var dataStaUrl = 'data/user/stadata.json';
            var sourceSta = 
            {
                //localdata: dataSta,
                url: dataStaUrl,
                datatype: "json",
                updaterow: function (rowid, rowdata, commit) {
                    commit(true);
                },
                datafields:
                [
	                { name: 'item_1', type: 'string'},
					{ name: 'item_2', type: 'string'},
					{ name: 'item_3', type: 'string'}
                ]
            };

		    var selModeData = [
                    "优化计算",
                    "清洗推演"
		        ];
		        
            var dataAdapterSta = new $.jqx.dataAdapter(sourceSta, {
            	loadComplete: function (data) { 
            		
            	}
            });

            var beginedit = function(row, datafield, columntype) {  
                if ((row == 2) || (row == 3)) {  
                    return true;  
                } else {
                	return false;
                }
            };  
            
            // initialize jqxGrid
            $("#staticGrid").jqxGrid(
            {
            	theme: themeConstant,
            	width: 1000,
                height: 715,
                source: dataAdapterSta,
                enabletooltips: true,
                editable : true,
                selectionmode: 'multiplecellsadvanced',
                columnsresize: true,
                columns: [
                  { text: '指标名称', columntype: 'textbox', editable : false, datafield: 'item_1', width: 360},//columntype: 'datetimeinput',
                  { text: '推演结果', columntype: 'textbox', editable : true, datafield: 'item_2', width: 320, cellbeginedit: beginedit, cellsformat: 'yyyy/MM/dd',
                	  validation: function (cell, value) {
                          if (value == "")
                             return true;

                          var year = value.getFullYear();
//                          if (year >= 2016) {
//                              return { result: false, message: "选择的日期必须处于光伏原始数据时间段内" };
//                          }
                          return true;
                      },
                      cellsrenderer: function (row, columnfield, value, defaulthtml, columnproperties) {
                    	  if (row == 2 || row == 3) {
                          	return '<span style="margin: 2px; float: ' + columnproperties.cellsalign + '; color: #ff0000;">' + defaulthtml + '</span>';
                      	  }
                      }   
                  },
                  { text: '指标单位', columntype: 'textbox', editable : false, datafield: 'item_3', width: 320 }
                 
                ],
                
                showtoolbar: true,
                toolbarheight : 40,
                rendertoolbar: function (statusbar) {
                    // appends buttons to the status bar.
                    var container = $("<div style='overflow: hidden; position: relative; margin: 5px;'></div>");
                    var dropSelMode = $("<div id='dropSelMode' style='float: left; margin-left: 5px;margin-top: 0px; z-index: 999;'>优化计算</div>");
                    var bytimeinput = $("<div id='bytimeinput'  style='float: left; margin-left: 5px;margin-top: 2px;'><input type='text' id='bytimeinput'/></div>");
                    var dropSelMode2 = $("<div id='dropSelMode2'  style='float: left; margin-left: 5px;margin-top: 7px;'>按结果反推</div>");
                    var byresultinput = $("<div id='byresultinput'  style='float: left; margin-left: 5px;margin-top: 0px;'><input type='text' id='byresultinput'/></div>");
                    var dlButton = $("<div style='float: left; margin-left: 5px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/arrowdown.gif'/><span style='margin-left: 4px; position: relative; top: -3px;'>下载记录</span></div>");
                    var calcButton = $("<div style='float: left; margin-left: 25px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/search.png'/><span style='margin-left: 4px; position: relative; top: -3px;'>计算结果</span></div>");
                    //container.append(dropSelMode);
                    //container.append(bytimeinput);
//                    container.append(dropSelMode2);
//                    container.append(byresultinput);
                    container.append(calcButton);
                    container.append(dlButton);
                    statusbar.append(container);
                    dropSelMode.jqxDropDownList({autoOpen: true, source: selModeData, selectedIndex: 1, width: '200', height: '25'});
//                    dropSelMode.jqxRadioButton({ theme: themeConstant,width: 100, height: 25, checked: true});
//                    dropSelMode2.jqxRadioButton({theme: themeConstant, width: 100, height: 25, checked: false});
                    dlButton.jqxButton({ theme: themeConstant,width: 100, height: 20 });
                    calcButton.jqxButton({ theme: themeConstant,width: 100, height: 20 });
                    bytimeinput.jqxInput({theme: themeConstant,placeHolder: "请输入参考天数", height: 25, width: 100, minLength: 1 });
                    byresultinput.jqxInput({theme: themeConstant,placeHolder: "请输入参考结果", height: 25, width: 100, minLength: 1,disabled:true });
                    
//                    dropSelMode.on('change', function (event) {
//                        var checked = event.args.checked;
//                        if (checked) {
//                        	bytimeinput.jqxInput({disabled: false });
//                        	byresultinput.jqxInput({disabled: true });
//                        	calcByDay = true;
//                        } else {
//                        	
//                        }
//                    });
//                    dropSelMode2.on('change', function (event) {
//                        var checked = event.args.checked;
//                        if (checked) {
//                        	bytimeinput.jqxInput({disabled: true });
//                        	byresultinput.jqxInput({disabled: false });
//                        	calcByDay = false;
//                        }
//                        else {
//                        	
//                        }
//                    });
                    
                    // reload grid data.
                    dlButton.click(function (event) {
                    	var rows = $('#staticGrid').jqxGrid('getrows');
                    	var jsonpara = JSON.stringify({"data":rows});
                    	var aj = $.ajax( {    
                    	    url:'static/download',// 跳转到 action    
                    	    type:'post',    
                    	    contentType : 'application/json',
                    	    async : false,
                    	    cache:false,    
                    	    data: jsonpara,
                    	    dataType:'json',    
                    	    success:function(data) {    
                    	        if(data.code == '1'){    
                    	        	window.location.href='./data/user/' + currentuser+ '/' + data.result;
                    	        }else{    
                    	        	alert("下载异常！" + data.message);
                    	        }  
                    	     },    
                    	     error : function() {    
                    	          alert("下载失败！");
                    	     }    
                    	});  
                    });
                    
                    calcButton.click(function (event) {
                    	var rows = $('#staticGrid').jqxGrid('getboundrows');
                    	if(rows[2].item_2 == '' || rows[2].item_2 == '--' ) {
                    		alert("请选择起始日期");
                    		return;
                    	}
                    	if(rows[3].item_2 == '' || rows[3].item_2 == '--' ) {
                    		alert("请选择评价日期");
                    		return;
                    	}
                    	var jsonpara = JSON.stringify({"data":rows});
                    	var len = rows.length;
                    	var aj = $.ajax( {    
                    	    url:'static/calcReportData?stationName='+encodeURIComponent(selectedStation),  
                    	    contentType : 'application/json',
                    	    data: jsonpara,
                    	    type:'post',    
                    	    cache:false,    
                    	    dataType:'json',    
                    	    success:function(data) {
                    	    	if(data.code == '1'){  
                    	    		// update data manually
                    	    		for (var i = 1; i < 14; i++) {
                    	    			if (i == 2 || i == 3) {
                    	    				continue;
                    	    			}
                    	    			var dd = data.data[i].item_2;
                    	    			$("#staticGrid").jqxGrid('setcellvalue', i, 'item_2', dd);
                    	    		}
                    	    		
                    	        }else{    
                    	        	alert("计算异常！" + data.message);
                    	        }  
                    	    },    
                    	    error : function() {    
                    	         alert("计算异常！");    
                    	    }    
                    	});  
                    });
                }
            });
            
            
            // deduce calculate
            var dataDeduceUrl = 'data/user/deducedata.json';
            var sourceDeduce = 
            {
                //localdata: dataSta,
                url: dataDeduceUrl,
                datatype: "json",
                updaterow: function (rowid, rowdata, commit) {
                    commit(true);
                },
                datafields:
                [
	                { name: 'item_1', type: 'string'},
					{ name: 'item_2', type: 'string'},
					{ name: 'item_3', type: 'string'}
                ]
            };

            var dataAdapterDeduce = new $.jqx.dataAdapter(sourceDeduce);

            var begineditDeduce = function(row, datafield, columntype) {  
                if ((row == 2) || (row == 3)) {  
                    return true;  
                } else {
                	return false;
                }
            };  
            
            // initialize jqxGrid
            $("#deduceGrid").jqxGrid(
            {
            	theme: themeConstant,
            	width: 1000,
                height: 715,
                source: dataAdapterDeduce,
                enabletooltips: true,
                editable : true,
                selectionmode: 'multiplecellsadvanced',
                columnsresize: true,
                columns: [
                  { text: '指标名称', columntype: 'textbox', editable : false, datafield: 'item_1', width: 360},//columntype: 'datetimeinput', 
                  { text: '推演结果', columntype: 'textbox', editable : true, datafield: 'item_2', width: 320, cellbeginedit: begineditDeduce,cellsformat: 'yyyy/MM/dd',
                	  validation: function (cell, value) {
                          if (value == "")
                             return true;

                          var year = value.getFullYear();
//                          if (year >= 2016) {
//                              return { result: false, message: "选择的日期必须处于光伏原始数据时间段内" };
//                          }
                          return true;
                      },
                      cellsrenderer: function (row, columnfield, value, defaulthtml, columnproperties) {
                    	  if (row == 2 || row == 3) {
                          	return '<span style="margin: 2px; float: ' + columnproperties.cellsalign + '; color: #ff0000;">' + defaulthtml + '</span>';
                      	  }
                      }   
                  },
                  { text: '指标单位', columntype: 'textbox', editable : false, datafield: 'item_3', width: 320 }
                 
                ],
                
                showtoolbar: true,
                toolbarheight : 40,
                rendertoolbar: function (statusbar) {
                    // appends buttons to the status bar.
                    var container = $("<div style='overflow: hidden; position: relative; margin: 5px;'></div>");
                    var deduceInput = $("<div id='bytimeinput'  style='float: left; margin-left: 5px;margin-top: 2px;'><input type='text' id='deduceInput'/></div>");
                    var dlButton = $("<div style='float: left; margin-left: 5px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/arrowdown.gif'/><span style='margin-left: 4px; position: relative; top: -3px;'>下载记录</span></div>");
                    var calcButton = $("<div style='float: left; margin-left: 25px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/search.png'/><span style='margin-left: 4px; position: relative; top: -3px;'>计算结果</span></div>");
                    container.append(deduceInput);
                    container.append(calcButton);
                    container.append(dlButton);
                    statusbar.append(container);
                    deduceInput.jqxInput({theme: themeConstant,placeHolder: "请输入计划清洗周期", height: 25, width: 150, minLength: 1});
                    dlButton.jqxButton({ theme: themeConstant,width: 100, height: 20 });
                    calcButton.jqxButton({ theme: themeConstant,width: 100, height: 20 });
                    
                    // reload grid data.
                    dlButton.click(function (event) {
                    	var rows = $('#deduceGrid').jqxGrid('getrows');
                    	var jsonpara = JSON.stringify({"data":rows});
                    	var aj = $.ajax( {    
                    	    url:'static/download',// 跳转到 action    
                    	    type:'post',    
                    	    contentType : 'application/json',
                    	    async : false,
                    	    cache:false,    
                    	    data: jsonpara,
                    	    dataType:'json',    
                    	    success:function(data) {    
                    	        if(data.code == '1'){    
                    	        	window.location.href='./data/user/' + currentuser+ '/' + data.result;
                    	        }else{    
                    	        	alert("下载异常！" + data.message);
                    	        }  
                    	     },    
                    	     error : function() {    
                    	          alert("下载失败！");
                    	     }    
                    	});  
                    });
                    
                    calcButton.click(function (event) {
                    	var deduceInputVal = $("#deduceInput").val();
                		if (deduceInputVal == null || deduceInputVal == "" || deduceInputVal == "请输入假设清洗周期") {
                			alert("请输入假设清洗周期");
                			return;
                		}
                		$("#deduceGrid").jqxGrid('setcellvalue', 11, 'item_2', deduceInputVal);
                    	var rows = $('#deduceGrid').jqxGrid('getrows');
                    	if(rows[2].item_2 == '' || rows[2].item_2 == '--' ) {
                    		alert("请选择起始日期");
                    		return;
                    	}
                    	if(rows[3].item_2 == '' || rows[3].item_2 == '--' ) {
                    		alert("请选择评价日期");
                    		return;
                    	}
                    	var jsonpara = JSON.stringify({"data":rows});
                    	var len = rows.length;
                    	var aj = $.ajax( {    
                    	    url:'static/calcDeduceData?stationName='+encodeURIComponent(selectedStation),  
                    	    contentType : 'application/json',
                    	    data: jsonpara,
                    	    type:'post',    
                    	    cache:false,    
                    	    dataType:'json',    
                    	    success:function(data) {
                    	    	if(data.code == '1'){  
                    	    		// update data manually
                    	    		for (var i = 12; i < 15; i++) {
                    	    			var dd = data.data[i].item_2;
                    	    			$("#deduceGrid").jqxGrid('setcellvalue', i, 'item_2', dd);
                    	    		}
                    	    		var yearLowest = $("#deduceGrid").jqxGrid('getcellvalue', 8, 'item_2');
                    	    		var yearPlan = $("#deduceGrid").jqxGrid('getcellvalue', 14, 'item_2');
                    	    		var deducePeriodCost = yearPlan - yearLowest;
                    	    		$("#deduceGrid").jqxGrid('setcellvalue', 15, 'item_2', deducePeriodCost.toFixed(2).toString());
                    	        }else{    
                    	        	alert("计算异常！" + data.message);
                    	        }  
                    	    },    
                    	    error : function() {    
                    	         alert("计算异常！");    
                    	    }    
                    	});  
                    });
                }
            });
            
            $("#username").jqxInput({theme: themeConstant, value: currentuser, height: 25, width: 200, minLength: 1});
//            $("#displayname").jqxInput({theme: themeConstant, placeHolder: "显示名称", height: 25, width: 200, minLength: 1});
            $("#pass").jqxInput({theme: themeConstant, placeHolder: "请输入新密码", height: 25, width: 200, minLength: 1});
            $("#passAgain").jqxInput({theme: themeConstant, placeHolder: "请再次输入新密码", height: 25, width: 200, minLength: 1});
            
            $("#modUserInfoSubmit").jqxButton({theme: themeConstant,  width: '50'});
            $("#modUserInfoCancel").jqxButton({theme: themeConstant,  width: '50'});
             
            $('#window').jqxWindow({
            				theme: themeConstant,
            				isModal: true,
            				resizable : false,
                    showCollapseButton: true,  height: 260, width: 500,
                    initContent: function () {
                        
                    }
                });
            $('#window').jqxWindow('close');
            
            $("#modUserInfoSubmit").bind('click', function() {
            	// check pass
            	var p1 = $("#pass").val();
            	var p2 = $("#passAgain").val();
            	if (p1 == p2) {
            		var aj = $.ajax( {    
                	    url:'user/modpass?username=' + currentuser+ '&password=' + p1 + '&displayname=' + currentuserDispName,    
                	    type:'post',    
                	    async : false,
                	    cache:false,    
                	    success:function(data) {
                	        alert("修改成功！");
                	        $('#window').jqxWindow('close');
                	     },    
                	     error : function() {
                	    	 alert("修改失败！");
                	    	 $('#window').jqxWindow('close');
                	     }    
                	});  
            	} else {
            		alert("两次输入的密码不一致，请重新输入！");
            	}
            });
            
            $("#modUserInfoCancel").bind('click', function() {
            	$('#window').jqxWindow('close');
            });
            
            $("#logoutSure").jqxButton({theme: themeConstant,  width: '50'});
            $("#logoutCancel").jqxButton({theme: themeConstant,  width: '50'});
                
            $('#logoutConfirmWin').jqxWindow({
            				theme: themeConstant,
            				isModal: true,
            				resizable : false,
                    height: 150, width: 260,
                    initContent: function () {
                        
                    }
                });
            $('#logoutConfirmWin').jqxWindow('close');
            
            if (currentuser != "") {
	            $('#loadingDiv').html("");
	            $('#loadingDiv').hide();
            }
            $('#logoutCancel').bind('click', function(){
            	$('#logoutConfirmWin').jqxWindow('close');
            });
            
            $('#logoutSure').bind('click', function(){
            	var aj = $.ajax( {    
            	    url:'user/logout?username=' + currentuser,    
            	    type:'get',    
            	    async : false,
            	    cache:false,    
            	    dataType:'json',    
            	    success:function(data) {    
            	        window.location.href="./index.html";
            	     },    
            	     error : function() {
            	    	 window.location.href="./index.html";
            	     }    
            	});  
            });
//            $('#jqxFileUpload').jqxFileUpload({ 
//            	width: 300, 
//            	theme: themeConstant,
//            	uploadUrl: 'imageUpload.php',
//            	multipleFilesUpload: false,
//				localization: { 
//					browseButton: '浏览', 
//					uploadButton: '上传', 
//					cancelButton: '取消', 
//					uploadFileTooltip: '上传', 
//					cancelFileTooltip: '取消' 
//				},
//            	fileInputName: 'fileToUpload' 
//            });
            $('#jqxFileUploadWin').jqxWindow({
					theme: themeConstant,
					isModal: true,
					resizable : false,
					height: 170, width: 320,
					initContent: function () {
					}
            });
           
            $('#jqxFileUploadWin').jqxWindow('close');
            
            $('#uploadCancelBtn').bind('click', function(){
            	$('#jqxFileUploadWin').jqxWindow('close');
            });
            
            $('#uploadSureBtn').bind('click', function(){
            	var fuv = $("#uploadFileName").val();
            	if (fuv == null || fuv == "") {
            		alert("请选择文件");
            	} else {
            		$('#jqxFileUploadWin').jqxWindow('close');
            		$("#uploadForm").submit();
            	}
            });
        	
            
            
});

function modifyPass() {
//	$("#displayname").val(currentuserDispName);
	$("#pass").val('不需要修改密码');
	$("#passAgain").val('不需要修改密码');
	$('#window').jqxWindow('open');
}

function logoutWin() {
	$('#logoutConfirmWin').jqxWindow('open');
}

function calcAllLineServer() {
	var rows = $('#jqxgrid').jqxGrid('getrows');
	var jsonpara = JSON.stringify({"data":rows});
	var len = rows.length;
	var aj = $.ajax( {    
	    url:'data/calcAllLine',  
	    contentType : 'application/json',
	    data: jsonpara,
	    type:'post',    
	    cache:false,    
	    dataType:'json',    
	    success:function(data) {
	    	if(data.code == '1'){    
	    		$('#jqxgrid').jqxGrid('updatebounddata');
	        }else{    
	        	alert("计算异常！" + data.message);
	        }  
	    },    
	    error : function() {    
	         alert("计算异常！");    
	    }    
	});  
}

function calcStaticData() {
//	标杆板评价期首日电量底数  (人工输入)
//	标杆板评价日电量底数
//	倍率  (人工输入)
//	评价发电天数
//	评价板评价期首日电量底数  (人工输入)
//	评价板评价日电量底数
//	折算系数  (人工输入)
//
//	单次清洗费用  (人工输入)
//	费用核算周期  (人工输入)
//	结算单价（不含税）  (人工输入)
//	每日平均发电量
//	不清洗光伏板日降系数
//	清洗周期
//
//	清洗次数
//	清洗周期损失电量
//	评价周期损失电量
//	评价周期损失费用
//	清洗费用
//	费用合计
//
//	清洗周期
//	全年每日清洗总费用
//	每天电量损失费用
	if (calcByDay) {
		// calc by day
		var bytimeinputVal = $("#bytimeinput").val();
		if (bytimeinputVal == null || bytimeinputVal == "" || bytimeinputVal == "请输入参考天数") {
			alert("请输入参考天数！");
			return;
		}
		var dataLine = $('#jqxgrid').jqxGrid('getrowdata', bytimeinputVal - 1);
		if (dataLine === undefined || dataLine == null) {
			alert("请先录入数据！");
			return;
		}
		// calc static data
		$("#staticGrid").jqxGrid('setcellvalue', 1, 'data', dataLine.sumary);
		$("#staticGrid").jqxGrid('setcellvalue', 3, 'data', dataLine.date);
		$("#staticGrid").jqxGrid('setcellvalue', 5, 'data', dataLine.sumary_elec_amout);
		
		$("#staticGrid").jqxGrid('setcellvalue', 11, 'data', (Number(getStaticLineData(1)) -  Number(getStaticLineData(0))) * Number(getStaticLineData(2)) /Number(getStaticLineData(3))*5/6 );
		$("#staticGrid").jqxGrid('setcellvalue', 12, 'data', (Number(getStaticLineData(1)) -  Number(getStaticLineData(0)) - Number(getStaticLineData(5)) -  Number(getStaticLineData(4))) * 2 / (Number(getStaticLineData(3)) + 1) / Number(getStaticLineData(1)));
		
		$("#staticGrid").jqxGrid('setcellvalue', 23, 'data', Number(getStaticLineData(8))*Number(getStaticLineData(9)));
		$("#staticGrid").jqxGrid('setcellvalue', 24, 'data', Number(getStaticLineData(9))*Number(getStaticLineData(10))*Number(getStaticLineData(11))*Number(getStaticLineData(12))/2);
		
		$("#staticGrid").jqxGrid('setcellvalue', 13, 'data', getStaticLineData(23));
		
		var unitspe = Math.sqrt(Number(getStaticLineData(23))/Number(getStaticLineData(24)));
		$("#staticGrid").jqxGrid('setcellvalue', 22, 'unit', unitspe);
		$("#staticGrid").jqxGrid('setcellvalue', 22, 'data', Math.round(unitspe));
		
		$("#staticGrid").jqxGrid('setcellvalue', 15, 'data', Number(getStaticLineData(9))/Number(getStaticLineData(13)));
		$("#staticGrid").jqxGrid('setcellvalue', 16, 'data', Number(getStaticLineData(13))*Number(getStaticLineData(12))*(Number(getStaticLineData(13))+1)/2*Number(getStaticLineData(11)));
		$("#staticGrid").jqxGrid('setcellvalue', 17, 'data', Number(getStaticLineData(16))*Number(getStaticLineData(15)));
		$("#staticGrid").jqxGrid('setcellvalue', 18, 'data', Number(getStaticLineData(17))*Number(getStaticLineData(10)));
		$("#staticGrid").jqxGrid('setcellvalue', 19, 'data', Number(getStaticLineData(8))*Number(getStaticLineData(9))/Number(getStaticLineData(13)));
		$("#staticGrid").jqxGrid('setcellvalue', 20, 'data', Number(getStaticLineData(18))+Number(getStaticLineData(19)));
	} else {
		// reverse calculate. need more specifications
		
	}
}

function selectStation(obj) {
	source.url = "./data/getStationData?stationName="+encodeURIComponent(obj.value);
    selectedStation = obj.value;
    $('#jqxgrid').jqxGrid('updatebounddata');
}

function getStaticLineData(lineInd) {
	var dataLine = $('#staticGrid').jqxGrid('getrowdata', lineInd);
	return dataLine.data;
}