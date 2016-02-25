var $TABLE = $('#table');
var $BTN = $('#export-btn');
var $RESPONSE = $('#response');
var $AUTORELOAD = $('#auto-reload');
var $PRESSED = false;
var $SERVERTIME = $('#serverTime');
var $SERVERPUBLICIPS = $('#serverPublicIPs');
var $SERVERLOCALIPS = $('#serverLocalIPs');

$('.table-add').click(function () {
  var $clone = $TABLE.find('tr.hide').clone(true).removeClass('hide table-line');
  $TABLE.find('table').append($clone);
});

$('.table-remove').click(function () {
    console.log("remove");
    $(this).closest('td').siblings('.status').html('delete');
    if ($(this).closest('td').siblings('.id').text() === '?')
        $(this).parents('tr').detach();
});

$('.changeable').blur(function() {
    if ( ($(this).closest('td').siblings('.status').text() === 'OK')  && $(this).text() !== ''){
        $(this).closest('td').siblings('.status').html('changed');
    } else if ( ($(this).closest('td').siblings('.status').text() === 'changed')  && $(this).text() === ''){
        $(this).closest('td').siblings('.status').html('OK');
    }
});

$('.table-up').click(function () {
  var $row = $(this).parents('tr');
  if ($row.index() === 1) return; // Don't go above the header
  $row.prev().before($row.get(0));
});

$('.table-down').click(function () {
  var $row = $(this).parents('tr');
  $row.next().after($row.get(0));
});

// A few jQuery helpers for exporting only
jQuery.fn.pop = [].pop;
jQuery.fn.shift = [].shift;

function reloadCountdown() {
    var timer = 5;
    $AUTORELOAD.append("Auto-reload in "+timer);
    setInterval(function() {
        timer--;
        $AUTORELOAD.append(", "+timer);
        if (timer < 1) location.reload();
    },1000);
}

$BTN.click(function () {
    if ($PRESSED) return;
    $PRESSED = true;
  var $rows = $TABLE.find('tr:not(:hidden)');
  var headers = [];
  var data = [];
  
  // Get the headers (add special header logic here)
  $($rows.shift()).find('th:not(:empty)').each(function () {
    headers.push($(this).text().toLowerCase());
  });
  
  // Turn all existing rows into a loopable array
  $rows.each(function () {
    var $td = $(this).find('td');
    var h = {};
    
    // Use the headers from earlier to name our hash keys
    headers.forEach(function (header, i) {
      h[header] = $td.eq(i).text();
    });
    
    data.push(h);
  });
  
  // Output the result
  
  //console.log(JSON.stringify(data));
  
  $("#export-btn").text('Saving...');
  
  $.ajax({
        url: "updateDevicesList",
        type: "post",
        dataType: "json",
        data: JSON.stringify(data),
        contentType: 'application/json',
        mimeType: 'application/json',
        success: function(data) {
            console.log("updateDeviceList OK");
            console.log(JSON.stringify(data));
            $RESPONSE.text(JSON.stringify(data));
        },
        error: function(error, status, errorCode) {
            console.log("Error: "+error+" Status: "+status+" Code: "+errorCode);
            $RESPONSE.text("Error: "+error+" Status: "+status+" Code: "+errorCode);
        },
        complete: function (jqXHR, textStatus) {
            console.log("Complete");
            reloadCountdown();
        }
  });
  
  
  
  
});

$(document).ready(function() {
    console.log("Starting Medusa GUI...");
    getServerInfo();
});

function getServerInfo() {
    $.ajax({
        url: "getServerInfo",
        type: "get",
        dataType: 'json',
        success: function(data) {
            console.log("Info OK");
            console.log(JSON.stringify(data));
            //$SERVERINFO.html(JSON.stringify(data));
            $SERVERTIME.html(data.time);
            $SERVERPUBLICIPS.html(data.public_ip);
            $SERVERLOCALIPS.html(data.local_ip);
            
            console.log("devices: "+data.devices.length);
            
            for (var i=0; i<data.devices.length; i++) {
                var $clone = $TABLE.find('tr.hide').clone(true).removeClass('hide table-line');
                var $td = $($clone).find('td');
                
                $td.eq(0).html(data.devices[i].id);
                $td.eq(1).html('OK');
                $td.eq(2).html(data.devices[i].name);
                $td.eq(2).attr('contenteditable','false');
                $td.eq(3).html('');
                $td.eq(4).html(data.devices[i].login);
                $td.eq(5).html(data.devices[i].ip);
                $td.eq(6).html(data.devices[i].ticket);
                $td.eq(7).html(data.devices[i].protocols);
                $td.eq(8).html(data.devices[i].valid);
                $td.eq(9).html(data.devices[i].timeout);
                
                $TABLE.find('table').append($clone);
                $TABLE.find('id').replaceWith(":)");
            }
            
        },
        error: function(error, status, errorCode) {
            console.log("Error: "+error+" Status: "+status+" Code: "+errorCode);
            $SERVERINFO.html("Error: "+error+" Status: "+status+" Code: "+errorCode);
        }
    });
}
