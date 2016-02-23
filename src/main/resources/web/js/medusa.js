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
            $SERVERLOCALIPS.html(data.local_ip);
            
            console.log("devices: "+data.devices.length);
            
            for (var i=0; i<data.devices.length; i++) {
                // {"valid":false,"ticket":"","ip":"","name":"pablo","id":1,"login":0,"protocols":"","timeout":0}
                console.log(data.devices[i].id+" OK "+data.devices[i].name+" ********** "+data.devices[i].login+" "+data.devices[i].ip+" "+data.devices[i].protocols+" "+data.devices[i].valid+" "+data.devices[i].timeout+" ");
                //var device = "<tr>"+
                        "<td class=\"id\">"+data.devices[i].id+"</td>"+
                        "<td class=\"status\">OK</td>"+
                        "<td contenteditable=\"false\">"+data.devices[i].name+"</td>"+
                        "<td contenteditable=\"true\" class=\"changeable\">******</td>"+
                        "<td>"+data.devices[i].login+"</td>"+
                        "<td>"+data.devices[i].ip+"</td>"+
                        "<td>"+data.devices[i].protocols+"</td>"+
                        "<td>"+data.devices[i].valid+"</td>"+
                        "<td>"+data.devices[i].timeout+"</td>"+
                        "<td><span class=\"table-remove glyphicon glyphicon-remove\"></span></td>"+
                        "</tr>";
                //$TABLE.find('table').append(device);
                //$TABLE.find('tr.hide.id')
                
                
                var $clone = $TABLE.find('tr.hide').clone(true).removeClass('hide table-line');
                console.log($clone.valueOf());
                
                $TABLE.find('table').append($clone);
                //$clone.
                //$TABLE.find('table').append($clone);
                //$TABLE.find('table tr:last').closest('td').siblings('.id').html(data.devices[i].id);
            }
            
        },
        error: function(error, status, errorCode) {
            console.log("Error: "+error+" Status: "+status+" Code: "+errorCode);
            $SERVERINFO.html("Error: "+error+" Status: "+status+" Code: "+errorCode);
        }
    });
}
