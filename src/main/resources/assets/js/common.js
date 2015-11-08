function toast(text) {
    var win = $(".toster-wrapper");
    $(".toster .text").text(text);
    
    if(win.is(':visible')) return;
    
    win.fadeIn(250);
    
    setTimeout(function() {
        win.fadeOut(250);
    }, 3000);
}

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
                    var productLink = "http://vk.com/share.php?url=" + offer.url + "&image=" + offer.image + "&title=" + offer.title + "&description=" + (offer.desc + "\n\n" + data.rid) + "&noparse=true";
                    window.open(productLink);
                } else {
                    if(data.status === 10) {
                        toast("Вы не авторизированы. Пожалуйста авторизируйтесь.");
                    } else if(data.status === 20) {
                        toast("У вас уже есть такой купон.");
                    } else if(data.status === 30) {
                        toast("У вас уже есть заявка на получение купона.");
                    } else {
                        toast("Неизвестная ошибка.");
                    }
                }
            },
            error: function () {
                toast("Неизвестная ошибка.");
            }
        });


    });
});


