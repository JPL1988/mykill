var i=0;
var a = document.getElementById("killtitle");
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