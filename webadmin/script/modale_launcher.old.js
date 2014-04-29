
function showEditor(loader, sql_classe, id) {
    $(function(){
	var container = $('.content_pane').find('.container');
	var element_view = $('.element_view.'+id);
	
	container.load('../views/pages/EditorView.php', {'loader': loader, 'sql_classe': sql_classe, 'id': id});
	$('.content_pane').modal({
	    onOpen: function (dialog) {
		dialog.overlay.fadeIn( function () {
			dialog.container.slideDown( function () {
			    dialog.data.fadeIn();
		    });
		});
	    },
	    onClose: function (dialog) {
		dialog.data.fadeOut( function () {
			dialog.container.hide( function () {
				dialog.overlay.slideUp( function () {
				    $.modal.close();
				});
			});
		});
	    }
	 });
    }
    );
}