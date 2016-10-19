var exec = require('cordova/exec');

exports.coolMethod = function(arg0, success, error) {
    exec(success, error, "ScreenShot", "coolMethod", [arg0]);
};


var ScreenShot = function(){
};
ScreenShot.prototype.receiveMessageInAndroidCallback = function(data){
    try{
        console.log("JPushPlugin:receiveMessageInAndroidCallback");
        //var bToObj  = JSON.parse(data);
        //this.receiveMessage=bToObj
        //cordova.fireDocumentEvent('jpush.receiveMessage',null);
        //console.log(data);
        //var message  = bToObj.message;
        //var extras  = bToObj.extras;

        //console.log(message);
        //console.log(extras['cn.jpush.android.MSG_ID']);
        //console.log(extras['cn.jpush.android.CONTENT_TYPE']);
        //console.log(extras['cn.jpush.android.EXTRA']);
    }
    catch(exception){
        console.log("JPushPlugin:pushCallback "+exception);
    }
};
//同意管里
ScreenShot.prototype.receiveCallback = function(data){
    try{
        console.log("JPushPlugin:receiveMessageInAndroidCallback");
        //var bToObj  = JSON.parse(data);
        //this.receiveMessage=bToObj
        //cordova.fireDocumentEvent('jpush.receiveMessage',null);
        //console.log(data);
        //var message  = bToObj.message;
        //var extras  = bToObj.extras;

        //console.log(message);
        //console.log(extras['cn.jpush.android.MSG_ID']);
        //console.log(extras['cn.jpush.android.CONTENT_TYPE']);
        //console.log(extras['cn.jpush.android.EXTRA']);
    }
    catch(exception){
        console.log("JPushPlugin:pushCallback "+exception);
    }
}