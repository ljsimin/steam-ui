var steamui = {}

steamui.gameList = new Array();
steamui.spinnerCounter = 0;

steamui.getGames = function() {
	steamui.startSpinner();
	steamui.steamId = $.url().param("id");
	var urlGames = "/steamui?id=" + steamui.steamId + "&offset="+ $('.game').length;
	console.log(urlGames);
	$.getJSON(urlGames, {}).done(
		function(data) {
			steamui.spinnerCounter--;
			if (steamui.addItemsToList(data)) {
				// if there is data returned call self recursively
				steamui.getGames();
			} else {
				// finished
				steamui.stopSpinner();
				console.log("Finshed loading, " + steamui.gameList.length
						+ " items are in the list");
			}
		});
}

steamui.getPlayerName = function() {
	var urlPlayer = "/player?id=" + $.url().param("id");
	console.log(urlPlayer);
	$.getJSON(urlPlayer, {}).done(function(data) {
		$('#player-name').text(data + " - ");
		document.title = data + "'s Improved Steam Game Library"
	});
}

steamui.registerClickHandlers = function() {
	$(".game").unbind().click(function () { 
		$(".game").removeClass('clicked');
		$(this).addClass('clicked');
		steamui.startSpinner();
		var gameName = $(this).data('game-name');
		var appid = $(this).data('appid');
		$.getJSON( '/game?name='+encodeURIComponent(gameName), {
		  })
		    .done(function(data) {
		    	if (data == null) {
		    		steamui.renderDetailsViewErrorMessage('No information available', appid);
		    		return;
		    	}
		    	steamui.renderDetailsView(data, appid);
		    	steamui.stopSpinner();
		    	 
		    })
			.fail(function() {
				steamui.renderDetailsViewErrorMessage('Error');			
			});
	  });
	$(".game").hover(
		function() {
			$('#game-button-add-tag-'+$(this).data('appid')).show();
		}, function() {
			$('#game-button-add-tag-'+$(this).data('appid')).hide();
		}
	);
	$('.game-button-add-tag').unbind().click(function () { 
		var newTag = window.prompt("Enter a new tag for this game","Favorite");
		if (newTag != null) {
			var appid = $(this).data('appid');
			var gameName = $('.game-name-'+appid).text();
			var steamId = steamui.steamId;
			var tag = newTag;
			$.getJSON('/tags?gameName='+gameName+'&steamId='+steamId+'&tag='+tag+'&action=ADD', {}).done(
					function(data) {
						if (data != null) {
							$('.game-tags-'+appid).empty();
							$.each(data, function(j, tag) {
						    	$('<li>')
									.text(tag)
									.addClass("game-tag")
									.appendTo(".game-tags-"+appid);
							});
						}
					});
		}
	});
}

steamui.registerSearchHandler = function() {
	$('#filter-input').on('keypress', function(e) {
		//enter
		if (e.keyCode == 13) {
	    	steamui.filter($(this).val());
		} 
	});
	$('#filter-input').on('keyup change', function(e) {
		steamui.filter($(this).val());
	});
}

steamui.renderDetailsView = function(data, appid) {
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
	 if (data.genres != null) {
		 $.each(data.genres, function(j, genre) {
	        	$('<li>')
	    			.text(genre)
	    			.addClass("detail-genre")
	    			.appendTo('#game-info-frame');
	    	});
	 }
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
    	.attr('href', "http://en.wikipedia.org/wiki/Special:Search/"+encodeURIComponent(data.gameTitle))
    	.text("Wikipedia")
    	.addClass("detail-links-value")
    	.appendTo('#game-info-frame');
	 $('<a>')
 		.attr('href', "http://thegamesdb.net/search/?string="+encodeURIComponent(data.gameTitle))
 		.text("TheGamesDB.net")
 		.addClass("detail-links-value")
 		.appendTo('#game-info-frame');
	 $('<div>')
		.appendTo('#game-info-frame');
	 $('<span>')
 		.text("Powered by TheGamesDB.net ")
 		.addClass('tgdb-text')
 		.appendTo('#game-info-frame');	 
	 $('<img/>')
 		.addClass('tgdb-logo')
 		.attr('src', 'http://wiki.thegamesdb.net/images/thumb/1/10/Icon-api.png/200px-Icon-api.png')
 		.appendTo('#game-info-frame');
}

steamui.renderDetailsViewErrorMessage = function(message, appid) {
	$('#game-info-frame').empty();
	if (appid !== undefined && appid !== null) {
		var gameName = $('#li-'+appid).data('game-name')
		$('<h2>')
			.text(gameName)
			.appendTo('#game-info-frame');
		$('<div>')
    		.appendTo('#game-info-frame');
		$('<span>')
			.text(message)
			.appendTo('#game-info-frame');		
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
	    	.attr('href', "http://en.wikipedia.org/wiki/Special:Search/"+gameName)
	    	.text("Wikipedia")
	    	.addClass("detail-links-value")
	    	.appendTo('#game-info-frame');
		 $('<div>')
 			.appendTo('#game-info-frame');
	} else {
		$('<span>')
			.text(message)
			.appendTo('#game-info-frame');		
	}
	steamui.stopSpinner();
}

steamui.filter = _.debounce(function(s) {
	var result = _(steamui.gameList).filter(function (game) {
		if (s != null) {
			var searchString = s.trim().toLowerCase();
			//searching the game name
			if (~game.name.toLowerCase().indexOf(searchString)) {
				return true;
			}
			var matches = false;
			if (game.genres != null) {
				//searching genres
				$.each(game.genres, function(i, genre){
					if (~genre.toLowerCase().indexOf(searchString)) {
						matches = true;
					}
				});
			}
			if (matches === true) {
				return true;
			}
			if (game.tags != null) {
				//searching tags
				$.each(game.tags, function(i, tag){
					if (~tag.toLowerCase().indexOf(searchString)) {
						matches = true;
					}
				});
			}
			return matches;
		}
	});
	
	$(".game").hide();
	$.each(result, function(i, game ) {
		$('#li-'+game.appid).show();
	});
}, 400);

steamui.render = function(game) {	
	var gameElement = $("<li/>")
		.attr("data-appid", game.appid)
		.attr("data-game-name", game.name)
		.attr("id", "li-" + game.appid)
		.addClass("game")
		.appendTo("#game-list");
//	$('<a>')
//		.attr('href', "steam://run/"+game.appid)
//		.attr("id", "run-" + game.appid)
//		.appendTo('#li-'+game.appid);
//    $('<img/>')
//    	.addClass('run-game')
//    	.attr("data-appid", game.appid)
//    	.attr('src', "/image/play-icon.png")
//    	.appendTo('#run-'+game.appid);
	$('<span>')
		.addClass("game-thumbnail-container")
		.attr('id', 'game-thumbnail-container-'+game.appid)
		.appendTo('#li-'+game.appid);
    $('<img/>')
    	.addClass('game-thumbnail')
    	.attr('src', "http://media.steampowered.com/steamcommunity/public/images/apps/"+game.appid+"/"+game.img_icon_url+".jpg")
    	.appendTo('#game-thumbnail-container-'+game.appid);
    $('<span>')
		.addClass("game-container")
		.attr('id', 'game-container-'+game.appid)
		.attr("data-appid", game.appid)
		.appendTo('#li-'+game.appid);
    $('<span>')
    	.text(game.name)
    	.addClass("game-name")
    	.addClass("game-name-"+game.appid)
    	.attr("data-appid", game.appid)
    	.appendTo('#game-container-'+game.appid);
    $('<div>')
		.addClass("game-break")
		.appendTo('#game-container-'+game.appid);
    $('<ul>')
    	.addClass("game-genres")
    	.addClass("game-genres-"+game.appid)
    	.appendTo('#game-container-'+game.appid);    
    if (game.genres != null) {
	    $.each(game.genres, function(j, genre) {
	    	$('<li>')
				.text(genre)
				.addClass("game-genre")
				.appendTo(".game-genres-"+game.appid);
		});
    } 
    $('<ul>')
    	.addClass("game-tags")
    	.addClass("game-tags-"+game.appid)
    	.appendTo('#li-'+game.appid);    
    if (game.tags != null) {
	    $.each(game.tags, function(j, tag) {
	    	$('<li>')
				.text(tag)
				.addClass("game-tag")
				.appendTo(".game-tags-"+game.appid);
		});
    } 
    $('<span>')
		.text("Add a tag")
		.addClass("game-button-add-tag")
		.attr('id', 'game-button-add-tag-'+game.appid)
		.attr("data-appid", game.appid)
		.appendTo('#li-'+game.appid);
    if (game.enrichUrl != null) {
		$.getJSON(game.enrichUrl, {}).done(
				function(data) {
					if (data != null) {
	    				$.each(data, function(j, genre) {
	    			    	$('<li>')
	    						.text(genre)
	    						.addClass("game-genre")
	    						.appendTo(".game-genres-"+game.appid);
	    				});
					}
				});
    }
}

steamui.addItemsToList = function(data) {
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

/**
 * Starts the spinner and increases the job count
 */
steamui.startSpinner = function() {
	steamui.spinnerCounter++;
	$("#spinner").show('fast');
}

/**
 * Hides the spinner if there are no pending jobs
 */
steamui.stopSpinner = function() {
	if (steamui.spinnerCounter > 0) {
		steamui.spinnerCounter--;
	}
	if (steamui.spinnerCounter < 1) {
		$("#spinner").hide('slow');
	}
}