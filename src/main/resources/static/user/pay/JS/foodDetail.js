window.addEventListener('load', function () {

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
    let $finalPrice;
    let ptList = [];
    const $numberBoxs = document.querySelectorAll('.numberBox');
    console.log($finalPrice);
    console.log($numberBoxs);
    if (document.getElementById('priceDisplay')) {

        const $checkBoxs = document.querySelectorAll('.checkBox');
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

        $checkBoxs.forEach((check, index) => {
            check.addEventListener('change', () => handleCheckBoxChange(check, index));
        });

        function handleCheckBoxChange(checkbox, index) {
            console.log(index, 'index1');
            console.log('handleCheckBoxChange 호출');
            //이전 상품번호에 현재 상품 번호 저장후 상품번호 업그레이드
            beforePtNo = ptNo;
            ptNo = checkbox.getAttribute('data-ptno');
            console.log(ptNo, '현재번호');
            console.log(beforePtNo, '예전번호');
            updateTotalPrice(checkbox, ptNo, index, beforePtNo);
        }
        function updateTotalPrice(checkbox, ptNo, index) {
            console.log(index, 'index2');
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

            if (checkbox.checked) {
                console.log(checkbox, 'checkbox2');
                totalPrice += countPrice;
                $priceDisplay.textContent = totalPrice;
                $finalPrice = totalPrice;
                console.log('뭔데1');
                console.log($finalPrice);
                console.log(index);
                let pt = {
                    ptNo: checkbox.getAttribute('data-ptno'),
                    ptNm: checkbox.getAttribute('data-ptnm'),
                    price: checkbox.getAttribute('value'),
                    ptSize: checkbox.getAttribute('data-ptSize'),
                    stCount: $numberBoxs[index].getAttribute('value')
                };
                
                ptList.push(pt);
                console.log(ptList);
                //이벤트 중첩 방지를 위한 체인이 이벤트마다 추가
                ptCountInput.addEventListener('change', ptCountInputEvent);
            } else {
                totalPrice -= countPrice;
                $priceDisplay.textContent = totalPrice;
                $finalPrice = totalPrice;
                console.log('뭔데2');
                console.log($finalPrice);

                // 체크해제시 저장 번호가 현재번호가 아닌 것들만 반환하라는 뜻
                ptList = ptList.filter(pt => pt.ptNo !== ptNo);
                console.log(ptList,'제거확인');
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

            ptList = ptList.map(pt => (pt.ptNo !== ptNo) ? pt : { ...pt, stCount: event.target.value });
            console.log(ptList ,'넘버박스 이벤트');
            numberInputEvent(numberInput, beforeNumberInput, ptNo, beforePtNo, event.target);
            //위의 호출이 끝나면 이전 번호 변수에 현재 번호 할당 필요한가?
            beforePtNo = ptNo;

            console.log(beforeNumberInput, 'beforeNumberInput');
            console.log(numberInput, 'numberInput');
        }

        function numberInputEvent(numberInput, beforeNumberInput, ptNo, beforePtNo, eventTarget) {

            //이건 굳이?
            console.log(eventTarget, 'checkbox4');
            //현재 체크박스 박스 이벤트 번호로 가져오기
            const ptPriceInput = document.getElementById('checkBox' + ptNo);
            price = ptPriceInput.value;
            console.log(ptNo, '현재번호');
            console.log(beforePtNo, '예전번호');
            console.log(price);
            console.log(ptCount);
            console.log(numberInput, 'numberInput');
            console.log(beforeNumberInput, 'beforeNumberInput');
            //상품번호가 같을 때
            if (ptNo == beforePtNo) {
                //현재 상품 개수 : 이전 상품 개수 위의 해설
                if (numberInput > beforeNumberInput) {
                    countPrice = price * (numberInput - beforeNumberInput);
                } else if (numberInput < beforeNumberInput) {
                    countPrice = -(price * (beforeNumberInput - numberInput));
                } else {
                    countPrice = price * (numberInput - ptCount == 0 ? 1 : numberInput - ptCount);
                }
                if (ptPriceInput.checked) {
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
                if (numberInput > beforeNumberInput) {
                    countPrice = price * (numberInput - beforeNumberInput);
                } else if (numberInput < beforeNumberInput) {
                    countPrice = -(price * (beforeNumberInput - numberInput));
                } else {
                    //다를때 카운트박스가 같을 때가 문제
                    countPrice = price * (numberInput - ptCount == 0 ? 1 : numberInput - ptCount);
                }
                if (ptPriceInput.checked) {
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
            $finalPrice = totalPrice;
            console.log($finalPrice);
        }
    }

    const $btns = document.querySelectorAll('.btn');
    const $text1s = document.getElementsByClassName('text1');

    //버튼 수에 따라 추가조절
    let states = ["off", "off"];

    function updateState(index) {
        states = states.map((state, i) => (i === index ? (state === "off" ? "on" : "off") : "off"));
        console.log('2');
    }
    // #3498db
    function handleClick(index) {
        return function () {
            if (states[index] === "off") {
                $btns[index].style.backgroundColor  = '#3498db';
                $text1s[index].style.color = 'black';
                console.log('1');
                states.forEach((state, i) => {
                    if (i !== index) {
                        $btns[i].style.backgroundColor  = 'white';
                        $text1s[i].style.color = 'black';
                    }
                });
                //요소를 찾아서 on/off
                updateState(index);
                console.log('3');
            } 
            else {
                $btns[index].style.backgroundColor  = 'white';
                $text1s[index].style.color = 'black';
                //요소를 찾아서 on/off
                updateState(index);
            }
        };
    }

    $btns.forEach((btn, index) => {
        btn.addEventListener('click', handleClick(index));
    });

    if(document.getElementById('paymentDiv')){

        const $paymentBtn = document.getElementById('paymentBtn');
        const $id = document.getElementById('userId');

        $paymentBtn.addEventListener('click', function(){
            console.log('결제 클릭');
            // console.log8($id);
            if(!($id)) {
                alert("로그인을 해주세요");
                return;
            } else {
                //typeof === 쓸꺼면 typeof 써야함
                if($finalPrice == 'undefined' || $finalPrice == null) {
                    console.log('무시안당함');
                    alert("결제 금액을 확인해주세요");
                } else {
                    console.log('무시당함');
                    paymentDivEvent();
                }
            }
        });
    }


    function paymentDivEvent(){
        

        states.forEach((state, index) => {
            if (state === "on") {
                // 해당 인덱스가 on인 경우 처리
                switch (index) {
                    case 0:
                        kakaopayEvent();
                        break;
                    case 1:
                        cardEvent();
                        break;
                    
                }
            } else {
                
                console.log(`Index ${index} is off`);
                
            }
        });
    
        // 이후 추가적인 로직을 작성하세요.
    }

    /* 구매자정보 */
    let $id;
    let $userNm;
    let $phone;
    let $address;
    if(document.getElementById('userId')){
        $id = document.getElementById('userId').value;
        $userNm = document.getElementById('userNm').value;
        $phone = document.getElementById('phone').value;
        $address = document.getElementById('address').value;
        console.log($id);
    }
    /* 상품 제목 */
    const $listNm = document.getElementById('listNm').textContent;
    console.log($listNm, '제목확인');
    //상품 번호 상품이름 상품가격
    // const $checkBoxs = document.querySelectorAll('.checkBox');
    // const $numberBoxs = document.querySelectorAll('.numberBox');

    // let ptList = [];

    // $checkBoxs.forEach((e, index) => {
    //         let pt = {
    //             ptNo: e.getAttribute('data-ptno'),
    //             ptNm: e.getAttribute('data-ptnm'),
    //             price: e.getAttribute('value'),
    //             stCount: $numberBoxs[index].getAttribute('value')
    //         };
    
    //         ptList.push(pt);
    // });
    // console.log(ptList);
    //상품 제목
    const $listNo = document.getElementById('listNo').value;
    //상품 총 가격
    console.log($listNo);
    IMP.init('imp63382662');

    function requestPayment(pg, payMethod) {
        console.log($finalPrice);
        IMP.request_pay({
            pg: pg,
            pay_method: payMethod,
            merchant_uid: 'ka' + new Date().getTime(),
            name: '카카오결제',
            amount: $finalPrice,
            buyer_email: $id,
            buyer_name: $userNm,
            buyer_tel: $phone,
            buyer_addr: $address,
            buyer_postcode: '개인',
            // m_redirect_url: 'redirect url'
        }, function (rsp) {
            handlePaymentResponse(rsp);
        });
    }

    function requestPayment1(pg, payMethod) {
        console.log($finalPrice);
        console.log(pg);
        console.log(payMethod);
        IMP.request_pay({
            pg: pg,
            pay_method: payMethod,
            merchant_uid: 'ca' + new Date().getTime(),
            name: '카드결제',
            amount: $finalPrice,
            buyer_email: $id,
            buyer_name: $userNm,
            buyer_tel: $phone,
            buyer_addr: $address,
            buyer_postcode: '개인',
            // m_redirect_url: 'redirect url'
        }, function (rsp) {
            handlePaymentResponse(rsp);
        });
    }
    
    function handlePaymentResponse(rsp) {
        if (rsp.success) {
            console.log(rsp);
            const testPay = {
                ptList: ptList,
                orderNo: rsp.merchant_uid,
                payNo: rsp.imp_uid,
                listNm: $listNm,
                listNo: $listNo
            };
            const json = JSON.stringify(testPay);
            console.log(json);
            //[1] 서버단에서 결제정보 조회를 위해 jQuery ajax로 imp_uid 전달하기
            $.ajax({
                url: "/user/payComplete", //cross-domain error가 발생하지 않도록 주의해주세요
                type: 'POST',
                data: json,
                contentType: 'application/json; charset=UTF-8',
                dataType: 'json',
    
                // success : json
                //기타 필요한 데이터가 있으면 추가 전달   
            })
                .then(function (data) {
                    //[2] 서버에서 REST API로 결제정보확인 및 서비스루틴이 정상적인 경우
                    if (data.status == 200) {
                        console.log(data, "ajax확인");
                        var msg = '결제가 완료되었습니다.';
                        msg += '\n고유ID : ' + rsp.imp_uid;
                        // msg += '\n상점 거래ID : ' + rsp.merchant_uid;
                        msg += '\n결제 금액 : ' + rsp.paid_amount;
                        // msg += '카드 승인번호 : ' + rsp.apply_num;
    
                        alert(msg);
                        location.reload();
                    } else if (data.status == 500) {
                        //[3] 아직 제대로 결제가 되지 않았습니다.
                        //[4] 결제된 금액이 요청한 금액과 달라 결제를 자동취소처리하였습니다.
                        alert("결제 실패");
                    } else if(data.status == 400){
                        console.log(data, "ajax확인");
                        var messages = data.data.ptStCountMinus;
                        var combinedMessage = messages.join('\n');
                        alert(combinedMessage);
                    } else {
                        alert("결제 실패123"); 
                    }
                });
            //성공시 이동할 페이지
        } else {
            msg = '결제에 실패하였습니다.';
            msg += '에러내용 : ' + rsp.error_msg;
            //실패시 이동할 페이지
            console.log(rsp);
            console.log(rsp.error_msg);
            alert('결제를 취소하셨습니다.');
        }

    }
    if(document.getElementById('replyWriteBtn')){


        const $replyWriteBtn = document.getElementById('replyWriteBtn');
        console.log($replyWriteBtn, 'replyWriteBtn 확인');
        const $replyWrite = document.getElementById('replyWrite');

        $replyWriteBtn.addEventListener('click', function(){
            console.log('클릭확인');
            replyInsert();
        });
    }
    
    /* 댓글 삽입 */
    function replyInsert() {
        const $replyWrite = document.getElementById('replyWrite');
        const $listNo = document.getElementById('listNo').value;

        if(!$replyWrite.value.trim()) {
            alert("댓글을 입력해주세요");
            return;
        }

        const replyWriteValue = $replyWrite.value
        const json = { replyBody : replyWriteValue, refListNo : $listNo}
        $.ajax({
            url : "/user/replyInsert",
            type : "POST",
            data : JSON.stringify(json),
            //POST인 경우에만 get 은 안해도 됨
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            },
            // contentType : 'application/json; charset=UTF8',
            // dataType: 'json', 이걸로하면 성공응답을 받아도 error 코드를 찍어줌 그래서 위에껄 써야함  
        }).then(data => {
            console.log(data);
            $replyWrite.value = '';	    //댓글 입력 창 비우기
            console.log($replyWrite);
            loadReply();				//댓글 다시 로드하기
        })
        .catch(error => console.log(error,'실패확인'));
    }

    function loadReply() {
		
        let page = 1;
        const refListNo = document.getElementById('listNo').value;
        
        $.ajax({
            url : "/user/loadReply?refListNo=" + refListNo + "&page=" + page,
        })
        .then(data => {
            console.log(data);
            makeReply(data);
        })
        .catch(error => console.log(error));
        
    }
    function makeReply(data) {
        console.log('makeReply');
        console.log(data);
        const $replyViewDiv = document.querySelector('#replyViewDiv');
        $replyViewDiv.innerHTML = '';
    
        data.data.replyList.forEach(reply => {
            const $replyView = document.createElement('div');
            $replyView.classList.add('replyView');
            const $id = document.createElement('span');
            const $replyDate = document.createElement('p');
            const $replyBody = document.createElement('p');

            $id.textContent = reply.writer.id;
            $replyDate.textContent = reply.replyDate;
            $replyBody.textContent = reply.replyBody;
            $replyView.append($id,$replyBody,$replyDate);
            $replyViewDiv.appendChild($replyView);
        });

        pagingCreate(data);
    }

    if(document.getElementById('paging')){

        const $paging = document.getElementById('paging');
        const $pagingBtn = document.querySelectorAll('.pagingBtn');

        $pagingBtn.forEach((pagingBtn, index) => { 
            pagingBtn.addEventListener('click', () =>
                pagingEvent(pagingBtn, index));
            });
    }

    function pagingEvent(pagingBtn, index) {
        ;
        const page = pagingBtn.value;
        const listNo = document.getElementById('listNo').value;
        
        console.log(pagingBtn, index, page);

        $.ajax({
            url : "/user/loadReply?refListNo=" + listNo + "&page=" + page,
        })
        .then(data => {
            console.log(data);
            makeReply(data);
            pagingCreate(data);
        })
        .catch(error => console.log(error));;
    }

    function pagingCreate(data) {
        
        console.log(data.data.paging);
        const $paging = document.getElementById('paging');
        $paging.innerHTML = '';

        const $firstBtn = document.createElement('button');
        $firstBtn.classList.add('pagingBtn');
        $firstBtn.id = 'firstBtn';
        $firstBtn.disabled = data.data.paging.page <= 1;
        $firstBtn.value = data.data.paging.startPage;
        $firstBtn.textContent = '<<';
        

        const $beforeBtn = document.createElement('button');
        $beforeBtn.classList.add('pagingBtn');
        $beforeBtn.disabled = data.data.paging.page <= 1;
        $beforeBtn.value = data.data.paging.page - 1;
        $beforeBtn.textContent = '<';

        $paging.append($firstBtn, $beforeBtn);
        for(let i = data.data.paging.startPage; i <= data.data.paging.endPage; i++){
            const $button = document.createElement('button');
            $button.classList.add('pagingBtn');
            $button.textContent = i;
            $button.disabled = data.data.paging.page == i;
            $button.value = i;

            $paging.append($button);
        }
        const $nextBtn = document.createElement('button');
        $nextBtn.classList.add('pagingBtn');
        $nextBtn.disabled = data.data.paging.page >= data.data.paging.maxPage;
        $beforeBtn.value = data.data.paging.page + 1;

        const $lastBtn = document.createElement('button');
        $lastBtn.classList.add('pagingBtn');
        $lastBtn.disabled = data.data.paging.page <= 1;
        $lastBtn.value = data.data.paging.endPage;

        
        $nextBtn.textContent = '>';
        $lastBtn.textContent = ">>";

        $paging.append($nextBtn, $lastBtn);

        const $pagingBtn = document.querySelectorAll('.pagingBtn');

        $pagingBtn.forEach((pagingBtn, index) => { 
            pagingBtn.addEventListener('click', () =>
                pagingEvent(pagingBtn, index));
            });

        
    }

    /* 댓글 수정 */
    if(document.getElementById('replyUpdateBtn')){

        const $replyUpdateBtn = document.getElementById('replyUpdateBtn');
        
        // 즉시실행임  $replyUpdateBtn.addEventListener('click', replyUpdate());
        $replyUpdateBtn.addEventListener('click', () => {
            replyUpdate();

        });

    }
    
    function replyUpdate() {

        const $replyWrite = document.getElementById('replyWrite');
        const $listNo = document.getElementById('listNo').value;

        if(!$replyWrite.value.trim()) {
            alert("댓글을 입력해주세요");
            return;
        }

        const replyWriteValue = $replyWrite.value
        const json = { replyBody : replyWriteValue, refListNo : $listNo}
        $.ajax({
            url : "/user/replyUpdate",
            type : "POST",
            data : JSON.stringify(json),
            //POST인 경우에만 get 은 안해도 됨
            headers : {
                'Content-Type' : 'application/json; charset=UTF-8'
            },
            // contentType : 'application/json; charset=UTF8',
            // dataType: 'json', 이걸로하면 성공응답을 받아도 error 코드를 찍어줌 그래서 위에껄 써야함  
        }).then(data => {
            console.log(data);
            $replyWrite.value = '';	    //댓글 입력 창 비우기
            console.log($replyWrite);
            loadReply();				//댓글 다시 로드하기
        })
        .catch(error => console.log(error,'실패확인'));
    }
    function kakaopayEvent() {
        requestPayment("kakaopay.TC0ONETIME", "kakao");
    }
    
    function cardEvent() {
        requestPayment1("html5_inicis", "card");
    }
});
