
const URL = 'http://localhost:8080/java-websocket/rest/';

let ws;
let currentUser;
let currentGroup;


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
    }).then(rs => rs.json());
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
    }).then(rs => rs.json());
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
    }).then(rs => rs.json());
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
 * Send point to point message.
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
 * Send group message
 */
function sendGroup(){
    let content = document.getElementById("groupMsg").value;
    let json = JSON.stringify({
                                  "content":content,
                                  "to":document.getElementById('toGroup').value,
                                    "sendToGroup": true
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
    }).then(rs => rs.json());
    console.log(response);
    let list = generateList(response, "getFollowers");

    openTab(evt, "Followers", list);
}

function generateList(response, operatoin){
    clearList("subGroupContent");
    let list = document.createElement('ul');
   // list.style.listStyleType = 'none';
    list.id = operatoin + '-list';

    response.forEach((u)=>{
        let userRow = document.createElement('li');
        let user = document.createElement("span");
        user.classList.add("list-text");
        user.innerText = u.name;
        userRow.appendChild(user);

        let follow = document.createElement("span");
        follow.classList.add('list-follow');
        if (operatoin === 'getFollowers'){
            follow.innerText = "+";
            follow.addEventListener('click', (event) => {
                followUser(u.userId);
            });
            user.addEventListener('click', (event)=>{
                document.getElementById('to').value = event.target.innerHTML;

            });
        }
        else if (operatoin === 'getFollowees'){
            follow.innerText = "-";
            follow.addEventListener('click', (event) => {
               unfollowUser(u.userId);
            });
            user.addEventListener('click', (event)=>{
                document.getElementById('to').value = event.target.innerHTML;

            });
        }
        else if (operatoin === 'getGroups'){
            user.addEventListener('click', (event)=>{
                document.getElementById('toGroup').value = event.target.innerHTML;

            });
        } else if (operatoin === 'getAllGroups') {
            user.addEventListener('click', (event) => {
                getSubGroups(u.groupId);

            })
        }

        userRow.appendChild(follow);

        list.appendChild(userRow);
    });
    return list;

}

/**
 * a user get list of followees.
 */
async function userGetFollowee(evt) {
    document.getElementsByClassName("subGroupContent").style.display='none';
    console.log(currentUser);
    const response = await fetch(URL + 'user/' + currentUser.userId + '/getFollowee', {
        method: 'GET',
        headers: {
            'content-type': 'application/json'
        }
    }).then(rs => rs.json());
    console.log(response);

    let list = generateList(response, 'getFollowees');

    openTab(evt, "Followees", list);
}

/**
 * get list of groups the user is in.
 */
async function getHasGroup(evt) {
    console.log(currentUser);
    const response = await fetch(URL + 'user/' + currentUser.userId + '/getHasGroup', {
        method: 'GET',
        headers: {
            'content-type': 'application/json'
        }
    }).then(rs => rs.json());
    console.log(response);

    let list = generateList(response, 'getGroups');

    openTab(evt, "Groups", list);
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
    }).then(rs => rs.json());
    console.log(response);
}


/**
 * follow a user.
 */
async function followUser(followeeId) {
    console.log(currentUser);

    let followerId = currentUser.userId;
    // followeeId to be replaced according to your frontend elements!!!
    // let followeeId = 1;

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
async function unfollowUser(followeeId) {
    console.log(currentUser);
    let followerId = currentUser.userId;
    // followeeId to be replaced according to your frontend elements!!!
    // let followeeId = 1;
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
    // console.log(currentUser);
    // console.log(currentGroup);
    // group, user to be completed according to your frontend elements!!!

    const response = await fetch(URL + 'group/'+currentUser.userId+'/moderate/'+currentGroup.groupId, {
        method: 'POST',
        headers: {
            'content-type': 'application/json'
        }
    }).then(rs => rs.json());
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
    // console.log(currentUser);
    // group, user to be completed according to your frontend elements!!!

    const response = await fetch(URL + 'group/'+currentUser.userId+'/member/'+currentGroup.groupId, {
        method: 'POST',
        headers: {
            'content-type': 'application/json'
        }
    }).then(rs => rs.json());
    console.log(response);
}

/**
 * delelte a group member.
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
    if (cur.childNodes.length === 0){
        cur.appendChild(content);

    }
    else {
    cur.replaceChild(content, cur.childNodes[0]);}
    cur.style.display = "block";
}

/**
 * get list of all groups.
 */
async function getAllGroups (event){
    const response = await fetch(URL + 'group/getAllGroups', {
        method: 'GET',
        headers: {
            'content-type': 'application/json'
        }
    }).then(rs => rs.json());
    console.log(response);
    let list = generateList(response, 'getAllGroups');

    openTab(event, "All Groups", list);
}


/**
 * Create a new group
 */
async function createGroup (){
    let markers = {'name': document.getElementById('groupName').value};
    const response = await fetch(URL + 'group/create', {
        method: 'POST',
        body: JSON.stringify(markers),
        headers: {
            'content-type': 'application/json'
        }
    }).then(rs => rs.json());
    console.log(markers.name);

    const response2 = await fetch(URL + 'group/'+markers.name, {
        method: 'GET',
        headers: {
            'content-type': 'application/json'
        }
    }).then(rs => rs.json());
    console.log(response2)

    if(response2){currentGroup=response2.value;}
    addGroupMember(response2);
    addGroupModerator(response2);

}

async function getSubGroups(groupId){
    const response = await fetch(URL + 'group/'+groupId+'/getSubGroups', {
        method: 'GET',
        headers: {
            'content-type': 'application/json'
        }
    }).then(rs => rs.json());
    let list = document.createElement('ul');
    list.id = 'subGroup-list';
    console.log(list);
    let title=document.createElement('h3');
    title.innerText="Sub-Group List:";
    list.appendChild(title);

    response.forEach(i => {

        let subGroupRow = document.createElement('div');
        let subGroup = document.createElement("p");
        subGroupRow.classList.add("panel");
        subGroup.innerText = i.name;
        subGroupRow.appendChild(subGroup);
        list.appendChild(subGroupRow);
    });
   clearList("subGroupContent");
    let cur = document.getElementById("Sub Groups");
    console.log(cur);
    if (cur.childNodes.length === 0) {
        cur.appendChild(list);

    } else {
        cur.replaceChild(list, cur.childNodes[0]);
    }
    cur.style.display = "block";
    cur.style.backgroundColor="Gainsboro"
}

async function clearList(className){
    tabcontent = document.getElementsByClassName(className);
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
}






