
$(document).ready(function() {
    $('#infoLogBar').hide();

    var handCardCount = 0;
    var buildingCardCount = 0;
    var roleCardCount = 0;

    $('#infoLogHideBtn').click(function() {
        $('#infoLogBar').fadeOut();
    });

    $('#createGameBtn').click(function() {
        $.ajax({
            type: 'GET',
            url: '/createGame'
        }).done(function(roleCardsAndPlayer) {
            displayHandOrBuildingCards(roleCardsAndPlayer.player.hand, 'hand');
            displayHandOrBuildingCards(roleCardsAndPlayer.player.buildings, 'building');
            displayRoleCard(roleCardsAndPlayer.roleCards)
        }).fail(function(error) {
            sendErrorToInfoLog(error);
            $('#infoLogBar').fadeIn();
        });
    });

    function displayHandOrBuildingCards(cards, handOrBuilding) {
        var cardCounterName = 'buildingCardCount';
        var cardCounterValue = buildingCardCount;
        var cardBGColour = 'bg-info';
        var cardDivName = 'playerBuildingsCards';
        if (handOrBuilding === 'hand') {
            cardCounterName = 'handCardCount';
            cardCounterValue = handCardCount;
            cardBGColour = 'bg-secondary';
            cardDivName = 'playerHandCards';
        }
        cards.forEach(function(card) {
            var divNewCol = $('<div>', { class: 'col-2' });
            var divNewCard = $('<div>', { id: (cardCounterName + (++cardCounterValue)), class: 'card text-center text-white ' + cardBGColour, style: 'width: 12rem;' });
            var divNewCardHeader = $('<div>', { class: 'card-header' }).text(card.name);
            var divCardBody = $('<div>', { class: 'card-body' })
                .append($('<b>', { class: 'card-title' }).text(card.abilityPhase))
                .append($('<p>', { class: 'card-text' }).text(card.ability));
            var divCardFooter = $('<div>', { class: 'card-footer' })
                .append($('<small>', { class: 'float-left' }).text('Cost: ' + card.cost + ' card(s)'))
                .append($('<small>', { class: 'float-right' }).text(card.victoryPoints + ' VP(s)'));
            var divNewCardComplete = divNewCol.append(divNewCard.append(divNewCardHeader).append(divCardBody).append(divCardFooter));
            var cardDiv = $('#' + cardDivName);
            cardDiv.append(divNewCardComplete);
        });
    }

    function displayRoleCard(roleCards) {
        roleCards.forEach(function(roleCard) {
            var divNewCol = $('<div>', { class: 'col-2' });
            var divNewCard = $('<div>', { id: ('roleCard' + (++roleCardCount)), class: 'card text-center text-white bg-warning small', style: 'width: 10rem;' });
            var divNewCardHeader = $('<div>', { class: 'card-header' }).text(roleCard.name);
            var divCardBody = $('<div>', { class: 'card-body' }).text('action: ' + roleCard.action);
            var divCardFooter = $('<div>', { class: 'card-footer' }).text('privilege: ' + roleCard.privilege);
            var divNewCardComplete = divNewCol.append(divNewCard.append(divNewCardHeader).append(divCardBody).append(divCardFooter));
            var roleCardsDiv = $('#roleCards');
            roleCardsDiv.append(divNewCardComplete);
        });
    }

    function sendErrorToInfoLog(message) {
        var currentTime = new Date();
        var currentTimeHMS = '[' + currentTime.getHours() + ':' + currentTime.getMinutes() + ':' + currentTime.getSeconds() + ' ' + message + '] ';
        $('#infoLogMessage').append($('<span>').append(currentTimeHMS));
        $('#infoLogMessage').append('<br>');
    }
});
