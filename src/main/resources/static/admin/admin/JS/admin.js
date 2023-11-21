window.addEventListener('load', function () {

    if(document.getElementById('sellerManage')){

        $sellerManage = document.getElementById('sellerManage');

        $sellerManage.onclick = function() {
            location.href="/admin/sellerManage";
        }
    }

    if(document.getElementById('buttonBox1')){

        console.log("클릭");

        const sellNo = document.getElementById('sellNo').value;
        const sellerNo = document.getElementById('sellerNo').value;
        const sellNm = document.getElementById('sellNm').value;
        const sellCategoryNm = document.getElementById('sellCategoryNm').value;
        const sellType = document.getElementById('sellType').value;
        const sellPhone = document.getElementById('sellPhone').value;
        const zipCode = document.getElementById('zipCode').value;
        const address1 = document.getElementById('address1').value;
        const address2 = document.getElementById('address2').value;

        const sellAddress = zipCode + "$" + address1 + "$" +  address2;

        const $buttonBox1 = document.getElementById('buttonBox1');
        const sellDto = { sellNo, sellerNo, sellNm, sellCategoryNm, sellType, sellPhone, sellAddress}
        const $json = JSON.stringify(sellDto);
        
        $buttonBox1.onclick = function(){
            fetch("/admin/sellRegist", {
                method : "post",
                headers: {
                    'Content-Type' : 'application/json; charset=UTF-8'
                },
                body: $json
            })
            .then(result => result.text())
            .then(result => {
                alert(result)
                location.href = "/admin/sellerManage";
            })
            .catch((error) => error.text().then((res) => alert(res)));
        }
    }

    if(document.getElementById('buttonBox2')){

        console.log("클릭");

        const sellNo = document.getElementById('sellNo').value;
        const sellerNo = document.getElementById('sellerNo').value;
        const sellNm = document.getElementById('sellNm').value;
        const sellCategoryNm = document.getElementById('sellCategoryNm').value;
        const sellType = document.getElementById('sellType').value;
        const sellPhone = document.getElementById('sellPhone').value;
        const zipCode = document.getElementById('zipCode').value;
        const address1 = document.getElementById('address1').value;
        const address2 = document.getElementById('address2').value;
        const sellReqDate = document.getElementById('sellReqDate').value;
        const userNo = document.getElementById('userNo').value;

        const userDto = {
            userNo: userNo,
            // 다른 필요한 필드들을 추가로 채워넣기
        };

        const sellAddress = zipCode + "$" + address1 + "$" +  address2;

        const $buttonBox2 = document.getElementById('buttonBox1');
        const sellDto = { sellNo, sellerNo, sellNm, sellCategoryNm, sellType, sellPhone, sellAddress, sellReqDate, user: userDto}
        const $json = JSON.stringify(sellDto);
        
        $buttonBox2.onclick = function(){
            fetch("/admin/sellReject", {
                method : "post",
                headers: {
                    'Content-Type' : 'application/json; charset=UTF-8'
                },
                body: $json
            })
            .then(result => result.text())
            .then(result => {
                alert(result)
                location.href = "/admin/sellerManage";
            })
            .catch((error) => error.text().then((res) => alert(res)));
        }
    }
});