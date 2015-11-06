$(document).ready(function() {
  var wrap = $(".action-bar, .navigation, .content .main");

  $(document).scroll(function(e) {
    if ($(document).scrollTop() > 80) {
      wrap.addClass("super-fixed");
    } else {
      wrap.removeClass("super-fixed");
    }
  });
});
