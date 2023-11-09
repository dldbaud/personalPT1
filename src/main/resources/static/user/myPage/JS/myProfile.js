window.addEventListener('load', function () {
    console.log("두 번째 스크립트가 로드되었습니다.");

    // 두 번째 스크립트 내용

    if(document.getElementById("sellRegist")){
        
        const $sellRegist = document.getElementById("sellRegist");

        $sellRegist.onclick = function() {
            console.log("야")
            location.href="/user/sell/sellRegist";
        }
    }

    if(document.getElementById("myInfoUpdate")){

        const $myInfoUpdate = document.getElementById("myInfoUpdate");

        $myInfoUpdate.onclick = function() {
            location.href="/user/myProfile/update";
        }
    }
});