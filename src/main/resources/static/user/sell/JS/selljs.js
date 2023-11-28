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

    /* 상위 카테고리 코드 조회 */
    let selectedRefCategory;  // 전역 변수 선언
    //전역 배열
    let $sellPtCategory = document.querySelectorAll('.sellPtCategory');
    if (document.getElementById("sellCategory")) {
        // 스크립트가 여러 번 로드되는 것을 방지하고, 초기화 코드가 한 번만 실행
        if (!window.loadedScript) {

            if (document.getElementById("sellCategory")) {
                $.ajax({
                    url: "/sell/RefCategory",
                    //응답 데이타 그대로 전송
                    success: function (data) {
                        console.log(data); //배열 전송 

                        const $categoryCode = $("#sellCategory");
                        for (let index in data) {
                            $categoryCode.append($("<option>").val(data[index].refCategory).text(data[index].refCategoryNm));
                        }

                        selectedRefCategory = data[0].refCategory;

                        categoryView(data[0].refCategory);
                    },
                    error: function (xhr) { console.log(xhr); }
                });
            }

            window.loadedScript = true;
        }

        $("#sellCategory").on("change", function () {
            // 선택된 카테고리 코드 가져오기
            const selectedCategoryCode = $(this).val();
            
            selectedRefCategory = selectedCategoryCode;
            // 클릭 이벤트 발생 (원하는 함수 호출 등)
            if (selectedCategoryCode) {
                // 선택된 카테고리 코드를 이용하여 원하는 작업 수행
                // 예: categoryView 함수 호출
                categoryView(selectedCategoryCode);
            }
        });

        /* 하위 카테고리 코드 조회 */
        function categoryView(categoryCode) {
            $.ajax({
                url: `/sell/category/${categoryCode}`,
                    //응답 데이타 그대로 전송
                    success: function (data) {
                        console.log(data); //배열 전송 

                        $sellPtCategory.forEach((element) => {
                            $(element).empty();
            
                            for (let index in data) {
                                $(element).append($("<option>").val(data[index].categoryCode).text(data[index].categoryName));
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
            <input type="file" name="attachImage[${itemCount}]" accept="image/jpg, image/png" multiple>
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
            newItem.innerHTML = `
                <label>상품 등록</label>
                
                <label for="ptNm">이름</label>
                <input type="text" id="ptNm" class="itemInput" name="ptList[${itemCount}].ptNm" required>

                <label for="sellPtCategory">카테고리:</label>
                <select id="sellPtCategory" class="sellPtCategory" name="ptList[${itemCount}].category.categoryCode" required>

                <label for="main-image" class="upload">파일 올리기</label>
                <input type="file" id="main-image[0]" name="attachImage[${itemCount}]" accept="image/jpg, image/png" multiple required>
                
                <label for="fileView">파일 미리보기</label>
                <div class="image-area" name="fileView[${itemCount}]">
                    <img style="width: 120px; height: 100px;">
                </div>
        
                <label for="main-image" class="upload">파일 올리기</label>
                <input type="file" id="main-image[0]" name="attachImage[${itemCount}]" accept="image/jpg, image/png" multiple>
        
                <label for="fileView">파일 미리보기</label>
                <div class="image-area" name="fileView[${itemCount}]">
                    <img style="width: 120px; height: 100px;">
                </div>
        
                <label for="main-image" class="upload">파일 올리기</label>
                <input type="file" id="main-image[0]" name="attachImage[${itemCount}]" accept="image/jpg, image/png" multiple>
                
                <label for="fileView">파일 미리보기</label>
                <div class="image-area" name="fileView[${itemCount}]">
                    <img style="width: 120px; height: 100px;">
                </div>
        
                <button type="button" id="fileAdd" class="fileAdd">파일 추가</button>
        
                <label for="ptDescrip">상품 설명</label>
                <textarea class="itemInput" id="ptDescrip" name="ptList[${itemCount}].ptDescrip" required></textarea>
                
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


            imageArea = document.querySelectorAll(".image-area");

            fileElements = document.querySelectorAll("[type=file]");

            $sellPtCategory = document.querySelectorAll('.sellPtCategory');
            attachEventListeners();

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
            const reader = new FileReader();
            reader.readAsDataURL(this.files[0]);
            reader.onload = function () {
                console.log(reader.result);
                if (index === 0) {
                    imageArea[index].innerHTML = `<img src='${reader.result}' style='width: 350px; height: 200px'>`;
                } else {
                    imageArea[index].innerHTML = `<img src='${reader.result}' style='width: 350px; height: 200px'>`;
                }
            };
        }
    }
    
    function attachEventListeners() {
        imageArea.forEach(item => item.addEventListener('click', open));
        fileElements.forEach(item => item.addEventListener('change', preview));
    }
    
    attachEventListeners();
});