$(function() {

	/*$.get( "http://localhost:8080/test", function( response ) {
    	alert( response ); // server response
	});
    */

    function syntaxHighlight(json) {
        json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
        return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
            var cls = 'number';
            if (/^"/.test(match)) {
                if (/:$/.test(match)) {
                    cls = 'key';
                } else {
                    cls = 'string';
                }
            } else if (/true|false/.test(match)) {
                cls = 'boolean';
            } else if (/null/.test(match)) {
                cls = 'null';
            }
            return '<span class="' + cls + '">' + match + '</span>';
        });
    }
    
$("#login-form").submit(function() {
        var user = $("#login-form input#username").val();
        var pass = $("#login-form input#password").val();
  		//alert(uesrname);
  		$.ajax({
                cache: false,
                type: "POST",
                url:"/login",
                data: {
                    username: user,
                    password: pass
                },
                async: true,
                error: function(request) {
                    console.log( request ); // server response

                    var output = "<div class=\"alert alert-danger alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Error </h4>";

                    output += "<strong>" + request.status + "</strong>\n";

                    output += syntaxHighlight(request.responseText);

                    output += "</div>";

                    $("#response").prepend(output);

                },
                success: function(data) {
                    console.log( data.Token); // server response
                    var output = "<div class=\"alert alert-success alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Success </h4>";

                    output += "<strong>200</strong>\n";

                    output += syntaxHighlight(JSON.stringify(data, undefined, 4));

                    output += "</div>";

                    $("#response").prepend(output);

                    $.cookie("HoneyCombToken", data.Token, { expires: 7 }); // Sample 2
                }
            });
  		return false;
	});

    $("#list-form").submit(function() {
        var user = $("#login-form input#username").val();
        //alert(uesrname);
        $.ajax({
                cache: false,
                type: "GET",
                url:"/faults/list",
                data: {
                    token: $.cookie("HoneyCombToken")
                },
                async: true,
                error: function(request) {
                    console.log( request ); // server response

                    var output = "<div class=\"alert alert-danger alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Error </h4>";

                    output += "<strong>" + request.status + "</strong>\n";

                    output += syntaxHighlight(request.responseText);

                    output += "</div>";

                    $("#response").prepend(output);

                },
                success: function(data) {
                    console.log( data); // server response
                    var output = "<div class=\"alert alert-success alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Success </h4>";

                    output += "<strong>200</strong>\n";

                    output += "<strong>Fault List: </strong>\n";


                    /*

                    output += "<ul>["

                    for (var i in data) 
                    {
                        output+="<li>" + JSON.stringify(data[i]); + "</li>";
                    }

                    output += "]</ul>"
                    */

                    output += syntaxHighlight(JSON.stringify(data, undefined, 4));


                    output += "</div>";

                    $("#response").prepend(output);
                }
            });
        return false;
    });

    $(document).on('click', '.browse', function(){
    var file = $(this).parent().parent().parent().find('.file');
      file.trigger('click');
    });
    $(document).on('change', '.file', function(){
      $(this).parent().find('.form-control').val($(this).val().replace(/C:\\fakepath\\/i, ''));
    });

    $("#remove-form").submit(function() {
        var faultId = $("#remove-form input#faultId1").val();
        $.ajax({
                cache: false,
                type: "POST",
                url:"/faults/deactivate",
                data: {
                    faultId: faultId,
                    token: $.cookie("HoneyCombToken")
                },
                async: true,
                error: function(request) {
                    console.log( request ); // server response

                    var output = "<div class=\"alert alert-danger alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Error </h4>";

                    output += "<strong>" + request.status + "</strong>\n";

                    output += syntaxHighlight(request.responseText);

                    output += "</div>";

                    $("#response").prepend(output);

                },
                success: function(data) {
                    console.log( data); // server response
                    var output = "<div class=\"alert alert-success alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Success </h4>";

                    output += "<strong>200</strong>\n";

                    output += syntaxHighlight(JSON.stringify(data, undefined, 4));

                    output += "</div>";

                    $("#response").prepend(output);
                }
            });
        return false;
    });

    $("#activate-form").submit(function() {
        var faultId = $("#activate-form input#faultId1").val();
        $.ajax({
                cache: false,
                type: "POST",
                url:"/faults/reactivate",
                data: {
                    faultId: faultId,
                    token: $.cookie("HoneyCombToken")
                },
                async: true,
                error: function(request) {
                    console.log( request ); // server response

                    var output = "<div class=\"alert alert-danger alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Error </h4>";

                    output += "<strong>" + request.status + "</strong>\n";

                    output += syntaxHighlight(request.responseText);

                    output += "</div>";

                    $("#response").prepend(output);

                },
                success: function(data) {
                    console.log( data); // server response
                    var output = "<div class=\"alert alert-success alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Success </h4>";

                    output += "<strong>200</strong>\n";

                    output += syntaxHighlight(JSON.stringify(data, undefined, 4));

                    output += "</div>";

                    $("#response").prepend(output);
                }
            });
        return false;
    });

     $("#inject-form").submit(function() {
        var faultId = $("#inject-form input#faultId2").val();
        var rawString = $("#inject-form textarea#arguments").val();

        var arguments = {};
        arguments.token = $.cookie("HoneyCombToken");

        if (rawString.length != 0) {
            var arr = rawString.split(';');

            for (var i in arr) 
            {
                var temp = arr[i].split('=');
                arguments[temp[0]] = temp[1];
            }
        }

        $.ajax({
                cache: false,
                type: "POST",
                url:"/inject/" + faultId,
                dataType: "json",
                data: arguments,
                async: true,
                error: function(request) {
                    console.log( request ); // server response

                    var output = "<div class=\"alert alert-danger alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Error </h4>";

                    output += "<strong>" + request.status + "</strong>\n";

                    output += syntaxHighlight(request.responseText);

                    output += "</div>";

                    $("#response").prepend(output);

                },
                success: function(data) {
                    console.log( data); // server response
                    var output = "<div class=\"alert alert-success alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Success </h4>";

                    output += "<strong>200</strong>\n";

                    output += syntaxHighlight(JSON.stringify(data, undefined, 4));

                    output += "</div>";

                    $("#response").prepend(output);
                }
            });
        return false;
    });

    $("#terminate-form").submit(function() {
        var faultInstanceId = $("#terminate-form input#faultInstanceId").val();
        $.ajax({
                cache: false,
                type: "POST",
                url:"/terminate/" + faultInstanceId,
                data: {
                    token: $.cookie("HoneyCombToken")
                },
                async: true,
                error: function(request) {
                    console.log( request ); // server response

                    var output = "<div class=\"alert alert-danger alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Error </h4>";

                    output += "<strong>" + request.status + "</strong>\n";

                    output += syntaxHighlight(request.responseText);

                    output += "</div>";

                    $("#response").prepend(output);

                },
                success: function(data) {
                    console.log( data); // server response
                    var output = "<div class=\"alert alert-success alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Success </h4>";

                    output += "<strong>200</strong>\n";

                    output += syntaxHighlight(JSON.stringify(data, undefined, 4));

                    output += "</div>";

                    $("#response").prepend(output);
                }
            });
        return false;
    });

    $("#responseClean").click(function() {
            $("#response").empty();
    });

   $("form#upload-form").submit(function(event){
      //disable the default form submission
      event.preventDefault();
      //grab all form data
      var formData = new FormData($(this)[0]);
      formData.append("token",$.cookie("HoneyCombToken"));
      $.ajax({
        url: '/faults/upload',
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        error: function(request) {
                    console.log( request ); // server response

                    var output = "<div class=\"alert alert-danger alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Error </h4>";

                    output += "<strong>" + request.status + "</strong>\n";

                    output += syntaxHighlight(request.responseText);

                    output += "</div>";

                    $("#response").prepend(output);

                },
                success: function(data) {
                    console.log( data); // server response
                    var output = "<div class=\"alert alert-success alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Success </h4>";

                    output += "<strong>200</strong>\n";

                    output += syntaxHighlight(JSON.stringify(data, undefined, 4));

                    output += "</div>";

                    $("#response").prepend(output);
                }
      });
      return false;
    });

    $("form#update-form").submit(function(event){
      //disable the default form submission
      event.preventDefault();
      //grab all form data
      var formData = new FormData($(this)[0]);
      formData.append("token",$.cookie("HoneyCombToken"));
      $.ajax({
        url: '/faults/update',
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        error: function(request) {
                    console.log( request ); // server response

                    var output = "<div class=\"alert alert-danger alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Error </h4>";

                    output += "<strong>" + request.status + "</strong>\n";

                    output += syntaxHighlight(request.responseText);

                    output += "</div>";

                    $("#response").prepend(output);

                },
                success: function(data) {
                    console.log( data); // server response
                    var output = "<div class=\"alert alert-success alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Success </h4>";

                    output += "<strong>200</strong>\n";

                    output += syntaxHighlight(JSON.stringify(data, undefined, 4));

                    output += "</div>";

                    $("#response").prepend(output);
                }
      });
      return false;
    });

    $("li#seeLog").on('click', function() {
        $.ajax({
                cache: false,
                type: "GET",
                url:"/logs",
                data: {
                    token: $.cookie("HoneyCombToken")
                },
                async: true,
                error: function(request) {
                    console.log( request ); // server response

                    var output = "<div class=\"alert alert-danger alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Error </h4>";

                    output += "<strong>" + request.status + "</strong>\n";

                    output += syntaxHighlight(request.responseText);

                    output += "</div>";

                    $("#logss").html(output);

                },
                success: function(data) {
                    console.log( data); // server response
                    var output = "<div class=\"alert alert-success alert-dismissable honeyAlert\">";

                    output += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>";

                    output += "<h4> Log </h4>";

                    output += data.logs;


                    output += "</div>";

                    $("#logss").html(output);
                }
            });
    });
});
