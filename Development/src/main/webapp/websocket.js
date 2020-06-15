function createUser (){
    var markers = {'name': document.getElementById('newName').value,
        'password': document.getElementById('newPsw').value};
    var xhr = new XMLHttpRequest();
    var url = "http://localhost:8080/java-websocket/rest/user/create";
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
// xhr.onreadystatechange = function () {
//     if (xhr.readyState === 4 && xhr.status === 200) {
//         var json = JSON.parse(xhr.responseText);
//         console.log(json.name + ", " + json.password);
//     }
// };
    var data = JSON.stringify(markers);
    xhr.send(data);}



var ws;

function connect() {
    var username = document.getElementById("username").value;

    var host = document.location.host;
    var pathname = document.location.pathname;

    ws = new WebSocket("ws://" +host  + pathname + "chat/" + username);

    ws.onmessage = function(event) {
        var log = document.getElementById("log");
        console.log(event.data);
        var message = JSON.parse(event.data);
        log.innerHTML += message.from + " : " + message.content + "\n";
    };
}

function getAllUsers() {
    // var username = document.getElementById("username").value;

    // var markers = {'name': document.getElementById('newName').value,
    //     'password': document.getElementById('newPsw').value};
    var xhr = new XMLHttpRequest();
    var url = "http://localhost:8080/prattle/rest/user/getAll";
    xhr.open("GET", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
// xhr.onreadystatechange = function () {
//     if (xhr.readyState === 4 && xhr.status === 200) {
//         var json = JSON.parse(xhr.responseText);
//         console.log(json.name + ", " + json.password);
//     }
// };
//     var data = JSON.stringify(markers);
    xhr.send();
    console.log("????");
    console.log(xhr.response);
}

function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
                                  "content":content,
                                  "to":document.getElementById('to').value
                              });

    ws.send(json);
}