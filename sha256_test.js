// e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855

var logger = JsLogger.logger;
logger.warn("start call test_test_create_script!");

var arr = JavaScriptTimeUtil.getUTCString().split(/[- :]/);
var dateStr = arr[0]+arr[1]+arr[2]+'T'+arr[3]+arr[4]+arr[5]+'Z';

logger.warn('dateStr:【'+dateStr);

var sign1 = DigestUtils.sha256(dateStr);
logger.warn('sign1：【'+sign1);

var msg = new Message();
msg.header = message.header;
msg.body = {};
msg.body.str = dateStr;

var resopnse = CloudServiceAccessor.process("app.service.common_crypto.SHA256",msg);
logger.warn('sign2：【'+resopnse.body.result);

msg.body = {};
msg.body.sign1 = sign1;
msg.body.sign2 = resopnse.body.result;

return msg;
