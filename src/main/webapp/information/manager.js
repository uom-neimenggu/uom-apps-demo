(function($,undefined){
    $(function() {
    	//init tabs
    	makeVerticalTabs("#im-tabs");
    	$("#im-tabs").show();
    	
        //lazily init for each tab
        $("#im-tabs .ui-tabs-nav-bg ul li a").click(function(){
        	
        	var href=$(this).attr("href");
        	var name=href.substring(8,href.length);
        	if($(this).prop("initialized")!=true){
        		callInitFunc(name);
        		$(this).prop("initialized",true);
        	}else{
        		callOnActiveFunc(name);
        	}
        });
        
        //open default tab
        $("#im-tabs .ui-tabs-nav-bg ul li a").get(0).click();
    
    });
})(jQuery);

function initCategorySel(elem,callback){
	//selects
	informationAction.queryCategoryList(function(r) {
		if (r[0] == "ok") {
			var list = r[1];
			for ( var i = 0; i < list.length; i++) {
				jQuery(elem).append(
						"<option value=" + list[i].code + ">" + list[i].name
								+ "</option>");
			}
			if(callback)
				callback();
		}
	});
}
