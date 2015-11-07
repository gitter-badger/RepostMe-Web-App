$(document).ready(function () {
    var wrap = $(".action-bar, .navigation, .content .main");

    $(document).scroll(function (e) {
        if ($(document).scrollTop() > 80) {
            wrap.addClass("super-fixed");
        } else {
            wrap.removeClass("super-fixed");
        }
    });

    $("#vk-button a").click(function () {
        //data-url=""
        var data = {
            oid: $("#vk-button a").data('oid'),
            title: $("#vk-button a").data('title'),
            desc: $("#vk-button a").data('desc'),
            url: $("#vk-button a").data('url')
        };
        $.ajax("/ajax/repost", {
            method: "POST",
            data: JSON.stringify({oid: $("#vk-button a").data('oid')}),
            success: function (data, msg) {
                if (data.success === true) {
                    var productLink = "http://vk.com/share.php?url=" + data.url + "&title=" + encodeURIComponent(data.title) + "&description=" + encodeURIComponent(data.desc + "\n\n" + data.rid) + "&noparse=true";
                    window.open(productLink);
                } else {
                    alert("Ошибка");
                }
            },
            error: function () {
                alert("Ошибка");
            }
        });


    });
});
