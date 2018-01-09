
$(document).ready(function() {
    $('#infoLogBar').hide();

    var supplyCardCount = 0;
    var roleCardCount = 0;
    var gameSocket = new WebSocket($('#gameWebSocket').val());

    gameSocket.onopen = function() {
        sendWebSockedStatusToInfoLog(gameSocket, 'socket open', true);
    }

    gameSocket.onclose = function() {
        sendWebSockedStatusToInfoLog(gameSocket, 'socket closed', true);
    }

    gameSocket.onmessage = function(event) {
        sendWebSockedStatusToInfoLog(gameSocket, 'message received from socket');
        var cardsResult = JSON.parse(event.data);
        if (cardsResult.cardType == 'role') {
            displayRoleCard(cardsResult.cards);
        } else if (cardsResult.cardType == 'supply') {
            displaySupplyCard(cardsResult.cards);
        }
    }

    $('#drawCard').click(function() {
        gameSocket.send("drawCard");
    });

    $('#buildRoleCards').click(function() {
        gameSocket.send("buildRoleCards");
    });

    $('#infoLogBtn').click(function() {
        $('#infoLogBar').fadeIn();
    });

    $('#infoLogHideBtn').click(function() {
        $('#infoLogBar').fadeOut();
    });

    function displaySupplyCard(supplyCards) {
        supplyCards.forEach(function(supplyCard) {
            var divNewCol = $('<div>', { class: 'col-2' });
            var divNewCard = $('<div>', { id: ('supplyCard' + (++supplyCardCount)), class: 'card text-center text-white bg-warning', style: 'width: 12rem;' });
            var divNewCardHeader = $('<div>', { class: 'card-header' }).text(supplyCard.name);
            var divCardBody = $('<div>', { class: 'card-body' })
                .append($('<b>', { class: 'card-title' }).text(supplyCard.abilityPhase))
                .append($('<p>', { class: 'card-text' }).text(supplyCard.ability));
            var divCardFooter = $('<div>', { class: 'card-footer' })
                .append($('<small>', { class: 'text-muted float-left' }).text('Cost: ' + supplyCard.cost + ' card(s)'))
                .append($('<small>', { class: 'text-muted float-right' }).text(supplyCard.victoryPoints + ' VP(s)'));
            var divNewCardComplete = divNewCol.append(divNewCard.append(divNewCardHeader).append(divCardBody).append(divCardFooter));
            var playerCardsDiv = $('#playerCards');
            playerCardsDiv.append(divNewCardComplete);
        });
    }

    function displayRoleCard(roleCards) {
        roleCards.forEach(function(roleCard) {
            var divNewCol = $('<div>', { class: 'col-2' });
            var divNewCard = $('<div>', { id: ('roleCard' + (++roleCardCount)), class: 'card text-center text-white bg-info', style: 'width: 12rem;' });
            var divNewCardHeader = $('<div>', { class: 'card-header' }).text(roleCard.name);
            var divCardBody = $('<div>', { class: 'card-body' }).text('action: ' + roleCard.action);
            var divCardFooter = $('<div>', { class: 'card-footer' }).text('privilege: ' + roleCard.privilege);
            var divNewCardComplete = divNewCol.append(divNewCard.append(divNewCardHeader).append(divCardBody).append(divCardFooter));
            var roleCardsDiv = $('#roleCards');
            roleCardsDiv.append(divNewCardComplete);
        });
    }

    function sendWebSockedStatusToInfoLog(socket, message, isImportant) {
        var currentTime = new Date();
        var currentTimeHMS = '[' + currentTime.getHours() + ':' + currentTime.getMinutes() + ':' + currentTime.getSeconds() + ' ' + message + '] ';
        if (isImportant) {
            $('#infoLogMessage').append($('<span>').append(currentTimeHMS + socket.url).css('font-weight', 'bold'));
        } else {
            $('#infoLogMessage').append($('<span>').append(currentTimeHMS + socket.url));
        }
        $('#infoLogMessage').append('<br>');
    }

});
