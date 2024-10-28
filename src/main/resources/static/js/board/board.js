let count = parseInt(document.getElementById('noticeCount').value); // 긴급 공지 게시물 수;
let pagingButtons = document.querySelectorAll('.paging'); // 페이징 버튼들
let optionValue = document.getElementById('boardOption').value; // 게시물 보기 옵션 값

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('boardOption').addEventListener('change', function () {
        optionValue = document.getElementById('boardOption').value;
        selectOption(optionValue);
    })

    pagingButtons.forEach(button => {
        let baseUrl = button.getAttribute('href');
        button.setAttribute('href', `${baseUrl}&boardOption=${optionValue}`)
    })

    const rows = document.querySelectorAll('tr[id^="boardRow_"]') // boardRow_로 시작하는 id를 지닌 모든 tr 요소들 선택
    rows.forEach(row => {
        let isSelected;
        const boardNo = row.id.split('_')[1]; // ID에서 boardNo 추출
        const isNoticeSelected = document.getElementById(boardNo).value.toLowerCase() === "true";
        const isNoticeSelectContainer = document.getElementById('noticeSelectionDiv' + boardNo);
        if (isNoticeSelected === true) { // 이미 긴급공지로 선택됐을 시
            isNoticeSelectContainer.classList.add('activeSelection'); // 클래스 activeSelection 추가 => css 효과 on
            isSelected = true;
        } else {
            isSelected = false;
        }

        isNoticeSelectContainer.addEventListener('click', function () {
            isSelected = !isSelected;
            if (isSelected) {
                console.log("9minute : " + count);
                if (count < 3) {
                    isNoticeSelectContainer.classList.add('activeSelection');
                    selectNotice(boardNo);
                } else {
                    alert('최대 공지 수를 넘겼습니다!')
                    isSelected = !isSelected;
                }
            } else {
                isNoticeSelectContainer.classList.remove('activeSelection');
                deleteNotice(boardNo);
            }
        })
    });
})

// 게시물 삭제 함수
function deleteBoard(boardNo, trBoardNo) {
    trBoardNo.remove();
    fetch(`/board/delete?no=${boardNo}`, {
        method: 'GET', // GET, POST, PUT, DELETE 중 선택
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = "/board";
            } else {
                throw new Error('Network response was not ok');
            }
        })
}

// 게시물 긴급공지로 등록 함수
function selectNotice(boardNo) {
    fetch(`/admin/notice/insert?boardNo=${boardNo}`, {
        method: 'GET', // GET, POST, PUT, DELETE 중 선택
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // JSON 형태로 응답받기
        })
        .then(data =>{
            count = parseInt(JSON.stringify(data));
        })
}


// 게시물 긴급공지에서 삭제 함수
function deleteNotice(boardNo) {
    fetch(`/admin/notice/delete?boardNo=${boardNo}`, {
        method: 'GET', // GET, POST, PUT, DELETE 중 선택
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // JSON 형태로 응답받기
        })
        .then(data => {
            count = parseInt(JSON.stringify(data));
        })
}

// 게시물 옵션 선택 시 ajax로 옵션에 맞게 보여주는 함수
function selectOption(selectedOption) {
    fetch(`/board?selectedOption=${selectedOption}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        } else {
            window.location.href=response.url;
        }
    })
}
