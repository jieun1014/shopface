var branchNo = 0;
var userId = $('#user').val();

function drawBranchList() {
	branchNo = localStorage.getItem('branchNo');
	var getBranchList = $.ajax({
			url: '/branch',
			type:'GET',
			data: {
				memberId: userId,
				approvalStatus: 'Y'
			},
			dataType: 'JSON',
			contentType : 'application/json;charset=UTF-8'
	});
	
	$.when(getBranchList).done(function(response) {
		var html="";
		html +="<select class='form-control ml-4' id='selectBranch' name='selectBranch'>";
		
		if(response.length > 0) {
			for(var i = 0; i < response.length; i++) {
				if(branchNo > 0) {
					if(branchNo == response[i].no) {
						html += "<option value='" + response[i].no +"' selected>" + response[i].name + "</option>";
					} else {
						html += "<option value='" + response[i].no +"'>" + response[i].name + "</option>";
					}
				} else {
					if(i == 0) {
						html += "<option value='" + response[i].no +"' 'selected'>" + response[i].name + "</option>";
						branchNo = response[i].no;
					} else {
						html += "<option value='" + response[i].no +"'>" + response[i].name + "</option>";
					}
				}
			}
		}
		html += "</select>";
		
		$('#selectBranchDiv').html(html);
		
	});
	
	return branchNo;
}

function moveRecord() {
	location.href="/record/" + branchNo;
}

function moveEmploy() {
	location.href="/employ/" + branchNo;
}

function moveOccupation() {
	location.href="/occupation/" + branchNo;
}

