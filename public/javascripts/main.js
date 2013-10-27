
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