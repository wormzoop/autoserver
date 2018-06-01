$("#config").click(function(){
	var url = "/autoserver/config";
	$.get(url, function(res){
		var json = jQuery.parseJSON(res);
		for(var item in json){
			$('#sel').prepend('<option value="' + json[item] + '">' + json[item] + '</option>');
		}
	})
});
$("#generate").click(function(){
	var url = "/autoserver/code?table="+$("#sel").val();
	$.get(url, function(res){
		
	})
});