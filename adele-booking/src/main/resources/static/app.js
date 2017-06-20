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
    var socket = new WebSocket("ws://localhost:8080/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/tickets', function (ticketEvent) {
            refreshTicketStatusTable(JSON.parse(ticketEvent.body));
            showEvent(ticketEvent.body);
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

function bookTickets() {
    $.ajax({
      type: "POST",
      url: "/events/1/book-tickets",
      data: "{\"eventId\":1,\"sectorId\":1,\"positions\":[" + $("#seats").val() + "]}",
      success: function(data) {console.log("BookingId: " + JSON.stringify(data))},
      dataType: "json",
      contentType: "application/json;charset=UTF-8"
    });
}

function showEvent(eventText) {
    $("#conversation").append("<p>" + eventText + "</p>");
}

function refreshTicketStatusTable(event) {
    $.each(event.positions, function(i, obj) {
        $("#p-" + obj.id + " > td:nth-child(2)").text("BOOKED");
    });
}

function getTickets() {
    $("#map").empty();
    $.get("/events/1/tickets", function(data) {
        $.each(data, function(i, obj){
            $("#map").append("<tr id=\"p-" + obj.position.id + "\"><td>" + obj.position.id + "</td><td>" + obj.status + "</td></tr>");
        });
    })
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#refresh").click(function() { refreshTicketStatusTable(); });
    $("#book").click(function() { bookTickets(); });
    getTickets();
    connect();
});

