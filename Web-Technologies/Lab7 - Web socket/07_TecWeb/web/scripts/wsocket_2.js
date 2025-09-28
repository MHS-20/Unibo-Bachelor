const socket = new WebSocket("ws://localhost:8080/07_TecWeb/actions2");

function send( data) {
    var json = JSON.stringify(data);

    socket.send(json);
}

socket.onmessage =  function (event){
	console.log(event)
}

