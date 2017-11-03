/**
 * This jQuery plugin displays pagination links inside the selected elements.
 *
 * @author Gabriel Birke (birke *at* d-scribe *dot* de)
 * @author 曾臻 改进
 * @version 1.2
 * @param {int} maxentries Number of entries to paginate
 * @param {Object} opts Several options (see README for documentation)
 * @return {Object} jQuery Object
 */
(function($) {
	var local={};
//	{
//		panel:null,
//		maxentries:0,
//		current_page:0
//	};
	var opts = {};
	var methods = {
		init:function(maxentries,_opts){
			return this.each(function() {
				var id=$(this).attr("id");
				opts[id]={};
				opts[id] = $.extend({
		    		items_per_page:10,
		    		num_display_entries:10,
		    		current_page:0,
		    		num_edge_entries:0,
		    		link_to:"#",
		    		prev_text:"Prev",
		    		next_text:"Next",
		    		ellipse_text:"...",
		    		prev_show_always:true,
		    		next_show_always:true,
		    		load_at_once:true,
		    		callback:function(){return false;}
		    	},_opts||{});
				
				local[id]={};
	    		// Extract current_page from options
	    		local[id].current_page = opts[id].current_page;
	    		// Create a sane value for maxentries and items_per_page
	    		local[id].maxentries = (!maxentries || maxentries < 0)?1:maxentries;
	    		opts[id].items_per_page = (!opts[id].items_per_page || opts[id].items_per_page < 0)?1:opts[id].items_per_page;
	    		// Store DOM element for easy access from all inner functions
	    		local[id].panel = $(this);
	    		// Attach control functions to the DOM element 
	    		this.selectPage = function(){ pageSelected(id);}
	    		this.prevPage = function(){ 
	    			if (local[id].current_page > 0) {
	    				pageSelected(local[id].current_page - 1);
	    				return true;
	    			}
	    			else {
	    				return false;
	    			}
	    		}
	    		this.nextPage = function(){ 
	    			if(local[id].current_page < numPages(pagination_id)-1) {
	    				pageSelected(local[id].current_page+1);
	    				return true;
	    			}
	    			else {
	    				return false;
	    			}
	    		}
	    		// When all initialisation is done, draw the links
	    		drawLinks(id);
	            // call callback function
	    		if(opts[id].load_at_once)
	    			opts[id].callback(local[id].current_page, this);
	    	});
		},
		gotoPage:function(page){
			pageSelected(page);
		},
		refresh:function(){
			//TODO multi widget support
			pageSelected(local.current_page);
		},
		getCurrentPage:function(){
			//TODO multi widget support
			return local.current_page;
		},
		getPageSize:function(){
			//TODO multi widget support
			return opts.items_per_page;
		}
	};
	
	$.fn.pagination = function(method) {
		
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call(
					arguments, 1));
		} else if (typeof method==='number' ||typeof method === 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist on jQuery.tooltip');
		}

	};
	
	/**
	 * Calculate the maximum number of pages
	 */
	function numPages(pagination_id) {
		return Math.ceil(local[pagination_id].maxentries/opts[pagination_id].items_per_page);
	}
	
	/**
	 * Calculate start and end point of pagination links depending on 
	 * current_page and num_display_entries.
	 * @return {Array}
	 */
	function getInterval(pagination_id)  {
		var ne_half = Math.ceil(opts[pagination_id].num_display_entries/2);
		var np = numPages(pagination_id);
		var upper_limit = np-opts[pagination_id].num_display_entries;
		var start = local[pagination_id].current_page>ne_half?Math.max(Math.min(local[pagination_id].current_page-ne_half, upper_limit), 0):0;
		var end = local[pagination_id].current_page>ne_half?Math.min(local[pagination_id].current_page+ne_half, np):Math.min(opts[pagination_id].num_display_entries, np);
		return [start,end];
	}
	
	/**
	 * This is the event handling function for the pagination links. 
	 * @param {int} page_id The new page number
	 */
	function pageSelected(page_id, evt){
		//TODO
		if(!evt)
			return;
		 
		var id=$(evt.currentTarget).closest("div").attr("id");
		local[id].current_page = page_id;
		drawLinks(id);
		var continuePropagation = opts[id].callback(page_id, local[id].panel);
		if (!continuePropagation&&evt) {
			if (evt.stopPropagation) {
				evt.stopPropagation();
			}
			else {
				evt.cancelBubble = true;
			}
		}
		return continuePropagation;
	}
	
	/**
	 * This function inserts the pagination links into the container element
	 */
	function drawLinks(pagination_id) {
		local[pagination_id].panel.empty();
		var interval = getInterval(pagination_id);
		var np = numPages(pagination_id);
		// This helper function returns a handler function that calls pageSelected with the right page_id
		var getClickHandler = function(page_id) {
			return function(evt){ return pageSelected(page_id,evt); }
		}
		// Helper function for generating a single link (or a span tag if it's the current page)
		var appendItem = function(pagination_id,page_id, appendopts){
			page_id = page_id<0?0:(page_id<np?page_id:np-1); // Normalize page id to sane value
			appendopts = $.extend({text:page_id+1, classes:""}, appendopts||{});
			if(page_id == local[pagination_id].current_page){
				var lnk = $("<span class='current'>"+(appendopts.text)+"</span>");
			}
			else
			{
				var lnk = $("<a>"+(appendopts.text)+"</a>")
					.bind("click", getClickHandler(page_id))
					.attr('href', opts[pagination_id].link_to.replace(/__id__/,page_id));
					
					
			}
			if(appendopts.classes){lnk.addClass(appendopts.classes);}
			local[pagination_id].panel.append(lnk);
		}
		// Generate "Previous"-Link
		if(opts[pagination_id].prev_text && (local[pagination_id].current_page > 0 || opts[pagination_id].prev_show_always)){
			appendItem(pagination_id,local[pagination_id].current_page-1,{text:opts[pagination_id].prev_text, classes:"prev"});
		}
		// Generate starting points
		if (interval[0] > 0 && opts[pagination_id].num_edge_entries > 0)
		{
			var end = Math.min(opts[pagination_id].num_edge_entries, interval[0]);
			for(var i=0; i<end; i++) {
				appendItem(pagination_id,i);
			}
			if(opts[pagination_id].num_edge_entries < interval[0] && opts[pagination_id].ellipse_text)
			{
				$("<span>"+opts[pagination_id].ellipse_text+"</span>").appendTo(local[pagination_id].panel);
			}
		}
		// Generate interval links
		for(var i=interval[0]; i<interval[1]; i++) {
			appendItem(pagination_id,i);
		}
		// Generate ending points
		if (interval[1] < np && opts[pagination_id].num_edge_entries > 0)
		{
			if(np-opts[pagination_id].num_edge_entries > interval[1]&& opts[pagination_id].ellipse_text)
			{
				$("<span>"+opts[pagination_id].ellipse_text+"</span>").appendTo(local[pagination_id].panel);
			}
			var begin = Math.max(np-opts[pagination_id].num_edge_entries, interval[1]);
			for(var i=begin; i<np; i++) {
				appendItem(pagination_id,i);
			}
			
		}
		// Generate "Next"-Link
		if(opts[pagination_id].next_text && (local[pagination_id].current_page < np-1 || opts[pagination_id].next_show_always)){
			appendItem(pagination_id,local[pagination_id].current_page+1,{text:opts[pagination_id].next_text, classes:"next"});
		}
	}

})(jQuery);