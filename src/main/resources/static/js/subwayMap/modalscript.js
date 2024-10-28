const svgContainer = document.getElementById("svgContainer");
const modal = document.getElementById("myModal");
const modalcontent = modal.querySelector(".modal-content")
const stationName = document.getElementById("stationName");
const lineNumber = document.getElementById("lineNumber");
const nowTime = document.getElementById("nowtime");
const directionType = document.getElementById("directionType")
const congestionLevel = document.getElementById("congestionLevel");
const closeModal = document.querySelector(".closeTab");

// 텍스트 클릭 이벤트 리스너 추가
svgContainer.addEventListener("click", async function(event) {
    const target = event.target;

    if (target.tagName === "text") {
        const clickedStation = target.textContent; // 클릭한 역 이름


        try {
            // 서버에서 데이터 가져오기
            const response = await fetch(`/station-info?stationName=${encodeURIComponent(clickedStation)}`);
            if (response.ok) {
                const data = await response.json();
                // 호선 설정
                lineNumber.innerText = `${data.station_line}호선`;
                //역이름 설정
                stationName.innerText = `${data.station_name}`;
                // 현재 시간 설정
                nowTime.innerText = new Date().toLocaleTimeString(); // 현재 시간
                // 방향 구분
                directionType.innerText = `${data.direction_type}`;
                // 혼잡도 정보 설정
                congestionLevel.innerText = `혼잡도 : ${data.congestion}`;


            } else {
                // 에러 처리(DB에 없는 역인 경우)
                lineNumber.innerText = "";
                stationName.innerText = "혼잡도 데이터가 없는 역입니다.";
                nowTime.innerText = "";
                directionType.innerText = "";
                congestionLevel.innerText = "";
            }
        } catch (error) {
            console.error("Error fetching station info:", error);
            lineNumber.innerText = "";
            stationName.innerText = "에러가 발생했습니다.";
            nowTime.innerText = "";
            directionType.innerText = "";
            congestionLevel.innerText = "";
        }

        // 마우스 클릭 위치를 기준으로 모달 위치 계산
        let modalX = event.clientX; // 클릭한 X좌표
        let modalY = event.clientY; // 클릭한 Y좌표에서 모달 높이만큼 위로 이동

        // 화면의 오른쪽 경계를 고려하여 모달 위치 조정
        const windowWidth = window.innerWidth;
        if (modalX + modal.offsetWidth > windowWidth) {
            modalX = windowWidth - modal.offsetWidth; // 오른쪽 경계에 맞추기
        }

        // 모달 위치 설정
        modal.style.left = `${modalX}px`;
        modal.style.top = `${modalY}px`;

        // 모달 가로 크기 설정
        modal.style.width = "20%";
        modal.style.display = "block"; // 모달 표시

    }
});

// 모달 닫기 기능
closeModal.addEventListener("click", function() {
    modal.style.display = "none"; // 모달 숨김
});

// 모달 외부 클릭 시 닫기
window.addEventListener("click", function(event) {
    if (event.target === modal) {
        modal.style.display = "none"; // 모달 숨김
    }
});

// ESC 키로 모달 닫기
window.addEventListener("keydown", function(event) {
    if (event.key === "Escape" && modal.style.display === "block") {
        modal.style.display = "none"; // 모달 숨김
    }
});
