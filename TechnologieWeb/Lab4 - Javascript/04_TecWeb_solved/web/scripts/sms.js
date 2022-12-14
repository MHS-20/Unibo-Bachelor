// sms.js

var MAX_CHAR = document.form_mess.contatore.value; // inizializzato la prima volta

document.form_mess.messaggio_testo.onmouseover = 
function() {
    var t = document.form_mess.messaggio_testo;
	t.style.backgroundColor = "blue";
	t.style.color = "white";
}

document.form_mess.messaggio_testo.onmouseout = 
function() {
    var t = document.form_mess.messaggio_testo;
	t.style.backgroundColor = "white";
	t.style.color = "black";
}

function aggiorna(f) {
    if (f.messaggio_testo.value.length > MAX_CHAR) {
        f.messaggio_testo.value = f.messaggio_testo.value.substring(0,MAX_CHAR);
        f.contatore.value = 0;
	}
	else {
		f.contatore.value = MAX_CHAR - f.messaggio_testo.value.length;
	}
}
