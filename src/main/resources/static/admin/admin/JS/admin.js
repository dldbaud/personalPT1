window.addEventListener('load', function () {

    if(document.getElementById('sellerManage')){

        $sellerManage = document.getElementById('sellerManage');

        $sellerManage.onclick = function() {
            location.href="/admin/sellerManage";
        }
    }

});