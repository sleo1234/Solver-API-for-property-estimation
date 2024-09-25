function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();

}

function showErrorModal(message) {
	showModalDialog("Error", message);
}

function showWarningModal(message) {
	showModalDialog("Warning", message);
}


function updateModal() {

$('.modal-footer button[data-dismiss="modal"]').remove();
$('.modal-footer').append('<button type="button" id="add_stream_list_btn" class="btn btn-danger" data-dismiss="modal">Yes</button>');
$('.modal-footer').append('<button type="button" class="btn btn-secondary" data-dismiss="modal">No</button>');
}