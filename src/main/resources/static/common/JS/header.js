window.onload = function () {
    
    if(document.getElementById("login")) {
        const $login = document.getElementById("login");
        $login.onclick = function() {
            location.href = "/user/login";
        }
    }

    if(document.getElementById("logout")) {
        const $logout = document.getElementById("logout");
        $logout.onclick = function() {
            location.href = "/user/logout";
        }
    }
    
    /* 가입하기 버튼 */
    if(document.getElementById("regist")) {
		const $regist = document.getElementById("regist");
		$regist.onclick = function() {
			location.href = "/user/regist";
		}
	}
    
    /* 아이디 중복 체크 */
    let isDuplicated = false;

    if(document.getElementById("duplicationCheck")){
        const $duplicationCheck = document.getElementById("duplicationCheck");

        $duplicationCheck.onclick = function() {
            let id = document.getElementById("id").value.trim();

            fetch("/user/idDupCheck", {
                method : "POST",
                headers: {
                    'Content-Type' : 'application/json; charset=UTF-8'
                },
                body: JSON.stringify({id : id})
            })
                .then(result => result.text())
                .then(result => {
                    
                    alert(result)
                    isDuplicated = true;
                })
                .catch((error) => error.text().then((res) => alert(res)));
        }
    }

    /* 가입하기 아이디 중복 확인 여부 */
    if(document.getElementById("registInfo")){
        document.getElementById("registInfo").addEventListener('submit', function(event) {
            // 여기서 추가적인 로직을 처리하고 폼 제출을 막을 수 있습니다.
            if (isDuplicated) {
                // 중복 확인이 완료되었을 때 폼을 제출합니다.
                this.submit();
            } else {
                // 중복 확인이 되지 않았을 때 폼 제출을 막습니다.
                alert("아이디 중복을 확인하세요");
                event.preventDefault();
            }
        });
    }

    /* 프로필 이동  */
	if(document.getElementById("myProfile")){
		
		const $myProfile = document.getElementById("myProfile")
		
		$myProfile.onclick = function(){
			location.href="/user/myProfile";
		}
	}

    /* 동의하기 확인 */
    if(document.getElementById("agreeRegist")){

        const $agreeRegist = document.getElementById("agreeRegist");
        const $check =  document.getElementById("agreeCheckbox");
        const $isCheck = $check.value;

        $agreeRegist.addEventListener('click', function(){
            const $isCheck = $check.checked;
    
            if($isCheck){
                location.href="/user/agreeRegist";
            } else {
                alert("동의하기 버튼을 체크해주세요");
                console.log($isCheck);
            }
        });
    }

    /* 헤더 버튼 */
    const $menu = document.querySelector('.main-nav');
    const $login = document.querySelector('.login');
    const $toggleB = document.querySelector('.hma');

    $toggleB.addEventListener('click', () => {
        $menu.classList.toggle('active');
        $login.classList.toggle('active');

})

}