// questionario.js

// La funzione 'dettaglia' aggiunge i dettagli di un evento
// all'elemento area di testo "area_testo" presente nel form
// "mio_form". Viene invocata da vari gestori evento.

function dettaglia(elemento, evento) {
    var t = elemento.form.area_testo;
    var nome_elemento = elemento.name;
		var valore = " ";
    if ((elemento.type == "select-one") || (elemento.type == "select-multiple")){
        for(var i = 0; i < elemento.options.length; i++)
            if (elemento.options[i].selected) 
                valore += elemento.options[i].value + " ";
    }
    else if (elemento.type == "textarea") valore = "...";
    else valore = elemento.value;
    var mess = evento + ": " + nome_elemento + ' (' + valore + ')\n';
	t.value += mess;
}

// La funzione aggiungi_gestori installa un insieme di gestori di eventi su
// ogni elemento di un form f, senza controllare se l'elemento supporta
// tutti questi tipi di eventi
function aggiungi_gestori(f) {
    var gestore_click       = new Function("dettaglia(this, 'Click')");
    var gestore_change      = new Function("dettaglia(this, 'Change')");
    var gestore_focus       = new Function("dettaglia(this, 'Focus')");
    var gestore_blur        = new Function("dettaglia(this, 'Blur')");
    var gestore_select      = new Function("dettaglia(this, 'Select')");
	var gestore_dblclick    = new Function("dettaglia(this, 'dblClick')");
	
    for(var i = 0; i < f.elements.length; i++) {
        var e = f.elements[i];
        e.onclick    = gestore_click;
        e.onchange   = gestore_change;
        e.onfocus    = gestore_focus;
        e.onblur     = gestore_blur;
        e.onselect   = gestore_select;
		e.ondblclick = gestore_dblclick;
	}

    // Gestori speciali per i bottoni:
    f.bottone_svuota.onclick = 
        new Function("this.form.area_testo.value=''; dettaglia(this, 'Click');");
    f.bottone_sottometti.onclick = 
        new Function("dettaglia(this, 'Click'); return false");
    f.bottone_azzera.onclick = 
        new Function("this.form.reset(); dettaglia(this, 'Click'); return false");
}

// Attiviamo il form aggiungendo i possibili gestori
aggiungi_gestori(document.mio_form);
