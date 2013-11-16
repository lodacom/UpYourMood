var listWord = [];

$(function(){
	addColors();
	
	$(document).on('click', '#linkWord', function(){
		sendTo();
	});
	
	$(document).on('click', '#choix_image img', function(){
	
	$.post('/validation', {sentiment: $('#sentiment').val(), valeur: $('#slider-range-max').slider('value'), urlImage: $(this).attr('src')});
		cleanForm();
		$('#choix_image').dialog('close');
	});
});

function sendTo()
{
	var sentiment   = $("#sentiment").val(),
		sliderValue = $("#slider-range-max").slider("value");
	
	if(sentiment && sliderValue > 0)
	{
      	$.post("/felt", { sentiment: sentiment, valeur: sliderValue}, function(response){
      		//ERREUR
      		if(typeof response == 'string') alert(response);
      		//CAS NORMAL
      		else
      		{
      			listWord.push(sentiment);
      			
	      		var content = '<p>Cliquez sur l\'image correspondant à ce que le mot que vous venez de saisir vous fait penser:</p><p>';
	      		
	      		for(i in response)
	  			{
	      			content += '<img src="' + response[i] + '" alt="image" height="100" width="80" /> ';
	      			if(i+1 % 3 == 0) content += '</p><p>';
	  			}
	      		content += '</p>';
	      				
	      		$('<div id="choix_image" />').appendTo('body').html(content).dialog({ width: 800, title: "Choississez une image:" });
      		}
        });
	}
	else
	{
		var msgError = '';
		if(!sentiment) msgError += "Erreur: mot manquant (champs vide)";
		if(sliderValue == 0) msgError += "\nErreur: aucune valeur sur le slider (doit etre > 0)";
		alert(msgError);
	}
}

function cleanForm()
{
	$("#sentiment").val("");
	$("#slider-range-max").slider("value", 0);
	$("#amount").val(0);
}
