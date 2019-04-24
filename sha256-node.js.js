var crypto = require('crypto');
var hash = crypto.createHash('sha256');// sha256或者md5
hash.update('123456');//'123456'为要加密的字符串
var res = hash.digest('hex');
console.log(res);// 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92