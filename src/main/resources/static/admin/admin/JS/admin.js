window.addEventListener('load', function () {

    if(document.getElementById('sellerManage')){

        $sellerManage = document.getElementById('sellerManage');

        $sellerManage.onclick = function() {
            location.href="/admin/sellerManage";
        }
    }

    if(document.getElementById('buttonBox1')){

        const $sellNo = document.getElementById('sellNo').value;
        const $sellNm = document.getElementById('sellNm');
        const $sellCategoryNm = document.getElementById('sellCategoryNm');
        const $sellType = document.getElementById('sellType');
        const $sellPhone = document.getElementById('sellPhone');
        const $zipCode = document.getElementById('zipCode');
        const $address1 = document.getElementById('address1');
        const $address2 = document.getElementById('address2');

        const $buttonBox1 = document.getElementById('buttonBox1');
        const $sellDto = { $sellNo, $sellNm, $sellCategoryNm, $sellType, $sellPhone, $zipCode, $address1, $address2}
        const $json = JSON.stringify($sellDto);
        
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
            })
            .catch((error) => error.text().then((res) => alert(res)));
        }
    }
});