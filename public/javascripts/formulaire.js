var listWord = [];

$(function(){
	addColors();
	
	$(document).on('click', '#linkWord', function(){
		sendTo();
	});
	
	$(document).on('click', '#choix_image img', function(){
		var sentiment = $('#sentiment').val();
		
		$.post('/validation', {
			'TransSentiment': sentiment,
			'TransValeur': $('#slider-range-max').slider('value'),
			'TransUrlImage': $(this).attr('src')
		}, function(){
			listWord.push(sentiment);
			$('#words').append('<span>'+ sentiment + '</span>');
			cleanForm();
		}).fail(function() {
			alert('Une erreur s\'est produite pendant la validation');
		});
		
		$('#choix_image').dialog('close');   
	});
	
	
	$(document).on('click', '#previous_sing', function(){
		$.get('player/previous', function(dom){ $('#player_music').html(dom); });
	});
	
	$(document).on('click', '#next_sing', function(){
		$.get('player/next', function(dom){ $('#player_music').html(dom); });
	});
});

//widgets.jamendo.com/v3/track/1080651?autoplay=0&layout=standard&manualWidth=242& 	width=220&theme=light&highlight=0&tracklist=false&tracklist_n=3&embedCode=
//widgets.jamendo.com/v3/track/1080792?autoplay=0&layout=standard&manualWidth=242& 	width=220&theme=light&highlight=0&tracklist=false&tracklist_n=3&embedCode=

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
	      		var content = '<p>Cliquez sur l\'image qui pour vous correspond au mieux au mot que vous venez de saisir :</p><p>';
	      		
	      		for(i in response)
	  			{
	      			content += '<img src="' + response[i] + '" alt="image" height="100" width="80" /> ';
	      			if(i+1 % 3 == 0) content += '</p><p>';
	  			}
	      		content += '</p>';
	      				
	      		getPopUpChoixImage().html(content).dialog({ width: 800, title: "Choisissez une image:" });
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

function getPopUpChoixImage()
{
 	if($('#choix_image').length == 0) return $('<div id="choix_image" />').appendTo('body');
 	else return $('#choix_image');
}
