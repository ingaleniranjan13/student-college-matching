function amsFC () {

	this.outputDiv = 'fcOutput';
	this.barDiv = 'fcSelected';
	this.xmlURL = 'fc.xml';
	this.xslURL = 'fc.xsl';

	this.xml;
	this.xsl;
	this.year = 0;
	this.author = 0;
	this.category = 0;

	this.load = function () {
	
		document.getElementById(this.outputDiv).innerHTML = 'loading...';

		this.xml = this.getXML(this.xmlURL);
		this.xsl = this.getXML(this.xslURL);

		this.loadSelect('fcAuthor','Author');
		this.loadSelect('fcYear','Year');
		this.loadSelect('fcCategory','Category');

		getParams(this, {'year':1, 'author':1, 'category':1});
		this.setSelects();
		this.displayBar();
		this.display();
	}

	this.getXML = function (url) {
		if (window.ActiveXObject || "ActiveXObject" in window){
			xhttp = new ActiveXObject("MSXML2.FreeThreadedDomDocument.3.0");
			xhttp.async = false;
			xhttp.load(url);
			return xhttp;
		}
		else {
			xhttp = new XMLHttpRequest();
			xhttp.open("GET", url, false);
			xhttp.send();
			return(xhttp.responseXML);
		}
	}


	this.display = function () {
		document.getElementById(this.outputDiv).innerHTML = 'loading...';

		if (window.ActiveXObject || "ActiveXObject" in window) {
			var template = new ActiveXObject("MSXML2.XSLTemplate");
			template.stylesheet = this.xsl;
			processor = template.createProcessor();
			processor.input = this.xml;
		
			processor.addParameter("year", this.year);
			processor.addParameter("author", this.author);
			processor.addParameter("category", this.category);

			processor.transform();
			ex = processor.output;
			document.getElementById(this.outputDiv).innerHTML = ex;

		}
		else if (document.implementation && document.implementation.createDocument) {
			xsltProcessor = new XSLTProcessor();
			xsltProcessor.importStylesheet(this.xsl);

			xsltProcessor.setParameter(null, 'year', this.year);
			xsltProcessor.setParameter(null, 'author', this.author);
			xsltProcessor.setParameter(null, 'category', this.category);

			resultDocument = xsltProcessor.transformToFragment(this.xml, document);
			document.getElementById(this.outputDiv).innerHTML = '';
			document.getElementById(this.outputDiv).appendChild(resultDocument);
		}

		var out = document.getElementById(this.outputDiv).innerHTML;
		if (! out || out == "" || out == "loading...") {
			document.getElementById(this.outputDiv).innerHTML = "No Results Found";
		}
		
	}

	this.filter = function (param, value) {
		if (value == 'Year' || value == 'Author' || value == 'Category') value = 0;
		this[param] = value;
		this.displayBar();
		this.display();		
	}

	this.filterClear = function () {
		this.year = 0;
		this.author = 0;
		this.category = 0;
		document.getElementById("fcYear").selectedIndex = 0;
		document.getElementById("fcAuthor").selectedIndex = 0;
		document.getElementById("fcCategory").selectedIndex = 0;
		this.displayBar();
		this.display();				
	}

	this.displayBar = function () {
		var barText = "";
		if (this.author) barText += "Author: " + this.author + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		if (this.year) barText += "Year: " + this.year + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		if (this.category) barText += "Category: " + this.category;
		if (! barText) barText = "All Feature Columns";

		document.getElementById(this.barDiv).innerHTML = barText;
	}

	this.loadSelect = function (selectID, tagName) {
		var tags = this.getTags(tagName);
		var select = document.getElementById(selectID);
		var optionCount = select.options.length;
		for (var t in tags) {	
			select.options[optionCount] = new Option(t);
			optionCount++;
		}
	}

	this.setSelects = function () {
		this.setSelect('fcYear', this.year);
		this.setSelect('fcAuthor', this.author);
		this.setSelect('fcCategory', this.category);
	}

	this.setSelect = function (selectID, selectValue) {
		var select = document.getElementById(selectID);

		for (var i = 0; i <= select.options.length; i++) {
			if (select[i] && select[i].value == selectValue) {
				select[i].selected = 1;
				break;
			}
		}
	}

	this.loadUL = function (ulID, tagName) {
		var tags = this.getTags(tagName);
		var ul = document.getElementById(ulID);
		for (var t in tags) {
			li = document.createElement('li'); 
			li.innerHTML = '<a href="/samplings/feature-column/fcbrowse?'+tagName.toLowerCase()+'='+t+'">'+t+'</a>';	
			ul.appendChild(li);
		}		
	}

	this.getTags = function (tagName) {
		var numeric = 0;
		if (tagName == 'Year') numeric = 1;
		var tagXML = this.xml.getElementsByTagName(tagName);
		var tags = [];
		for (var i = 0; i < tagXML.length; i++) {
			tags[tagXML[i].firstChild.nodeValue] = 1;
		}
		tags = sortObj(tags, numeric);		
		return tags;
	}

	function numberSort(a, b) {
		return a - b;
	}

	function numberSortReverse(a, b) {
		return b - a;
	}

	function sortObj(myArray, numeric) {
		var sortedKeys = new Array();
		var sortedObj = {};
		for (var i in myArray) {
			sortedKeys.push(i);
		}
		if (numeric) {
			sortedKeys.sort(numberSortReverse);
		}
		else {
			sortedKeys.sort();
		}
		for (var i in sortedKeys) {
			sortedObj[sortedKeys[i]] = myArray[sortedKeys[i]];
		}
		return sortedObj;
	}

	function getParams (object, array) {
		var query = window.location.search.substring(1);
		var params = query.split('&');
		var myKey = "";
		var myValue = "";

		for (var i = 0; i < params.length; i++) {
			var pos = params[i].indexOf('=');
			if (pos > 0) {
				myKey = params[i].substring(0, pos);
				myValue = params[i].substring(pos + 1);
				if (array[myKey]) {
					myValue = myValue.replace(/%20/g,' ');
					object[myKey] = myValue;
				}
			}
		}
	}
}
