$(function() {
    var modal = $('#myModal');
    var pdfModal = $('#pdfModal');
	
    function loadEditor(sql_classe, id) {
	var body = modal.find('.modal-body');
	
	body.load('../views/pages/EditorView.php', {'sql_classe': sql_classe, 'id': id});
    }
    
    function reload_elementViewWithId(id) {
	var className = '.element_view.'+id;
	var element_view = $(className);
	var url = window.location.href;

	element_view.slideUp(function() {
	    $(this).load(url, function() {
		$(this).replaceWith($(this).show('slow').find(className), function() {
		    $(this).slideDown(800);
		});
	    });
	});
    }
    
    $('.element_view').find('.showEditor').click(function() {
	var id = $(this).parent().find('input[name="id"]').attr('value');
	var sql_class = $(this).parent().find('input[name="sql_class"]').attr('value');
	modal.on('shown', function() {
	    loadEditor(sql_class, id);
	    /* attach a submit handler to the form */
	    $('#myModal').find('button.submitForm').click(function() {
		/* get some values from elements on the page: */
		var form = $(this).parent().parent().find('form');
		var url = form.attr('action');
		var vars = form.serialize();
		/* Send the data using post and put the results in a div */
		$.post( url, vars, function() {
		    form.fadeOut('slow', function () {
			$(this).fadeIn('slow', function() {
			    var id = $(this).find('input[name="id"]').attr('value');
			    reload_elementViewWithId(id);
			});
		    });
		});
	    });
	});
    });
    
    function loadBilan(sql_class, id){
        var body = pdfModal.find('.modal-body');
	
	body.load('../views/pages/BilanView.php', {'sql_class': sql_class, 'id': id}, function(){
            pdfModal.modal('toggle');
        });
    }
    
    $('.element_view').find('.showBilan').click(function(){
        var id = $(this).parent().find('input[name="id"]').attr('value');
	var sql_class = $(this).parent().find('input[name="sql_class"]').attr('value');
        loadBilan(sql_class, id);
    });
});
