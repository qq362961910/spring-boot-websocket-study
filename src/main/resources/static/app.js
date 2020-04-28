var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

//参考地址: https://github.com/sockjs/sockjs-client
function connect() {
    var options = {
        transports: ["websocket"]
    };
    var socket = new SockJS('/websocket', null, options);
    stompClient = Stomp.over(socket);
    stompClient.debug = null;
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/topic/p2p', function (result) {
            dealP2pMsg(result.body);
        });
        stompClient.subscribe('/topic/broadcast', function (result) {
            dealBroadcastMsg(result.body);
        });
    }, function(err) {
        console.log(err);
    });
}

function dealP2pMsg(msg) {
    appendContent(msg);
}

function dealBroadcastMsg(msg) {
    appendContent(msg);
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}
function echo() {
    stompClient.send("/app/echo", {}, $("#echo-content").val());
}
function broadcastMsg(){
    stompClient.send("/app/chat/broadcast", {}, JSON.stringify({'content': $("#broadcast").val()}));
}
function needAuthMessage() {
    stompClient.send("/app/auth/need_login", {}, $("#need-auth").val());
}
function noNeedAuthMessage() {
    stompClient.send("/app/auth/no_need_login", {}, $("#no-need-auth").val());
}


function appendContent(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}
function showCurrentTime(time) {
    $("#current-time").text(time);
}
function alertContent(message) {
    window.alert(message);
}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#echo-send" ).click(function() { echo(); });
    $( "#broad-send" ).click(function() { broadcastMsg(); });
    $( "#need-auth-btn" ).click(function() { needAuthMessage(); });
    $( "#no-need-auth-btn" ).click(function() { noNeedAuthMessage(); });
});
