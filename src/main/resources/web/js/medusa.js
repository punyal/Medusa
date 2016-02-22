var $TABLE = $('#table');
var $BTN = $('#export-btn');
var $RESPONSE = $('#response');
var $PRESSED = false;
var $SERVERTIME = $('#serverTime');
var $SERVERPUBLICIPS = $('#serverPublicIPs');
var $SERVERLOCALIPS = $('#serverLocalIPs');

$('.table-add').click(function () {
  var $clone = $TABLE.find('tr.hide').clone(true).removeClass('hide table-line');
  $TABLE.find('table').append($clone);
});

$('.table-remove').click(function () {
    $(this).closest('td').siblings('.status').html('delete');
    if ($(this).closest('td').siblings('.id').text() === '?')
        $(this).parents('tr').detach();
});

var contents = $('.changeable').html();
$('.changeable').blur(function() {
    if (contents!=$(this).html() && ($(this).closest('td').siblings('.status').text() !== 'delete') ){
        contents = $(this).html();
        $(this).closest('td').siblings('.status').html('changed');
        
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
    $RESPONSE.append(".<br> Auto-reload in "+timer);
    setInterval(function() {
        timer--;
        $RESPONSE.append(", "+timer);
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
  
  console.log(JSON.stringify(data));
  
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
            if (data.response === true)
                $RESPONSE.text("DONE saved "+data.length+" elements");
            else
                $RESPONSE.text("Problem");
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
            $SERVERLOCALIPS.html(data.local_ip)
        },
        error: function(error, status, errorCode) {
            console.log("Error: "+error+" Status: "+status+" Code: "+errorCode);
            $SERVERINFO.html("Error: "+error+" Status: "+status+" Code: "+errorCode);
        }
    });
}
