const socket = new WebSocket("ws://localhost:8080/07_TecWeb_solved/actions2");

function send(data) {
  var json = JSON.stringify(data);

  socket.send(json);
}

socket.onmessage = function (event) {
  console.log("[socket.onmessage]", event.data);

  var message = JSON.parse(event.data);
  if (event.data.includes("update")) {
    var toUpdate = message.update;
    var tuUpdateElement = document.getElementById(toUpdate);
    tuUpdateElement.value = message.valore;
    return;
  }
  if (message.valid) {
    var oldLog = document.getElementById("oldRis");
    var log = document.getElementById("risultato");
    log.value = "";
    oldLog.value = "";
    console.log(event.data);
    oldLog.value = message.oldRis;
    log.value = message.risultato;
  } else {
    alert("hai superato il limite massimo di richieste per sessione");
  }
};

function myFunction() {
  var op1 = document.getElementById("op1").value;
  var op2 = document.getElementById("op2").value;
  if (isNaN(op1) || isNaN(op2)) {
    alert("uno dei due operandi non è un numero");
    return;
  }
  var operazione = document.getElementsByName("op");
  var op = false;
  for (var i = 0; i < operazione.length; i++) {
    if (operazione[i].checked) {
      op = operazione[i].value;
      break;
    }
  }
  if (!op) {
    alert("Selezionare operazione");
    return;
  }
  var operationReq = {};
  operationReq.op1 = op1;
  operationReq.op2 = op2;
  operationReq.operazione = op;

  send(operationReq);
}

function myUpdate(element) {
  var elemento = document.getElementById(element);

  if (isNaN(elemento.value)) {
    alert("il contenuto inserito in " + element + " non è un numero");
    return;
  }

  // empty check
  if (elemento.value.length == 0) {
    console.log("[myUpdate] element string is empty, returning");
    return;
  }
  var updateReq = {};
  updateReq.update = element;
  updateReq.valore = elemento.value;

  var json = JSON.stringify(updateReq);

  console.log("[myUpdate] sending", json);
  socket.send(json);
}
