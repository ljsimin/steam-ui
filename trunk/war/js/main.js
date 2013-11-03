var steamui = {}

steamui.gameList = new Array();

steamui.getGames = function() {
	  var urlGames = "/steamui?id="+$.url().param("id")+"&offset="+$('.game').length;
	  console.log(urlGames);
	  $.getJSON(urlGames, {
	  })
	    .done(function(data ) {
	    	if (addItemsToList(data)) {
	    		//if there is data returned call self recursively 
				steamui.getGames();
	    	} else {
	    		//finished
	    		console.log("Finshed loading, " + steamui.gameList.length + " items are in the list");
	    	}
	    });
	  var urlPlayer = "/player?id="+$.url().param("id");
	  console.log(urlPlayer);
	  $.getJSON(urlPlayer, {
	  })
	    .done(function(data ) {
	    	$('#player-name').text(data + " - ");
	    });
	}

steamui.registerClickHandlers = function() {
	$(".game").unbind().click(function () { 
		var gameName = $(this).data('game-name');
		var appid = $(this).data('appid');
		console.log(gameName);
		$.getJSON( '/game?name='+gameName, {
		  })
		    .done(function(data) {
		    	steamui.renderDetailsView(data);
		    	 
	    });
	  });
}

steamui.registerSearchHandler = function() {
	$('#filter-input').on('keypress', function(e) {
		//enter
		if (e.keyCode == 13) {
	    	steamui.filter($(this).val());
		} 
	});
}

steamui.renderDetailsView = function(data) {
	$('#spinner-header').empty();
	$('#game-info-frame').empty();
	$('<h2>')
		.text(data.gameTitle)
		.addClass("detail-title")
		.appendTo('#game-info-frame');
	 $('<img/>')
    	.addClass('detail-image')
    	.attr('src', data.imageUrl)
    	.appendTo('#game-info-frame');
	 $('<span>')
    	.text(data.overview)
    	.addClass("detail-overview")
    	.appendTo('#game-info-frame');
	 $('<div>')
    	.appendTo('#game-info-frame');
	 $('<span>')
    	.text("Rating: ")
    	.addClass("detail-rating-label")
    	.appendTo('#game-info-frame');
	 $('<span>')
    	.text(data.rating)
    	.addClass("detail-rating-value")
    	.appendTo('#game-info-frame');
	 $('<div>')
    	.appendTo('#game-info-frame');
	 $('<span>')
    	.text("Developer: ")
    	.addClass("detail-developer-label")
    	.appendTo('#game-info-frame');
	 $('<span>')
    	.text(data.developer)
    	.addClass("detail-develoepr-value")
    	.appendTo('#game-info-frame');
	 $('<div>')
    	.appendTo('#game-info-frame');
	 $('<span>')
    	.text("Publisher: ")
    	.addClass("detail-publisher-label")
    	.appendTo('#game-info-frame');
	 $('<span>')
    	.text(data.publisher)
    	.addClass("detail-publisher-value")
    	.appendTo('#game-info-frame');
	 $('<div>')
    	.appendTo('#game-info-frame');
	 $('<span>')
    	.text("Genres: ")
    	.addClass("detail-genres-label")
    	.appendTo('#game-info-frame');
	 $.each(data.genres, function(j, genre) {
        	$('<li>')
    			.text(genre)
    			.addClass("detail-genre")
    			.appendTo('#game-info-frame');
    	});
	 $('<div>')
    	.appendTo('#game-info-frame');
	 $('<span>')
    	.text("Links: ")
    	.addClass("detail-links-label")
    	.appendTo('#game-info-frame');
	 $('<a>')
    	.attr('href', "http://store.steampowered.com/app/"+appid)
    	.text("Steam store")
    	.addClass("detail-links-value")
    	.appendTo('#game-info-frame');
	 $('<a>')
    	.attr('href', "http://en.wikipedia.org/wiki/"+gameName+"_(video_game)")
    	.text("Wikipedia")
    	.addClass("detail-links-value")
    	.appendTo('#game-info-frame');
}

steamui.filter = function(s) {
	var result = _(list).filter(function (x) { 
		return ~x.searchField.toLowerCase().indexOf(s.toLowerCase()); 
	});
	$("#game-list").empty();
	$.each(result, function(i, game ) {
    	//rendering the game in the visual list
        steamui.render(game);
        steamui.registerClickHandlers();
    });
}

steamui.render = function(game) {
	$("<li/>")
		.attr("data-appid", game.appid)
		.attr("data-game-name", game.name)
		.attr("id", "li-" + game.appid)
		.addClass("game")
		.appendTo("#game-list");
	$('<a>')
		.attr('href', "steam://run/"+game.appid)
		.attr("id", "run-" + game.appid)
		.appendTo('#li-'+game.appid);
    $('<img/>')
    	.addClass('run-game')
    	.attr("data-appid", game.appid)
    	.attr('src', "/image/play-icon.png")
    	.appendTo('#run-'+game.appid);
    $('<img/>')
    	.addClass('game-thumbnail')
    	.attr('src', "http://media.steampowered.com/steamcommunity/public/images/apps/"+game.appid+"/"+game.img_icon_url+".jpg")
    	.appendTo('#li-'+game.appid);
    $('<span>')
    	.text(game.name)
    	.addClass("game-name")
    	.attr("data-appid", game.appid)
    	.appendTo('#li-'+game.appid);
    $('<ul>')
    	.addClass("game-genres")
		.addClass("game-genres-"+game.appid)
		.appendTo('#li-'+game.appid);
    $.each(game.genres, function(j, genre) {
    	$('<li>')
			.text(genre)
			.addClass("game-genre")
			.appendTo(".game-genres-"+game.appid);
	});
}

steamui,addItemsToList = function(data) {
	console.log("data length " + data.length)
	if (data.length > 0) {	    		
	      $.each(data, function(i, game ) {
	    	//adding game to the internal list
	    	steamui.gameList.push(game);
	    	
	    	//rendering the game in the visual list
	        steamui.render(game);
	        
	      });
		  steamui.registerClickHandlers();				  
		  return true;
	} else {
		return false;
	}
}