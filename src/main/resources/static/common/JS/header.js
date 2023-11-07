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
    
    if(document.getElementById("regist")) {
		const $regist = document.getElementById("regist");
		$regist.onclick = function() {
			location.href = "/user/regist";
		}
	}
    
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
                .then(result => alert(result))
                .catch((error) => error.text().then((res) => alert(res)));
        }
    }

    const $menu = document.querySelector('.main-nav');
    const $login = document.querySelector('.login');
    const $toggleB = document.querySelector('.hma');

    $toggleB.addEventListener('click', () => {
        $menu.classList.toggle('active');
        $login.classList.toggle('active');

})

}