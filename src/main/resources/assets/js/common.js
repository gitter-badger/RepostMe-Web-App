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
        var offer = {
            oid: $("#vk-button a").data('oid'),
            title: $("#vk-button a").data('title'),
            desc: $("#vk-button a").data('desc'),
            url: $("#vk-button a").data('url'),
            image: $("#vk-button a").data('image')
        };
        $.ajax("/ajax/repost", {
            method: "POST",
            data: JSON.stringify({oid: $("#vk-button a").data('oid')}),
            success: function (data, msg) {
                if (data.success === true) {
                    var productLink = "http://vk.com/share.php?url=" + offer.url + "&image=" + offer.image + "&title=" + encodeURIComponent(offer.title) + "&description=" + encodeURIComponent(offer.desc + "\n\n" + data.rid) + "&noparse=true";
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
