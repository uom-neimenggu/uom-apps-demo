/**
 * 基本功能，如dwr调用的“正在加载”显示
 * @author 曾臻
 */
jQuery(function(){
	//TODO 解决ie6下下面代码与bgiframe.js冲突
	if(isIE6())
		return;
//XXX mod for uom
var imageSrc="/uom-apps/public/utils/images/loading.gif";
  //var imageSrc="/uniportal-plugins/public/utils/images/loading.gif";
  DWREngine.setPreHook(function() {
    var disabledImageZone = jQuery('#disabledImageZone');
    if (!disabledImageZone.length) {
      disabledImageZone = jQuery('<div>');
      disabledImageZone.attr('id', 'disabledImageZone');
      disabledImageZone.css({
    	position : "absolute",
    	zIndex : "10000",
      	width : "40px",
      	height : "40px",
      	backgroundColor:"white",
      	border:"1px solid #dddddd"
      });
      var imageZone = jQuery('<img>');
      imageZone.attr('id','imageZone');
      imageZone.attr('src',imageSrc);
      imageZone.css({
    	position : "absolute"
      });
      disabledImageZone.append(imageZone);
      jQuery(document.body).append(disabledImageZone);
    }
    else {
      jQuery('#imageZone').attr("src" , imageSrc);
      disabledImageZone.show();
    }
    jQuery("#imageZone").position({
  	  of:disabledImageZone
    });
    disabledImageZone.position({
   	 	of:window
    });
  });
  DWREngine.setPostHook(function() {
	  jQuery('#disabledImageZone').hide();
  }); 
});
function isIE6(){
	var flag=jQuery.browser.msie && /msie 6\.0/i.test(navigator.userAgent);
	return flag;
}
/**
 * 选中下拉框 ie6 hack
 * @author 曾臻
 * @date 2013-2-19
 * @param sel
 * @param val
 * @returns
 */
function selectOption(sel,val,onDone){
	setTimeout(function(){
		jQuery(sel+" > option[value='"+val+"']").attr("selected",true);
		if(onDone){
			onDone();
		}
	},0);
}
