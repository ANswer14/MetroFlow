const svgContainer = document.getElementById("svgContainer");
const modal = document.getElementById("myModal");
const modalContent = modal.querySelector(".modal-content");
const modalBody = modalContent.querySelector(".modal-body table"); // 테이블을 직접 참조
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

                // 역 이름 설정
                const stationName = data[0].station_name; // 첫 번째 데이터에서 역 이름 추출
                modalContent.querySelector("#stationName").innerText = stationName; // 헤더에 역 이름 설정

                // 기존 내용 초기화 (테이블 헤더 제외)
                const existingRows = modalBody.querySelectorAll("tr:not(:first-child)"); // 첫 번째 행을 제외하고 제거
                existingRows.forEach(row => row.remove()); // 기존 행 제거

                // 데이터 항목 추가
                data.forEach(stationInfo => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                        <td>${stationInfo.station_line}호선</td>
                        <td>${new Date().toLocaleTimeString()}</td>
                        <td>${stationInfo.direction_type}</td>
                        <td>혼잡도: ${stationInfo.congestion}</td>
                    `;
                    modalBody.appendChild(row);
                });

                // 마우스 클릭 위치를 기준으로 모달 위치 계산
                let modalX = event.clientX; // 클릭한 X좌표
                let modalY = event.clientY; // 클릭한 Y좌표

                // 화면의 오른쪽 경계를 고려하여 모달 위치 조정
                const windowWidth = window.innerWidth;
                if (modalX + modal.offsetWidth > windowWidth) {
                    modalX = windowWidth - modal.offsetWidth; // 오른쪽 경계에 맞추기
                }

                // 모달 위치 설정
                modal.style.left = `${modalX}px`;
                modal.style.top = `${modalY}px`;

                // 모달 표시
                modal.style.width = "750px";
                modal.style.display = "block";

            } else {
                console.error("Error fetching station info: ", response.statusText);
            }
        } catch (error) {
            console.error("Error fetching station info:", error);
        }
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
