/**
 * 对各个tab页的延迟初始化支持
 */
var inits={};
var onActive={};
/**
 * 注册初始化函数
 * @author 曾臻
 * @date 2012-11-24
 * @param name
 * @param func
 */
function registerInitFunc(name,func){
	inits[name]=func;
}
/**
 * 调用初始化函数
 * @author 曾臻
 * @date 2012-11-24
 * @param name
 */

function callInitFunc(name){
	var func=inits[name];
	if(func)
		func();
}
function registerOnActiveFunc(name,func){
	onActive[name]=func;
}
function callOnActiveFunc(name){
	var func=onActive[name];
	if(func)
		func();
}