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
}

function connect() {
    var socket = new WebSocket("ws://localhost:8080/ticketEvents");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/tickets', function (ticketEvent) {
            showEvent(JSON.parse(ticketEvent.body));
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showEvent(event) {
    $("#conversation").append("<tr><td>" + event.bookingId + "</td><td>" + event.eventId + "</td><td>" + event.positions[0].id + "</td></tr>");
}

$(function () {
    $("#disconnect").prop("disabled", "true");
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#start" ).click(function() { $.post('/rs/api/triggerTicketsBooked/start') });
    $( "#stop" ).click(function() { $.post('/rs/api/triggerTicketsBooked/stop') });
});

