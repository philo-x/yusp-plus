// urlParams is null when used for embedding
window.urlParams = (function(url){
  var result = new Object();
  var idx = url.lastIndexOf('?');
  if (idx > 0) {
    var params = url.substring(idx + 1).split('&');
    
    for (var i = 0; i < params.length; i++)    {
      idx = params[i].indexOf('=');
      
      if (idx > 0) {
        result[params[i].substring(0, idx)] = params[i].substring(idx + 1);
      }
    }
  }
  
  return result;
})(window.location.href);

// Public global variables
window.MAX_REQUEST_SIZE = window.MAX_REQUEST_SIZE  || 10485760;
window.MAX_AREA = window.MAX_AREA || 15000 * 15000;
// window.basePath = 'libs/mxgraph/';
window.mxBasePath = 'static/libs/mxgraph/';
window.mxLoadResources = false;
window.IMAGE_PATH = window.mxBasePath + 'images/';
window.STYLE_PATH = window.mxBasePath + 'styles/';
window.STENCIL_PATH = window.mxBasePath + 'images/';
window.mxLanguage = 'zh';

// URLs for save and export
window.EXPORT_URL = window.EXPORT_URL || '/export';
window.SAVE_URL = window.SAVE_URL || '/save';
window.OPEN_URL = window.OPEN_URL || '/open';
window.RESOURCES_PATH = window.mxBasePath + 'resources/';
window.RESOURCE_BASE = window.RESOURCE_BASE || window.RESOURCES_PATH + 'grapheditor';
window.CSS_PATH = window.mxBasePath + 'styles/';
window.OPEN_FORM = window.OPEN_FORM || 'open.html';

// Sets the base path, the UI language via URL param and configures the
// supported languages to avoid 404s. The loading of all core language
// resources is disabled as all required resources are in grapheditor.
// properties. Note that in this example the loading of two resource
// files (the special bundle and the default bundle) is disabled to
// save a GET request. This requires that all resources be present in
// each properties file since only one file is loaded.
Node = function(){
  Object.call(this);
}
Line = function(){
  Object.call(this);
}