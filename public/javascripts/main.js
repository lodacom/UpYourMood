function ShowContent(f,a){
	$.ajax({
	    		type: "GET",
				url: "/menu",
				data: {file: f},
				dataType: "text",
				success: function (msg) {
					$("#content").empty();
					$("#content").append(msg);
					var str = a.innerHTML;
					var n = str.replace("&amp;","&");
					document.title = n;
	            }
		});
}

function Inscription(){
	$.ajax({
	    		type: "GET",
				url: "/inscription",
				success: function (msg) {
					$("#content").empty();
					$("#content").append(msg);
	            }
		});
}

function CreateUser(){
		$("#error").text("");
		var nom = $("#nom").val();
		var prenom = $("#prenom").val();
		var email = $("#email").val();
		var test = $("#test").val();
		var pseudo = $("#Pseudo").val();
		var mdp = document.getElementById("Mdp").value;
		
		if (email!="" && pseudo!="" && mdp!="" && email_ok(email)){
	      	
	      	$.ajax({
	    		type: "POST",
				url: "/inscription",
				data: {nom: nom, prenom: prenom, email: email, pseudo: pseudo, mdp: mdp},
				dataType: "text",
				success: function (msg) {
	                if (msg=="OK"){
	                	//$("#content").empty();
						//$("#content").append("<br/> Votre inscription a été validée avec succès. Maintenant vous pouvez rentrez dans l'application!");
						var adresseActuelle = window.location;
  						window.location = adresseActuelle;
					}else{
	                	$("#error").text("*"+msg);
	                }
	            }
				});	
				
		}else{
			if (!email_ok(email)){
				$("#error").text("*Email est incorrect!");
			}else{
				$("#error").text("*Ramplissez les champs obligatoires!");
			}
		}
	}

function email_ok(email)
{
	var reg = new RegExp('^[a-z0-9]+([_|\.|-]{1}[a-z0-9]+)*@[a-z0-9]+([_|\.|-]{1}[a-z0-9]+)*[\.]{1}[a-z]{2,6}$', 'i');
	return(reg.test(email));
}