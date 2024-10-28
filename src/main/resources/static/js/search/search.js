// 역 이름 자동완성
$('#searchInput').on('input', function() {
    var query = $('#searchInput').val();

    // console.log(query);

    if (query.trim() === '') {
        $('#stationList').empty().hide();
        return;
    }

    $.ajax({
        type: 'GET',
        url: '/goSearch/stations',
        data: { search: query},
        dataType: 'json',
        success: function (data) {
            $('#stationList').empty();
            console.log(data);

            var seenStations = new Set();
            var maxItems = 5;
            var uniqueStations = $.grep(data, function(station) {  // $.grep: 배열에서 특정 항목만 걸러냄
                if (seenStations.has(station.stationName)) {
                    return false;
                }
                seenStations.add(station.stationName);
                return true;
            });

            uniqueStations.slice(0, maxItems).forEach(function(station) {
                $('#stationList').append('<li>' + station.stationName + '</li>').show();
            });
        },
        error: function(err) {
            console.log('오류: ', err);
            // alert('오류가 발생했습니다.');
        }
    });
});

$('#stationList').on('click', 'li', function() {
    var stationName = $(this).text();
    $('#searchInput').val(stationName);
    $('#searchInput').trigger('change');
    $('#stationList').hide().empty();
});

$(document).click(function(event) {
    // 리스트 이외의 다른 곳을 클릭했을때 리스트 사라짐
    if (!$(event.target).closest('#searchInput, #stationList').length) {
        console.log('리스트 숨기기')
        $('#stationList').hide().empty();
      }
});

$('#searchInput').on('keydown', function(event) {
    // Backspace 키가 눌렸을 때 확인 (keyCode 8은 Backspace)
    if (event.key === 'Backspace' || event.keyCode === 8) {
        // 키를 눌렀을 때, 입력 필드가 비었는지 확인
        setTimeout(function() {
            var query = $('#searchInput').val();

            if (query.trim() === '') {
                // searchInput이 비었을 때 실행할 코드
                $('#stationList').empty().hide();  // 예: 호선 리스트 비우기
                $('#searchInput').val('');      // searchInput 값 초기화
            }
        }, 0); // keydown 이벤트 후에 실행되도록 setTimeout으로 지연
    }
});

// 역이름을 넣었을때 호선에 대한 데이터가 바뀜
$('#searchInput').on('change', function() {
    var query = $('#searchInput').val();


    $.ajax({
        type: 'GET',
        url: '/goSearch/stationLines', // 역의 호선 정보를 가져오는 API
        data: { stationName: query }, // 역 이름을 전송
        dataType: 'json',
        success: function(data) {
            // 기존의 select 박스 비우기
            $('#stationLineList').empty();
            $('#stationLineList').append('<option value="">' + '호선' + '</option>');

            // 중복 제거를 위한 Set 사용
            var seenStationLine = new Set();

            // 고유한 stationLine들만 필터링
            var uniqueStationLine = $.grep(data,function(station) {
                if (seenStationLine.has(station.stationLine)) {
                    return false; // 중복되면 제외
                }
                seenStationLine.add(station.stationLine);
                return true; // 고유한 경우만 포함
            });

            console.log(uniqueStationLine);

            // 필터링된 호선 정보를 select 박스에 추가
            uniqueStationLine.forEach(function(station) {
                $('#stationLineList').append('<option value="' + station.stationLine + '">' + station.stationLine + '호선' + '</option>');
            });
        },
        error: function(err) {
            console.log(err);

        }
    });
});

// 역이름 호선 시간 데이터가 모두 들어가있으면 혼잡도 결과 보여주기
$('.search-btn2').on('click', function (event) {
    event.preventDefault();

    let stationName = $('#searchInput').val();
    let stationLine = $('#stationLineList').val();
    let ampm = $('#ampm').val();
    let hour = $('#hour').val();
    let minute = $('#minute').val();


    if (!stationName || !ampm || !hour || !minute || !stationLine) {
        alert('모든 값을 입력해주세요!');
        return;
    }

    // 결과창에 시간 표시
    $('#input-time').text(`${ampm} ${hour}시 ${minute}분`);

    $.ajax( {
        type: 'POST',
        url: `/goSearch/result`,
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            stationName:stationName,
            stationLine:stationLine,
            ampm: ampm,
            hour:hour,
            minute: minute
        }),
        beforeSend: function(xhr) {
            // CSRF 토큰과 헤더를 설정
            const csrfToken = $('meta[name="_csrf"]').attr('content');
            const csrfHeader = $('meta[name="_csrf_header"]').attr('content');
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function (response) {
            console.log('응답내용: ',response);

            let station = {
                stationName: stationName,
                stationLine: stationLine
            }

            displayResults(response);
            insertToLocalStorage(station);

            // 검색후에 값 초기화
            $('#stationLineList').empty();
            $('#stationLineList').append('<option value="">' + '호선' + '</option>');
            $('#searchInput').val('');
            $('#ampm').val('AM');
            $('#hour').val('');
            $('#minute').val('');

        },
        error: function(err) {
            console.log('에러: ', err);
            const messageElement = $('#result-container');
            messageElement.text('혼잡도 데이터가 없습니다').show();
            // messageElement.style.display = 'block';

            $('#stationLineList').empty();
            $('#stationLineList').append('<option value="">' + '호선' + '</option>');
            $('#searchInput').val('');
            $('#ampm').val('AM');
            $('#hour').val('');
            $('#minute').val('');
        }
    });
});


// 데이터를 받아서 HTML로 변환하는 함수
function displayResults(data) {
    $('#result-container').empty();  // 기존 결과 초기화
    // console.log('displayresults 호출됨: ', data);

    data.forEach(item => {
        // console.log('아이템: ', item);
        const statusColor = getStatusColor(item.congestion);
        const lineColor = lineColors[item.stationLine] || "#000000";

        const resultItem = `
            <div class="result-item">
                <div class="left-section">
                    <span class="line-number" style="background-color: ${lineColor};">${item.stationLine}</span>
                    <span class="station-name">${item.stationName}</span>
                </div>
                <div class="status-bar">
                    <span class="status-time">${item.directionType}</span>
                    <div class="progress-bar">
                        <div class="progress" style="width: 100%; background-color: ${statusColor};"></div>
                    </div>
                    <span class="status-label" style="color: ${statusColor};">${getCongestion(item.congestion)}</span>
                </div>
            </div>
        `;

        console.log('resultItem: ', resultItem);

        $('#result-container').append(resultItem);
    });
}

const lineColors = {
    1: "#0032A0", // 1호선
    2: "#00B140", // 2호선
    3: "#FC4C02", // 3호선
    4: "#00A9E0", // 4호선
    5: "#A05EB5", // 5호선
    6: "#A9431E", // 6호선
    7: "#67823A", // 7호선
    8: "#E31C79", // 8호선
    9: "#8C8279", // 9호선
};


function getStatusColor(congestion) {
    if (congestion <= 33) {
        return "green";    // 여유
    } else if (congestion > 33 && congestion <= 66) {
        return "orange"; // 보통
    } else if (congestion > 66) {
        return "red";  // 혼잡
    } else {
        return "gray";   // 기본값
    }
}

function getCongestion(congestion) {
    if (congestion <= 33) {
        return "여유";    // 여유
    } else if (congestion > 33 && congestion <= 66) {
        return "보통"; // 보통
    } else if (congestion > 66) {
        return "혼잡";  // 혼잡
    } else {
        return "오류";   // 기본값
    }
}

// GET방식으로 넘어온 파라미터 가져오기
function getQueryParam(params) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(params);
}

// 최근검색에서 가져온 데이터를 기반으로 칸 채우기
function setRecentSearch() {
    const stationName = getQueryParam('stationName');
    const stationLine = getQueryParam('stationLine');

    if (stationName) {
        $('#searchInput').val(stationName);
    }

    if (stationLine) {
        $('#stationLineList').append(`<option value="${stationLine}">${stationLine}호선</option>`);
        $('#stationLineList').val(stationLine)
    }
}

function insertToLocalStorage(station) {
    let storageCount = window.localStorage.length;
    let storage = window.localStorage;
    let JSONStation = JSON.stringify(station);
    if (storageCount === 0) {
        storage.setItem(1, JSONStation);
    } else if (storageCount === 1) {
        storage.setItem(2, storage.getItem(1));
        storage.removeItem(1);
        storage.setItem(1, JSONStation);
    } else if (storageCount === 2 || storageCount === 3) {
        storage.setItem(3, storage.getItem(2));
        console.log('2번 객체' + storage.getItem(2));
        storage.removeItem(2);
        storage.setItem(2, storage.getItem(1));
        storage.removeItem(1);
        storage.setItem(1, JSONStation);
    }

}

document.addEventListener('DOMContentLoaded', function() {
    setRecentSearch();
});



