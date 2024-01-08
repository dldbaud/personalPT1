window.onload = function () {

    if (document.getElementById("login")) {
        const $login = document.getElementById("login");
        $login.onclick = function () {
            location.href = "/user/login";
        }
    }

    if (document.getElementById("logout")) {
        const $logout = document.getElementById("logout");
        $logout.onclick = function () {
            location.href = "/user/logout";
        }
    }

    /* 가입하기 버튼 */
    if (document.getElementById("regist")) {
        const $regist = document.getElementById("regist");
        $regist.onclick = function () {
            location.href = "/user/regist";
        }
    }

    /* 아이디 중복 체크 */
    let isDuplicated = false;
    let isVerify = false;
    if (document.getElementById("duplicationCheck")) {
        const $duplicationCheck = document.getElementById("duplicationCheck");

        $duplicationCheck.onclick = function () {
            const id = document.getElementById("id").value.trim();
            // \s공백  +&은 a + b + c 식으로 끝나야 함 
            const idCheck = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if(!(idCheck.test(id))){
                alert("이메일 형식의 아이디를 입력해주세요");
                return;
            }
            
            if(id === ""){
                alert("아이디를 입력하세요");
            } else {
                fetch("/user/idDupCheck", {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    },
                    //ajax는 data fetch는 body
                    body: JSON.stringify({ id: id })
                })  //펫치에서만 result.text() 사용 데이터를 문자열로 바로 꺼낼떄
                    .then(result => result.text())
                    .then(result => {
    
                        alert(result)
                        if(result == "사용 가능한 아이디입니다."){
                            isDuplicated = true;
                        }
                    })
                    .catch((error) => error.text().then((res) => alert(res)));
            }
        }

        // /*이메일 인증 감은 잡았으니 보류 이메일에 새로운 인증 링크를 보내고 버튼에 비동기를 달고 그 값을 세션으로 활용하면 됨 */
        // const $Verify = document.getElementById("Verify");

        // $Verify.onclick = function () {

        //     if(isDuplicated){
        //         let id = document.getElementById("id").value.trim();
    
        //         fetch("/user/Verify", {
        //             method: "POST",
        //             headers: {
        //                 'Content-Type': 'application/json; charset=UTF-8'
        //             },
        //             body: JSON.stringify({ id: id })
        //         })
        //             .then(result => result.text())
        //             .then(result => {
    
        //                 alert(result)
        //                 isVerify = true;
        //             })
        //             .catch((error) => error.text().then((res) => alert(res)));
        //     } else {
        //         alert("아이디 중복을 확인하세요");
        //     }
        // }
    }
    
    /* 가입하기 아이디 중복 확인 여부 */
    if (document.getElementById("registInfo")) {
        document.getElementById("registInfo").addEventListener('submit', function (event) {
            // 서브밋 전에 검사
            if (isDuplicated && isVerify) {
                // 중복 확인이 완료되었을 때 폼을 제출.
                this.submit();
            } else if(!(isDuplicated)){
                // 중복 확인이 되지 않았을 때 폼 제출 안함.
                alert("아이디 중복을 확인하세요");
                event.preventDefault();
            } else if(!(isVerify)){
                alert("아이디 인증을 확인하세요");
                event.preventDefault();
            }
        });
    }

    /* 프로필 이동  */
    if (document.getElementById("myProfile")) {

        const $myProfile = document.getElementById("myProfile")

        $myProfile.onclick = function () {
            location.href = "/user/myProfile";
        }
    }

    /* 동의하기 확인 */
    if (document.getElementById("agreeRegist")) {

        const $agreeRegist = document.getElementById("agreeRegist");
        const $check = document.getElementById("agreeCheckbox");
        const $isCheck = $check.value;

        $agreeRegist.addEventListener('click', function () {
            const $isCheck = $check.checked;

            if ($isCheck) {
                location.href = "/user/agreeRegist";
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

    if (document.getElementById('toggleB')) {
        $toggleB.addEventListener('click', () => {
            console.log('메뉴버튼 클릭');
            $menu.classList.toggle('active');
            $login.classList.toggle('active');

        })
    }

    if (document.getElementById('adminButton')) {

        $adminButton = document.getElementById('adminButton');

        $adminButton.onclick = function () {
            location.href = "/admin/main";
        }
    }

    if (document.getElementById('sellButton')) {

        $adminButton = document.getElementById('sellButton');

        $adminButton.onclick = function () {
            location.href = "/sell/manage";
        }
    }

    if (document.getElementById('findId')) {

        $findId = document.getElementById('findId');

        $findId.onclick = function () {
            location.href = "/user/findId";
        }
    }

    if (document.getElementById('findPwd')) {

        $findPwd = document.getElementById('findPwd');

        $findPwd.onclick = function () {
            location.href = "/user/findPwd";
        }

    }

    if (document.getElementById('findPwd2')) {
        console.log('로드 됨');
        $findPwd2 = document.getElementById('findPwd2');

       // 감은 잡았으니 보류 일단 sns 홈페이지로 보내주는 방법을 씀
        $findPwd2.onclick = function () {
            mailsend();
        }

        function mailsend() {
            const phone = document.getElementById("phone").value;
            const id = document.getElementById("id").value;
            const email = document.getElementById("email").value;

            $.ajax({
                url : "/user/mailsend",
                type : "POST",
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                data: JSON.stringify({ "id" : id,
                           "phone" : phone,
                           "email" : email })
                })
                .then(result => {
                    console.log("result : "+ result);
                    if(result == "google") {
                        location.href = `https://accounts.google.com/signin/recovery`;
                    }else if(result == "naver"){
                        location.href = `https://nid.naver.com/user2/help/myInfo.nhn?menu=reissuePassword`;
                    }else if(result == "null"){
                        alert("회원정보가 없습니다");
                    }else {
                        alert("해당 이메일로 임시 비밀번호를 발송 하였습니다. \n 확인부탁드립니다.")
                        console.log("result : "+ result);
                    }
                })
                .catch((error) => error.text().then((res) => alert(res)));;
        }
    }

    /* 카테고리목록 이벤트 */
    if (document.getElementsByClassName('.refList')) {

        const $refList = document.querySelectorAll('.refList');
        const $categoryList = document.querySelectorAll('.categoryList');
        const $nav = document.querySelector('.main-nav');
        let currentCategoryList = null;

        $categoryList.forEach(item => {
            item.style.display = "none";
        });
        changeCategoryEvent();

        window.addEventListener('resize', function () {
            removeEventListeners(); // 화면 사이즈 변경 시 이전 리스너 제거
            changeCategoryEvent();
        });

        function changeCategoryEvent() {
            if (window.innerWidth > 760) {
                /* 큰 카테고리 */
                $refList.forEach(item => {
                    item.addEventListener('mouseover', categoryMouseOver);
                    item.addEventListener('click', refCategoryClick);
                });

                $nav.addEventListener('mouseout', categoryMouseOut);

                /* 작은 카테고리 */
                $categoryList.forEach(item => {
                    item.addEventListener('click', categoryListClick)
                });
            } else {
                $refList.forEach(item => {
                    item.addEventListener('click', mobileRefCategoryClick);
                });
            }
        }

        function removeEventListeners() {
            $refList.forEach(item => {
                item.removeEventListener('mouseover', categoryMouseOver);
                item.removeEventListener('click', refCategoryClick);
                item.removeEventListener('click', mobileRefCategoryClick);
            });

            $nav.removeEventListener('mouseout', categoryMouseOut);

            /* 작은 카테고리 */
            $categoryList.forEach(item => {
                item.removeEventListener('click', categoryListClick)
            });
        }

        function categoryMouseOver() {
            console.log('마우스오버확인');
            const $childCategoryLists = this.nextElementSibling.querySelectorAll('.categoryList');
            console.log($childCategoryLists);

            $categoryList.forEach(item => {
                item.style.display = "none";
            });

            $childCategoryLists.forEach(item => {
                item.style.display = "block";
            });
        }

        /* 큰 카테고리  */
        function refCategoryClick(event) {
            searchCondition = event.target.textContent;
            console.log('클릭확인', searchCondition);

            currentPage = 1;
            load = true;
            /* 수정필요 메인 페이지에서는 파일을 처음부터 비동기로 불러오기 때문에 이 동작으로 메인페이지 이동시 searchCondition 값 초기화가 안됨
            그러므로 카테고리를 가져오는 것 처럼 메인에 넘어갈때 정적인 파일을 보내고 클릭시 정적인 파일들을 받은 다음에 스크롤 시에는 
            비동기를 이용해 inerHTML= ''를 하여 지우고 다시 html 요소들을 추가해 줘야 할 것 같음 (댓글 처럼)*/
            if (document.getElementById('productDetail')) {

                location.href = `/?condition=${searchCondition}`;
                
            } else {
                // foodDetailHtml이 없는 경우 바로 loadProducts 호출
                loadProducts(searchCondition);
            }
        }

        /* 펼치기 접기 */
        function mobileRefCategoryClick() {
            console.log('클릭확인');

            //categoryList 의 다음 형제 요소 즉 All이 없으면 1=> 3으로 안감
            const $childCategoryLists = this.nextElementSibling.querySelectorAll('.categoryList');
            console.log($childCategoryLists);

            $childCategoryLists.forEach(item => {
                if (item.style.display === "block") {
                    item.style.display = "none";
                } else {
                    item.style.display = "block";
                }
            });
        }

        function categoryMouseOut(event) {
            // 마우스 이벤트가 발생한 요소와 그 자식 요소를 벗어나면 트루
            if (!$nav.contains(event.relatedTarget)) {
                $categoryList.forEach(item => {
                    item.style.display = "none";
                });
            }
        }

        /* 작은 카테고리 */
        function categoryListClick(event){
            // preventDefault(event);
            searchCondition = 'category';
            searchValue = event.target.textContent;
            console.log('클릭확인', searchValue);
            currentPage = 1;
            load = true;
            if (document.getElementById('productDetail')) {

                location.href = `/?condition=${searchCondition}&searchValue=${searchValue}`;
                
            } else {
                // foodDetailHtml이 없는 경우 바로 loadProducts 호출
                loadProducts(searchCondition, searchValue);
            }
        }

    }

    /* 파일 비동기 통신 */

    let currentPage = 1;
    let searchCondition = 'null';
    let searchValue ='null';
    const $categoryCode = $(".outBox");
    let load = false;

    if(document.getElementById('outBox')){

        /* header에있는 검색어를 가져옴 */
        const urlParams = new URLSearchParams(window.location.search);
        const condition = urlParams.get('condition');
        const $searchValue = urlParams.get('searchValue');
    
        if (condition) {
            // 조건이 있다면 해당 파일을 비동기적으로 불러옴 여기서 스크롤시 동작 되긴하는데 이상함 구상을 잘 못 한 거같음 208줄 참고
            load = true;
            searchCondition = condition;
            searchValue = $searchValue;
            loadProducts(searchCondition,searchValue);
            console.log('새로고침안됨');
        } else {
            //로고 버튼을 클릭해야 여기로 이동 구상을 잘 못 한 거같음
            loadProducts(searchCondition,searchValue);
            console.log('새로고침확인');
        }
        function loadProducts(searchCondition, searchValue) {
            
            if (searchCondition == 'null') {
                if(searchValue != 'null' && load === true) {
                    console.log('이게뭐야야야양');
                    $categoryCode.empty();
                }
                $.ajax({
                    url: `/user/listView?page=${currentPage}&searchCondition=${searchCondition}&searchValue=${searchValue}`,
                    success: function (data) {
                        console.log(data);
                        startListView(data.data.boardList);
                    },
                    error: function (xhr) {
                        console.log(xhr);
                    }
                });
                load = false;
            } else if (searchCondition != 'null' || searchValue != 'null') {
                if (load) {
                    $categoryCode.empty();
                }
                $.ajax({
                    url: `/user/listView?page=${currentPage}&searchCondition=${searchCondition}&searchValue=${searchValue}`,
                    success: function (data) {
                        console.log(data);
                        startListView(data.data.boardList);
                    },
                    error: function (xhr) {
                        console.log(xhr);
                    }
                });
                load = false;
            }

        }
        
        function startListView(data) {
    
            const $categoryCode = $(".outBox");
    
            for (let index of data) {
                const $newDivItem = document.createElement('div');
                $newDivItem.classList.add('itemBox');
                console.log(index.listNm);
    
                const $img = new Image();
                $img.width = "70%";
                $img.height = "70%";
    
                // FileDto 객체 생성 및 이미지 파일 경로 설정
                const imgSrc = index.fileMain && index.fileMain.mainFilePath ? index.fileMain.mainFilePath
                    + index.fileMain.savedFileNm
                    : '메인 사진';
                $img.src = imgSrc;
    
                // $newDivItem.appendChild($img);
    
                $newDivItem.innerHTML += `
                        <img class="myImage" src="${imgSrc}" width="100%" height="70%">
                        <p> ${index.listNm} </p>
                        <p> ${index.ptList[0].price}</p>
                    `;
    
                /* 상품 디테일 이벤트 */
                $newDivItem.addEventListener('click', function () {
                    productDetail(index.listNo);
                });
                $categoryCode.append($newDivItem);
                console.log($img.src);
            }
        }
    
        function productDetail(listNo) {

            const page = 1;
            console.log('디테일 클릭확인');
            console.log(listNo);
    
            location.href = `/user/productDetail?listNo=${listNo}&page=${page}`;
        }
    
        let isFetching = false;
    
        // 스크롤 이벤트 리스너 등록
        window.addEventListener('scroll', () => {
            // 이미 데이터를 불러오고 있는 중이라면 더 이상 호출하지 않음
            if (isFetching) return;
    
            // 현재 스크롤 위치
            const scrollY = window.scrollY || window.pageYOffset;
    
            // 문서 전체의 높이
            const documentHeight = document.body.offsetHeight;
    
            // 브라우저 창의 높이
            const windowHeight = window.innerHeight;
    
            // 추가 페이지 로드 조건 확인 (스크롤이 하단에 닿았을 때)
            if (scrollY + windowHeight >= documentHeight) {
                // 데이터를 불러오기 전에 상태를 업데이트하여 중복 호출 방지
                isFetching = true;
    
                currentPage++;
                console.log(currentPage);
                // 로딩 함수 호출
                loadProducts(searchCondition, searchValue);
    
                // 일정 시간 후에 상태를 리셋하여 다시 호출 가능하도록 함
                setTimeout(() => {
                    isFetching = false;
                }, 3000); // 1초 (원하는 시간으로 조절 가능)
            }
        });
    }
   
    if(document.getElementById('searchBarBtn')){
        const $searchBarBtn = document.getElementById('searchBarBtn');

        $searchBarBtn.addEventListener('click', ()=>{
            searchCondition = 'null';
            searchValue = document.getElementById('searchBarValue').value;
            load = true;
            console.log("473확인인인인", load);
            console.log("473확인인인인", searchValue);
            loadProducts(searchCondition,searchValue)
        });
       
    }
}
