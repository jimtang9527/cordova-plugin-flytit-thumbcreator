var exec = require('cordova/exec');

function ThumbCreator() {}

ThumbCreator.prototype.createThumb = function(fromPath, successCallback, errorCallback) {
    var args = [fromPath];
    exec(function(result) {
            successCallback(JSON.parse(result));
        },
        function(result) {
            errorCallback(result);
        },
        "ThumbCreator",
        "createThumb",
        args
    );
}

var thumb = new ThumbCreator();
window.Thumbnails = thumb;
module.exports = thumb;
