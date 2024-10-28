

document.addEventListener('DOMContentLoaded', function () {
    const recentSearchList = document.getElementById('search-list');
    const storageCount = window.localStorage.length;
    for (let i = 1; i < storageCount + 1; i++) {
        let recentSearch = JSON.parse(window.localStorage.getItem(i));
        console.log(recentSearch);
        recentSearchList.innerHTML += `<li><a href="/goSearch?stationName=${recentSearch.stationName}&stationLine=${recentSearch.stationLine}">
            ${recentSearch.stationName}역 ${recentSearch.stationLine}호선</a></li><br>`
    }
})

