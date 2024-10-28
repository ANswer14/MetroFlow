let nowZoom = 1; // 현재 비율 (1 = 100%)
let isDragging = false; // 드래그 상태
let startX, startY; // 드래그 시작 좌표
let scrollLeft, scrollTop; // 스크롤 위치

// 화면크기 축소
function zoomOut() {
    nowZoom -= 0.1; // 비율을 10% 줄임
    
    // 화면크기 최대 축소율 0.9 (90%)
    if (nowZoom < 0.9) {
        nowZoom = 0.9;
    }
    zooms();
}

// 화면크기 확대
function zoomIn() {
    nowZoom += 0.2; // 비율을 20% 늘림
    
    // 화면크기 최대 확대율 2 (200%)
    if (nowZoom > 3) {
        nowZoom = 3;
    }
    zooms();
}

// 원래 화면크기도 되돌아가기
function zoomReset() {
    nowZoom = 1; // 초기 비율로 리셋
    zooms();
}

function zooms() {
    // transform을 사용하여 비율 적용
    // document.getElementById("svgContainer").style.transform = `scale(${nowZoom})`;
    // document.getElementById("svgContainer").style.transformOrigin = "center"; // 확대/축소 기준점 설정
    svgContainer.style.transform = `scale(${nowZoom})`;
    svgContainer.style.transformOrigin = "left center"; // 확대/축소 기준점 설정
}

// 드래그 시작
function startDrag(e) {
    console.log("drag on")
    isDragging = true;
    startX = e.pageX - svgContainer.offsetLeft;
    startY = e.pageY - svgContainer.offsetTop;
    scrollLeft = svgContainer.scrollLeft;
    scrollTop = svgContainer.scrollTop;
}

// 드래그 중
function drag(e) {
    if (!isDragging) return;
    e.preventDefault(); // 기본 동작 방지

    const x = e.pageX - svgContainer.offsetLeft;
    const y = e.pageY - svgContainer.offsetTop;

    const walkX = (x - startX) * nowZoom; // 드래그 거리
    const walkY = (y - startY) * nowZoom;

    svgContainer.scrollLeft = scrollLeft - walkX; // 스크롤 위치 업데이트
    svgContainer.scrollTop = scrollTop - walkY;
}

// 드래그 종료
function endDrag() {
    console.log("drag off")
    isDragging = false;
}

// 이벤트 리스너 등록
svgContainer.addEventListener("mousedown", startDrag);
svgContainer.addEventListener("mousemove", drag);
svgContainer.addEventListener("mouseup", endDrag);
svgContainer.addEventListener("mouseleave", endDrag);

