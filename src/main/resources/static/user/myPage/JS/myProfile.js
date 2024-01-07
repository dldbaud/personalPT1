window.addEventListener('load', function () {
    console.log("두 번째 스크립트가 로드되었습니다.");
    
    // 두 번째 스크립트 내용
    var pathname = window.location.pathname;
    if(document.getElementById("myInfoUpdate")){
    
        const $myInfoUpdate = document.getElementById("myInfoUpdate");
        if (pathname === '/user/myProfile/update') {
            $myInfoUpdate.style.color = 'blue';
          }
        $myInfoUpdate.onclick = function() {
            location.href="/user/myProfile/update";
        }
    }  
    
    if(document.getElementById("myOrderList")){
        
        const $myOrderList = document.getElementById("myOrderList");
        if (pathname === '/user/myOrderList') {
            $myOrderList.style.color = 'blue';
          }
    $myOrderList.onclick = function() {
        location.href="/user/myOrderList";
    }
}
    if(document.getElementById("sellRegist")){
        
        const $sellRegist = document.getElementById("sellRegist");
        if (pathname === '/user/sellRegist') {
            $sellRegist.style.color = 'blue';
          }
        $sellRegist.onclick = function() {
            console.log("야")
            location.href="/user/sellRegist";
        }

    }

    if(document.getElementById('userLeave')){

        const $userLeave = document.getElementById('userLeave');

        if (pathname === '/user/leave') {
            $userLeave.style.color = 'blue';
          }
        $userLeave.onclick = function() {
            location.href = "/user/leave";
            
        }
    }

    if(document.getElementById('agreeLeave')){

        const $agreeLeave = document.getElementById('agreeLeave');
        if (pathname === '/user/leaveUpdate') {
            $agreeLeave.style.color = 'blue';
          }
        $agreeLeave.onclick = function() {
            location.href = "/user/leaveUpdate";
        }
    }


    if(document.getElementById("sellCategory")){
        if (!window.loadedScript) {
        
            if (document.getElementById("sellCategory")) {
                $.ajax({
                    url: "/user/RefCategory",
                    //응답 데이타 그대로 전송
                    success: function (data) {
                        console.log(data); //배열 전송 
                        
                        const $categoryCode = $("#sellCategory");
                        for (let index in data) {
                            $categoryCode.append($("<option>").val(data[index].refCategoryNm).text(data[index].refCategoryNm));
                        }
                    },
                    error: function (xhr) { console.log(xhr); }
                });
            }
    
            window.loadedScript = true;
        }
    }

    if(document.getElementById("searchZipCode")){

        const $searchZipCode = document.getElementById("searchZipCode");
        // const $searchZip = document.getElementById("searchZip");
            
        $searchZipCode.onclick = function() {
            
            new daum.Postcode({
                oncomplete: function(data) {
                    document.getElementById("zipCode").value = data.zonecode;
                    document.getElementById("address1").value = data.address;
                    document.getElementById("address2").focus();
                }
            }).open();
            
        };

    }

    if(this.document.getElementById('myOrder')){

        const $myOrder = document.getElementById('myOrder');
        if (pathname === '/user/myProfile') {
            $myOrder.style.color = 'blue';
          }
        $myOrder.onclick = function() {
            console.log('야');
            location.href = "/user/myProfile";
        }
    }

    if(document.getElementById('myRefundList')) {

        const $myRefundList = document.getElementById('myRefundList');
        if (pathname === '/user/myRefundList') {
            $myRefundList.style.color = 'blue';
          }
        $myRefundList.onclick = function() {
            location.href = "/user/myRefundList";
        }
    }

    //재고 조절
    if(document.getElementById('ptUpdateBtn')) {

        const $ptUpdateBtn = document.getElementById('ptUpdateBtn');

        $ptUpdateBtn.onclick = function() {
            console.log('확인1');
            ptUpdate();
        }

        function ptUpdate(){
            
            
            const $ptSize = document.getElementsByName('ptSize')[0];
            console.log(ptSize);
            var ptSize;
            if (ptSize) {
                ptSize = $ptSize[0].value;
                console.log(ptSize);
            }
            const price = document.getElementsByName('price')[0].value;
            const stCount = document.getElementsByName('stCount')[0].value;
            const ptNo = document.getElementsByName('ptNo')[0].value;
            const sellNo = document.getElementsByName('sellNo')[0].value;
            console.log(price);
            console.log(stCount);
            const ptList = { ptSize, price, stCount, ptNo, sellNo}
            const json = JSON.stringify(ptList);
            $.ajax({
                url : `/sell/ptUpdate`,
                type : "POST",
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                data: json,
                error: function (xhr) {
                    console.log(xhr);
                },
				success : function(data) {
                    alert('변경 완료');
                    location.reload();
                }
            });
        }
    }
});