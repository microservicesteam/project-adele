var stompClient = null;

function connect() {
    var currentSector = getCurrentSector();
    var topic = "/topic/sectors/" + currentSector + "/reservations";
    var socket = new WebSocket("ws://localhost:8080/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe(topic, function (ticketEvent) {
            refreshTicketStatusTable(JSON.parse(ticketEvent.body));
            showEvent(ticketEvent.body);
        });
    });
    showEvent("--- Connected to " + topic);
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
    showEvent("--- Disconnected");
}

function reservePositions() {
    var currentSector = getCurrentSector();
    var $seats = $("#seats");
    var request = {};
    request.positions = [];
    $.each($seats.val().split(","), function(i, seat) {
        request.positions[i] = {}
        request.positions[i].eventId = "1";
        request.positions[i].sectorId = currentSector;
        request.positions[i].seatId = seat;
    });
    $.ajax({
      type: "POST",
      url: "/reservations",
      data: JSON.stringify(request),
      success: function(data) {console.log("Response: " + JSON.stringify(data)); $seats.val('');},
      dataType: "json",
      contentType: "application/json;charset=UTF-8"
    });
}

function showEvent(eventText) {
    $("#conversation").append("<p>" + eventText + "</p>");
}

function refreshTicketStatusTable(event) {
    var status;
    switch (event.type) {
        case "ReservationAccepted":
            status = "RESERVED";
            break;
        case "ReservationCancelled":
            status = "FREE";
            break;
        case "ReservationRejected":
            return; //nothing has to be changed
        default:
            status = "UNKNOWN";
    }
    $.each(event.reservation.positions, function(i, position) {
        $("#p-" + position.seatId + " > td:nth-child(3)").text(status);
    });
}

function getTickets() {
    $("#map").empty();
    var currentSector = getCurrentSector();
    $.get("/tickets?eventId=1&sectorId=" + currentSector, function(data) {
        $.each(data, function(i, obj){
            $("#map").append("<tr id=\"p-" + obj.position.seatId + "\"><td>" + obj.position.sectorId + "</td><td>" + obj.position.seatId + "</td><td>" + obj.status + "</td></tr>");
        });
    })
}

function getCurrentSector() {
    return parseInt($("#sectorId").text());
}

function updateSectorId(newSectorId) {
    $("#sectorId").text(newSectorId);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#reserve").click(function() { reservePositions(); });
    $(".sectors button").click(function() {
        var newSectorId = $(this).data("sector-id");
        updateSectorId(newSectorId);
        getTickets();
        disconnect(); //disconnect from old sector topic
        connect(); //connect to new sector topic
    });
    getTickets();
    connect();
});

