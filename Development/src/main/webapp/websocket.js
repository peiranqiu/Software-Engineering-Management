const URL = 'http://localhost:8080/java-websocket/rest/';

let ws;
let currentUser;
let currentGroup;

/**
 * Create a new user
 */
async function createUser() {
    let markers = {
        'name': document.getElementById('newName').value,
        'password': document.getElementById('newPsw').value
    };
    const response = await fetch(URL + 'user/create',
        {
            method: 'POST',
            body: JSON.stringify(markers),
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    console.log(response);
}

/**
 * Government watches a user
 */
async function watchUser() {
    //userId to be changed to a user to watch
    let userId = currentUser.userId;
    const response = await fetch(URL + 'user/' + userId + '/watch',
        {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    console.log(response);
}

/**
 * get list of all users.
 */
async function getAllUsers() {
    const response = await fetch(URL + 'user/getAllUser',
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    console.log(response);

    let select = document.getElementById('to');
    response.forEach((user) => {
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

    const response = await fetch(URL + 'user/login',
        {
            method: 'POST',
            body: JSON.stringify(markers),
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    console.log(response);

    if (response !== null) {
        currentUser = response;
        let host = document.location.host;
        let pathname = document.location.pathname;

        ws = new WebSocket("ws://" + host + pathname + "chat/" + username);

        ws.onmessage = function (event) {
            var log = document.getElementById("log");
            console.log(event.data);
            var message = JSON.parse(event.data);
            log.innerHTML += message.from + " : " + message.content + "\n";
        };
    } else {
        console.log("Log in failed.")
    }
}

/**
 * Send point to point message.
 */
function send() {
    let content = document.getElementById("msg").value;
    let json = JSON.stringify(
        {
            "content": content,
            "to": document.getElementById('to').value
        });
    ws.send(json);
}

/**
 * Send group message
 */
function sendGroup() {
    let content = document.getElementById("groupMsg").value;
    let json = JSON.stringify(
        {
            "content": content,
            "to": document.getElementById('toGroup').value,
            "sendToGroup": true
        });
    ws.send(json);
}

/**
 * a user get list of followers.
 */
async function userGetFollower(evt) {
    console.log(currentUser);
    const response = await fetch(URL + 'user/' + currentUser.userId + '/getFollower',
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        })
        .then(rs => rs.json());
    console.log(response);
    let list = generateList(response, "getFollowers");

    openTab(evt, "Followers", list);
}

function generateList(response, operatoin) {
    clearList("subGroupContent");
    clearList("groupFollowers");
    clearList("groupMembers");
    clearList("groupModerators");
    let list = document.createElement('ul');
    // list.style.listStyleType = 'none';
    list.id = operatoin + '-list';

    response.forEach((u) => {
        let userRow = document.createElement('li');
        let user = document.createElement("span");
        user.classList.add("list-text");
        user.innerText = u.name;
        userRow.appendChild(user);

        let follow = document.createElement("span");
        follow.classList.add('list-follow');
        if (operatoin === 'getFollowers') {
            follow.innerText = "+";
            follow.addEventListener('click', (event) => {
                followUser(u.userId);
            });
            user.addEventListener('click', (event) => {
                document.getElementById('to').value = event.target.innerHTML;

            });
        } else if (operatoin === 'getFollowees') {
            follow.innerText = "-";
            follow.addEventListener('click', (event) => {
                unfollowUser(u.userId);
            });
            user.addEventListener('click', (event) => {
                document.getElementById('to').value = event.target.innerHTML;

            });
        } else if (operatoin === 'getGroups') {
            user.addEventListener('click', (event) => {
                document.getElementById('toGroup').value = event.target.innerHTML;

            });
        } else if (operatoin === 'getAllGroups') {
            follow.innerText = "+";
            follow.addEventListener('click', (event) => {
                followGroup(u.groupId);
            });
            user.addEventListener('click', (event) => {
                getSubGroups(u.groupId);
                getGroupFollowers(u.groupId);
                getGroupModerators(u.groupId);
                getGroupMembers(u.groupId);

            })
        } else if (operatoin === 'getFollowingGroups') {
            follow.innerText = "-";
            follow.addEventListener('click', (event) => {
                unfollowGroup(u.groupId);
            });
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
    console.log(currentUser);
    const response = await fetch(URL + 'user/' + currentUser.userId + '/getFollowee',
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        })
        .then(rs => rs.json());
    console.log(response);

    let list = generateList(response, 'getFollowees');

    openTab(evt, "Followees", list);
}

/**
 * get list of groups the user is in.
 */
async function getHasGroup(evt) {
    console.log(currentUser);
    const response = await fetch(URL + 'user/' + currentUser.userId + '/getHasGroup',
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        })
        .then(rs => rs.json());
    console.log(response);

    let list = generateList(response, 'getGroups');

    openTab(evt, "Groups", list);
}

/**
 * get list of groups the user is following.
 */
async function getFollowedGroup(evt) {
    console.log(currentUser);
    const response = await fetch(URL + 'user/' + currentUser.userId + '/getFollowedGroup',
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        })
        .then(rs => rs.json());
    console.log(response);

    let list = generateList(response, 'getFollowingGroups');

    openTab(evt, "FollowingGroups", list);
}

/**
 * follow a user.
 */
async function followUser(followeeId) {
    console.log(currentUser);

    let followerId = currentUser.userId;
    // followeeId to be replaced according to your frontend elements!!!
    // let followeeId = 1;

    const response = await fetch(URL + 'user/' + followerId + '/follow/' + followeeId,
        {
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
    const response = await fetch(URL + 'user/' + followerId + '/unfollow/' + followeeId,
        {
            method: 'DELETE',
            headers: {
                'content-type': 'application/json'
            }
        });
    console.log(response);
}

/**
 * unfollow a group.
 */
async function unfollowGroup(id) {
    console.log(currentUser);
    let userId = currentUser.userId;
    // groupId to be replaced according to your frontend elements!!!
    const response = await fetch(URL + 'group/' + userId + '/unfollow/' + id,
        {
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
async function createInvitation(groupId, inviteeId) {
    const response = await fetch(URL + 'invitation/' + groupId + '/add/' + inviteeId,
        {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            }
        });
    console.log(response);
}


/**
 * approve an invitation.
 */
async function approveInvitation(groupId, inviteeId) {
    const response = await fetch(URL + 'invitation/' + groupId + '/approve/' + inviteeId,
        {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            }
        });
    console.log(response);
}

/**
 * delete/disapprove an invitation.
 */
async function deleteInvitation(groupId, inviteeId) {
    const response = await fetch(URL + 'invitation/' + groupId + '/delete/' + inviteeId,
        {
            method: 'DELETE',
            headers: {
                'content-type': 'application/json'
            }
        });
    console.log(response);
}

/**
 * Get all invitations associated with the group
 */
async function getGroupInvitations(groupId) {
    const response = await fetch(URL + 'invitation/' + groupId,
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    console.log(response);
}

/**
 * add a group moderator.
 */
async function addGroupModerator() {
    // console.log(currentUser);
    // console.log(currentGroup);
    // group, user to be completed according to your frontend elements!!!

    const response = await fetch(
        URL + 'group/' + currentUser.userId + '/moderate/' + currentGroup.groupId,
        {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            }
        })
        .then(rs => rs.json());
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
    const response = await fetch(URL + 'group/moderator/delete',
        {
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
    const response = await fetch(
        URL + 'group/' + currentUser.userId + '/member/' + currentGroup.groupId,
        {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            }
        })
        .then(rs => rs.json());
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
    const response = await fetch(URL + 'group/member/delete',
        {
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
    if (cur.childNodes.length === 0) {
        cur.appendChild(content);

    } else {
        cur.replaceChild(content, cur.childNodes[0]);
    }
    cur.style.display = "block";
}

/**
 * get list of all groups.
 */
async function getAllGroups(event) {
    const response = await fetch(URL + 'group/getAllGroups',
        {
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
async function createGroup() {
    let markers = {'name': document.getElementById('groupName').value};
    const response = await fetch(URL + 'group/create',
        {
            method: 'POST',
            body: JSON.stringify(markers),
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    console.log(response);
    console.log(markers.name);

    const response2 = await fetch(URL + 'group/' + markers.name,
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    console.log(response2);

    if (response2) {
        currentGroup = response2.value;
    }
    addGroupMember();
    addGroupModerator();

}

/**
 * Get sub group list based on groupId.
 *
 */

async function getSubGroups(groupId) {
    const response = await fetch(URL + 'group/' + groupId + '/getSubGroups',
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        })
        .then(rs => rs.json());
    let list = document.createElement('ul');
    list.id = 'subGroup-list';
    let title = document.createElement('h3');
    title.innerText = "Sub-Group List:";
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
    if (cur.childNodes.length === 0) {
        cur.appendChild(list);

    } else {
        cur.replaceChild(list, cur.childNodes[0]);
    }
    cur.style.display = "block";
    cur.style.backgroundColor = "Gainsboro"
}

async function clearList(className) {
    let tabcontent = document.getElementsByClassName(className);
    for (let i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

}

async function setGroupPassword() {
    let groupName = document.getElementById('groupName2').value;
    let password = document.getElementById("groupPass").value;

    const response2 = await fetch(URL + 'group/' + groupName,
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    console.log(response2);

    if (response2) {
        currentGroup = response2.value;
    }

    const response = await fetch(URL + 'group/' + currentGroup.groupId + '/password',
        {
            method: 'POST',
            body: JSON.stringify(
                password),
            headers: {
                'content-type': 'application/json'
            }
        })
        .then(rs => rs.json());
    console.log(response);
}

/**
 * Get sub group list based on groupId.
 *
 */

async function getGroupFollowers(groupId) {
    const response = await fetch(URL + 'group/' + groupId + '/followers',
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    let list = document.createElement('ul');
    list.id = 'subGroup-list';
    let title = document.createElement('h3');
    title.innerText = "Group Followers List:";
    list.appendChild(title);

    response.forEach(i => {

        let subGroupRow = document.createElement('div');
        let subGroup = document.createElement("p");
        subGroupRow.classList.add("panel");
        subGroup.innerText = i.name;
        subGroupRow.appendChild(subGroup);
        list.appendChild(subGroupRow);
    });
    clearList("groupFollowers");
    let cur = document.getElementById("Group Followers");
    if (cur.childNodes.length === 0) {
        cur.appendChild(list);

    } else {
        cur.replaceChild(list, cur.childNodes[0]);
    }
    cur.style.display = "block";
    cur.style.backgroundColor = "Gainsboro"
}

/**
 *follow a group
 */
async function followGroup(groupId) {
    const response = await fetch(URL + 'group/' + currentUser.userId + '/follow/' + groupId,
        {
            method: 'POST',
            body: JSON.stringify(
                password),
            headers: {
                'content-type': 'application/json'
            }
        })
        .then(rs => rs.json());
    console.log(response);

}

/**
 * add subGroup into superGroup
 */
async function addSubGroup() {
    let SuperGroupName = document.getElementById("SuperGroupName").value;
    let SubGroupName = document.getElementById("SubGroupName").value;
    console.log(SubGroupName);
    console.log(SuperGroupName);
    const response1 = await fetch(URL + 'group/' + SuperGroupName,
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());

    const response2 = await fetch(URL + 'group/' + SubGroupName,
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());

    const response = await fetch(
        URL + 'group/' + response1.value.groupId + '/add/' + response2.value.groupId,
        {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            }
        })
        .then(rs => rs.json());
    console.log(response);
}

/**
 * find Group by name
 */
async function findGroupByName(name) {
    const response = await fetch(URL + 'group/' + name,
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    console.log(response);
}

/**
 * Get moderators based on groupId.
 *
 */

async function getGroupModerators(groupId) {
    const response = await fetch(URL + 'group/' + groupId + '/moderators',
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    let list = document.createElement('ul');
    list.id = 'subGroup-list';
    let title = document.createElement('h3');
    title.innerText = "Group Moderators List:";
    list.appendChild(title);

    response.forEach(i => {

        let subGroupRow = document.createElement('div');
        let subGroup = document.createElement("p");
        subGroupRow.classList.add("panel");
        subGroup.innerText = i.name;
        subGroupRow.appendChild(subGroup);
        list.appendChild(subGroupRow);
    });
    clearList("groupModerators");
    let cur = document.getElementById("Group Moderators");
    if (cur.childNodes.length === 0) {
        cur.appendChild(list);

    } else {
        cur.replaceChild(list, cur.childNodes[0]);
    }
    cur.style.display = "block";
    cur.style.backgroundColor = "Gainsboro"
}

/**
 * Get moderators based on groupId.
 *
 */

async function getGroupMembers(groupId) {
    const response = await fetch(URL + 'group/' + groupId + '/members',
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    let list = document.createElement('ul');
    list.id = 'subGroup-list';
    let title = document.createElement('h3');
    title.innerText = "Group Members List:";
    list.appendChild(title);

    response.forEach(i => {

        let subGroupRow = document.createElement('div');
        let subGroup = document.createElement("p");
        subGroupRow.classList.add("panel");
        subGroup.innerText = i.name;
        subGroupRow.appendChild(subGroup);
        list.appendChild(subGroupRow);
    });
    clearList("groupMembers");
    let cur = document.getElementById("Group Members");
    if (cur.childNodes.length === 0) {
        cur.appendChild(list);

    } else {
        cur.replaceChild(list, cur.childNodes[0]);
    }
    cur.style.display = "block";
    cur.style.backgroundColor = "Gainsboro"
}

async function translateMessage() {
    let toLanguage = document.getElementById('language').value;
    let parseXml;
// create xml parsing functions
// the next if/else block is from the following thread
    if (typeof window.DOMParser != "undefined") {
        parseXml = function (content) {
            return ( new window.DOMParser() ).parseFromString(content, "text/xml");
        };
    } else if (typeof window.ActiveXObject != "undefined" &&
               new window.ActiveXObject("Microsoft.XMLDOM")) {
        parseXml = function (content) {
            let xmlDoc = new window.ActiveXObject("Microsoft.XMLDOM");
            xmlDoc.async = "false";
            xmlDoc.loadXML(content);
            return xmlDoc;
        };
    } else {
        throw new Error("No XML parser found");
    }
    const data = encodeURI(document.getElementById('messageToTranslate').value);
    fetch(
        "https://microsoft-azure-translation-v1.p.rapidapi.com/translate?from=en&to=" + toLanguage
        + "&text=" + data,
        {
            "method": "GET",
            "headers": {
                "x-rapidapi-host": "microsoft-azure-translation-v1.p.rapidapi.com",
                "x-rapidapi-key": "b496f70aa3mshd47b6501eee64dep117676jsn1e1d922eb551",
            }
        })
        .then(response => response.text())
        .then(text => {
            let document = parseXml(text);
            let stringElementArray = document.getElementsByTagName('string');
            console.log(stringElementArray[0].innerHTML);
            updateTranslationOutput(stringElementArray[0].innerHTML)
        })
        .catch(err => {
            console.log(err);
        });
}

function updateTranslationOutput(translatedMessage) {
    document.getElementById("translated").value = translatedMessage;
}

async function getLanguageAPI() {
    let select = document.getElementById('language');
    const languages = [
        {value: "zh-Hans", text: "Chinese Simplified"},
        {value: "da", text: "Danish"},
        {value: "fr", text: "French"},
        {value: "de", text: "German"},
        {value: "it", text: "Italian"},
        {value: "ja", text: "Japanese"},
        {value: "ko", text: "Korean"},
        {value: "pl", text: "Polish"},
        {value: "ru", text: "Russian"},
        {value: "ru", text: "Russian"},
        {value: "es", text: "Spanish"},];
    languages.forEach((language) => {
        let option = document.createElement("option");
        option.value = language.value;
        option.text = language.text;
        select.appendChild(option);
    })
}










