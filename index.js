function loginForm() {
  document.getElementById("notice").innerHTML = "Connecting...";
  var request = require('request');

  var options = {
    uri: 'https://tilki-server-rootg.c9users.io/api/v1/students',
    method: 'POST',
    json: {
      "name": document.getElementById("name").value,
      "surname": document.getElementById("surname").value,
      "number": document.getElementById("number").value
    }
  };

  var x = request(options, function(error, response, body) {
    if(!error && response.statusCode == 200) {
      if(body.errors) {
        var errors = "";
        for(var i = 0; i < body.errors.length - 1; i++) {
          errors += body.errors[i] + ".<br>";
        }
        errors += body.errors[body.errors.length - 1];
          document.getElementById("notice").innerHTML = errors + ".";
      }
    }
  });
}
