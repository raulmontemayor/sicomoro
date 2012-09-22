$(function() {
	$("#movementType").change(function() {
		if ($("#movementType :selected").val() == 'TITHE') {
			// Carga el combo de contribuidor solo si no se ha cargado
			if(!$("#movement\\.idContributor option").length) {
				$.getJSON('/sicomoro/catalog/contributor/getList.html', function(data) {
					$.each(data, function(index, object) {
						$("#movement\\.idContributor").append($("<option/>", {
							value : object.value,
							text : object.text
						}));
					});
				});
			}
			$(".contributor").removeClass("hide");
		} else {
			$(".contributor").addClass("hide");
		}
	});
});