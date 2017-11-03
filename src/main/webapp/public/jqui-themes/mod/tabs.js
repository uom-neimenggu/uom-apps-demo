/**
 * 将jquery ui tabs 改为垂直
 * @author 曾臻
 **/
function makeVerticalTabs(elem){
	//wrap with table
	var nav=jQuery(elem).children("ul");
	var content=jQuery(elem).children("ul").nextAll();
	var table=jQuery('<table class="ui-tabs-framework">'+
    		'<tr>'+
    		'<td class="ui-tabs-nav-bg">'+
    		'</td>'+
    		'<td class="ui-tabs-panel-bg">'+
			'</td>'+
			'</tr>'+
			'</table>');
	table.find(".ui-tabs-nav-bg").append(nav.clone());
	table.find(".ui-tabs-panel-bg").append(content.clone());
	jQuery(elem).empty();
	jQuery(elem).append(table);
	
	//make query ui tabs
	jQuery(elem).tabs();
	
	//mod styles
	jQuery(elem).addClass("ui-tabs-vertical");
	jQuery(elem).find(".ui-tabs-nav-bg ul li").removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );
	jQuery(elem).find(".ui-tabs-nav-bg ul li a").click(function(){
		jQuery(this).parent().parent().find(".ui-tabs-noline").removeClass("ui-tabs-noline");
		jQuery(this).parent().prev().addClass("ui-tabs-noline");
	});
	jQuery(elem).find(".ui-tabs-nav-bg ul").prepend("<li class='ui-state-default ui-tabs-noline' style='height:15px'></li>");

}
