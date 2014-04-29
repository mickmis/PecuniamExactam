//$(function() {
//    function reload_elementViewWithId(id) {
//	var className = '.element_view.'+id;
//	var element_view = $(className);
//	var url = window.location.href;
//
//	element_view.slideUp(function() {
//	    $(this).load(url, function() {
//		$(this).replaceWith($(this).show('slow').find(className), function() {
//		    $(this).slideDown(800);
//		});
//	    });
//	});
//    }
//    
//    
//    /* attach a submit handler to the form */
//    $('#myModal').find('button.submitForm').click(function() {
//	/* get some values from elements on the page: */
//	var form = $(this).parent().parent().find('form');
//	var url = form.attr('action');
//	var vars = form.serialize();
//	/* Send the data using post and put the results in a div */
//	$.post( url, vars, function() {
//	    form.fadeOut('slow', function () {
//		$(this).fadeIn('slow', function() {
//		    var id = $(this).find('input[name="id"]').attr('value');
//		    reload_elementViewWithId(id);
//		});
//	    });
//	});
//  });
//});