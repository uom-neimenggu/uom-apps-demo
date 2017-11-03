/**
 * @author 曾臻
 * @date 2012-12-3
 * @param url
 * @param key
 * @returns
 */
function parseUrlParams(url,key){
	var obj={};
	var str=url.substring(url.indexOf("?")+1,url.length);
	var arr=str.split("&");
	for(var i=0;i<arr.length;i++){
		var pair=arr[i].split("=");
		obj[pair[0]]=pair[1];
	}
	return obj;
}
function toReadableLength(size){
	if(size>1024*1024){
		return Math.floor(size/1024/1024)+" MB";
	}else if(size>1024){
		return Math.floor(size/1024)+" KB";
	}else{
		return size+" Bytes";
	}
	return "";
};