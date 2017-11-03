jQuery(function() {
	ibimage.init();
});
function BulImage() {
}
var ibLastImgId=-1;
var ibimage = new BulImage();
BulImage.prototype.init = function() {
	var _this=this;
	this.render();
	
	unregisterInterval("image","refresh");
	var intId=setInterval(function(){
		//MOD FOR UOM，加try catch解决frame index框架下当切换到其他tab报charWdith undefined的问题
		try{
			_this.render();
		}catch(e){}
	},ib_refresh_interval);
	registerInterval("image","refresh",intId);
};

BulImage.prototype.render = function() {
	var div = jq("div.ib-bulletin[cat='image']");
	var imgDiv = div.find("div.ib-image");
	var titleDiv = div.find("div.ib-title");
	informationAction
			.queryBulletinImage(function(r) {
				if (r[0] == "ok" && r[1] != "notfound") {
					var article = r[1];
					if(ibLastImgId==article.thumbnail.thumbnailId)
						return;
					imgDiv
							.html("<img height='600' src='/uom-apps/informationThumbnailDownload.action?id="
									+ article.thumbnail.thumbnailId + "'/>");
					ibLastImgId=article.thumbnail.thumbnailId;
					
					if(article.thumbnail.brief)
						imgDiv.attr("title",article.thumbnail.brief);
					
					var maxlen = ibimage.calculateMaxChars(titleDiv);
					titleDiv.css({"font-size":"2em","margin-top":"10px"});
					titleDiv.text(ibimage.createTitle(article.title, maxlen));
					
				}else if(r[0]=="ok"&&r[1]=="notfound"){
					imgDiv.empty();
					titleDiv.empty();
				}
			});
};
/*
 * 计算标题的最大字符数（标题本身）
 */
BulImage.prototype.calculateMaxChars = function(ul) {
	var width=930;// ul[0].clientWidth;
	var charWidth = ul.css("font-size");
	charWidth = charWidth.substring(0, charWidth.length - 2);//2 is 'px'
	charWidth = parseFloat(charWidth);
	var n = Math.floor(parseFloat(width) / charWidth) - 5;//5 is length of "... xx月xx日 xx:xx"
	return n;
};
BulImage.prototype.createTitle = function(title, maxchars) {
	if (title.length <= maxchars + 1)//1 is length of "..." , maxchars do not include ...
		return title;
	var t = title.substring(0, maxchars);
	return t + "...";
};
