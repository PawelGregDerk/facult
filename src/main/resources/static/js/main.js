$(document).ready(function () {
    $('#suc').hide();
    $('#err').hide();
});

// for ajax request to check if user email is free
function checkIfFree() {
    let email = $('#email').val();
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/check",
        data: JSON.stringify({
            "email": email
        }),
        dataType: 'text',
        cache: false,
        timeout: 600000,
        success: function (msg) {
            $('#msg').hide();
            if (msg === 'wrong') {
                $('#suc').hide();
                $('#err').show();
            } else {
                $('#err').hide();
                $('#suc').show();
            }
        },
        error: function (e) {
            let error = "<h4>Error</h4><pre>" + e.responseText + "</pre>";
            $('#msg').hide();
            $('#suc').hide();
            $('#err').show();
            $('#err').html(error);
        }
    });
}

let i = 0;
function anime(speed, line, id) {
    setInterval(function () {
        if (i++ < line.length) {
            $("#" + id).val(line.substring(0, i));
        } else {
            $("#" + id).val(" ");
            i = 0;
        }
    }, speed);
}