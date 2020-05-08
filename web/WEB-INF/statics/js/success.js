function  pay() {
    var content = $('#content');
    var url = "/mykill/payfor/"+content.attr('killid')+"/"+content.attr('orderid')+"/"+content.attr('userPhone');
    $.post(url,{},function (res) {
        if(res&&res=='true'){
            alert("付款成功，感谢您的参与");
        }else {
            alert("付款失败，请重试");
        }
    });
}
function  init() {
    var content = $('#content');
    if(content.attr("status")!=0){
        $('but').addClass('but_failed');
    }
}
init();
