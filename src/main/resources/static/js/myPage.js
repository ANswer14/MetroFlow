// 스페이스바 사용 금지 로직
const disableSpacebar = (event) => {
    if (event.code === "Space") {
        event.preventDefault();
    }
};

// 한글 입력 시 한글 제거

const disableKorean = (event) => {
    console.log("어글리코리안")
    const value = event.target.value;
    console.log(value)
    if (/[\u3131-\uD79D]/.test(value)) {
        // 한글이 포함되어 있다면 제거
        event.target.value = value.replace(/[\u3131-\uD79D]/g, '');
    }
};

document.addEventListener("DOMContentLoaded", function () {
    const inputFields = document.getElementsByTagName("input");
    // 한국어, 스페이스바 방지(닉네임 칸 제외)
    const nickname = document.getElementById('nickname');
    disableKoreanAndSpaceBar(inputFields, nickname);

    // 프로필 사진 변경처리
    const modal = document.getElementById("profileModal");
    const openModalButton = document.getElementById("openModalButton");
    const closeModal = document.querySelector(".close");
    const profilePic = document.getElementById("profilePic");
    const selectableImages = document.querySelectorAll(".selectable-image");
    const selectedProfilePicInput = document.getElementById("selectedProfilePic");

    injectValue(selectedProfilePicInput, profilePic);

    // 모달 열기
    openModalButton.addEventListener("click", function () {
        modal.style.display = "block";
    });

    // 모달 닫기
    closeModal.addEventListener("click", function () {
        modal.style.display = "none";
    });

    // 이미지 선택
    selectableImages.forEach(function (image) {
        image.addEventListener("click", function () {
            selectableImages.forEach(img => img.classList.remove("selected"));
            image.classList.add("selected");
            profilePic.src = image.src;  // 선택한 이미지를 프로필로 미리보기
            selectedProfilePicInput.value = image.src;  // 선택한 이미지 경로를 hidden input에 저장
        });
    });

    // 선택한 이미지 저장
    document.getElementById("changeProfilePic").addEventListener("click", function () {
        modal.style.display = "none";  // 모달 닫기
    });

    // 회원 탈퇴 확인 처리
    document.getElementById('deleteAccount').addEventListener('click', function(event) {
        event.preventDefault();
        if (confirm('정말로 회원을 탈퇴하시겠습니까?')) {
            window.location.href = '/user/delete';
        }
    });

});

// 초기 이미지 저장 로직
function injectValue(pathValue, profilePath) {
    pathValue.value = profilePath.src;
}


// nickname 필드를 제외하고 한글과 스페이스바 입력을 막는 함수
function disableKoreanAndSpaceBar(inputFields, nickname) {
    Array.from(inputFields).forEach(input => {
        console.log("input : " + input)
        console.log("nickname : " + nickname)
        input.addEventListener('keydown', disableSpacebar);
        if (input !== nickname) {
            console.log(input)
            input.addEventListener('input', disableKorean);
        }
    });
}