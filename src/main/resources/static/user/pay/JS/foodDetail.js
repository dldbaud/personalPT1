window.addEventListener('load', function() {

    // if(document.querySelectorAll('input[type="checkbox"]')){

    //     console.log("야");
    //     document.querySelectorAll('input[type="checkbox"]').forEach(checkBox => {
    //         checkBox.addEventListener('click', function() {
    //             countClick(this);
    //             console.log("야5");
    //         });
    //     });

    //     function countClick(checkCheckBox){
    //         document.querySelectorAll('input[type="checkbox"]').forEach(otherCheckbox => {
    //             otherCheckbox.addEventListener('click', function(){
    //                 if (checkCheckBox !== otherCheckbox) {
    //                     checkCheckBox.checked = false;
    //                 }
    //             });
    //         });
    //     }
    // }

    if(document.getElementById('priceDisplay')){

        const $checkBoxs = document.querySelectorAll('.checkBox');
        const $numberBoxs = document.querySelectorAll('.numberBox');
        let $priceDisplay = document.getElementById('priceDisplay');
        let totalPrice = 0;
        let countPrice = 0;
        let ptNo;
        /* 현재 상품 갯수 배열 */
        let numberInputs = [];
        let beforeNumberInputs = [];
        let numberInput;
        //이전 상품갯수 배열
        let beforeNumberInput;
        //이전 상품 번호
        let beforePtNo;
        //가격
        let price;

        //카운트박스 숫자
        let ptCount;
        
        $checkBoxs.forEach(check => {
            check.addEventListener('change', () => handleCheckBoxChange(check));
        });
        
        function handleCheckBoxChange(checkbox) {
            console.log('handleCheckBoxChange 호출');
            //이전 상품번호에 현재 상품 번호 저장후 상품번호 업그레이드
            beforePtNo = ptNo;
            ptNo = checkbox.getAttribute('data-ptno');
            console.log(ptNo,'현재번호');
            console.log(beforePtNo,'예전번호');
            updateTotalPrice(checkbox, ptNo, beforePtNo);
        } 
        function updateTotalPrice(checkbox,ptNo) {
            
            console.log(checkbox, 'checkbox1');
            totalPrice = totalPrice;

            //현재 번호로 상품 개수 배열에서 단일 변수에 할당 기본값 1 중요
            numberInput = numberInputs[ptNo] || 1;
            //현재 상품 번호로 이전번호할당 이전번호가 없으니 널
            beforeNumberInput = beforeNumberInputs[ptNo];


            //ptNo로 동적으로 할당
            const ptCountInput = document.getElementById('ptCount' + ptNo);
            const ptPriceInput = document.getElementById('checkBox' + ptNo);
            ptCount = parseInt(ptCountInput.value);

            price = ptPriceInput.value;

            //새로 체크 시 기본(현재)값
            countPrice = price * (ptCount);

            if(checkbox.checked){
                console.log(checkbox,'checkbox2');
                totalPrice += countPrice;
                $priceDisplay.textContent = totalPrice;

                console.log('뭔데1');
                //이벤트 중첩 방지를 위한 체인이 이벤트마다 추가
                ptCountInput.addEventListener('change', ptCountInputEvent);
            } else {
                totalPrice -= countPrice;
                $priceDisplay.textContent = totalPrice;
                
                console.log('뭔데2');
                //이벤트 중첩 방지를 위한 체인이 이벤트마다 제거
                ptCountInput.removeEventListener('change', ptCountInputEvent);
                return;
            }
      
        }

        //체크 이벤트 객체는 ptNo로 동적 으로 가져올수있기 때문에 현재 타겟을가져와서 pt 번호할당 
        function ptCountInputEvent(event) {
            console.log(event.target, 'checkbox3');
                //이전 상품 개수 이전 상품 배열에 이전 상품 번호로 할당
                //이전 상품 번호와 현재 상품 번호가 같다면 결국 이전 상품 개수를 의미
                beforeNumberInput = beforeNumberInputs[beforePtNo];
                numberInput = parseInt(this.value);
                
                /* 현재 상품 개수  현재 상품 번호 키로 배열저장*/
                numberInputs[ptNo] = numberInput;
                
                //이전 상품 번호(넘버 박스 이벤트 동작시 체인지체크박스 이벤트가 동작안하니) 그리고 체크박스 이벤트후 바로 넘버박스 이벤트 시 설정
                beforePtNo = ptNo;
                /* 넘버 박스 이벤트시 새로운 번호 할당 */
                ptNo = event.target.getAttribute('data-ptno');
                
                numberInputEvent(numberInput,beforeNumberInput,ptNo,beforePtNo,event.target);
                //위의 호출이 끝나면 이전 번호 변수에 현재 번호 할당 필요한가?
                beforePtNo = ptNo;
                
                console.log(beforeNumberInput, 'beforeNumberInput');
                console.log(numberInput,'numberInput');
        }
        
        function numberInputEvent(numberInput,beforeNumberInput,ptNo,beforePtNo,eventTarget){

            //이건 굳이?
            console.log(eventTarget,'checkbox4');
            //현재 체크박스 박스 이벤트 번호로 가져오기
            const ptPriceInput = document.getElementById('checkBox' + ptNo);
            price = ptPriceInput.value;
            console.log(ptNo,'현재번호');
            console.log(beforePtNo,'예전번호');
            console.log(price);
            console.log(ptCount);
            console.log(numberInput,'numberInput');
            console.log(beforeNumberInput,'beforeNumberInput');
            //상품번호가 같을 때
            if(ptNo == beforePtNo){
                //현재 상품 개수 : 이전 상품 개수 위의 해설
                if(numberInput > beforeNumberInput){
                    countPrice = price * (numberInput - beforeNumberInput);
                } else if(numberInput < beforeNumberInput){
                    countPrice = -(price * (beforeNumberInput - numberInput));
                } else {
                     countPrice = price * (numberInput - ptCount == 0 ? 1 :numberInput - ptCount) ;
                }
                if(ptPriceInput.checked){
                    totalPrice += countPrice;
                    console.log('뭔데3');
                } else {
                    totalPrice -= countPrice;
                    console.log('뭔데4');
                }
            } else {
                console.log(price);
                //다를 때 현재 상품 번호로 이전 현재 상품 이전 개수 가져오기
                beforeNumberInput = beforeNumberInputs[ptNo];
                if(numberInput > beforeNumberInput){
                    countPrice = price * (numberInput - beforeNumberInput);
                } else if(numberInput < beforeNumberInput){
                    countPrice = -(price * (beforeNumberInput - numberInput));
                } else {
                    //다를때 카운트박스가 같을 때가 문제
                    countPrice = price * (numberInput - ptCount == 0 ? 1 :numberInput - ptCount) ;
                }
                if(ptPriceInput.checked){
                    totalPrice += countPrice;
                    console.log('뭔데5');
                } else {
                    totalPrice -= countPrice;
                    console.log('뭔데6');
                }
            }
            //이전 상품 개수 배열에 현재 상품번호로 현재 상품 개수 추가
            beforeNumberInputs[ptNo] = numberInput;

            $priceDisplay.textContent = totalPrice;
        }
    }
});
