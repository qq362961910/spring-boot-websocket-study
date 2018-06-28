/**
 * Created by amen on 17-11-18.
 */
var stompClient = null;
function runCallback(data) {
    var message = JSON.parse(data.body);
    var opt = message.data.opt;
    if(opt) {
        try {
            var m = eval(opt + "Callback");
            if(typeof m === 'function') {
                m(message);
            }
        } catch(e) {}
    }
}
function connect() {
    var socket = new SockJS('http://api.summerpalace-china.com/socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        //广播消息
        stompClient.subscribe('/topic/broadcast', function (data) {
            console.info("receive a broadcast message");
            runCallback(data);
        });
        //点对点
        stompClient.subscribe('/user/topic/p2p', function (data) {
            console.info("receive a response message");
            runCallback(data);
        });
    });
}
function broadcast(user, message) {
    stompClient.send("/app/publishMessage", {}, JSON.stringify({'user': user, content: message}));
}
function broadcastCallback(result) {
    if (result.status === 1) {
        var message = result.data.message;
        if(message) {
            console.info("user: " + message.user + ", say: " + message.content);
        }
    }
    else {
        console.error("broadcast request failed");
    }
}
function echo(content) {
    stompClient.send("/app/echo", {}, content);
}
function echoCallback(result) {
    if (result.status === 1) {
        console.info("echo: " + result.data.message);
    }
    else {
        console.error("echo request failed");
    }
}
connect();