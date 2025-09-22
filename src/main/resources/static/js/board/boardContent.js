document.addEventListener('DOMContentLoaded', function () {
    let thumbsUp = document.getElementById("thumbsUp").value.toLowerCase() === "true"; // 현재 유저가 그 보드에 대해 좋아요를 눌렀는지 true / false
    let thumbsDown = document.getElementById("thumbsDown").value.toLowerCase() === "true"; // 현재 유저가 그 보드에 대해 싫어요를 눌렀는지 true / false
    const thumbsUpContainer = document.getElementById('thumbsUpContainer'); // 좋아요 컨테이너
    const thumbsDownContainer = document.getElementById('thumbsDownContainer'); // 싫어요 컨테이너
    let thumbsUpCount = document.getElementById('thumbsUpCount'); // 좋아요 수
    let thumbsDownCount = document.getElementById('thumbsDownCount'); // 싫어요 수
    const boardNo = document.getElementById('boardNo').value; // 해당 보드의 번호
    let priorThumbsUp = document.getElementById('priorThumbsUp').value.toLowerCase() === "true"; // 이전 좋아요가 true 인지
    let priorThumbsDown = document.getElementById('priorThumbsDown').value.toLowerCase() === "true"; // 이전 싫어요가 true 인지
    const header = document.querySelector('meta[name="_csrf_header"]').content; // csrf header
    const token = document.querySelector('meta[name="_csrf"]').content; // csrf token

    judgePriorThumbs(thumbsUp, thumbsUpContainer, thumbsDown, thumbsDownContainer);
    thumbsUpContainer.addEventListener('click', function () { // 좋아요 컨테이너 클릭 시
        thumbsUp = !thumbsUp; // 스위치 기능 작동(현재 좋아요가 true 였다면 클릭시 false)
        if (thumbsUp === true && thumbsDown === true) { // false 에서 true 가 된 좋아요고, 싫어요가 true 였다면
            thumbsDown = false; // 싫어요 false
            console.log("before one : " + thumbsUpCount.value)
            thumbsUpPlus(thumbsUpContainer, thumbsUpCount);
            thumbsDownMinus(thumbsDownContainer, thumbsDownCount);
        } else if (thumbsUp === true) { // false 에서 true 가 된 좋아요, false 였던 싫어요
            thumbsUpPlus(thumbsUpContainer, thumbsUpCount);
        } else { // 그 외
            thumbsUpMinus(thumbsUpContainer, thumbsUpCount);
        }
        goRecommendation(boardNo, thumbsUp, thumbsDown, priorThumbsUp, priorThumbsDown, header, token);
        priorThumbsUp = thumbsUp; // DB에 데이터를 반영 후 이전 데이터로 반영 당시 데이터를 넣음
        priorThumbsDown = thumbsDown; // DB에 데이터를 반영 후 이전 데이터로 반영 당시 데이터를 넣음
    });

    thumbsDownContainer.addEventListener('click', function () { // 싫어요 컨테이너
        thumbsDown = !thumbsDown;
        if (thumbsDown === true && thumbsUp === true) {
            thumbsUp = false;
            thumbsDownPlus(thumbsDownContainer, thumbsDownCount);
            thumbsUpMinus(thumbsUpContainer, thumbsUpCount);
        } else if (thumbsDown === true) {
            thumbsDownPlus(thumbsDownContainer, thumbsDownCount);
        } else {
            thumbsDownMinus(thumbsDownContainer, thumbsDownCount);
        }
        goRecommendation(boardNo, thumbsUp, thumbsDown, priorThumbsUp, priorThumbsDown, header, token);
        priorThumbsUp = thumbsUp; // DB에 데이터를 반영 후 이전 데이터로 반영 당시 데이터를 넣음
        priorThumbsDown = thumbsDown; // DB에 데이터를 반영 후 이전 데이터로 반영 당시 데이터를 넣음
    })
});

// Recommendation 컨트롤러 호출 함수
function goRecommendation(boardNo, isThumbsUp, isThumbsDown, priorThumbsUp, priorThumbsDown, header, token) {
    const url = "/board/recommendation"
    const data = {
        boardNo: boardNo,
        isThumbsUp: isThumbsUp,
        isThumbsDown: isThumbsDown,
        priorThumbsUp: priorThumbsUp,
        priorThumbsDown: priorThumbsDown,
    }; // 전송할 데이터

    fetch(url, {
        method: "POST",
        headers: {
            [header]: token,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data) // 데이터를 JSON 문자열로 변환
    })
        .then(response => {
        })
        .catch(error => {
            console.error("문제가 발생했습니다:", error);
        });
}

function thumbsUpPlus(thumbsUpContainer, thumbsUpCount) {
    thumbsUpContainer.classList.add('thumbsUpActive'); // 강조 효과
    thumbsUpCount.text = String(parseInt(thumbsUpCount.text) + 1); // 카운트 증가
    thumbsUpCount.value = String(parseInt(thumbsUpCount.value) + 1); // 카운트 증가
}

function thumbsUpMinus(thumbsUpContainer, thumbsUpCount) {
    thumbsUpContainer.classList.remove('thumbsUpActive'); // 강조 효과
    thumbsUpCount.text = String(parseInt(thumbsUpCount.text) - 1); // 카운트 감소
    thumbsUpCount.value = String(parseInt(thumbsUpCount.value) - 1); // 카운트 감소
}

function thumbsDownPlus(thumbsDownContainer, thumbsDownCount) {
    thumbsDownContainer.classList.add('thumbsDownActive');
    thumbsDownCount.text = String(parseInt(thumbsDownCount.text) + 1); // 카운트 증가
    thumbsDownCount.value = String(parseInt(thumbsDownCount.value) + 1); // 카운트 증가
}

function thumbsDownMinus(thumbsDownContainer, thumbsDownCount) {
    thumbsDownContainer.classList.remove('thumbsDownActive'); // 강조 효과
    thumbsDownCount.text = String(parseInt(thumbsDownCount.text) - 1); // 카운트 감소
    thumbsDownCount.value = String(parseInt(thumbsDownCount.value) - 1); // 카운트 감소
}

function judgePriorThumbs(thumbsUp, thumbsUpContainer, thumbsDown, thumbsDownContainer) {
    if (thumbsUp === true) { // 유저가 해당 보드에 대해 좋아요가 true 이면
        thumbsUpContainer.classList.add('thumbsUpActive'); // 좋아요 css 이벤트 작동
    } else if (thumbsDown === true) { // 유저가 해당 보드에 대해 싫어요가 true 이면
        thumbsDownContainer.classList.add('thumbsDownActive') // 싫어요 css 이벤트 작동
    }
}