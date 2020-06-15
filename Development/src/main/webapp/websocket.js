let ws;


/**
 * Create a new user
 */
async function createUser (){
    let markers = {'name': document.getElementById('newName').value,
        'password': document.getElementById('newPsw').value};
    const response = await fetch('http://localhost:8080/java-websocket/rest/user/create', {
        method: 'POST',
        body: JSON.stringify(markers),
        headers: {
            'content-type': 'application/json'
        }
    }).then(response => response.json());
    console.log(response);
}

/**
 * get list of all users.
 */
async function getAllUsers (){
    const response = await fetch('http://localhost:8080/java-websocket/rest/user/getAll', {
        method: 'GET',
        headers: {
            'content-type': 'application/json'
        }
    }).then(response => response.json());
    console.log(response);
}

/**
 * Log in a user
 */
async function connect() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let markers = {'name': username, 'password': password};

    const response = await fetch('http://localhost:8080/java-websocket/rest/user/login', {
        method: 'POST',
        body: JSON.stringify(markers),
        headers: {
            'content-type': 'application/json'
        }
    }).then(response => response.json());
    console.log(response);

    if(response !== null) {
        let host = document.location.host;
        let pathname = document.location.pathname;

        ws = new WebSocket("ws://" +host  + pathname + "chat/" + username);

        ws.onmessage = function(event) {
            var log = document.getElementById("log");
            console.log(event.data);
            var message = JSON.parse(event.data);
            log.innerHTML += message.from + " : " + message.content + "\n";
        };
    }
    else {
        console.log("Log in failed.")
    }
}












/**
 * Send message.
 */
function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
                                  "content":content,
                                  "to":document.getElementById('to').value
                              });
    ws.send(json);
}