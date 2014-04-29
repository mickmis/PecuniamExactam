function show() {
	$('.container').load('content.php .content', {'bonjour': ''});
	$('.firstTest').modal({
	    onOpen: function (dialog) {
		dialog.overlay.fadeIn('slow', function () {
			dialog.container.slideDown('slow', function () {
			    dialog.data.fadeIn('slow');
		    });
		});
	    },
	    onClose: function (dialog) {
		dialog.data.fadeOut('slow', function () {
			dialog.container.hide('slow', function () {
				dialog.overlay.slideUp('slow', function () {
					$.modal.close();
				});
			});
		});
	    }
	 });
    }
 $
 (function(){
    
    
    function show2() {
	$('.container').load('content.php .content', {'bonjour': ''});
	$('.firstTest').modal({
	    onOpen: function (dialog) {
		dialog.overlay.fadeIn('slow', function () {
			dialog.container.slideDown('slow', function () {
			    dialog.data.fadeIn('slow');
		    });
		});
	    },
	    onClose: function (dialog) {
		dialog.data.fadeOut('slow', function () {
			dialog.container.hide('slow', function () {
				dialog.overlay.slideUp('slow', function () {
					$.modal.close();
				});
			});
		});
	    }
	 });
    }
    function hide() {
	$.modal.close();
    }
    
    
    //$('button.afficher').click(show);
    //$('button.masquer').click(hide);
});