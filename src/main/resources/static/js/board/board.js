let count = 0; // 긴급 공지 게시물 수
let selectedValue = "";
let isRestoring = true;
let priorURL = document.referrer.substring(21);

window.onload = function () {
    document.getElementById('boardOption').addEventListener('change', function () {
        console.log("prior value : " + selectedValue);
        if (!isRestoring) {
            selectedValue = this.value;
            console.log("after value : " + selectedValue)
            window.localStorage.setItem('selectedOption', selectedValue);
            selectOption(selectedValue);
        }
    })
}

document.addEventListener('DOMContentLoaded', function () {
    const savedValue = localStorage.getItem('selectedOption');
    if (savedValue) {
        isRestoring = true;  // 복원 중임을 표시
        document.getElementById('boardOption').value = savedValue;
        console.log("이전 URL : " + document.referrer.substring(21));
        console.log("이전 URL 파람 X : " + removeQueryString(priorURL));
        alert("A : " + (savedValue === "myBoards"));
        alert("B : " + (removeQueryString(priorURL) !== "/board/isMyBoards"));
        alert("C : " + (priorURL !== "/board"));
        alert("D : " + (priorURL !== "/board?isMyBoardsOption=true"));
        alert("D : " + (priorURL !== "/board?isMyBoardsOption=false"));
        alert("D : " + (priorURL !== "/board?isMyBoardsOption=null"));
        if (savedValue === "myBoards" && removeQueryString(priorURL) !== "/board/isMyBoards" && priorURL !== "/board" && priorURL !== "/board?isMyBoardsOption=true" && priorURL !== "/board?isMyBoardsOption=false" && priorURL !== "/board?isMyBoardsOption=null") {
            alert("1234");
            selectOption(savedValue);
        }
        isRestoring = false; // 복원 완료 후 플래그 변경
    } else {
        isRestoring = false;
    }
    const rows = document.querySelectorAll('tr[id^="boardRow_"]')
    rows.forEach(row => {
        let isSelected;
        const boardNo = row.id.split('_')[1]; // ID에서 boardNo 추출
        const isNoticeSelected = document.getElementById(boardNo).value.toLowerCase() === "true";
        const isNoticeSelectContainer = document.getElementById('noticeSelectionDiv' + boardNo);
        // console.log(isNoticeSelected)
        if (isNoticeSelected === true) { // 이미 긴급공지로 선택됐을 시
            isNoticeSelectContainer.classList.add('activeSelection'); // 클래스 activeSelection 추가 => css 효과 on
            isSelected = true;
            count ++;
        } else {
            isSelected = false;
        }

        isNoticeSelectContainer.addEventListener('click', function () {
            isSelected = !isSelected;
            if (isSelected) {
                count ++;
                if (count < 4) {
                    isNoticeSelectContainer.classList.add('activeSelection');
                    selectNotice(boardNo);
                } else {
                    alert('최대 공지 수를 넘겼습니다!')
                    isSelected = !isSelected;
                    // console.log("현재 카운트 : " + count)
                    count --;
                }

            } else {
                isNoticeSelectContainer.classList.remove('activeSelection');
                count --;
                deleteNotice(boardNo);

            }
        })
    });
})

// 게시물 삭제 함수
function deleteBoard(boardNo, trBoardNo) {
    trBoardNo.remove();
    fetch(`/admin/board/delete?boardNo=${boardNo}`, {
        method: 'GET', // GET, POST, PUT, DELETE 중 선택
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            // console.log('응답 성공!')
            if (response.redirected) {
                // console.log('redirect 시도!')
                window.location.href = "/board";
            } else {
                throw new Error('Network response was not ok');
            }
            return response.json(); // JSON 형태로 응답받기
        })
        .then(data => {
            // console.log('Success:', data);
        })
        .catch((error) => {
            // console.error('Error:', error + '에러남!');
        });
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
            // console.log('응답 성공!')
            if (response.redirected) {
                // console.log('redirect 시도!')
            } else {
                throw new Error('Network response was not ok');
            }
            // return response.json(); // JSON 형태로 응답받기
        })
        .then(data => {
            // console.log('Success:', data);
        })
        .catch((error) => {
            // console.error('Error:', error + '에러남!');
        });
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
            // console.log('응답 성공!')
            if (response.redirected) {
                throw new Error('Network response was not ok');
            }
            // return response.json(); // JSON 형태로 응답 받기
        })
        .then(data => {
            // console.log('Success:', data);
        })
        .catch((error) => {
            // console.error('Error:', error + '에러남!');
        });
}

// 게시글 보기 옵션 고를 때 발동되는 함수
function selectOption(selectedValue) {
    fetch(`/board/isMyBoards?isMyBoardsOption=${selectedValue}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            // console.log('응답 성공!')
            // if (response.redirected) {
            //     // window.location.href = response.redirected.url;
            // }
            return response.json(); // JSON 형태로 응답 받기
        })
        .then(data => {
            if (data.redirectUrl) {
                // 서버에서 받은 URL로 브라우저를 리디렉션
                window.location.href = data.redirectUrl;
            }
        })
        .catch((error) => {
            // console.error('Error:', error + '에러남!');
        });
}
function removeQueryString(url) {
    const questionMarkIndex = url.indexOf('?'); // ?의 인덱스를 찾음

    // ?가 존재하면 해당 인덱스 이전의 문자열을 반환, 없으면 원래 문자열을 반환
    return questionMarkIndex !== -1 ? url.slice(0, questionMarkIndex) : url;
}
