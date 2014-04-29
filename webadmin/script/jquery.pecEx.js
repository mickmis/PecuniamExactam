$(function() {
    var adderView_url = '../views/pages/AdderView.php';
    var addElementView_url = '../views/AddElementView.php';
    var adderArea = $('.adder_area_content');
    var adderEmptyArea= $('.adder_area_content.empty');
    
    var buttonShowHide = $('.adder_area').find('.showHide');
    var addRowButton  = $('.adder_area').find('.addRow');
    var copyButton = $('.element_view').find('.copyButton');
    var supButton = $('.element_view').find('.supButton');
    
    $.extend({
	getUrlVars: function(){
	    var vars = [], hash;
	    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
	    for(var i = 0; i < hashes.length; i++)
	    {
		hash = hashes[i].split('=');
		vars.push(hash[0]);
		vars[hash[0]] = hash[1];
	    }
	    return vars;
	},
	getUrlVar: function(name){
	    return $.getUrlVars()[name];
	}
    });
    
    function slideDownArea() {
	adderArea.show('slow').slideDown('slow', function() {
	    buttonShowHide.addClass('hideAdder').removeClass('showAdder');
	});
    }
    
    function slideUpArea() {
	adderArea.hide('slow').slideUp('slow');
	buttonShowHide.addClass('showAdder').removeClass('hideAdder');
    }
    
    function reload_result_view(){
	var content_pane = $('.result_view');
	var url = window.location.href;
	
	content_pane.fadeOut('slow', function(){
	    content_pane.load(url, function(){
		$(this).replaceWith($(this).find('.result_view')).fadeIn('slow');
	    });
	});
    }
    
    function addRow(copy_source, form) {
	var adderview = $('.adder_view');
	var data = {'copy_source': copy_source};
	if(copy_source == 'form'){
	   data = form.serializeArray();
	}
	else if(copy_source != 'empty')
	    $.extend(data, {'id': copy_source, 'for': form});
	else
	    $.extend(data, {'for': form});
	
	adderview.prepend('<div class="added_row new"></div>');
	adderview.find('.added_row.new').hide('slow', function() {
	    $(this).load(addElementView_url, data, function() {
		var add_element_view = $(this).find('.add_element_view');
		add_element_view.hide();
		$('.added_row.new').replaceWith(add_element_view);
		add_element_view.show('slow').fadeIn('slow', function() {
		    adderview.animate({scrollTop: '0px'}, 800);
		});
		
		$('.add_element_view').find('.copyButton').click(function() {
		    addRow('form', $(this).parent().find('form'));
		});
		$('.add_element_view').find('.deleteButton').click(function(){
		    $(this).parent().slideUp('slow', function() {
			$(this).remove();
		    });  
		});
		
		add_element_view.submit(function(event){
		    event.preventDefault();
		    
		    var submitForm = $(this);
		    var form = submitForm.find('form');
		    var url = form.attr('action');
		    var vars = form.serialize() + '&submitForm=""';

		    $.post( url, vars, function() {
			    submitForm.fadeOut('slow', function () {
			    $(this).hide('slow', function() {
				$(this).remove();
				reload_result_view();
			    });
			});
		    });
		});
    
	    });
	});
    }
    
    function addEmptyRow() {
	var sql_class = $.getUrlVar('for');
	addRow('empty', sql_class);
    }
    
    function resetContent() {
	adderEmptyArea.hide(function() {
	    $(this).load(adderView_url , function() {
		addEmptyRow();
		$(this).find('.sendAll').click(function(){
		    var forms = $(this).parent().find('.add_element_view');
		    forms.each(function(){
			$(this).submit();
		    });
		});
	    });
	});
	adderEmptyArea.removeClass('empty');
    }
    
    resetContent();
    $('input[type="submit"]').addClass('btn');
    
    // EVÈNEMENTS !
    
    buttonShowHide.toggle(slideDownArea, slideUpArea);
    
    addRowButton.click(addEmptyRow);
    
    copyButton.click(function() {
	var id = $(this).parent().find('[name="id"]').attr('value');
	var sql_class = $(this).parent().find('[name="sql_class"]').attr('value');
	addRow(id, sql_class);
	slideDownArea();
    })
    
    supButton.click(function(){
	var element_view = $(this).parent().parent();
	var id = element_view.find('[name="id"]').attr('value');
	var sql_class = element_view.parent().find('[name="sql_class"]').attr('value');
	
	var data = {'sql_class': sql_class, 'toDelete': id };
	
	$.post(addElementView_url, data, function(response){
	   $('.alert-error').find('.alert_content').html(response);
	   $('.alert-error').show().alert();
	   if(response == null) {
	     element_view.fadeOut('slow', function() {
		 $(this).remove();
	     });
	   }
	});
    });
    
    $('.result_view').find('.deleteAll').click(function() {
	var supButtons = $(this).parent().find('.element_view').find('.supButton');
	supButtons.each(function() {
	    $(this).click();
	    reload_result_view();
	});
    });
    
    function reload_search_inputField(choice) {
	var inputField = $('.Page_SearchArea').find('.inputField');
	var url = window.location.href;
	
	inputField.fadeOut(function() {
	    inputField.load(url, {'predicat': choice}, function() {
		var newInputField = $(this).find('.inputField');
		$(this).replaceWith(newInputField).fadeIn(function (){
		    
		});
	    });
	});
    }
    
    function slideShowResult() {
	var btnGroup = $(this).parent().find('.btn-group');
	var extendedView = $(this).parent().find('.element_field_extended_view');
	extendedView.fadeIn(800,function(){
	   btnGroup.fadeIn(800);
	});
    }
    function slideHideResult() {
	var btnGroup = $(this).parent().find('.btn-group');
	var extendedView = $(this).parent().find('.element_field_extended_view');
	
	btnGroup.fadeOut(function(){
	    extendedView.fadeOut();
	});
    }
    
    $('.element_field_view_header').toggle(slideShowResult, slideHideResult);
    
    $('.Page_SearchArea').find('select[name="predicat"]').change(function(){
	var choice = $(this).val();
	reload_search_inputField(choice);
    });
    
    // copy de groupe
    $('.element_view').find('.groupCopyBtn').click(function() {
	var container = $(this).parent();
	var sql_class = container.find('input[name="sql_class"]').attr('value');
	var tag_name = container.find('input[name="tag_name"]').attr('value');
	var tag_source = container.find('input[name="tag_source"]').attr('value');
	var url = '../views/pages/GroupCopyView.php';
	$('#myModal').find('.modal-body').load(url, {'tag_name': tag_name, 'tag_source': tag_source, 'sql_class': sql_class}, function() {
	    var form = $(this).find('form');
	    
	    $(this).parent().modal();
	    $(this).parent().find('.submitForm').click(function(){
		var data = form.serialize();
		$.post(url, data, function() {
		    form.fadeOut(function(){
			$(this).fadeIn();
		    });
		});
	    });
	});
    });
    
    // date time
    Locale.use('fr-FR');
    $('.datepicker_dashboard').remove();
    
    var datepicker = new DatePicker($('.hero-unit.container').find('.dateTime'),{
	timePicker: true,
	positionOffset: {x: 5, y: 0},
	pickerClass: 'datepicker_dashboard'
    });
});