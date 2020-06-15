
const URL = 'http://localhost:8080/java-websocket/rest/';

let ws;
let currentUser;


/**
 * Create a new user
 */
async function createUser (){
    let markers = {'name': document.getElementById('newName').value,
        'password': document.getElementById('newPsw').value};
    const response = await fetch(URL + 'user/create', {
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
    const response = await fetch(URL + 'user/getAllUser', {
        method: 'GET',
        headers: {
            'content-type': 'application/json'
        }
    }).then(response => response.json());
    console.log(response);

    let select = document.getElementById('to');
    response.forEach((user)=>{
        let option = document.createElement("option");
        option.value = user.name;
        option.text = user.name;
        select.appendChild(option);
    })
}

/**
 * Log in a user
 */
async function connect() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let markers = {'name': username, 'password': password};

    const response = await fetch(URL + 'user/login', {
        method: 'POST',
        body: JSON.stringify(markers),
        headers: {
            'content-type': 'application/json'
        }
    }).then(response => response.json());
    console.log(response);

    if(response !== null) {
        currentUser = response;
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
    let content = document.getElementById("msg").value;
    let json = JSON.stringify({
                                  "content":content,
                                  "to":document.getElementById('to').value
                              });
    ws.send(json);
}

/**
 * a user get list of followers.
 */
async function userGetFollower(evt) {
    console.log(currentUser);
    const response = await fetch(URL + 'user/' + currentUser.userId +'/getFollower', {
        method: 'GET',
        headers: {
            'content-type': 'application/json'
        }
    }).then(response => response.json());
    console.log(response);
    let list = generateList(response);

    openTab(evt, "Followers", list);
}

function generateList(response){
    let list = document.createElement('ul');
    response.forEach((u)=>{
        let user = document.createElement('li');
        user.innerText = u.name;
        list.appendChild(user);
    })
    return list;

}

/**
 * a user get list of followees.
 */
async function userGetFollowee() {
    console.log(currentUser);
    const response = await fetch(URL + 'user/' + currentUser.userId + '/getFollowee', {
        method: 'GET',
        headers: {
            'content-type': 'application/json'
        }
    }).then(response => response.json());
    console.log(response);
}

/**
 * get list of groups the user is in.
 */
async function getHasGroup() {
    console.log(currentUser);
    const response = await fetch(URL + 'user/' + currentUser.userId + '/getHasGroup', {
        method: 'GET',
        headers: {
            'content-type': 'application/json'
        }
    }).then(response => response.json());
    console.log(response);
}

/**
 * get list of groups the user is following.
 */
async function getFollowedGroup() {
    console.log(currentUser);
    const response = await fetch(URL + 'user/' + currentUser.userId + '/getFollowedGroup', {
        method: 'GET',
        headers: {
            'content-type': 'application/json'
        }
    }).then(response => response.json());
    console.log(response);
}


/**
 * follow a user.
 */
async function followUser() {
    console.log(currentUser);

    let followerId = currentUser.userId;
    // followeeId to be replaced according to your frontend elements!!!
    let followeeId = 1;

    const response = await fetch(URL + 'user/'+ followerId +'/follow/' + followeeId, {
        method: 'POST',
        headers: {
            'content-type': 'application/json'
        }
    });
    console.log(response);
}

/**
 * unfollow a user.
 */
async function unfollowUser() {
    console.log(currentUser);
    let followerId = currentUser.userId;
    // followeeId to be replaced according to your frontend elements!!!
    let followeeId = 1;
    const response = await fetch(URL + 'user/'+ followerId +'/unfollow/' + followeeId, {
        method: 'DELETE',
        headers: {
            'content-type': 'application/json'
        }
    });
    console.log(response);
}

/**
 * follow a group.
 */
async function followGroup() {
    console.log(currentUser);
    let userId = currentUser.userId;
    // groupId to be replaced according to your frontend elements!!!
    let groupId = null;
    const response = await fetch(URL + 'group/'+ userId +'/follow/' + groupId, {
        method: 'POST',
        headers: {
            'content-type': 'application/json'
        }
    });
    console.log(response);
}

/**
 * unfollow a group.
 */
async function unfollowGroup() {
    console.log(currentUser);
    let userId = currentUser.userId;
    // groupId to be replaced according to your frontend elements!!!
    let groupId = null;
    const response = await fetch(URL + 'group/'+ userId +'/unfollow/' + groupId, {
        method: 'DELETE',
        headers: {
            'content-type': 'application/json'
        }
    });
    console.log(response);
}

/**
 * create add member invitation.
 */
async function createAddInvitation() {
    console.log(currentUser);
    // group, invitee to be completed according to your frontend elements!!!
    let invitation= {
        'group': null,
        'inviter': currentUser,
        'invitee': null,
        'isInvite': true
    };
    const response = await fetch(URL + 'invitation/create', {
        method: 'POST',
        body: JSON.stringify(invitation),
        headers: {
            'content-type': 'application/json'
        }
    });
    console.log(response);
}

/**
 * create delete member request.
 */
async function createDeleteInvitation() {
    console.log(currentUser);
    // group, invitee to be completed according to your frontend elements!!!
    let invitation= {
        'group': null,
        'inviter': currentUser,
        'invitee': null,
        'isInvite': false
    };
    const response = await fetch(URL + 'invitation/delete', {
        method: 'DELETE',
        body: JSON.stringify(invitation),
        headers: {
            'content-type': 'application/json'
        }
    });
    console.log(response);
}


/**
 * approve an invitation.
 */
async function approveInvitation() {
    console.log(currentUser);
    // group, invitee, isInvite to be completed according to your frontend elements!!!
    let invitation= {
        'group': null,
        'inviter': currentUser,
        'invitee': null,
        'isInvite': false
    };
    const response = await fetch(URL + 'invitation/approve', {
        method: 'POST',
        body: JSON.stringify(invitation),
        headers: {
            'content-type': 'application/json'
        }
    });
    console.log(response);
}

/**
 * add a group moderator.
 */
async function addGroupModerator() {
    console.log(currentUser);
    // group, user to be completed according to your frontend elements!!!
    let markers = {
        'group': null,
        'moderator': currentUser,
        'user': null,
    };
    const response = await fetch(URL + 'group/moderator/add', {
        method: 'POST',
        body: JSON.stringify(markers),
        headers: {
            'content-type': 'application/json'
        }
    }).then(response => response.json());
    console.log(response);
}


/**
 * delete a group moderator.
 */
async function deleteGroupModerator() {
    console.log(currentUser);
    // group, user to be completed according to your frontend elements!!!
    let markers = {
        'group': null,
        'moderator': currentUser,
        'user': null,
    };
    const response = await fetch(URL + 'group/moderator/delete', {
        method: 'DELETE',
        body: JSON.stringify(markers),
        headers: {
            'content-type': 'application/json'
        }
    });
    console.log(response);
}



/**
 * add a group member.
 */
async function addGroupMember() {
    console.log(currentUser);
    // group, user to be completed according to your frontend elements!!!
    let markers = {
        'group': null,
        'moderator': currentUser,
        'user': null,
    };
    const response = await fetch(URL + 'group/member/add', {
        method: 'POST',
        body: JSON.stringify(markers),
        headers: {
            'content-type': 'application/json'
        }
    }).then(response => response.json());
    console.log(response);
}

/**
 * add a group member.
 */
async function deleteGroupMember() {
    console.log(currentUser);
    // group, user to be completed according to your frontend elements!!!
    let markers = {
        'group': null,
        'moderator': currentUser,
        'user': null,
    };
    const response = await fetch(URL + 'group/member/delete', {
        method: 'DELETE',
        body: JSON.stringify(markers),
        headers: {
            'content-type': 'application/json'
        }
    });
    console.log(response);
}

function openTab(evt, tabName, content) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    let cur = document.getElementById(tabName);


    evt.target.className += " active";
    if (cur.children.length === 0){
        console.log('empty');
        cur.appendChild(content);

    }
    cur.style.display = "block";
}