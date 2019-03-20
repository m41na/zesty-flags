// small helper function for selecting element by id
let id = id => document.getElementById(id);

let user = document.querySelector("input[name='user']").value;

//Establish the WebSocket connection and set up event handlers
let ws = new WebSocket("ws://" + location.hostname + ":" + location.port + "/ws/chat/" + user);
ws.onmessage = msg => updateChat(msg);
ws.onclose = () => alert("WebSocket connection closed");

// Add event listeners to button and input field
id("send").addEventListener("click", () => sendAndClear(id("message").value));
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { // Send message if enter is pressed in input field
        sendAndClear(e.target.value);
    }
});

function sendAndClear(message) {
    if (message !== "") {
        ws.send(message);
        id("message").value = "";
    }
}

function updateChat(msg) { // Update chat-panel and list of connected users
	console.log(msg);
    let data = JSON.parse(msg.data);
    id("chat").appendChild(createMessage(data));
}

function createMessage(msg){
	if ('content' in document.createElement('template')) {
		let template = document.querySelector('#chatmessage');
	    let clone = document.importNode(template.content, true);
	    let from = clone.querySelector(".from .sender");
	    from.textContent = msg.from;
	    let time = clone.querySelector(".from .time")
	    time.textContent = msg.time;
	    let content = clone.querySelector(".content")
	    content.textContent = msg.message || msg.error;
	    return clone;
	}
	else{
		let err = document.createElement("div");
		err.innerHTML = "browser does not support templates";
		return err;
	}
}