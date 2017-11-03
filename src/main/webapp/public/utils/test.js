/**
 * 性能测试工具
 * @author 曾臻
 */
function beginTimeCost(){
	return new Date().getTime();
}
function endTimeCost(begin){
	return new Date().getTime()-begin;
}
function endTimeCostPrompt(begin,title){
	if(title)
		prompt(title+"(ms):",endTimeCost(begin));
	else 
		prompt("(ms):",endTimeCost(begin));
	
}