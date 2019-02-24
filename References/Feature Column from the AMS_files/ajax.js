// amsAjax Options
// url (required) - the url you are requesting data from
// elementID - the html element id to fill (innerHTML) with returned data
// globalVar - name of global variable to fill with returned data
// confirmMessage - popup message for user, yes/no
// hideEmpty - boolean - set id display to none for empty data set
// callback - function to call after request is complete


// Legacy Function
function makeRequest (url, elementID, confirmMessage, hideEmpty, callback) {

    var params = [];
    params['url'] = url;
    params['elementID'] = elementID;
    params['confirmMessage'] = confirmMessage;
    params['hideEmpty'] = hideEmpty;
    params['callback'] = callback;

    var ajax = new amsAjax(params);
    ajax.start();
}

function makeRequestP (params) {
    var ajax = new amsAjax(params);
    ajax.start();
}

function amsAjax (params) {
    var p = params || [];
    this.url = p['url'] || null;
    this.elementID = p['elementID'] || null;
    this.globalVar = p['globalVar'] || null;    
    this.confirmMessage = p['confirmMessage'] || null;
    this.hideEmpty = p['hideEmpty'] || null;
    this.callback = p['callback'] || null;
    this.method = p['method'] || 'GET';
    this.httpRequest = false;
    this.postData = p['postData'] || [];
    
    this.start = function() {
        if (this.confirmMessage != null && this.confirmMessage != '')
            if (!confirm(this.confirmMessage))
                return false;
        
	this.httpRequest = getRequest();
	//this.httpRequest.withCredentials = true;
        
        if ('POST' == this.method) 
            return doPost(this);
        else 
            return doGet(this);
    }    
    
    function doGet(myAjax) {
        myAjax.httpRequest.onreadystatechange = function () {
            processData(myAjax);
        };
            
        myAjax.httpRequest.open('GET', myAjax.url, true);
        myAjax.httpRequest.send(null);
    
        return true;
    }

    function doPost(myAjax) {
        myAjax.httpRequest.onreadystatechange = function () {
            processData(myAjax);
        };

        myAjax.httpRequest.open('POST', myAjax.url, true);
        myAjax.httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        myAjax.httpRequest.setRequestHeader("Content-length", myAjax.postData.length);
        myAjax.httpRequest.setRequestHeader("Connection", "close");
        myAjax.httpRequest.send(myAjax.postData);
        
        return true;
    }

    function processData (myAjax) {  
        if (myAjax.httpRequest.readyState == 4) {
            if (myAjax.httpRequest.status == 200) {
                var data = myAjax.httpRequest.responseText;
                if (null != myAjax.elementID && myAjax.elementID.length > 0) {              
                    var elem = document.getElementById(myAjax.elementID);
                    if (myAjax.hideEmpty && !data.match(".*\\w.*"))
                        elem.style.display = 'none';
                    else
                        elem.innerHTML = data;
                    if (elem.style.visibility == "none")
                        document.getElementById(myAjax.elementID).style.display = 'inline';
                }

                if (null != myAjax.globalVar && '' != myAjax.globalVar)
                    window[myAjax.globalVar] = data;

                data = '';

                if (null != myAjax.callback && '' != myAjax.callback) {
                    var myCallback = myAjax.callback;
                    myCallback(); 
                }
            }    
            else if (myAjax.httpRequest.status > 0) {
                alert('There was a problem with the request. Error #: '+myAjax.httpRequest.status);
			}
        }
        return true;        
    }
    
    function getRequest () {
        var httpRequest = false;
        if (window.XMLHttpRequest) { // Firefox, Safari, Opera
            httpRequest = new XMLHttpRequest();
            if (httpRequest.overrideMimeType)
                httpRequest.overrideMimeType('text/html');                        
        } else if (window.ActiveXObject) { // Internet Explorer
            try {
                httpRequest = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                try {
                  httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (e) {}
            }
        }

        if (! httpRequest) {
            alert('Giving up - Cannot create an XMLHTTP instance');
            return false;
        }

        return httpRequest;
    }
}

function replaceElement (id, text) {
    var oldDiv = document.getElementById(id);  
    var newDiv = oldDiv.cloneNode(false);
    newDiv.innerHTML = text;
    oldDiv.parentNode.replaceChild(newDiv, oldDiv);               
}




           
