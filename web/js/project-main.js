function ContestsConfig() {}

// constants
ContestsConfig.CONTESTS_TYPE_ACTIVE = "active";
ContestsConfig.CONTESTS_TYPE_UPCOMING = "upcoming";
//ContestsConfig.CONTESTS_TYPE_ALL = "all";

ContestsConfig.COMPONENT_DEVELOPMENT = "cdev";

// global state
ContestsConfig.cdev_type = ContestsConfig.CONTESTS_TYPE_ACTIVE;

function activateCdevType(cdevType) {
    if (ContestsConfig.cdev_type == cdevType)
        return;
    ContestsConfig.cdev_type = cdevType;
    var dataTable = $('#cdevtable').dataTable();
    var show = true;
    if (cdevType == ContestsConfig.CONTESTS_TYPE_UPCOMING) {
        show = false;
    }
    dataTable.fnSetColumnVis( 4, show );
    dataTable.fnSetColumnVis( 5, show );
    dataTable.fnSetColumnVis( 6, show );
    dataTable.fnSetColumnVis( 7, show );
    dataTable.fnAdjustColumnSizing();
    dataTable.fnDraw();
}

function showDialog(title, text) {
//    $('#dialog').attr('title', title);
    $('#dialog').dialog( "option", "title", title );
    $('#dialog p').html(text);
    $('#dialog').dialog('open');
}

function subscribe() {
    var email = $('#subscriberMail').val();
    if (!validEmail(email)) {
        showDialog("Email validation failed", "You need to enter valid email to complete subscription.");
    } else {
        $.ajax({
            type: "POST",
            url: "/subscribe",
            data: "email=" + email,
            success: function(msg) {
                showDialog("Success", "Congratulations! Email " + email + " was successfully subscribed." +
                                     "<br>If you ever need to unsubscribe - just follow a link at the end of any email you'll get from this service.");
                $('#subscriberMail').val("");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                showDialog("Error", "Error occurred while processing a request:<br>" +
                                   xhr.statusText);
                $('#subscriberMail').val("");
            }
        });
    }
}

function validEmail(email) {
    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
    return reg.test(email);
}

$(document).ready(function() {

    $("#dialog").dialog({
        modal: true,
        buttons: {
            Ok: function() {
                $(this).dialog('close');
            }
        },
        autoOpen: false
    });

    $("button, input:submit").button();

    $("#tabs").tabs();

    $("#radio").buttonset();

    $('#cdevtable').dataTable({
        "bJQueryUI": true,
        "bServerSide": true,
        "sAjaxSource": "/contests/cdev",
        "bFilter": false,
        "aoColumns": [
            { "bSortable": false, "aTargets": [ 0 ] },
            { "bSortable": false, "aTargets": [ 1 ] },
            { "bSortable": false, "aTargets": [ 2 ] },
            { "bSortable": false, "aTargets": [ 3 ] },
            { "bSortable": false, "aTargets": [ 4 ] },
            { "bSortable": false, "aTargets": [ 5 ] },
            { "bSortable": false, "aTargets": [ 6 ] },
            { "bSortable": false, "aTargets": [ 7 ] }
        ],
        "fnServerData": function (sSource, aoData, fnCallback) {
            aoData.push({ "name": "type", "value": ContestsConfig.cdev_type });
            $.getJSON(sSource, aoData, function (json) {
                fnCallback(json)
            });
        },
        "bAutoWidth": false
    });

    var dataTable = $('#cdevtable').dataTable();
    dataTable.fnAdjustColumnSizing();
    dataTable.fnDraw();

});