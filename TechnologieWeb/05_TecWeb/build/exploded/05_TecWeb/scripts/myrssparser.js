// --------------------------My Function ------------------------------
function startsWith(theUri) { //uri jsp che cerca la categoria partendo dal prefisso
	xhr = new XMLHttpRequest();

	//my callback
	xhr.onreadystatechange = function(xhr) {
		if (xhr.readyState === 4)
			if (xhr.status === 200)
				myGetElementById("category").innerHTML = xhr.responseText;
	}

	try {
		xhr.open("get", theUri, true);
	}
	catch (e) {
		alert(e);
	}

	xhr.setRequestHeader("connection", "close");
	xhr.send(null);
};