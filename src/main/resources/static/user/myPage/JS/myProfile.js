window.addEventListener('load', function () {
    console.log("두 번째 스크립트가 로드되었습니다.");
    
    // 두 번째 스크립트 내용
    
    if(document.getElementById("myInfoUpdate")){
    
        const $myInfoUpdate = document.getElementById("myInfoUpdate");
        
        $myInfoUpdate.onclick = function() {
            location.href="/user/myProfile/update";
        }
    }  
    
    if(document.getElementById("myOrderList")){
        
        const $myOrderList = document.getElementById("myOrderList");
        
    $myOrderList.onclick = function() {
        location.href="/user/myOrderList";
    }
}
    if(document.getElementById("sellRegist")){
        
        const $sellRegist = document.getElementById("sellRegist");

        $sellRegist.onclick = function() {
            console.log("야")
            location.href="/sell/sellRegist";
        }

    }

    if(document.getElementById("sellCategory")){
        if (!window.loadedScript) {
        
            if (document.getElementById("sellCategory")) {
                $.ajax({
                    url: "/sell/RefCategory",
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

        // const $searchZipCode = document.getElementById("searchZipCode");
        const $searchZip = document.getElementById("searchZip");
            
        $searchZip.onclick = function() {
            
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

        $myOrder.onclick = function() {
            location.href = "/user/myProfile";
        }
    }

    if(document.getElementById('myRefundList')) {

        const $myRefundList = document.getElementById('myRefundList');

        $myRefundList.onclick = function() {
            location.href = "/user/myRefundList";
        }
    }
});