// orologio.js

var MESE = [
			"gennaio",			"febbraio",        "marzo",
			"aprile",       "maggio",          "giugno",
			"luglio", 			"agosto",     		 "settembre",
			"ottobre", 			"novembre",   		 "dicembre"
];

var GIORNO = [
			"domenica",	 		"lunedì",					 "martedì",
			"mercoledì",		"giovedì",				 "venerdì",
			"sabato"
];

// sistema lo stile della casella di testo "orologio"
var s = document.mio_form.orologio.style;
s.borderStyle = "none";
s.fontFamily  = "monospace";
s.fontWeight  = "bolder";
s.fontSize    = "x-large";

// Visualizza l'ora nella casella di testo "orologio"
function orologio() {
    var adesso = new Date();            	// istante corrente
	var m = adesso.getMonth();			// 0 = gennaio,  1 = febbraio, ...
	var d = adesso.getDate();			// 1 = primo del mese,...
	var g	= adesso.getDay();			// 0 = domenica, 1 = lunedì, ...
	var a = adesso.getFullYear();					  
	var o = adesso.getHours();          // 0 = mezzanotte, ...
    var p = adesso.getMinutes();            
    var s = adesso.getSeconds();
    var ora = "Oggi è " + GIORNO[g] + ' ' + d + ' ' + MESE[m] +
		    ' ' + a + ", ore " + o + ':' + p + ':' + s;
    document.mio_form.orologio.value = ora;				 // aggiorna il testo
}
