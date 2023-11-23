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


    if (document.getElementById('addItemBtn')) {
        let itemCount = 1; // 상품 번호를 관리하는 변수

        document.getElementById('addItemBtn').addEventListener('click', function () {
            const itemsContainer = document.getElementById('itemsContainer');
            const newItem = document.createElement('div');
            newItem.classList.add('item');
            newItem.innerHTML = `
                <label>상품 등록</label>
                
                <label for="itemNm">이름</label>
                <input type="text" class="itemInput" name="itemNm[${itemCount}]" required>

                <label for="main-image" class="upload">파일 올리기</label>
                <input type="file" id="main-image[0]" name="mainImage[0]" accept="image/jpg, image/png" multiple required>
                
                <label for="fileView">파일 미리보기</label>
                <div class="image-area" name="fileView[0]">
                    <img style="width: 120px; height: 100px;">
                </div>

                <label for="main-image" class="upload">파일 올리기</label>
                <input type="file" id="main-image[0]" name="mainImage[0]" accept="image/jpg, image/png" multiple required>

                <label for="fileView">파일 미리보기</label>
                <div class="image-area" name="fileView[0]">
                    <img style="width: 120px; height: 100px;">
                </div>

                <label for="main-image" class="upload">파일 올리기</label>
                <input type="file" id="main-image[0]" name="mainImage[0]" accept="image/jpg, image/png" multiple required>
                
                <label for="fileView">파일 미리보기</label>
                <div class="image-area" name="fileView[0]">
                    <img style="width: 120px; height: 100px;">
                </div>

                <label for="itemDescription">상품 설명</label>
                <textarea class="itemInput" name="itemDescription[${itemCount}]" required></textarea>
                
                <label for="itemPrice">가격</label>
                <input type="number" class="itemInput" name="itemPrice[${itemCount}]" required>
                
                <label for="ST_COUNT">재고</label>
                            <input type="number" class="itemInput" name="ST_COUNT[${itemCount}]" required>
    
                <button type="button" class="removeItemBtn">상품 삭제</button>
            `;
            itemsContainer.appendChild(newItem);

            // 새로 생성된 삭제 버튼에 이벤트 리스너 추가
            newItem.querySelector('.removeItemBtn').addEventListener('click', function () {
                itemsContainer.removeChild(newItem);
            });
            
            itemCount++; // 다음 상품 번호 증가

            const imageArea = newItem.querySelectorAll(".image-area");

            const fileElements = newItem.querySelectorAll("[type=file]");

            imageArea.forEach(item => item.addEventListener('click', open));

            fileElements.forEach(item => item.addEventListener('change', preview));

            function open() {
                console.log('this확인', this);
                const index = Array.from(imageArea).indexOf(this);
                fileElements[index].click();
            }

            function preview() {
                const index = Array.from(fileElements).indexOf(this);
                console.log(this);
                console.log(this.files, this.files[0]);
                /* 첨부 된 파일이 존재한다면 */
                if (this.files && this.files[0]) {
                    const reader = new FileReader();
                    reader.readAsDataURL(this.files[0]);
                    /* 파일 로드 후 동작하는 이벤트 설정 */
                    reader.onload = function () {
                        console.log(reader.result);
                        if (index === 0) {
                            imageArea[index].innerHTML = `<img src='${reader.result}' style='width: 350px; height: 200px'>`;
                        }
                        else
                            imageArea[index].innerHTML = `<img src='${reader.result}' style='width: 350px; height: 200px'>`;
                    }
                }
            }
        });

        (function () {

            const imageArea = document.querySelectorAll(".image-area");

            const fileElements = document.querySelectorAll("[type=file]");

            imageArea.forEach(item => item.addEventListener('click', open));

            fileElements.forEach(item => item.addEventListener('change', preview));

            function open() {
                console.log('this확인', this);
                const index = Array.from(imageArea).indexOf(this);
                fileElements[index].click();
            }

            function preview() {
                const index = Array.from(fileElements).indexOf(this);
                console.log(this);
                console.log(this.files, this.files[0]);
                /* 첨부 된 파일이 존재한다면 */
                if (this.files && this.files[0]) {
                    const reader = new FileReader();
                    reader.readAsDataURL(this.files[0]);
                    /* 파일 로드 후 동작하는 이벤트 설정 */
                    reader.onload = function () {
                        console.log(reader.result);
                        if (index === 0) {
                            imageArea[index].innerHTML = `<img src='${reader.result}' style='width: 350px; height: 200px'>`;
                        }
                        else
                            imageArea[index].innerHTML = `<img src='${reader.result}' style='width: 350px; height: 200px'>`;
                    }
                }
            }

        })();

    }
});