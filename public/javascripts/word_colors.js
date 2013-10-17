
		window.onload = function() { addColors(); };
	
	function sendTo(){
		var s = $("#sentiment").val();
		var v = $( "#slider-range-max" ).slider( "value" );
		if (s!="" && v!=0){
			clean();
	      	$.ajax({
	    		type: "POST",
				url: "/felt",
				data: { sentiment: s, valeur: v},
				dataType: "text",
				success: function (msg) {
	                $("#sentiment").val(msg);
	            }
				});
		}else{
			if (s==""){
				alert("Marquez un mot");
			}else{
				alert("Indiquez la valeur de ce mot sur le slider");
			}
		}
      	
	}
	
	function clean(){
		$("#sentiment").val("");
		$( "#slider-range-max" ).slider( "value", 0 );
		$( "#amount" ).val( $( "#slider-range-max" ).slider( "value" ) );
	}
	
	 $(function() {
		$( "#slider-range-max" ).slider({
		range: "max",
		min: 0,
		max: 10,
		value: 0,
		slide: function( event, ui ) {
			$( "#amount" ).val( ui.value );
			}
		});
		$( "#amount" ).val( $( "#slider-range-max" ).slider( "value" ) );
	});
	
	
	function sendColor(color){
	
		$.ajax({
	    		type: "POST",
				url: "/color",
				data: { color: color},
				dataType: "text",
				success: function (msg) {
	                alert("Vous avez associé une couleur à cette musique!");
	            }
		});
	}
	
	function addColors(){
	
		var cols = new Array();
		
		cols[0] = "#FFDEAD";
		cols[1] = "#FFFF00";
		cols[2] = "#FFD700";
		cols[3] = "#FFA500";
		cols[4] = "#DAA520";
		cols[5] = "#FF8C00";
		cols[6] = "#FF7F50";
		
		cols[7] = "#FFFFFF";
		cols[8] = "#C0C0C";
		cols[9] = "#D3D3D3";
		cols[10] = "#708090";
		cols[11] = "#808080";
		cols[12] = "#696969";
		cols[13] = "#000000";
		
		cols[14] = "#FFE4E1";
		cols[15] = "#FFC0CB";
		cols[16] = "#FFB6C1";
		cols[17] = "#FF00FF";
		cols[18] = "#FF69B4";
		cols[19] = "#FF0000";
		cols[20] = "#DC143C";
		
		cols[21] = "#DA70D6";
		cols[22] = "#BA55D3";
		cols[23] = "#800080";
		cols[24] = "#8A2BE2";
		cols[25] = "#00BFFF";
		cols[26] = "#1E90FF";
		cols[27] = "#0000FF";
		
		cols[28] = "#00008B";
		cols[29] = "#5F9EA0";
		cols[30] = "#40E0D0";
		cols[31] = "#00FF7F";
		cols[32] = "#7CFC00";
		cols[33] = "#9ACD32";
		cols[34] = "#008000";
		
		cols[35] = "#6B8E23";
		cols[36] = "#556B2F";
		cols[37] = "#808000";
		cols[38] = "#D2B48C";
		cols[39] = "#CD853F";
		cols[40] = "#D2691E";
		cols[41] = "#800000";
		cols[42] = "#8B0000";
		cols[43] = "#5C4033";
		
		$("#divcolors").empty();
		for (i = 0; i < cols.length; i++){
			$("#divcolors").append("<input type=\"button\" style=\"background-color:"+cols[i]+"; border:0;\" onclick=\"sendColor('"+cols[i]+"')\"/>");

      	} 
	}
		