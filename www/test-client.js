(function(){
    var connect = function(user, game, onopen, onmessage){
        var socket = new WebSocket("ws://localhost:8088/messagerelay/"+user+"/"+ game + (onmessage ? "" : "/send_only"));
        socket.onopen=onopen;
        socket.onmessage =onmessage;
        return socket;
    }

    $(function(){
        var jqOpenClose =$("#openclose");
        var jqSend = $("#send-message");
        var s;
        var openClickHandler = function(){
            var user = $("#user").val();
            var game = $("#game").val();
            s = connect(user, game, function(){
                jqSend.prop("disabled","");
                jqSend.off("click");
                jqSend.click(function(){
                    var sendConnection=connect($("#recipient").val(), game,
                        function(){
                            var sentmsg =$("#message-box").val();
                            sendConnection.send(sentmsg);
                            $("#message-box").val("");
                            $("#result-box").append(new Date().toString() + " [" + game + "/"+ user +"]:</br>");
                            $("#result-box").append(sentmsg+"</br>");
                            sendConnection.close()
                        }
                    )
                })
            },
            function(msg){
                $("#result-box").append(new Date().toString() + " [" + game + "/"+ user +"]:</br>");
                $("#result-box").append(msg.data+"</br>");
            });
            jqOpenClose.off("click",openClickHandler);
            jqOpenClose.click(closeClickHandler);
            jqOpenClose.val("CLOSE CONNECTION");
        }
        var closeClickHandler = function(){
            s.close();
            $("result-box").html("");
            jqSend.prop("disabled","disabled");
            jqOpenClose.off("click",closeClickHandler);
            jqOpenClose.click(openClickHandler);
            jqOpenClose.val("OPEN CONNECTION");
        }
        jqOpenClose.click(openClickHandler);
    })

})()