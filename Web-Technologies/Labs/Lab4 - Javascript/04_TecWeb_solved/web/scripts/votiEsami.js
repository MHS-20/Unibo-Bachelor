// votiEsami.js

// array di messaggi utente, istanziati prima che la pagina venga visualizzata
MESSAGGI_UTENTE = [
  "Inserisci una sequenza di voti tra 18 e 33",
  " non e' un numero",
  " e' un numero minore di 18 o maggiore di 33"
];

// gestore dei messaggi di errore
function inputError(dato, codMess) {
    window.alert("Errore: '" + dato + "' " + MESSAGGI_UTENTE[codMess]);
    return false;
}

// Oggetto Statistico, costruttore con proprieta' e metodi
function Statistico(dati) {
    this.dati 		= dati;
    this.datiNumerici = null;
    this.min		= 0;
    this.mas 		= 0;
    this.med		= null;
    this.variab	= null;

	this.analizzaDati =
	function() {
	    this.datiNumerici = this.dati.split(/\s\s*/);
	    for (var i = 0; i < this.datiNumerici.length; i++) {
	        var num = parseInt(this.datiNumerici[i]);	
	        if (isNaN(num)) 
	        	return inputError(this.datiNumerici[i],1);		     
	        else if (num<18 || num>33)
	        	return inputError(num,2);	
	        this.datiNumerici[i] = num;
	    }
	    return true;
	}
	
	this.minimo =
	function() {
	    var mini = this.datiNumerici[0];
	    for (var i=1; i<this.datiNumerici.length ; i++ ) {
	        mini = Math.min(mini, this.datiNumerici[i]);
	    }
	    this.min = mini;
	}	
	
	this.massimo =
	function() {
	    var massi = this.datiNumerici[0];
	    for (var i=1; i<this.datiNumerici.length ; i++ ) {
	        massi = Math.max(massi, this.datiNumerici[i]);
	    }
	    this.mas = massi;
	}	
	
	this.media =
	function() {   
	    var i = 0, medi = 0;
	    while (i < this.datiNumerici.length) {
	        medi += this.datiNumerici[i];
	        i++;
	    }
	    medi /= this.datiNumerici.length;
	    medi = Math.round(medi*100)/100;
	    // invece di switch si potrebbe usare un array di stringhe
	    var mediaQual = null;
	    switch(Math.floor((medi-18)/(33-18)*5 )) {
	        case 0:   mediaQual = "sufficiente";  break;
	        case 1:   mediaQual = "discreta";     break;
	        case 2:   mediaQual = "buona";        break;
	        case 3:   mediaQual = "distinta";     break;
	        case 4:   mediaQual = "ottima";       break;
	        default:  mediaQual = "eccellente";
	    }
	    this.med = { numerica:medi, qualitativa:mediaQual };
	}	
	
	this.variabilita =
	function() {
	    var i = 0, varia = 0;
	    do {
	        varia += Math.abs(this.datiNumerici[i]-this.med.numerica);
	        i++;
	    }
	    while (i < this.datiNumerici.length);
	    varia /= this.datiNumerici.length;
	    varia = Math.round(varia*100)/100;
	    // invece di switch si potrebbe usare un array di stringhe
	    var variabQual = null;
	    switch(Math.ceil(varia/7.5*3)) { 			// 7.5 massima variabilita
	        default:  variabQual = "nessuna";  break;
	        case 1:   variabQual = "bassa";    break;
	        case 2:   variabQual = "normale";  break;
	        case 3:   variabQual = "alta";
	    }
	    this.variab = { numerica: varia, qualitativa: variabQual };
	}	
	
	this.stampa =
	function() {
	    document.writeln("<p style=\"font-size: 30pt\">");
	    document.writeln("voti: " +        this.datiNumerici + "<br>");
	    document.writeln("minimo: " +      this.min + "<br>");
	    document.writeln("massimo: " +     this.mas + "<br>");	
	    document.writeln("media: " + 	  this.med.numerica + " (" + 
	                                       this.med.qualitativa +")<br>");
	    document.writeln("variabilit&agrave;: " + this.variab.numerica + " (" +
	                                              this.variab.qualitativa +")<br>");
	    document.writeln("</p>");
	}

}	
	
// funzione eseguita al caricamento della pagina
function main() {
    var voti = window.prompt(MESSAGGI_UTENTE[0]);
    if (voti==null)
		return;
	var stat = new Statistico(voti);
    if (!stat.analizzaDati())				
        return;
    stat.minimo();	
    stat.massimo();
    stat.media();
    stat.variabilita();
    stat.stampa()
}
