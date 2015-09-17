var currentuser = "";

$(document).ready(function () {
	var aj = $.ajax( {    
	    url:'user/getCurrentUser',    
	    type:'get',    
	    async : false,
	    cache:false,    
	    dataType:'json',    
	    success:function(data) {    
	        if(data.code == "1" ){ 
	        	currentuser = data.result;
	        }else{    
	        }    
	     },    
	     error : function() {
	     }    
	});  
	if (currentuser == "") {
		alert("未登陆，请从新登陆！");
		window.location.href="./index.html";
	}
    //$('#splitContainer').jqxSplitter({ theme: themeConstant, height: 750, width: '100%', disabled: true, orientation: 'horizontal', panels: [{ size: 60 }, { size: 800 }] });
    $('#splitter').jqxSplitter({ splitBarSize: 3, theme: themeConstant, height: 750, width: '100%',  panels: [{ size: 950,collapsible: false }, { size: 200, collapsed : true}] });
    $("#tabswidget").jqxTabs({ theme: themeConstant,  height: '100%', width: '100%' });
    
    $("#leftPanel").jqxPanel({theme: themeConstant, width: '100%', height: '100%'});
    
    //$("#titleLabel").jqxDockPanel({ width: '100%', height: 89});
    //$("#titleLabel > div > div").css({ width: '250px' });
    //$('#first').attr('dock', 'left');
    //$('#second').attr('dock', 'left');
    //$('#third').attr('dock', 'left');
    ////$('#fourth').attr('dock', 'left');
    $('#titleLabel').jqxDockPanel('render');   
    // prepare the data
            var data = [{
            	date : 1,
            	wash_elec_amout : '20',
            	sumary:'-',
            	no_wash_elec_amout:'-',
            	no_wash_elec_sumary:'-',
            	cal_rate_index_sumary:'-',
            	cal_rate_index:'0.9',
            	cal_rate_sumary:'-',
            	cal_rate:'-',
            	no_wash_elec_amout_2:'-',
            	sumary_elec_amout:'-',
            	sumary_cal_rate:'-',
            	reduce_ratio:'-',
            	lose_sumary:'-'}
            	];
            var url = "./data/user/" + currentuser + "/calcData.json";
            var source = 
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
                   { name: 'date', type: 'int'},
				           { name: 'wash_elec_amout', type: 'string'},
				           { name: 'sumary', type: 'string'},
				           { name: 'no_wash_elec_amout', type: 'string'},
				           { name:  'no_wash_elec_sumary', type: 'string'},
				           { name:  'cal_rate_index_sumary', type: 'string'},
				           { name:  'cal_rate_index', type: 'string'},
				           { name:  'cal_rate_sumary', type: 'string'},
				           { name:  'cal_rate', type: 'string'},
				           { name:  'no_wash_elec_amout_2', type: 'string'},
				           { name:  'sumary_elec_amout', type: 'string'},
				           { name:  'sumary_cal_rate', type: 'string'},
				           { name:  'reduce_ratio', type: 'string'},
				           { name:  'lose_sumary', type: 'string'}
                ]
            };

            var dataAdapter = new $.jqx.dataAdapter(source);

            // initialize jqxGrid
            $("#jqxgrid").jqxGrid(
            {
            	theme: themeConstant,
                width: '99%',
                height: 715,
                source: dataAdapter,
                editable: true,
                enabletooltips: true,
                selectionmode: 'multiplecellsadvanced',
                columnsresize: true,
                columns: [
                  { text: '日期', columntype: 'textbox', datafield: 'date', width: 120,columntype: 'numberinput' },
                  { text: '清洗电量', columntype: 'textbox', datafield: 'wash_elec_amout', width: 120 },
                  { text: '累计', editable : false, columntype: 'textbox', datafield: 'sumary', width: 120 },
                  { text: '不清洗电量', columngroup: 'indexModeGroup', editable : false,columntype: 'textbox', datafield: 'no_wash_elec_amout', width: 120 },
                  { text: '不清洗累计', columngroup: 'indexModeGroup', editable : false,columntype: 'textbox', datafield: 'no_wash_elec_sumary', width: 120 },
                  { text: '折算率指数（累计）', columngroup: 'indexModeGroup', editable : false,columntype: 'textbox', datafield: 'cal_rate_index_sumary', width: 150 },
                  { text: '折算率指数', columngroup: 'indexModeGroup', columntype: 'textbox', datafield: 'cal_rate_index', width: 120 },
                  { text: '累计折算率', columngroup: 'indexModeGroup', editable : false,columntype: 'textbox', datafield: 'cal_rate_sumary', width: 120 },
                  { text: '折算率', columngroup: 'reduceModeGroup', editable : false,columntype: 'textbox', datafield: 'cal_rate', width: 120 },
                  { text: '不清洗电量', columngroup: 'reduceModeGroup', editable : false,columntype: 'textbox', datafield: 'no_wash_elec_amout_2', width: 120 },
                  { text: '累计电量', columngroup: 'reduceModeGroup', editable : false,columntype: 'textbox', datafield: 'sumary_elec_amout', width: 120 },
                  { text: '累计折算率', columngroup: 'reduceModeGroup', editable : false,columntype: 'textbox', datafield: 'sumary_cal_rate', width: 120 },
                  { text: '反推日降系数', columngroup: 'reduceModeGroup', editable : false,columntype: 'textbox', datafield: 'reduce_ratio', width: 120 },
                  { text: '损失累计', columngroup: 'reduceModeGroup', editable : false,columntype: 'textbox', datafield: 'lose_sumary', width: 120 },
                ],
                columngroups: 
                [
                	{ text: '指数模式', align: 'center', name: 'indexModeGroup' },
                  { text: '递减模式', align: 'center', name: 'reduceModeGroup' }
                ],
                showtoolbar: true,
                toolbarheight : 40,
                rendertoolbar: function (statusbar) {
                    // appends buttons to the status bar.
                    var container = $("<div style='overflow: hidden; position: relative; margin: 5px;'></div>");
                    var addButton = $("<div style='float: left; margin-left: 5px; margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/add.png'/><span style='margin-left: 4px; position: relative; top: -3px;'>添加记录</span></div>");
                    var deleteButton = $("<div style='float: left; margin-left: 5px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/close.png'/><span style='margin-left: 4px; position: relative; top: -3px;'>删除记录</span></div>");
                    var uploadButton = $("<div style='float: left; margin-left: 5px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/arrowup.gif'/><span style='margin-left: 4px; position: relative; top: -3px;'>上传记录</span></div>");
                    var downldButton = $("<div style='float: left; margin-left: 5px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/arrowdown.gif'/><span style='margin-left: 4px; position: relative; top: -3px;'>下载记录</span></div>");
                    var saveButton = $("<div style='float: left; margin-left: 5px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/search.png'/><span style='margin-left: 4px; position: relative; top: -3px;'>保存记录</span></div>");
                    container.append(addButton);
                    container.append(deleteButton);
                    container.append(uploadButton);
                    container.append(downldButton);
                    container.append(saveButton);
                    statusbar.append(container);
                    addButton.jqxButton({  theme: themeConstant,width: 100, height: 20 });
                    deleteButton.jqxButton({  theme: themeConstant,width: 100, height: 20 });
                    uploadButton.jqxButton({  theme: themeConstant,width: 100, height: 20 });
                    downldButton.jqxButton({  theme: themeConstant,width: 100, height: 20 });
                    saveButton.jqxButton({  theme: themeConstant,width: 100, height: 20 });
                    // add new row.
                    addButton.click(function (event) {
                    	var rows = $('#jqxgrid').jqxGrid('getrows');
                    	var rowLen = rows.length;
                        var datarow = {
                            	date : rowLen + 1,
                            	wash_elec_amout : '',
                            	sumary:'-',
                            	no_wash_elec_amout:'-',
                            	no_wash_elec_sumary:'-',
                            	cal_rate_index_sumary:'-',
                            	cal_rate_index:'',
                            	cal_rate_sumary:'-',
                            	cal_rate:'-',
                            	no_wash_elec_amout_2:'-',
                            	sumary_elec_amout:'-',
                            	sumary_cal_rate:'-',
                            	reduce_ratio:'-',
                            	lose_sumary:'-'};
                        $("#jqxgrid").jqxGrid('addrow', null, datarow);
                    });
                    // delete selected row.
                    deleteButton.click(function (event) {
                        var rowscount = $("#jqxgrid").jqxGrid('getdatainformation').rowscount;
                        if (rowscount == 0) {
                        	return;
                        }
                        var rows = $("#jqxgrid").jqxGrid('getboundrows');
                        $("#jqxgrid").jqxGrid('deleterow', rows[rowscount-1].uid);
                    });
                    // reload grid data.
                    uploadButton.click(function (event) {
                    	//console.log("start to upload");
                    	$('#jqxFileUploadWin').jqxWindow('open');
                    });
                    downldButton.click(function (event) {
                        //console.log("start to download");
                    	var rows = $('#jqxgrid').jqxGrid('getrows');
                    	var jsonpara = JSON.stringify({"data":rows});
                    	var aj = $.ajax( {    
                    	    url:'data/download',// 跳转到 action    
                    	    type:'post',    
                    	    async : false,
                    	    contentType : 'application/json',
                    	    cache:false,    
                    	    data: jsonpara,
                    	    dataType:'json',    
                    	    success:function(data) {    
                    	        if(data.code == '1'){    
                    	        	window.location.href='./data/user/' + currentuser+ '/calcData.xls';
                    	        }else{    
                    	        	alert("下载异常！" + data.message);
                    	        }  
                    	     },    
                    	     error : function() {    
                    	          alert("下载失败！");
                    	     }    
                    	});  
                    });
                    saveButton.click(function (event) {
                    	//console.log("start to save");
                    	var rows = $('#jqxgrid').jqxGrid('getrows');
                    	var jsonpara = JSON.stringify({"data":rows});
                    	var aj = $.ajax( {    
                    	    url:'data/save',// 跳转到 action    
                    	    contentType : 'application/json',
                    	    data: jsonpara,
                    	    type:'post',    
                    	    cache:false,    
                    	    dataType:'json',    
                    	    success:function(data) {    
                    	        if(data.code == '1'){    
                    	        	alert("保存成功！");
                    	            window.location.reload(); 
                    	        } else {    
                    	        	alert("保存异常！" + data.message);
                    	        }    
                    	     },    
                    	     error : function() {    
                    	          alert("保存异常！");    
                    	     }    
                    	});  
                    });
                },
            });

            // events
            $("#jqxgrid").on('cellbeginedit', function (event) {
                //var args = event.args;
                //$("#cellbegineditevent").text("Event Type: cellbeginedit, Column: " + args.datafield + ", Row: " + (1 + args.rowindex) + ", Value: " + args.value);
            });

            $("#jqxgrid").on('cellendedit', function (event) {
            	//console.log("after edit: " + "Event Type: cellendedit, Column: " + event.args.datafield + ", Row: " + (1 + event.args.rowindex) + ", Value: " + event.args.value);
            	//console.log("row data is: " + event.args);
            	// row's bound index.
                var rowBoundIndex = event.args.rowindex;
                //console.log("rowBoundIndex is: " + rowBoundIndex);
                var dataField = event.args.datafield;
                var rowBoundIndex = event.args.rowindex;
                var value = args.value;
                $("#jqxgrid").jqxGrid('setcellvalue', rowBoundIndex, dataField, value);  
                calcLineData(rowBoundIndex);
            });
            
            var dataSta = [{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'人工输入',
            	comment:'标杆板评价期首日电量底数'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'',
            	comment:'标杆板评价日电量底数'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'人工输入',
            	comment:'倍率'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'',
            	comment:'评价发电天数'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'人工输入',
            	comment:'评价板评价期首日电量底数'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'',
            	comment:'评价板评价日电量底数'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'人工输入',
            	comment:'折算系数'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'',
            	comment:''
            	},{
            	ratio:'',
            	data:'',
            	unit:'万元',
            	source:'人工输入',
            	comment:'单次清洗费用'
            	},{
            	ratio:'',
            	data:'',
            	unit:'万元',
            	source:'人工输入',
            	comment:'费用核算周期'
            	},{
            	ratio:'',
            	data:'',
            	unit:'万元/万Kwh',
            	source:'人工输入',
            	comment:'结算单价（不含税）'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'系统自己算',
            	comment:'每日平均发电量'
            	},{
            	ratio:'',
            	data:'',
            	unit:'天',
            	source:'系统自己算',
            	comment:'不清洗光伏板日降系数'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'系统自己算',
            	comment:'清洗周期'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'',
            	comment:''
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'系统自己算',
            	comment:'清洗次数'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'系统自己算',
            	comment:'清洗周期损失电量'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'系统自己算',
            	comment:'评价周期损失电量'
            	},{
            	ratio:'',
            	data:'',
            	unit:'万元',
            	source:'系统自己算',
            	comment:'评价周期损失费用'
            	},{
            	ratio:'',
            	data:'',
            	unit:'万元',
            	source:'系统自己算',
            	comment:'清洗费用'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'',
            	comment:'费用合计'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'',
            	comment:''
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'',
            	comment:'清洗周期'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'',
            	comment:'全年每日清洗总费用'
            	},{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'',
            	comment:'每天电量损失费用'
            	}
            	];

            var sourceSta = 
            {
                localdata: dataSta,
                datatype: "array",
                updaterow: function (rowid, rowdata, commit) {
                    // synchronize with the server - send update command
                    // call commit with parameter true if the synchronization with the server is successful 
                    // and with parameter false if the synchronization failder.
                    commit(true);
                },
                datafields:
                [
                   { name: 'ratio', type: 'string'},
					         { name: 'data', type: 'string'},
					         { name: 'unit', type: 'string'},
					         { name: 'source', type: 'string'},
					         { name: 'comment', type: 'string'}
                ]
            };

						var selModeData = [
                    "由天数计算",
                    "由效益计算"
		        ];
		        
            var dataAdapterSta = new $.jqx.dataAdapter(sourceSta);

            // initialize jqxGrid
            $("#staticGrid").jqxGrid(
            {
            	theme: themeConstant,
                width: '99%',
                height: 715,
                source: dataAdapterSta,
                editable: true,
                enabletooltips: true,
                selectionmode: 'multiplecellsadvanced',
                columnsresize: true,
                columns: [
                  { text: '名称', columntype: 'textbox', editable : false,datafield: 'comment'},
                  { text: '系数', columntype: 'textbox', editable : false,datafield: 'ratio', width: 150 },
                  { text: '数值', columntype: 'textbox', datafield: 'data', width: 150 },
                  { text: '单位', columntype: 'textbox', editable : false,datafield: 'unit', width: 170 },
                  { text: '参数来源', columntype: 'textbox', editable : false,datafield: 'source', width: 350 }
                 
                ],
                
                showtoolbar: true,
                toolbarheight : 40,
                rendertoolbar: function (statusbar) {
                    // appends buttons to the status bar.
                    var container = $("<div style='overflow: hidden; position: relative; margin: 5px;'></div>");
                    var dropSelMode = $("<div style='float: left; margin-left: 5px;margin-top: 7px;'>按时间计算</div>");
                    var bytimeinput = $("<div style='float: left; margin-left: 5px;margin-top: 0px;'><input type='text' id='bytimeinput'/></div>");
                    var dropSelMode2 = $("<div style='float: left; margin-left: 5px;margin-top: 7px;'>按结果反推</div>");
                    var byresultinput = $("<div style='float: left; margin-left: 5px;margin-top: 0px;'><input type='text' id='byresultinput'/></div>");
                    var dlButton = $("<div style='float: left; margin-left: 5px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/arrowdown.gif'/><span style='margin-left: 4px; position: relative; top: -3px;'>下载记录</span></div>");
                    var saveButton = $("<div style='float: left; margin-left: 25px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/search.png'/><span style='margin-left: 4px; position: relative; top: -3px;'>计算结果</span></div>");
                    container.append(dropSelMode);
                    container.append(bytimeinput);
                    container.append(dropSelMode2);
                    container.append(byresultinput);
                    container.append(saveButton);
                    container.append(dlButton);
                    statusbar.append(container);
                    //dropSelMode.jqxDropDownList({autoOpen: true, source: selModeData, selectedIndex: 1, width: '200', height: '25'});
                    dropSelMode.jqxRadioButton({ theme: themeConstant,width: 100, height: 25, checked: true});
                    dropSelMode2.jqxRadioButton({theme: themeConstant, width: 100, height: 25, checked: false});
                    dlButton.jqxButton({ theme: themeConstant,width: 100, height: 20 });
                    saveButton.jqxButton({ theme: themeConstant,width: 100, height: 20 });
                    bytimeinput.jqxInput({theme: themeConstant,placeHolder: "请输入参考天数", height: 25, width: 100, minLength: 1 });
                    byresultinput.jqxInput({theme: themeConstant,placeHolder: "请输入参考结果", height: 25, width: 100, minLength: 1,disabled:true });
                    
                    dropSelMode.on('change', function (event) {
                        var checked = event.args.checked;
                        if (checked) {
                        	bytimeinput.jqxInput({disabled: false });
                        	byresultinput.jqxInput({disabled: true });
                        } else {
                        	
                        }
                    });
                    dropSelMode2.on('change', function (event) {
                        var checked = event.args.checked;
                        if (checked) {
                        	bytimeinput.jqxInput({disabled: true });
                        	byresultinput.jqxInput({disabled: false });
                        }
                        else {
                        	
                        }
                    });
                    
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
                    	        	window.location.href='./data/user/' + currentuser+ '/staticResult.xls';
                    	        }else{    
                    	        	alert("下载异常！" + data.message);
                    	        }  
                    	     },    
                    	     error : function() {    
                    	          alert("下载失败！");
                    	     }    
                    	});  
                    });
                    
                    saveButton.click(function (event) {
                    	// calc data from records
                    });
                },
            });
            
//            $("#staticGrid").on('cellendedit', function (event) {
//            	//console.log("after edit: " + "Event Type: cellendedit, Column: " + event.args.datafield + ", Row: " + (1 + event.args.rowindex) + ", Value: " + event.args.value);
//            	//console.log("row data is: " + event.args);
//            	// row's bound index.
//                var rowBoundIndex = event.args.rowindex;
//                var dataField = event.args.datafield;
//                var rowBoundIndex = event.args.rowindex;
//                var valueSource = $("#staticGrid").jqxGrid('getcellvalue', rowBoundIndex, 'source');
//                if (valueSource === '人工输入') {
//                	return;
//                } else {
//	                var value = args.value;
//	                var oldvalue = args.oldvalue;
//	                args.value = oldvalue;
//	                alert('该字段不允许人工输入！' + oldvalue);
//	                $("#staticGrid").jqxGrid('setcellvalue', rowBoundIndex, dataField, oldvalue);
//                }
//            });
            
            $("#username").jqxInput({theme: themeConstant, placeHolder: "admin", height: 25, width: 200, minLength: 1});
            $("#displayname").jqxInput({theme: themeConstant, placeHolder: "显示名称", height: 25, width: 200, minLength: 1});
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
            
            $('#loadingDiv').html("");
            $('#loadingDiv').hide();
            
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
            
            
});

function modifyPass() {
	$('#window').jqxWindow('open');
}

function logoutWin() {
	$('#logoutConfirmWin').jqxWindow('open');
}

function calcLineData(linenum) {
	var data = $('#jqxgrid').jqxGrid('getrowdata', linenum);
//    $("#jqxgrid").jqxGrid('setcellvalue', 0, 'cal_rate_sumary', 'My Value');  
    var lastRec;
    if (linenum == 0) {
    	lastRec = null;
    } else {
    	lastRec = $('#jqxgrid').jqxGrid('getrowdata', linenum - 1);
    }
   
	// sumary
	if (lastRec != null) {
		$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'sumary', (Number(data['wash_elec_amout']) + Number(lastRec['sumary'])));  
	} else {
		$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'sumary', (Number(data['wash_elec_amout'])));  
	}
	
	// cal_rate_index_sumary
	$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'cal_rate_index_sumary', Math.pow(Number(data['cal_rate_index']), Number(data['date'])));  
	
	// no_wash_elec_amout
	$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'no_wash_elec_amout', Number(data['wash_elec_amout']) * Number(data['cal_rate_index_sumary']));  
	
	// no_wash_elec_sumary
	if (lastRec != null) {
	 	$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'no_wash_elec_sumary', Number(data['no_wash_elec_amout']) + Number(lastRec['no_wash_elec_sumary'])); 
	} else {
		$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'no_wash_elec_sumary', Number(data['no_wash_elec_amout']));
	}
	
	// cal_rate_sumary
	$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'cal_rate_sumary', Number(data['no_wash_elec_sumary']) / Number(data['sumary']));
	
	// cal_rate
	$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'cal_rate', 1 - Number(data['date']) * 0.0004);
	
	// no_wash_elec_amout_2
	$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'no_wash_elec_amout_2', Number(data['cal_rate']) * Number(data['wash_elec_amout']));
	
	// sumary_elec_amout
	if (lastRec != null) {
	 	$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'sumary_elec_amout', Number(data['no_wash_elec_amout_2']) + Number(lastRec['sumary_elec_amout']));
	} else {
		$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'sumary_elec_amout', Number(data['no_wash_elec_amout_2']));
	}
	
	// sumary_cal_rate
	$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'sumary_cal_rate', Number(data['sumary_elec_amout']) / Number(data['sumary']));
	
	// reduce_ratio
	$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'reduce_ratio', (Number(data['sumary']) - Number(data['sumary_elec_amout'])) * 2 / Number(data['sumary']) / (Number(data['date']) + 1));
	
	// lose_sumary
	$("#jqxgrid").jqxGrid('setcellvalue', linenum, 'lose_sumary', (Number(data['sumary']) - Number(data['no_wash_elec_sumary'])) * 50000 / 6 / 10000);
}