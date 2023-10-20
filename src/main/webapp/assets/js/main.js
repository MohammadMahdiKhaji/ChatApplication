var webSocket = new WebSocket("ws://localhost:80/letterEndPoint");

// webSocket.onopen= function(message){console.log("Open")};
// webSocket.onclose= function(message){console.log("Close")};
// webSocket.onmessage= function(message){
//     console.log("Message")
// };
// webSocket.onerror= function(message){console.log("Error")};

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

// var stompClient = null;
var username = null;

var xhr = new XMLHttpRequest();

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

webSocket.onopen= function(){console.log("Open")};
function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        // var socket = new SockJS('/ws');
        // stompClient = Stomp.over(socket);
        //
        // stompClient.connect({}, onConnected, onError);
        onConnected();
    }
    event.preventDefault();
}



function onConnected() {
    // Subscribe to the Public Topic
    // stompClient.subscribe('/topic/public', onMessageReceived);
    var url_addUser = "/app/chat.addUser";
    xhr.open("POST", url_addUser, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify({sender:username, type:'JOIN'}));
    connectingElement.classList.add('hidden');
}


webSocket.onerror = function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent != null) {
    var url_sendMessage = "/app/chat.sendMessage";
    xhr.open("POST", url_sendMessage, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify({sender:username, content:messageInput.value, type:'CHAT'}));
    messageInput.value = '';
    }
    event.preventDefault();
}


webSocket.onmessage = function (jsonText) {
    var message = JSON.parse(jsonText.data);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (jsonText.data.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {

    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

webSocket.onclose= function(){console.log("Close")};

xhr.onreadystatechange = function() {
    if (xhr.readyState === 4 && xhr.status === 200) {
        // Handle the response from the servlet
        console.log(xhr.responseText);
    }
};

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)