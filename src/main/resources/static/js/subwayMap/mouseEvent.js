const subwayMap = document.getElementsByClassName('subwayMap')[0];

// 지하철 노선도 영역에 마우스 커서 진입시 커서 바뀌도록
subwayMap.addEventListener('mouseenter', () => {
    console.log("mouseEnter ON");
    subwayMap.style.cursor = 'url(/images/mouse/train_transparent.cur), auto';
});

subwayMap.addEventListener('mouseleave', () => {
    subwayMap.style.cursor = 'auto'; // 기본 커서로 돌아가기
});
