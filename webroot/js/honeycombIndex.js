// Empty JS for your own code to be here
$(function() {

	/*$.get( "http://localhost:8080/test", function( response ) {
    	alert( response ); // server response
	});
*/
	$("#login-form").submit(function() {
        var user = $("#login-form input#username").val();
        var pass = $("#login-form input#password").val();
  		//alert(uesrname);
  		$.ajax({
                cache: true,
                type: "POST",
                url:"/login",
                data: {
                    username: user,
                    password: pass
                },
                async: false,
                error: function(request) {
                    alert(request.status);
                    console.log( request ); // server response
                    return false;
                },
                success: function(data) {
                    console.log( data.Token); // server response
                    var output = "<div class=\"alert alert-success alert-dismissable\">";
                    $("#response1").html("<p>shit</p>")
                    return false;
                }
            });
  		return false;
	});
});
