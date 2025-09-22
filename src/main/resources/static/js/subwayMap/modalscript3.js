const svgContainer = document.getElementById("svgContainer");
const modal = document.getElementById("myModal");
const modalContent = modal.querySelector(".modal-content");
const modalBody = modalContent.querySelector(".modal-body table"); // 테이블을 직접 참조
const closeModal = document.querySelector(".closeTab");



// 즐겨찾기 모달
const favoriteModal = document.querySelector("#favoriteModal");
const favoriteCloseModal = document.querySelector(".favoriteCloseTab");

// SVG 네임스페이스 정의
const SVG_NS = "http://www.w3.org/2000/svg";
const XLINK_NS = "http://www.w3.org/1999/xlink";

document.addEventListener("DOMContentLoaded", function() {

    // 즐겨찾기 중복 검사를 위한 set
    let previousLines = new Set();

    // 텍스트 클릭 이벤트 리스너 추가
    svgContainer.addEventListener("click", async function (event) {
        // 모달창 중복 표시를 막기 위해 클릭시 기존 모달창 모두 숨김 처리
        hideModal(modal);
        hideModal(favoriteModal);

        // 즐겨찾기 별모양 버튼
        const favoriteStarButton = document.querySelector("#favorite");
        if (favoriteStarButton) {
            favoriteStarButton.style.color = "gray"; // 스타일 변경
        }

        const target = event.target;

        if (target.tagName === "text") {
            const clickedStation = target.textContent; // 클릭한 역 이름

            try {
                // 서버에서 데이터 가져오기
                const response = await fetch(`/station-info?stationName=${encodeURIComponent(clickedStation)}`);
                if (response.ok) {

                    const data = await response.json();
                    const stationInfoList = data.stationInfoList; // 리스트 정보
                    let isFavorite = data.isFavorite; // 즐겨찾기 등록 여부(Boolean값)
                    const favoriteStationIdList = data.favoriteStationIdList;
                    // 로그인하지 않은 유저의 경우 버튼 생성 X -> 예외처리 필요
                    if (favoriteStarButton) {
                        // 즐겨찾기 등록 검증 & 별 색깔 바꾸는 함수
                        isFavoriteAndChangeStar(isFavorite,favoriteStarButton);
                        
                        // 즐겨찾기 등록한 상태이면 별을 눌렀을 때 즐겨찾기 삭제가 동작해야함
                    }

                    // 혼잡도 정보 표시 로직
                    // 역 이름 설정
                    // 첫 번째 데이터에서 역 이름 추출
                    modalContent.querySelector("#stationName").innerText = stationInfoList[0].station_name; // 헤더에 역 이름 설정

                    // 현재 시간 설정
                    modalContent.querySelector(".nowTime").innerText =
                        new Date().toLocaleTimeString([], { hour: 'numeric', minute: 'numeric', hour12: true });

                    // modal 내용물 초기화
                    clearModal(modalBody);
                    // 즐겨찾기 모달 내용물 초기화
                    clearModal(favoriteModal);

                    const favoriteListIds = new Set();

                    // 즐겨찾기에 이미 등록한 호선인지 판단하기 위해 set에 즐겨찾기 station_id 정보를 담기
                    favoriteStationIdList.forEach(stationId =>{
                        favoriteListIds.add(stationId);
                    });

                    // 데이터 항목 추가
                    stationInfoList.forEach(stationInfo => {
                        const row = document.createElement("tr");

                        // 받아온 호선 -> #S1, #S2 형태로 포맷팅하는 함수
                        const station_line_svg = getStationLineSVG(stationInfo.station_line);

                        // use 요소 생성
                        const useElement = document.createElementNS(SVG_NS, "use");
                        // <use xlink="http://www.w3.org/1999/xlink"> 태그에 href="S1" 같은 속성을 추가하는 스크립트
                        useElement.setAttributeNS(XLINK_NS, "href", `${station_line_svg}`);

                        // y축 반전 변환 추가
                        useElement.setAttribute("transform", "scale(1, -1) translate(0, -1)"); // 15는 반전된 원의 높이의 절반

                        // overflow 옵션 ( 크기 넘칠 시 )
                        // useElement.setAttribute("style", "overflow:visible;");

                        // SVG 태그 생성
                        const svgWrapper = document.createElementNS(SVG_NS, "svg");
                        svgWrapper.setAttribute("viewBox", "0 -30 30 30"); // 파라미터: (min-x, min-y, width, height)
                        svgWrapper.setAttribute("style", "overflow:visible; width: 40px; height: 40px;"); // 크기를 조절
                        svgWrapper.appendChild(useElement); // SVG 요소를 래퍼에 추가

                        // 셀에 SVG 추가
                        const cellWithSvg = document.createElement("td");
                        cellWithSvg.appendChild(svgWrapper); // SVG 래퍼를 셀에 추가

                        row.appendChild(cellWithSvg); // 행에 셀 추가

                        row.innerHTML += `
                            <td id="td_direction_type">${stationInfo.direction_type}</td>
                            <td id="td_congestion">혼잡도 : ${stationInfo.congestion} %</td>
                        `;

                        // style 조정을 위한 접근
                        const svgCell = row.children[0]; // 역마크 svg
                        const directionCell = row.children[1]; // 방향
                        const congestionCell = row.children[2]; // 혼잡도

                        // style 조정
                        svgCell.style.width = "20%";

                        directionCell.style.width = "20%";
                        directionCell.style.textAlign = "left";

                        congestionCell.style.width = "60%";
                        congestionCell.style.textAlign = "left";

                        modalBody.appendChild(row); // 모달 테이블에 행 추가

                        // 즐겨찾기 모달 데이터 처리
                        const favoriteRow = document.createElement("tr");
                        const favoriteCell = document.createElement("td");
                        favoriteCell.className = "favoriteCell";

                        // 중복 검사 - 같은 호선이 모달창에 중복으로 나타나지 않도록 검사
                        // previousLines set에 이미 존재하는 데이터이면 추가하지 않음
                        if (!previousLines.has(stationInfo.station_line)) {
                            favoriteCell.innerHTML = `${stationInfo.station_line}호선`; // 호선 정보 추가
                            // 여기 원래 행 추가 있었음 (tr)
                            previousLines.add(stationInfo.station_line); 

                            // 추가할 대상 즐겨찾기 모달
                            const favoriteModalBody = favoriteModal.querySelector("table");

                            // 즐겨찾기 데이터 목록과 같은 station_id가 있다면 이미 즐겨찾기로 등록한 호선임
                            // favoriteListIds.has(stationInfo.station_id) 가 false 인 경우
                            // td 클릭으로 ajax 요청 보낼 이벤트 추가
                            // 즐겨찾기 등록 호출 AJAX 요청
                            // 즐겨찾기 td 클릭시 post 요청 보내는 이벤트

                            // favoriteListIds.has(stationInfo.station_id) 가 true 인 경우
                            // td 클릭으로 ajax 요청 보낼 이벤트 추가
                            // 즐겨찾기 삭제 AJAX 요청

                            // 이벤트 핸들러를 다르게 추가해야함!!

                            // 즐겨찾기로 등록된 호선인 경우
                            // 삭제 이벤트 등록
                            // td 글자 색상 gold로 변경
                            if (favoriteListIds.has(stationInfo.station_id)){
                                // console.log("즐겨찾기 등록된 경우 처리 : "+stationInfo.station_id);

                                // 즐겨찾는 호선 -> 글자 색상 gold로 변경
                                favoriteCell.style.color="gold";

                                // 즐겨찾기 삭제 이벤트 리스너 등록
                                favoriteCell.addEventListener("click", async function(){
                                    console.log("즐겨찾기 삭제 : 이벤트 리스너 정상 동작")

                                    // 확인창 출력
                                    const userConfirmed = confirm("즐겨찾기를 삭제하시겠습니까?");
                                    if(userConfirmed){
                                        // POST 요청을 위한 데이터
                                        const postData = {
                                            station_id: stationInfo.station_id
                                        };
                                        try {
                                            //csrf 토큰 -> 포함 안하면 POST 요청을 SpringSecurity에서 걸러버림
                                            const csrfToken = document.querySelector('meta[name="_csrf"]').content;
                                            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
                                            console.log("Token : "+ csrfToken);
                                            console.log("csrfHeader : " + csrfHeader);
                                            // fetch를 이용한 POST 요청
                                            const FavoriteResponse = await fetch('/deleteFromFavoriteList', {
                                                method: 'POST',
                                                headers: {
                                                    'Content-Type': 'application/json',
                                                    [csrfHeader]: csrfToken
                                                },
                                                body: JSON.stringify(postData) // 데이터 JSON 문자열로 변환
                                            });

                                            // 응답 상태에 따라 다르게 처리
                                            if (FavoriteResponse.ok) {
                                                hideModal(favoriteModal);
                                                alert("즐겨찾기 삭제 완료");
                                                const FavoriteResponseData = await FavoriteResponse.json(); // 성공적인 응답이면 JSON으로 변환
                                                console.log('Success:', FavoriteResponseData); // 성공적으로 응답을 받았을 때 처리
                                                window.location.href = '/home';
                                            } else {
                                                // 오류 처리: 상태 코드에 따라 다른 메시지 출력 가능
                                                console.error('Error:', FavoriteResponse.status, FavoriteResponse.statusText);
                                            }
                                        } catch (error) {
                                            console.error('Fetch error:', error); // 오류 처리
                                        }

                                    }
                                    else{
                                        alert("삭제를 취소하셨습니다.");
                                        window.location.href = '/home';
                                    }
                                });

                                // 즐겨찾기 모달 테이블에 행 추가
                                favoriteRow.appendChild(favoriteCell);
                                favoriteModalBody.appendChild(favoriteRow);
                            }
                            // 즐겨찾기 등록으로 이벤트 리스너 설정
                            else {

                                // 즐겨찾기 모달 테이블에 행 추가
                                favoriteRow.appendChild(favoriteCell);
                                favoriteModalBody.appendChild(favoriteRow);

                                favoriteCell.addEventListener("click", async function() {
                                    console.log("stationName : " + favoriteCell.textContent);
                                    console.log("station_id : " + stationInfo.station_id);

                                    const userConfirmed = confirm("즐겨찾기로 등록하시겠습니까?");
                                    if(userConfirmed){
                                        // POST 요청을 위한 데이터
                                        const postData = {
                                            station_id: stationInfo.station_id
                                        };

                                        try {
                                            //csrf 토큰 -> 포함 안하면 POST 요청을 SpringSecurity에서 걸러버림
                                            const csrfToken = document.querySelector('meta[name="_csrf"]').content;
                                            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
                                            console.log("Token : "+ csrfToken);
                                            console.log("csrfHeader : " + csrfHeader);
                                            // fetch를 이용한 POST 요청
                                            const FavoriteResponse = await fetch('/addToFavorite', {
                                                method: 'POST',
                                                headers: {
                                                    'Content-Type': 'application/json',
                                                    [csrfHeader]: csrfToken
                                                },
                                                body: JSON.stringify(postData) // 데이터 JSON 문자열로 변환
                                            });

                                            // 응답 상태에 따라 다르게 처리
                                            // 즐겨찾기 등록 성공시 바뀌어야 할 상태
                                            // 별 버튼 색상, 별 버튼 연결된 이벤트 리스너, isFavorite 값
                                            if (FavoriteResponse.ok) {
                                                isFavoriteAndChangeStar(true,favoriteStarButton);
                                                isFavorite=true;
                                                hideModal(favoriteModal);
                                                alert("즐겨찾기 등록 완료")
                                                const FavoriteResponseData = await FavoriteResponse.json(); // 성공적인 응답이면 JSON으로 변환
                                                console.log('Success:', FavoriteResponseData); // 성공적으로 응답을 받았을 때 처리
                                                window.location.href = '/home';
                                            } else {
                                                // 오류 처리: 상태 코드에 따라 다른 메시지 출력 가능
                                                console.error('Error:', FavoriteResponse.status, FavoriteResponse.statusText);
                                            }
                                        } catch (error) {
                                            console.error('Fetch error:', error); // 오류 처리
                                        }
                                    }
                                    else{
                                        alert("즐겨찾기 등록을 취소하셨습니다.")
                                        window.location.href = '/home';
                                    }

                                });
                            }

                        }

                    });
                    // 즐겨찾기 모달용 중복검사 변수 내용 초기화
                    previousLines =new Set();
                    showModal(modal,550);

                // 불러올 데이터가 없는 역 예외처리
                } else {
                    modalContent.querySelector("#stationName").innerText = clickedStation;
                    // modal 테이블 내용물 초기화
                    clearModal(modalBody);
                    // 혼잡도 정보가 없는 역에 대한 메시지 추가
                    addNoInfoMessage(modalBody,"혼잡도 정보가 없는 역입니다.")
                    // 모달 보이도록 좌표, 표시 속성 설정
                    showModal(modal,550);
                    console.error("Station has no information: ", response.statusText);
                }
            } catch (error) {
                modalContent.querySelector("#stationName").innerText = clickedStation;
                // modal 테이블 내용물 초기화
                clearModal(modalBody);
                // 혼잡도 정보가 없는 역에 대한 메시지 추가
                addNoInfoMessage(modalBody,"혼잡도 정보가 없는 역입니다.")
                // 모달 보이도록 좌표, 표시 속성 설정
                showModal(modal,750);
                console.error("Error at script : ",error);
            }
        }
    });

    // 즐겨찾기 버튼 누르면 모달창 뜨도록 하는 이벤트
    const favoriteButton = document.getElementById("favorite");
    // 로그인 하지 않은 상태에서도 동작하는 스크립트라서 favoriteButton null인 경우가 있음
    if (favoriteButton) {
        // 즐겨찾기 버튼 클릭 이벤트 리스너 추가
        favoriteButton.addEventListener("click", function () {
            hideModal(favoriteModal);
            showFavoriteModal(favoriteModal,400);
        });
    }

});

// 모달 닫기 기능
closeModal.addEventListener("click", function() {
    modal.style.display = "none"; // 모달 숨김
    favoriteModal.style.display= "none";

    // modal 내용물 초기화
    clearModal(modalBody);
    // 즐겨찾기 모달 내용물 초기화
    clearModal(favoriteModal);
});

// 즐겨찾기 모달 닫기 기능
favoriteCloseModal.addEventListener("click", function(){
   favoriteModal.style.display= "none";
    clearModal(favoriteModal);
});

// 모달 외부 클릭 시 닫기
window.addEventListener("click", function(event) {
    if (event.target === modal) {
        modal.style.display = "none"; // 모달 숨김
        favoriteModal.style.display= "none";

        // modal 내용물 초기화
        clearModal(modalBody);
        // 즐겨찾기 모달 내용물 초기화
        clearModal(favoriteModal);
    }
});

// ESC 키로 모달 닫기
window.addEventListener("keydown", function(event) {
    if (event.key === "Escape" && modal.style.display === "block") {
        modal.style.display = "none"; // 모달 숨김
        favoriteModal.style.display = "none";

        // modal 내용물 초기화
        clearModal(modalBody);
        // 즐겨찾기 모달 내용물 초기화
        clearModal(favoriteModal);
    }
});

// 호선 정보를 받아서 String으로 포맷팅해서 반환
function getStationLineSVG(station_line) {
    return `#S${station_line}`;
}

//모달창 테이블 초기화 함수
function clearModal(modalBody){
    // 기존 내용 초기화 (테이블 헤더 제외)
    const existingRows = modalBody.querySelectorAll("tr"); // 테이블 row 초기화
    existingRows.forEach(row => row.remove()); // 기존 행 제거
}

// 모달창 표시 함수
function showModal(modal, modalWidth) {
    // 화면 중앙 좌표 받아오기
    const windowWidth = window.innerWidth;
    const windowHeight = window.innerHeight;

    // 모달 위치 보정
    modal.style.left = `${((windowWidth - modalWidth) / 2) - 280}px`; // x 위치
    modal.style.top = `${((windowHeight - modal.offsetHeight) / 2) - 300}px`; // y 위치

    // 모달 표시
    modal.style.width = `${modalWidth}px`;
    modal.style.display = "block";
}

// 모달창 숨기기 함수
function hideModal(modal){
    modal.style.display="none"
}

// 정보가 없는 역 접근시 메시지 설정용 함수
function addNoInfoMessage(modalBody, message) {
    const row = document.createElement("tr"); // 새로운 행 생성
    const msgCell = document.createElement("td"); // 새로운 셀 생성
    msgCell.id = "td_noInfo"; // ID 설정
    msgCell.innerText = message; // 텍스트 설정

    row.appendChild(msgCell); // 셀을 행에 추가
    modalBody.appendChild(row); // 행을 모달 본문에 추가
}

// 즐겨찾기 모달창 표시 함수
function showFavoriteModal(modal, modalWidth) {
    // 화면 중앙 좌표 받아오기
    const windowWidth = window.innerWidth;
    const windowHeight = window.innerHeight;

    // 모달 위치 보정
    modal.style.left = `${((windowWidth - modalWidth) / 2) - 280 + 500}px`; // x 위치
    modal.style.top = `${((windowHeight - modal.offsetHeight) / 2) - 300}px`; // y 위치

    // 모달 표시
    modal.style.width = `${modalWidth}px`;
    modal.style.display = "flex";
}

// 즐겨찾기 별 모양 색 변환 함수
function isFavoriteAndChangeStar(booleanValue, button){
    // 즐겨찾기 등록 여부 판단 로직
    if (typeof booleanValue !== 'undefined') {
        if (booleanValue) {
            console.log("true boolean : "+booleanValue);
            button.style.color = "gold";
        } else {
            console.log("false boolean : "+booleanValue);
            button.style.color = "gray";
        }
    } else {
        console.log("Boolean 변수가 정의되지 않았습니다.");
    }
}



