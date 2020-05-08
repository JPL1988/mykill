var i=0;
var a = document.getElementsByTagName("a")[0];
function changeclor() {
    switch (i) {
        case 1:
            a.style.color = "red";
            break;
        case 2:
            a.style.color = "orange";
            break;
        case 3:
            a.style.color = "yellow";
            break;
        case 4:
            a.style.color = "blue";
            break;
        case 5:
            a.style.color = "green";
            break;
        case 0:
            a.style.color = "purple";
            break;
    }
    i=(i+1)%6;
}
setInterval(changeclor,200);
function toIndex() {
    window.location.href = "/mykill/killindex";
}
/*
setTimeout(toIndex,3000);*/




function scroll_content1() {
    var item = document.getElementsByClassName('scroll-content')[0];
    var url = "/mykill/killindex/"+item.getAttribute('date_banch');
    window.location.href=url;
}
function scroll_content2() {
    var item = document.getElementsByClassName('scroll-content')[1];
    var url = "/mykill/killindex/"+item.getAttribute('date_banch');
    window.location.href=url;
}
function scroll_content3() {
    var item = document.getElementsByClassName('scroll-content')[2];
    var url = "/mykill/killindex/"+item.getAttribute('date_banch');
    window.location.href=url;
}
function scroll_content4() {
    var item = document.getElementsByClassName('scroll-content')[3];
    var url = "/mykill/killindex/"+item.getAttribute('date_banch');
    window.location.href=url;
}

