const URL = 'http://cs5500team4.us-east-1.elasticbeanstalk.com/rest/';

let ws;
let currentUser;
let currentGroup;
let moderators;
let allUsers = [];
let members;

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
    window.open(URL + 'user/' + document.getElementById("watch").value + '/watch',
        '_blank', 'location=yes,height=400,width=600,scrollbars=yes,status=yes');
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
    //let invitee = document.getElementById('invitee');
    response.forEach((user) => {
        allUsers.push(user);
        let option = document.createElement("option");
        option.value = user.name;
        option.text = user.name;
        select.appendChild(option);
        //invitee.appendChild(option);
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

        ws.onmessage = async function (event) {
            var log = document.getElementById("log");
            console.log(event.data);
            var message = JSON.parse(event.data);
            log.innerHTML += message.from + " : " + message.content + "\n";

            let content = document.getElementById("msg").value;
            let json = JSON.stringify({
                                          "from": currentUser.name,
                                          "content": content,
                                          "to": document.getElementById('to').value
                                      });
            console.log(json);

            const response0 = await fetch(URL + 'user/send', {

                                                                 method: 'POST',
                                                                 body: JSON.stringify(message),
                                                                 headers: {
                                                                     'content-type': 'application/json'
                                                                 }
                                                             }).then(rs => rs.json());

            await optionsPersonChat();
            await optionsGroupChat();
            console.log(response0);
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
 * Send point to point message for emoji.
 */
function addEmoji() {
    let msg = document.getElementById("msg").value;
    document.getElementById("msg").value = msg + document.getElementById("emoji").value;
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
    clearList("invitations");
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

            user.addEventListener('click', async function (event) {
                currentGroup = u;
                document.getElementById('toGroup').value = event.target.innerHTML;
                await getSubGroups(u.groupId)
                    .then(() => getGroupFollowers(u.groupId))
                    .then(() => (getGroupModerators(u.groupId)))
                    .then(() => getGroupMembers(u.groupId))
                    .then(() => getGroupInvitations(u.groupId));

                // if current user is in group moderator list, then get group invitations

            });
        } else if (operatoin === 'getAllGroups') {
            follow.innerText = "+";
            follow.addEventListener('click', (event) => {
                followGroup(u.groupId);
            });
            user.addEventListener('click', async function () {
                currentGroup = u;
                await getSubGroups(u.groupId)
                    .then(() => getGroupFollowers(u.groupId))
                    .then(() => (getGroupModerators(u.groupId)))
                    .then(() => getGroupMembers(u.groupId))
                    .then(() => getGroupInvitations(u.groupId));

            });
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
    const response = await fetch(URL + 'group/' + groupId + '/createInvitation/' + inviteeId,
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
    const response = await fetch(URL + 'group/' + groupId + '/approveInvitation/' + inviteeId,
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
    const response = await fetch(URL + 'group/' + groupId + '/deleteInvitation/' + inviteeId,
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
    const response = await fetch(URL + 'group/' + groupId + '/getAllInvitation',
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    console.log(response);

    let list = document.createElement('ul');
    list.id = 'invitation-list';
    let title = document.createElement('h3');
    title.innerText = "Invitations";
    list.appendChild(title);
    //debugger;

    response.forEach(i => {

        let invitationRow = document.createElement('div');
        let invitation = document.createElement("p");
        invitationRow.classList.add("panel");
        let ok = document.createElement("button");
        let cancel = document.createElement('button');
        ok.innerText = "OK";
        ok.addEventListener('click', () => {
            console.log(invitation.innerText);
            list.removeChild(invitationRow);
            approveInvitation(groupId, i.userId);
            // deleteInvitation(groupId, i.userId);
        });
        cancel.innerText = "Cancel";
        cancel.addEventListener('click', () => {
            list.removeChild(invitationRow);
            deleteInvitation(groupId, i.userId);
        });
        invitation.innerText = i.name;
        invitationRow.appendChild(invitation);
        invitationRow.appendChild(ok);
        invitationRow.appendChild(cancel);
        list.appendChild(invitationRow);
    });
    clearList("invitations");
    let cur = document.getElementById("Invitations");
    if (cur.childNodes.length === 0) {
        cur.appendChild(list);

    } else {
        cur.replaceChild(list, cur.childNodes[0]);
    }

    if (moderators.includes(currentUser.name)) {
        cur.style.display = "block";
        cur.style.backgroundColor = "Gainsboro"
    }
    else {
        cur.style.display = "none";
    }
}

/**
 * add a group moderator.
 */
async function addGroupModerator(userId) {
    const response = await fetch(
        URL + 'group/' + userId + '/moderate/' + currentGroup.groupId,
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
async function deleteGroupModerator(userId, groupId) {
    const response = await fetch(URL + 'group/' + groupId + '/deleteModerator/' + userId,
        {
            method: 'DELETE',
            headers: {
                'content-type': 'application/json'
            }
        });
    console.log(response);
}

/**
 * add a group member.
 */
async function addGroupMember(userId) {
    const response = await fetch(
        URL + 'group/' + userId + '/member/' + currentGroup.groupId,
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
async function deleteGroupMember(userId, groupId) {
    const response = await fetch(URL + 'group/' + groupId + '/deleteMember/' + userId,
        {
            method: 'DELETE',
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
    addGroupModerator(currentUser.userId);
    addGroupMember(currentUser.userId);

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

/**
 * clear a render list in the front end by input className
 */
async function clearList(className) {
    let tabcontent = document.getElementsByClassName(className);
    for (let i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

}

/**
 * a method to set group password.
 */
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
    moderators = [];

    response.forEach(i => {
    <<<<<<<
        HEAD
        moderators.push(i.name);
    ======
        =
            moderators.push(i.name);
    >>>>>>>
        invitation
        let subGroupRow = document.createElement('div');
        let subGroup = document.createElement("p");
        subGroupRow.classList.add("panel");
        subGroup.innerText = i.name;
        subGroupRow.appendChild(subGroup);

        if (currentUser !== null && moderators.includes(currentUser.name) && (i.userId
                                                                              !== currentUser.userId)) {
            let remove = document.createElement("button");
            remove.innerText = 'DownGrade to Member';
            subGroupRow.appendChild(remove);
            remove.addEventListener('click', async (event) => {
                await deleteGroupModerator(i.userId, groupId)

            });
        }

        list.appendChild(subGroupRow);
    });

    clearList("groupModerators");
    let cur = document.getElementById("Group Moderators");
    cur.removeChild(cur.lastChild);
    if (cur.childNodes.length === 0) {
        cur.appendChild(list);

    } else {
        cur.replaceChild(list, cur.childNodes[0]);
    }

    if (currentUser !== null && moderators.includes(currentUser.name)) {
        let addModerator = document.createElement('div');
        let select = document.createElement('select');
        let btn = document.createElement('button');
        btn.innerText = 'Add';
        btn.addEventListener('click', async () => {
            // console.log("current group:" + currentGroup.name);
            // console.log("current group user:" + select.value);
            await addGroupModerator(select.value)
        });

        let placeholder = document.createElement('option');
        placeholder.hidden = true;
        placeholder.disabled = true;
        placeholder.selected = true;
        placeholder.value = "";

        select.append(placeholder);

        allUsers.forEach(user => {
            let option = document.createElement("option");
            option.value = user.userId;
            option.text = user.name;
            select.appendChild(option);
        });
        addModerator.append(select);
        addModerator.append(btn);

        cur.appendChild(addModerator);

    }

    cur.style.display = "block";
    cur.style.backgroundColor = "Gainsboro"
}

/**
 * Get members based on groupId.
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

    // let moderators = await fetch(URL + 'group/' + groupId + '/moderators',
    //                              {
    //                                  method: 'GET',
    //                                  headers: {
    //                                      'content-type': 'application/json'
    //                                  }
    //                              }).then(rs => rs.json());

    // moderators.forEach(i => {
    //                        if (i.userId === currentUser.userId) {
    //                            isModerator = true;
    //                        }
    //                    }
    // );
    //
    let isModerator = false;

    if (moderators.includes(currentUser.name)) {
        isModerator = true;
        // console.log("Is moderator!");
    }

    members = [];
    response.forEach(i => {
        members.push(i.name);

        let subGroupRow = document.createElement('div');
        let subGroup = document.createElement("p");
        subGroupRow.classList.add("panel");
        subGroup.innerText = i.name;
        subGroupRow.appendChild(subGroup);

        if (isModerator) {
            let remove = document.createElement("button");
            remove.innerText = 'delete';
            subGroupRow.appendChild(remove);
            remove.addEventListener('click', async (event) => {
                await deleteGroupModerator(i.userId, groupId)
                    .then(() => deleteGroupMember(i.userId, groupId))

            });
        }
        list.appendChild(subGroupRow);
    });

    let isMember = currentUser === null ? false : members.includes(currentUser.name);

    clearList("groupMembers");
    let cur = document.getElementById("Group Members");
    cur.innerHTML = "";
    if (cur.childNodes.length === 0) {
        cur.appendChild(list);

    } else {
        cur.replaceChild(list, cur.childNodes[0]);
    }

    let addMember = document.createElement('div');
    let select = document.createElement('select');
    let btn = document.createElement('button');
    if (isModerator) {
        btn.innerText = 'Add';
        btn.addEventListener('click', async () => {
            // console.log("current group:" + currentGroup.name);
            // console.log("current group user:" + select.value);
            await addGroupMember(select.value)
        });

    }
    else {
        btn.innerText = 'Invite';
        btn.addEventListener('click', async () => {
            await createInvitation(currentGroup.groupId, select.value)
        });
    }
    let placeholder = document.createElement('option');
    placeholder.hidden = true;
    placeholder.disabled = true;
    placeholder.selected = true;
    placeholder.value = "";

    select.append(placeholder);

    allUsers.forEach(user => {
        let option = document.createElement("option");
        option.value = user.userId;
        option.text = user.name;
        select.appendChild(option);
    });
    addMember.append(select);
    addMember.append(btn);

    // cur.removeChild(cur.lastChild);
    if (isMember || isModerator) {
        cur.appendChild(addMember);
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

/**
 * get list of all messages.
 */
async function getAllPrivateMessages() {
    const response = await fetch(URL + 'user/getAllMessages/' + currentUser.name + '/'
                                 + document.getElementById('toChatLog').value,
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    console.log(response);
    response.forEach(printMessage);
}

async function printMessage(item, index) {
    document.getElementById("personalChatLog").innerHTML +=
        item.from + ": " + item.messageText + "      $time: " + item.timeStamp;
    document.getElementById("personalChatLog").innerHTML += "&#13;&#10";
}

async function optionsPersonChat() {
    let select = document.getElementById('toChatLog');
    select.innerHTML = '';
    allUsers.forEach(user => {
        let option = document.createElement("option");
        option.value = user.name;
        option.text = user.name;
        select.appendChild(option);
    });

}

function fillWatched() {
    let select = document.getElementById("watch");
    allUsers.forEach((user) => {
        let option = document.createElement("option");
        option.value = user.userId;
        option.text = user.name;

        select.appendChild(option);
    })
}

/**
 * get list of all messages on current group.
 */
async function getAllGroupMessages() {
    const response = await fetch(URL + 'group/getAllGroupMessages/' + document.getElementById(
        'toGroupChatLog').value,
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        }).then(rs => rs.json());
    console.log(response);
    response.forEach(printGroupMessage);
}

async function printGroupMessage(item, index) {
    document.getElementById("groupChatLog").innerHTML +=
        item.from + ": " + item.messageText + "      $time: " + item.timeStamp;
    document.getElementById("groupChatLog").innerHTML += "&#13;&#10";
}

async function optionsGroupChat() {
    const response = await fetch(URL + 'user/' + currentUser.userId + '/getHasGroup',
        {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        })
        .then(rs => rs.json());
    console.log(response);

    let select = document.getElementById('toGroupChatLog');
    select.innerHTML = '';
    response.forEach((group) => {
        let option = document.createElement("option");
        option.value = group.groupId;
        option.text = group.name;
        select.appendChild(option);
    })
}

async function cleanPrivateChat() {
    console.log("cleaning call");
    document.getElementById("personalChatLog").innerHTML = "";
}

async function cleanGroupChat() {
    document.getElementById("groupChatLog").innerHTML = "";
}

async function getEmojiList() {
    var target = document.getElementById("emoji");
    var emojiCount = 20;

    for (var index = 0; index < emojiCount; index++) {
        addemoji(emoji[index]);
    }

    function addemoji(code) {
        var option = document.createElement('option');
        option.innerHTML = code;
        target.appendChild(option);
    }
}










