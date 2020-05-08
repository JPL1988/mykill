var version = new Date().getTime();
document.write("<script src='../statics/js/timeAndurl.js?v="+version+"'><"+'/'+"script>");
var i=0;
var a = $('#killtitle');
var content = $('#content');
var random="";
var reminds=false;
var mykill = {
    URL:{
        now:function () {
            return '/mykill/time/now';
        },
        exposer: function(killid){
            return '/mykill/killdetail/'+ killid + '/exposer';
        },
        execute:function (killid) {
            return '/mykill/killdetail/kill/'+killid+'/'+random;
        }
    },
    killexposer:function(){
        var killid = content.attr('killid');
        $.post(mykill.URL.exposer(killid),{},function (result) {
            if(result&&result['expose']){
                reminds=true;
                random = result['random'];
                if($('.st2').text()>0){
                    $('.but').removeClass('but_failed');
                    $('.but').addClass('but_success');
                }
            }
        })
    },
    killexecute:function(){
        if(!reminds){
            return;
        }
        var userphone = content.attr('userPhone');
        if(userphone==null||userphone.length==0){
            userphone = prompt("请输入电话号码");
            if(userphone==null){
                return;
            }
            while (!mykill.validatePhone(userphone)){
                userphone = prompt("请输入正确的电话号码");
                if(userphone==null){
                    return;
                }
            }
        }
        content.attr('userPhone',userphone);
        /*禁用按钮*/
        $('.but').removeClass('but_success');
        $('.but').addClass('but_failed');
        $('.but').text('抢 购 中...');
        var url=mykill.URL.execute(content.attr('killid'));
        $.ajax({
            url: url,//请求路径
            data: {phone:userphone},
            async:false,
            type: "POST",//GET
            //dataType: "JSON",//需要返回JSON对象(如果Ajax返回的是手动拼接的JSON字符串,需要Key,Value都有引号)
            success: function(result) {
                if(result&&!result['success']){
                    $('.but').removeClass('but_failed');
                    $('.but').addClass('but_success');
                    $('.but').text('立 即 抢 购');
                    if(result['error']=='phnoe number error')
                        content.attr('userPhone',"");
                    alert(result['error']);
                }else {
                     $(window).attr('location',result['data']);
                }
            },
        })
    },
    //改变颜色
    changec:function () {
        switch (i) {
            case 1:
                a.css("color","red");
                break;
            case 2:
                a.css("color","orange");
                break;
            case 3:
                a.css("color","yellow");
                break;
            case 4:
                a.css("color","blue");
                break;
            case 5:
                a.css("color","green");
                break;
            case 0:
                a.css("color","purple");
                break;
        }
        i=(i+1)%6;
    },
    formatTime:function (timestamp) {
        var date=new Date(timestamp);
        var hour=date.getHours()-8;
        var min=date.getMinutes();
        var sec=date.getSeconds();
        hour=parseInt(hour)<10?"0"+hour:hour;
        min=parseInt(min)<10?"0"+min:min;
        sec=parseInt(sec)<10?"0"+sec:sec;
        var d=hour+"时"+min+"分"+sec+"秒";
        return d;
    },
    //倒计时计算
    gettime:function () {
        var datestart = new Date().getTime();
        $.post(mykill.URL.now(),{},function (result) {
            var datenow = result['data'];
            var dateend = new Date().getTime();
            datenow = dateend-datestart+datenow;
            datestart = $("#content").attr('date_start');
            dateend = $("#content").attr('date_end');
            if(datenow<datestart){
                var resTime = datestart-datenow;
                $('#timeToStart').attr('timeOfStart',resTime);
                $('#timeToStart').text("秒杀倒计时： "+mykill.formatTime(resTime));
                setInterval(mykill.freshStartTime,1000);
            }else if(datenow<dateend){
                var resTime = dateend-datenow;
                $('#timeToStart').attr('timeOfEnd',resTime);
                $('#timeToStart').text("距秒杀结束： "+mykill.formatTime(resTime));
                setInterval(mykill.freshEndTime,1000);
                mykill.killexposer();
            }else {
                $('#timeToStart').text('秒杀已结束，请期待下一场');
            }
        });
    },
    freshStartTime:function () {
        var d = $('#timeToStart').attr('timeOfStart')-1000;
        if(d<=0){
            $('#timeToStart').attr('timeOfStart',0);
            $('#timeToStart').text('抢 购 中！');
            mykill.killexposer();
        }else {
            $('#timeToStart').attr('timeOfStart',d);
            $('#timeToStart').text("秒杀倒计时： "+mykill.formatTime(d));
        }
    },
    freshEndTime:function () {
        var d = $('#timeToStart').attr('timeOfEnd')-1000;
        if(d<=0){
            $('#timeToStart').attr('timeOfEnd',0);
            $('#timeToStart').text('秒杀已结束，请期待下一场');
        }else {
            $('#timeToStart').attr('timeOfEnd',d);
            $('#timeToStart').text("距秒杀结束： "+mykill.formatTime(d));
        }
    },
    //验证手机号
    validatePhone: function (phone) {
        if (!(/^1[3456789]\d{9}$/.test(phone))) {
            return false;
        } else {
            return true;
        }
    }
}
setInterval(mykill.changec,200);
mykill.gettime();

