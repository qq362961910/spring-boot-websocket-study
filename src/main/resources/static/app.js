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
        stompClient.subscribe('/topic/p2p', function (result) {
            alertContent(result.body);
        });
        stompClient.subscribe('/topic/greetings', function (result) {
            appendContent(JSON.parse(result.body).content);
        });
        stompClient.subscribe('/user/topic/echo', function (result) {
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
});
