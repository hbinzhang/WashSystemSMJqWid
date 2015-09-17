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
            var url = "./data/json/calcData.json";
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
                        var datarow = generatedata(1);
                        $("#jqxgrid").jqxGrid('addrow', null, datarow[0]);
                    });
                    // delete selected row.
                    deleteButton.click(function (event) {
                        var selectedrowindex = $("#jqxgrid").jqxGrid('getselectedrowindex');
                        var rowscount = $("#jqxgrid").jqxGrid('getdatainformation').rowscount;
                        var id = $("#jqxgrid").jqxGrid('getrowid', selectedrowindex);
                        $("#jqxgrid").jqxGrid('deleterow', id);
                    });
                    // reload grid data.
                    uploadButton.click(function (event) {
                    	//console.log("start to upload");
                    	$('#jqxFileUploadWin').jqxWindow('open');
                    });
                    downldButton.click(function (event) {
                        //console.log("start to download");
                    	var aj = $.ajax( {    
                    	    url:'data/get?userid=zhb',// 跳转到 action    
                    	    type:'get',    
                    	    async : false,
                    	    cache:false,    
                    	    dataType:'json',    
                    	    success:function(data) {    
                    	        if(data.msg =="true" ){    
                    	            alert("修改成功！");    
                    	            window.location.href='./data/json/calcData.json'; 
                    	        }else{    
                    	            view(data.msg);    
                    	        }    
                    	     },    
                    	     error : function() {    
                    	          window.location.href='./data/excel/data.xls'; 
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
                    	        }else{    
                    	        	alert("保存异常！");
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
                var data = $('#jqxgrid').jqxGrid('getrowdata', rowBoundIndex);
                $("#jqxgrid").jqxGrid('setcellvalue', 0, 'cal_rate_sumary', 'My Value');  
                
            });
            
            var dataSta = [{
            	ratio:'',
            	data:'',
            	unit:'',
            	source:'',
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
            	source:'',
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
            	source:'',
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
            	source:'',
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
                  { text: '系数', columntype: 'textbox', datafield: 'ratio', width: 150 },
                  { text: '数值', columntype: 'textbox', datafield: 'data', width: 150 },
                  { text: '单位', columntype: 'textbox', datafield: 'unit', width: 170 },
                  { text: '参数来源', columntype: 'textbox', datafield: 'source', width: 350 },
                  { text: '备注', columntype: 'textbox', datafield: 'comment'}
                ],
                
                showtoolbar: true,
                toolbarheight : 40,
                rendertoolbar: function (statusbar) {
                    // appends buttons to the status bar.
                    var container = $("<div style='overflow: hidden; position: relative; margin: 5px;'></div>");
                    var dropSelMode = $("<div id = 'dropSelMode' style='float: left; margin-left: 5px;margin-top: 2px;'></div>");
                    var dlButton = $("<div style='float: left; margin-left: 5px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/arrowdown.gif'/><span style='margin-left: 4px; position: relative; top: -3px;'>下载记录</span></div>");
                    var saveButton = $("<div style='float: left; margin-left: 5px;margin-bottom: 5px;'><img style='position: relative; margin-top: 2px;' src='./images/search.png'/><span style='margin-left: 4px; position: relative; top: -3px;'>保存记录</span></div>");
                    container.append(dropSelMode);
                    container.append(dlButton);
                    container.append(saveButton);
                    statusbar.append(container);
                    dropSelMode.jqxDropDownList({autoOpen: true, source: selModeData, selectedIndex: 1, width: '200', height: '25'});
                    dlButton.jqxButton({ theme: themeConstant,width: 100, height: 20 });
                    saveButton.jqxButton({ theme: themeConstant,width: 100, height: 20 });
                    // reload grid data.
                    dlButton.click(function (event) {
                    });
                    
                    saveButton.click(function (event) {
                    });
                },
            });
            
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