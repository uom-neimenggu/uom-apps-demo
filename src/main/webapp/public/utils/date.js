/**
 *需要导入的脚本：
 *fullcalendar.js
 *ui.dialog.js
 * @author 曾臻
 */
function datestr2timestamp(datestr){
	if(!datestr)
		return 0;
	var d=jQuery.fullCalendar.parseISO8601(datestr);
	var ts=d.getTime();
	return ts;
}
function date2datestr(d){
	var str=jQuery.fullCalendar.formatDate(d,"yyyy-MM-dd");
	return str;
}

function date2timestr(d){
	var str=jQuery.fullCalendar.formatDate(d,"HH:mm");
	return str;
}
function date2timestr2(d){
	var str=jQuery.fullCalendar.formatDate(d,"H:mm");
	return str;
}

function timestamp2datestr(ts){
	var text="";
	if(ts)
		text=date2datestr(new Date(ts));
	return text;
}
function formatDate(d){
	var date=d;
	if(typeof(d)=="number")
		date=new Date(d);
	var str=jQuery.fullCalendar.formatDate(date,"yyyy-MM-dd");
	return str;
}
function formatDateFull(d){
	var date=d;
	if(typeof(d)=="number")
		date=new Date(d);
	var str=jQuery.fullCalendar.formatDate(date,"yyyy-MM-dd H:mm:ss");
	return str;
}
function getDateName(d){
	var str=jQuery.fullCalendar.formatDate(d,"yyyy年MM月dd日");
	return str;
}
function getWeekOfMonth(d){
	
}
function getDayName(d){
	var dayNames=["周日", "周一", "周二", "周三", "周四", "周五", "周六"];
	return dayNames[d.getDay()];
}
/**
 * 设置input控件日期
 * @param elem
 */
function setDate(elem,onSelect){
	jQuery(elem).datepicker({
		onSelect:onSelect,
		changeMonth: true,
        changeYear: true,
        showOtherMonths: true,
        selectOtherMonths: true,
        dateFormat:"yy-mm-dd",
        monthNamesShort:["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        dayNamesMin:["日", "一", "二", "三", "四", "五", "六"]
	});
	jQuery(elem).focus().blur().focus();//fixed:not popup problem
	
}
function showDatePicker(element, onSelect) {
	setDate(element, onSelect);
	jQuery("#ui-datepicker-div").addClass("css-fix");
}

function showDateTimePicker(element, onSelect) {
	jQuery(element).datetimepicker({
		onSelect:onSelect,
		changeMonth: true,
        changeYear: true,
        showOtherMonths: true,
        selectOtherMonths: true,
        dateFormat:"yy-mm-dd",
        monthNamesShort:["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
        //showOn: "button",  
        showSecond: false,  
        timeFormat: 'HH:mm',//ss
        stepHour: 1,
        stepMinute: 5,
        stepSecond: 10
	});
	jQuery(element).focus().blur().focus();//fixed:not popup problem
	jQuery("#ui-datepicker-div").addClass("css-fix");
}

// 获取日期元素的日期值
function dateStr2Date(dateStr) {
	if (dateStr == "") {
		return null;
	} else {
		return new Date(Date.parse(dateStr.replace(/-/g, "/")));
	}
}
function getDateValue(elementId) {
	var value = jQuery("#" + elementId).val();
	return dateStr2Date(value);
}