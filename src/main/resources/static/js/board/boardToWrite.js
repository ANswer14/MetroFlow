const selectedStation = document.getElementById('stationName').value;
document.addEventListener('DOMContentLoaded', function () {
    const selectElement = document.getElementById('line-select');

    selectElement.addEventListener('change', function () {
        // 기존 클래스 제거
        selectElement.classList.remove(
            'line-1', 'line-2', 'line-3', 'line-4',
            'line-5', 'line-6', 'line-7', 'line-8', 'line-9'
        );

        // 선택된 값에 따라 클래스 추가
        switch (selectElement.value) {
            case '1호선':
                selectElement.classList.add('line-1');
                break;
            case '2호선':
                selectElement.classList.add('line-2');
                break;
            case '3호선':
                selectElement.classList.add('line-3');
                break;
            case '4호선':
                selectElement.classList.add('line-4');
                break;
            case '5호선':
                selectElement.classList.add('line-5');
                break;
            case '6호선':
                selectElement.classList.add('line-6');
                break;
            case '7호선':
                selectElement.classList.add('line-7');
                break;
            case '8호선':
                selectElement.classList.add('line-8');
                break;
            case '9호선':
                selectElement.classList.add('line-9');
                break;
            default:
                // 기본 배경색으로 변경
                selectElement.style.backgroundColor = 'white';
                selectElement.style.color = 'black';
        }
    });
});


// 자바스크립트를 사용해 폼이 제출될 때 호선 선택 확인 알람
document.getElementById('post-form').addEventListener('submit', function (event) {
    const lineSelect = document.getElementById('line-select');
    const stationSelect = document.getElementById('station-select');

    if (lineSelect.value === 'default') {
        event.preventDefault();
        alert('호선을 선택해주세요.');
    } else if (stationSelect.value === 'default') {
        event.preventDefault();
        alert('역을 선택해주세요.');
    }
});
document.getElementById('update-form').addEventListener('submit', function () {
    // 수정 버튼 클릭 시 첫 번째 form의 값을 업데이트
    document.getElementById('hidden-station-line').value = document.getElementById('line-select').value;
    document.getElementById('hidden-station-name').value = document.getElementById('station-select').value;
    document.getElementById('hidden-title').value = document.getElementById('title').value;
    document.getElementById('hidden-board-text').value = document.getElementById('text').value;
});

document.addEventListener('DOMContentLoaded', function () {
    const lineSelect = document.getElementById('line-select');
    const stationSelect = document.getElementById('station-select');
    // 초기 selectedLine 값 설정 (예: 1호선)
    const initialLine = lineSelect.value;
    updateStations(initialLine, stationSelect, lineSelect);
    stationSelect.value=selectedStation;

    lineSelect.addEventListener('change', function () { // 라인 바꿀 시
        const selectedLine = this.value; // 호선 값 호선 변경에따라 반영
        updateStations(selectedLine, stationSelect, lineSelect);
    });


});

// List 형태의 String 을 List 형태로
function toList(listString) {
    return listString.replace(/[\[\]\s]/g, '').split(',');
}

function updateStations(selectedLine, stationSelect, lineSelect) {
    stationSelect.innerHTML = '';

    const lineColors = {
        '1호선': '#0032a0',
        '2호선': '#00b140',
        '3호선': '#fc4c02',
        '4호선': '#00a9e0',
        '5호선': '#a05eb5',
        '6호선': '#a9431e',
        '7호선': '#67823a',
        '8호선': '#e31c79',
        '9호선': '#8c8279',
        'default': 'white' // 기본값
    };

    // 선택한 호선에 따라 배경색 변경
    if (selectedLine !== 'default') {
        lineSelect.style.backgroundColor = lineColors[selectedLine];
        lineSelect.style.color = '#fff'; // 텍스트 색상도 변경
    } else {
        lineSelect.style.backgroundColor = lineColors['default'];
        lineSelect.style.color = '#000'; // 기본 텍스트 색상
    }

    if (selectedLine !== 'default') {
        const stations = { // 타임리프로 받아온 호선당 역 이름들
            '1호선': toList(document.getElementById("lineOne").value),
            '2호선': toList(document.getElementById("lineTwo").value),
            '3호선': toList(document.getElementById("lineThree").value),
            '4호선': toList(document.getElementById("lineFour").value),
            '5호선': toList(document.getElementById("lineFive").value),
            '6호선': toList(document.getElementById("lineSix").value),
            '7호선': toList(document.getElementById("lineSeven").value),
            '8호선': toList(document.getElementById("lineEight").value),
            '9호선': toList(document.getElementById("lineNine").value)
        };

        stations[selectedLine].forEach(function (station) {
            const option = document.createElement('option'); // option 속성 생성
            option.value = station; // 역 명 옵션 값
            option.textContent = station; // 역 명 옵션 텍스트
            stationSelect.appendChild(option); // stationSelect에 option 노드 추가
        });
    } else {
        const defaultOption = document.createElement('option'); // option 속성 생성
        defaultOption.value = 'default'; // 기본 옵션 값
        defaultOption.textContent = '역 선택'; // 기본 옵션 텍스트
        stationSelect.appendChild(defaultOption); // 기본 옵션 추가
    }
}