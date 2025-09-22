const disableSpacebar = (event) => {
    if (event.code === "Space") {
        event.preventDefault();
    }
};
// 한글 입력 시 제거
const disableKorean = (event) => {
    const value = event.target.value;
    if (/[\u3131-\uD79D]/.test(value)) {
        // 한글이 포함되어 있다면 제거
        event.target.value = value.replace(/[\u3131-\uD79D]/g, '');
    }
};
const inputFields = document.getElementsByTagName("input");

window.addEventListener('load', function () {
    Array.from(inputFields).forEach(input => {
        input.addEventListener('keydown', disableSpacebar);
        input.addEventListener('input', disableKorean);
    });
});