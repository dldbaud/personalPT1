window.addEventListener('load', function () {

    console.log('js확인');
    if (document.getElementById('addProduct')) {
        console.log('id확인');

        $addProduct = document.getElementById('addProduct');

        $addProduct.onclick = function () {
            console.log('클릭 확인');
            location.href = "/sell/addProduct";
        }
    }

    if (document.getElementById('manageProducts')) {
        console.log('id확인');

        $manageProducts = document.getElementById('manageProducts');

        $manageProducts.onclick = function () {
            console.log('클릭 확인');
            location.href = "/sell/manageProducts";
        }
    }
    

    /* 상위 카테고리 코드 조회 */
    let selectedRefCategory;  // 전역 변수 선언
    //전역 배열
    let $sellPtCategory = document.querySelectorAll('.sellPtCategory');

    /* 셀렉트박스 전역숨김 */
    let $ptSizeLabel = document.querySelectorAll('.ptSizeLabel');
    let $ptSize = document.querySelectorAll('.ptSize');
    let $author = document.querySelectorAll('.author');
    let $authorLabel = document.querySelectorAll('.authorLabel');

    if (document.getElementById("sellCategory")) {

        // 스크립트가 여러 번 로드되는 것을 방지하고, 초기화 코드가 한 번만 실행
        if (!window.loadedScript) {

            if (document.getElementById("sellCategory")) {
                $.ajax({
                    url: "/user/RefCategory",
                    //응답 데이타 그대로 전송
                    success: function (data) {
                        console.log(data); //배열 전송 

                        const $categoryCode = $("#sellCategory");
                        for (let index in data) {
                            $categoryCode.append($("<option>").val(data[index].refCategory).text(data[index].refCategoryNm));
                        }

                        selectedRefCategory = data[0].refCategory;
                        categoryView(data[0].refCategory);

                        if (selectedRefCategory == 1) {
                            $ptSizeLabel.forEach((element) => {
                                element.style.display = 'block';
                            });

                            $ptSize.forEach((element) => {
                                element.style.display = 'block';
                            });

                            $author.forEach((element) => {
                                element.style.display = 'none';
                            });

                            $authorLabel.forEach((element) => {
                                element.style.display = 'none';
                            });
                        } else {
                            $ptSizeLabel.forEach((element) => {
                                element.style.display = 'none';
                            });

                            $ptSize.forEach((element) => {
                                element.style.display = 'none';
                            });

                            $author.forEach((element) => {
                                element.style.display = 'none';
                            });

                            $authorLabel.forEach((element) => {
                                element.style.display = 'none';
                            });
                        }
                    },
                    error: function (xhr) { console.log(xhr); }
                });
            }

            window.loadedScript = true;
        }

        function inputView() {
            if (selectedRefCategory == 1) {
                // Category 1에 대한 처리
                $ptSizeLabel.forEach((element) => {
                    element.style.display = 'block';
                });

                $ptSize.forEach((element) => {
                    element.style.display = 'block';
                });

                $author.forEach((element) => {
                    element.style.display = 'none';
                });

                $authorLabel.forEach((element) => {
                    element.style.display = 'none';
                });
            } else if (selectedRefCategory == 4) {
                // Category 4에 대한 처리
                $ptSizeLabel.forEach((element) => {
                    element.style.display = 'none';
                });

                $ptSize.forEach((element) => {
                    element.style.display = 'none';
                });

                $author.forEach((element) => {
                    element.style.display = 'block';
                });

                $authorLabel.forEach((element) => {
                    element.style.display = 'block';
                });
            } else {
                // 기타 Category에 대한 처리
                $ptSizeLabel.forEach((element) => {
                    element.style.display = 'none';
                });

                $ptSize.forEach((element) => {
                    element.style.display = 'none';
                });

                $author.forEach((element) => {
                    element.style.display = 'none';
                });

                $authorLabel.forEach((element) => {
                    element.style.display = 'none';
                });
            }
        }

        $("#sellCategory").on("change", function () {
            // 선택된 카테고리 코드 가져오기
            const selectedCategoryCode = $(this).val();

            /*전역 변수 설정 */
            selectedRefCategory = selectedCategoryCode;
            // 클릭 이벤트 발생 (원하는 함수 호출 등)
            if (selectedCategoryCode) {

                categoryView(selectedCategoryCode);
            }

            inputView(selectedRefCategory);
        });



        /* 하위 카테고리 코드 조회 */
        function categoryView(categoryCode) {
            $.ajax({
                url: `/user/category/${categoryCode}`,
                //응답 데이타 그대로 전송
                success: function (data) {
                    console.log(data); //배열 전송 

                    $sellPtCategory.forEach((element) => {
                        const selectedValue = element.value; // 기존에 선택한 값 저장
                        $(element).empty(); // 모든 옵션 제거

                        // 새로운 데이터 추가
                        data.forEach(item => {
                            const option = $("<option>").val(item.categoryCode).text(item.categoryName);
                            $(element).append(option);
                            console.log(item.categoryCode);
                        });

                        // 기존에 선택한 값이 새로운 데이터에도 존재하면 선택 상태로 변경
                        // some 배열에서 하나라도 만족하면 true toStirng으로 타입 변환
                        if (data.some(item => item.categoryCode.toString() === selectedValue)) {
                            $(element).val(selectedValue);
                            console.log(selectedValue);
                        }
                    });
                },
                error: function (xhr) { console.log(xhr); }
            });
        }
    }
    
    

    let itemCount = 0;
    let imageArea = document.querySelectorAll(".image-area");
    let fileElements = document.querySelectorAll("[type=file]");
    let bundle = document.querySelectorAll(".bundle");

        /* 체크박스 이벤트 */
        if(document.getElementById(`bundlech${itemCount}`)){

            console.log(`야${itemCount}`);
            const $bundlech = document.getElementById(`bundlech${itemCount}`);
    
            $bundlech.addEventListener('click', function(){
                
                checkboxEvent(this);
            });
        }

    function checkboxEvent(e) {
        const checkboxId = e.id.replace('bundlech', ''); // 현재 체크박스의 ID에서 번호 추출
        const correspondingBundle = document.querySelectorAll('.bundle' + checkboxId); // 해당 체크박스에 대응하는 bundle 인풋
        if (e.checked) {
            correspondingBundle.forEach((element) => {
                element.style.display = 'block';
            });
        } else {
            correspondingBundle.forEach((element) => {
                element.style.display = 'none';
            });
        }
    }
    const fileAddClickHandler = (event) => {

        // 현재 클릭한 fileAdd 버튼의 부모 item 요소 찾기

        const item = event.currentTarget.closest('.item');

        if (!item) {
            console.error('부모 .item을 찾을 수 없습니다.');
            return;
        }

        const allImageAreas = item.querySelectorAll('.image-area');
        const lastImageArea = allImageAreas[allImageAreas.length - 1];

        console.log(allImageAreas, 'allImageAreas 확인');
        console.log(lastImageArea, 'lastImageArea 확인');

        if (lastImageArea) {


            const fileInputHTML = `<label for="main-image" class="upload">상품 사진</label>
            <input type="file" name="attachImage[${itemCount}]" accept="image/jpeg, image/png" multiple>
            `;

            const fileViewHTML = `<label for="fileView">파일 미리보기</label>
            <div class="image-area" name="fileView[${itemCount}]">
            <img style="width: 120px; height: 100px;">
            </div>
            `;

            lastImageArea.insertAdjacentHTML('afterend', fileViewHTML);
            lastImageArea.insertAdjacentHTML('afterend', fileInputHTML);
            console.log("파일추가동작 확인");
            console.log(fileViewHTML, 'fileViewHTML 확인');

            imageArea = document.querySelectorAll(".image-area");
            fileElements = document.querySelectorAll("[type=file]");

            attachEventListeners();

        }

        console.log("파일추가 확인");

    };

    if (document.getElementById('addItemBtn')) {
        // let itemCount = 1; // 상품 번호를 관리하는 변수


        if (document.getElementById('fileAdd')) {
            document.getElementById('fileAdd').addEventListener('click', (event) => {
                fileAddClickHandler(event);
            });
        }

        document.getElementById('addItemBtn').addEventListener('click', function () {
            const itemsContainer = document.getElementById('itemsContainer');
            const newItem = document.createElement('div');
            newItem.classList.add('item');
            ++itemCount;
            console.log(itemCount);
            newItem.innerHTML = `
                <label>상품 등록</label>
                
                <label for="ptNm">이름</label>
                <input type="text" id="ptNm" class="itemInput" name="ptList[${itemCount}].ptNm" required>

                <label for="sellPtCategory">카테고리:</label>
                <select id="sellPtCategory" class="sellPtCategory" name="ptList[${itemCount}].category.categoryCode" required>

                <label for="main-image" class="upload">파일 올리기</label>
                <input type="file" id="main-image[0]" name="attachImage[${itemCount}]" accept="image/gif, image/jpeg, image/png" multiple>
                
                <label for="fileView">파일 미리보기</label>
                <div class="image-area" name="fileView[${itemCount}]">
                    <img style="width: 120px; height: 100px;">
                </div>
        
                </div>
        
                <button type="button" id="fileAdd" class="fileAdd">파일 추가</button>
        
                <label for="ptDescrip">상품 설명</label>
                <textarea class="itemInput" id="ptDescrip" name="ptList[${itemCount}].ptDescrip"></textarea>
                
                <label for="ptSize" class="ptSizeLabel">사이즈</label>
                            <select id="ptSize" class="ptSize" name="ptList[${itemCount}].ptSize">
                                <option value="S">S</option>
                                <option value="M">M</option>
                                <option value="L">L</option>
                                <option value="X">X</option>
                                <option value="XL">XL</option>
                                <option value="XXL">XXL</option>
                </select>

                <label for="author" class="authorLabel">저자</label>
                <input type="text" id="author" class="author" ptList[${itemCount}].ptAuthor>

                <label for="price">가격</label>
                <input type="number" id="price" class="itemInput" name="ptList[${itemCount}].price" required>
                
                <label for="stCount">재고</label>
                <input type="number" id="stCount" class="itemInput" name="ptList[${itemCount}].stCount" required>
        
                <button type="button" class="removeItemBtn">상품 삭제</button>
            `;
            itemsContainer.appendChild(newItem);

            // 새로 생성된 삭제 버튼에 이벤트 리스너 추가
            newItem.querySelector('.removeItemBtn').addEventListener('click', function () {
                itemsContainer.removeChild(newItem);
                itemCount--;
            });

            newItem.querySelector('.fileAdd').addEventListener('click', fileAddClickHandler);
            // document.getElementById(`bundlech${itemCount}`).addEventListener('click', function() {
            //     checkboxEvent(this);
            // });
            

            imageArea = document.querySelectorAll(".image-area");

            fileElements = document.querySelectorAll("[type=file]");

            $ptSizeLabel = document.querySelectorAll('.ptSizeLabel');
            $ptSize = document.querySelectorAll('.ptSize');
            $author = document.querySelectorAll('.author');
            $authorLabel = document.querySelectorAll('.authorLabel');
            $sellPtCategory = document.querySelectorAll('.sellPtCategory');

            attachEventListeners();
            inputView();
            categoryView(selectedRefCategory);
            console.log('selectedRefCategory확인', selectedRefCategory);
        });
    }

    function open() {
        console.log('this확인', this);
        const index = Array.from(imageArea).indexOf(this);
        fileElements[index].click();
    }

    function preview() {
        const index = Array.from(fileElements).indexOf(this);

        console.log(this);
        console.log(this.files, this.files[0]);


        if (this.files && this.files[0]) {

            const self = this; // this를 다른 변수에 저장 this 바인딩
            const reader = new FileReader();
            reader.readAsDataURL(this.files[0]);
            reader.onload = function () {
                console.log(reader.result);

                const image = new Image();
                const maxwidth = 'calc(100% - 6rem)';
                image.src = reader.result;


                image.onload = function () {
                    const imageWidth = image.width;
                    const imageHeight = image.height;
                    let width, height;

                    // 비율에 따라 동적으로 크기 조절
                    width = imageWidth;
                    height = imageHeight;

                    if (width > maxwidth) {
                        width = maxwidth;
                    }
                    imageArea[index].innerHTML = `<img src='${reader.result}' style='width: ${width}px; height: ${height}px'>`;
                };
            }
        }
    }

    function attachEventListeners() {
        imageArea.forEach(item => item.addEventListener('click', open));
        fileElements.forEach(item => item.addEventListener('change', preview));
    }

    attachEventListeners();

    /* 폼 제출시 필수 입력 확인  */
    if (document.getElementById("sellRegistInfo")) {
        document.getElementById("sellRegistInfo").addEventListener('submit', function (event) {

            if ($ptSize[0].style.display === 'block') {
                let ptSizeValues = [];
                document.querySelectorAll('.ptSize').forEach((element) => {
                    ptSizeValues.push(element.value);
                });

                if (ptSizeValues.some((item) => item === null || item.trim() === '')) {
                    alert("사이즈를 입력하세요");
                    event.preventDefault(); // 서브밋 방지
                } else {
                    this.submit();
                }
            }

            if ($author[0].style.display === 'block') {
                let authorValues = [];
                document.querySelectorAll('.ptAuthor').forEach((element) => {
                    authorValues.push(element.value);
                });

                if (authorValues.some((item) => item === null || item.trim() === '')) {
                    alert("저자를 입력하세요");
                    event.preventDefault(); // 서브밋 방지
                } else {
                    this.submit();
                }
            }
        });
    }
//     <div class="diq">
//     <input type="checkbox" id="bundlech${itemCount}">
//     <label for="bundlech${itemCount}" id="bundelText">묶음 여부</label>
// </div>

// <label for="bundle${itemCount}" class="bundle${itemCount}">묶음 개수</label>
// <input type="number" id="bundle${itemCount}" class="bundle${itemCount}" name="ptList[${itemCount}].ptBundle">
});
