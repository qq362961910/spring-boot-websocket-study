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

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/topic/error', function (result) {
            alertContent(result.body);
        });
        stompClient.subscribe('/topic/hello', function (result) {
            appendContent(JSON.parse(result.body).content);
        });
        stompClient.subscribe('/user/topic/echo', function (result) {
            appendContent(result.body);
        });
        stompClient.subscribe('/user/topic/auth/need_login', function (result) {
            appendContent(result.body);
        });
        stompClient.subscribe('/user/topic/auth/no_need_login', function (result) {
            appendContent(result.body);
        });
        stompClient.subscribe('/topic/chat/broadcast', function (result) {
            appendContent(result.body);
        });
        stompClient.subscribe('/topic/sync/time', function (result) {
            showCurrentTime(result.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'username': $("#name").val()}));
}

function echo() {
    stompClient.send("/app/echo", {}, $("#echo-content").val());
}
function broadcastMsg(){
    stompClient.send("/app/chat/broadcast", {}, $("#chat-broadcast").val());
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
    $( "#name-send" ).click(function() { sendName(); });
    $( "#echo-send" ).click(function() { echo(); });
    $( "#broad-send" ).click(function() { broadcastMsg(); });
    $( "#need-auth-btn" ).click(function() { needAuthMessage(); });
    $( "#no-need-auth-btn" ).click(function() { noNeedAuthMessage(); });
});
