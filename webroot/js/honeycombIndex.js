// Empty JS for your own code to be here
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
                async: false,
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

                    output += syntaxHighlight(data);

                    output += "</div>";

                    $("#response").prepend(output);

                    $.cookie("token", data.Token, { expires: 7 }); // Sample 2
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
                    token: $.cookie("token")
                },
                async: false,
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

    $("#responseClean").click(function() {
            $("#response").empty();
    });
});
