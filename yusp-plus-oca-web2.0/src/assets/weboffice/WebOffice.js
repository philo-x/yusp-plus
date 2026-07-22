
function LoadCtrl(obj)
{
var webofficeload;
if (!!window.ActiveXObject || "ActiveXObject" in window){
webofficeload = '<object classid="clsid:FF09E4FA-BFAA-486E-ACB4-86EB0AE875D5" codebase="http://www.officectrl.com/weboffice/WebOffice.ocx#Version=2018,1,6,2" id="WebOffice" width="100%" height="900" ><param name="BorderStyle" value="1"><param name="Menubar" value="1"><param name="Titlebar" value="1"><param name="Toolbars" value="1"></object>'; 
}
else
{
webofficeload = '<object id="WebOffice" CLSID="{FF09E4FA-BFAA-486E-ACB4-86EB0AE875D5}" TYPE="application/x-itst-activex"  width=100% height=900></object>';
}
	obj.innerHTML=webofficeload;
}

function SetMenu()
{
	 //控件菜单的JS代码可以加在Open方法的前面，即在控件打开文档时修改好菜单。
	 //集成项目菜单控制代码可以写在这里......
	
	WebOffice.WebClearMenuItem(0);//清除控件默认文件菜单里的所有子项
	WebOffice.SetMenuName(0,'新建文件');//修改文件菜单名称为：新建文件
	WebOffice.CustomMenuCount=2;//设置新增的菜单个数
	WebOffice.SetMenuName(1,'打开文件'); 
	WebOffice.SetMenuName(2,'保存文件'); 
 
} 

function ShowPage()
{ 
 
var varpath = decodeURI(window.location.href);
var root = varpath;
var path="edit";
var strUrl;
	var pre = "WebOffice://|Officectrl|";//智能窗打开的固定参数

	var v=getBrowser();	
	
	if(v==1){//当浏览器返回值为1时定义为使用智能窗方式打开网址
		 strUrl = pre + root + path;			 
		 window.open(strUrl,'_self');
	}
	else
	{ //当浏览器返回值1以外的数据时采用传统方式打开网址
		strUrl = root + path;		 
		window.open(strUrl,'_blank');
	}	 
}
function getBrowser(){	
    var Sys = {};
    var ua = navigator.userAgent.toLowerCase();
    var s;
	var ver;
    (s = ua.match(/edge\/([\d.]+)/)) ? Sys.edge = s[1] :
    (s = ua.match(/rv:([\d.]+)\) like gecko/)) ? Sys.ie = s[1] :
    (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
    (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
    (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
    (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
    (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
	if (Sys.edge) return 1;
	if (Sys.ie) return 1;
    if (Sys.firefox) return 1;
    if (Sys.chrome){ ver = Sys.chrome;ver.toLowerCase();var arr = ver.split('.');if(parseInt(arr[0])>43){return 1;}else{return 0;}}
    if (Sys.opera) return 1;
    if (Sys.safari) return 1;
	return 1;
}
function isNotCopy(bflag)
{
	WebOffice.IsNotCopy=bflag;	
	}

function WebSignature(strPic)
{
	 

	document.all.WebOffice.SetFieldValue('mark_1','','::ADDMARK::');
	document.all.WebOffice.SetFieldValue('mark_1',strPic,'::FLOATJPG::');
	var doc = document.all.WebOffice.ActiveDocument;
	//doc.Shapes.AddPicture(strPic, false, true,100,0,207,209,doc.Application.Selection.Range);
	doc.Shapes(doc.Shapes.Count).Select(); 
	var range = doc.Application.Selection.ShapeRange;
	range.WrapFormat.Type = 3;
	range.PictureFormat.TransparentBackground = true;
	range.PictureFormat.TransparencyColor = 0xFFFFFF;
	range.Fill.Visible = false;

}

export { 
 LoadCtrl,ShowPage,SetMenu,isNotCopy,WebSignature
}