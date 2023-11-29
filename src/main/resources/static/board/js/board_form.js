function notifysubmit() {

	var title = document.getElementsByName('title')[0].value
	var content = document.getElementsByName('content')[0].value

	if (title == '') {

		alert('제목을 입력해주세요.');
		return false;
	}
	else if (content == '') {

		alert('내용을 입력해주세요.');
		return false;
	}
}

function boardsubmit() {

	var title = document.getElementsByName('title')[0].value
	var content = document.getElementsByName('content')[0].value

	if (title == '') {

		alert('제목을 입력해주세요.');
		return false;
	}
	else if (content == '') {

		alert('내용을 입력해주세요.');
		return false;
	}
}

var header = 'X-CSRF-TOKEN';
var token = document.querySelector('[name=_csrf]').value

$(document).ready(function() {

	console.log("test")

	$('#Comment_regist').click(function() {

		//Json으로 전달할 파라미터 변수선언
		const com_bno = $('#bnum').val();
		const com_writer = $('#com_writer').val();
		const com_content = $('#com_content').val();

		var events = new Array(); // Json 데이터를 받기 위한 배열 선언
		var obj = new Object();     // Json 을 담기 위해 Object 선언

		obj.com_bno = com_bno;
		obj.com_writer = com_writer;
		obj.com_content = com_content;
		events.push(obj);

		console.log(com_bno);
		console.log(com_writer);
		console.log(com_content);

		if (com_writer == '') {
			alert('로그인 후 이용해주세요');
			return;
		} else if (com_content == '') {
			alert('내용을 입력하세요');
		}

		//var token = document.querySelector('[name=_csrf.parameterName]').value

		console.log(header)
		console.log(token)

		$.ajax({
			type: 'post',
			url: "/reply/add",
			beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			data: JSON.stringify(events),
			contentType: 'application/json',
			success: function(data) {
				console.log('통신성공' + data);
				alert('댓글 등록이 완료되었습니다.');
				console.log('댓글 등록 완료');
				$('#com_writer').val(com_writer);
				$('#com_content').val('');
				getList();
			},
			error: function() {
				alert('통신실패');
			}
		});// 댓글 비동기 끝

	});// 댓글등록 이벤트 끝

	getList();
	/*$("#del").click(function () {
		location.href = "/board/del?num=[[${b.num }]]";//url /board/del로 삭제요청. 삭제할 글번호를 파라메터 num으로 보냄
	});*/
});

function getList() {

	const com_bno = $('#bnum').val();
	const com_writer = $('#com_writer').val();
	const com_content = $('#com_content').val();

	/* const com_no = ${com}; */
	$.ajax({
		type: 'get',
		url: "/reply/list/" + com_bno,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		contentType: 'application/json',
		//"<c:url value='/list/{com_bno}'/>" + com_bno,
		function(data) {
			if (data.total > 0) {
				var list = data.list;

				var comment_html = "<div>";

				$('#count').html(data.total);
				for (i = 0; i < list.length; i++) {
					var content = list[i].com_content;
					var writer = list[i].com_writer;
					comment_html += "<div><span id='com_writer'><strong>" + writer + "</strong></span><br/>";
					comment_html += "<span id='com-content'>" + content + "</span><br>";
					if (writer === $("#com_writer").val()) {
						comment_html += "<span id='delete' style='cursor:pointer;' data-id =" + content + ">[삭제]</span><br></div><hr>";

					}
					else {
						comment_html += "</div><hr>";
					}
				}
				$(".comment_Box").html(comment_html);
			}
			else {
				var comment_html = "<div>등록된 댓글이 없습니다.</div>";
				$(".comment_Box").html(comment_html);
			}
		}
	});// 댓글 비동기 끝

}
