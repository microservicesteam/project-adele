var stompClient = null;

function connect() {
    var currentSector = getCurrentSector();
    var topic = "/topic/sectors/" + currentSector + "/tickets";
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

function bookTickets() {
    var currentSector = getCurrentSector();
    var $seats = $("#seats");
    $.ajax({
      type: "POST",
      url: "/bookings",
      data: "{\"eventId\":1,\"sectorId\":" + currentSector + ",\"positions\":[" + $seats.val() + "]}",
      success: function(data) {console.log("BookingId: " + JSON.stringify(data)); $seats.val('');},
      dataType: "json",
      contentType: "application/json;charset=UTF-8"
    });
}

function showEvent(eventText) {
    $("#conversation").append("<p>" + eventText + "</p>");
}

function refreshTicketStatusTable(event) {
    $.each(event.positions, function(i, obj) {
        $("#p-" + obj.id + " > td:nth-child(3)").text("BOOKED");
    });
}

function getTickets() {
    $("#map").empty();
    var currentSector = getCurrentSector();
    $.get("/bookings?eventId=1&sectorId=" + currentSector, function(data) {
        $.each(data, function(i, obj){
            $("#map").append("<tr id=\"p-" + obj.position.id + "\"><td>" + obj.position.sectorId + "</td><td>" + obj.position.id + "</td><td>" + obj.status + "</td></tr>");
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
    $("#book").click(function() { bookTickets(); });
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

