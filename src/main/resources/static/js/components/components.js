// Show comment section
$(document).ready(function () {
    $('.show-comments').click(function () {
        var id = $(this).parent().parent().attr('id');
        $('div#' + id + ' .hide-comments').slideToggle("slow");
        // Alternative animation for example
        // slideToggle("fast");
    });
    $(".post-comment").on('click', function (event) {
        var post = $(this).closest('.post.border-bottom.p-3.bg-white');
        var temp = $(this);
        var id = post.attr('id');
        var commentContent = post.find('input.comment-input').first().val();
        post.find('input.comment-input').first().val("");
        if (commentContent) {
            $.post("api/v1/comment", {
                "commentContent": commentContent,
                "postId": id.split("_")[1]
            }, function (data, status) {
                alert("Status: " + status);
                if (status == "success") {
                    var comment_html = `<li class="media"><a href="#" class="pull-left"><img src="/api/v1/file/download/${data.ownerImg}" alt="" class="img-circle"></a><div class="media-body"><div class="d-flex justify-content-between align-items-center w-100"><strong class="text-gray-dark"><a href="#" class="fs-8">${data.ownerName}</a></strong><a href="#"><i class="bx bx-dots-horizontal-rounded"></i></a></div>
                    <span class="d-block comment-created-time">${data.createdDate}</span><p class="fs-8 pt-2">${data.commentContent}</p></div></li> `
                    var commentSection = $(temp).closest('ul.media-list.comments-list');
                    $(".post-card-buttons.show-comments span").text(function (index, currentContent) {
                        return parseInt(currentContent.trim()) + 1;
                    });
                    $(commentSection).append(comment_html);
                }
            });
        }
    });
});

// Video.js
$(function () {
    var $refreshButton = $('#refresh');
    var $results = $('#css_result');

    function refresh() {
        var css = $('style.cp-pen-styles').text();
        $results.html(css);
    }

    refresh();
    $refreshButton.click(refresh);

    // Select all the contents when clicked
    $results.click(function () {
        $(this).select();
    });
});

//Messenger page script
$(".messages").animate({
    scrollTop: $(document).height()
}, "fast");

$(".conv-img").click(function () {
    $("#status-options").toggleClass("messenger-user-active");
});

$(".expand-button").click(function () {
    $(".message-profile").toggleClass("expanded");
    $("#contacts").toggleClass("expanded");
});

$("#status-options ul li").click(function () {
    $(".conv-img").removeClass();
    $("#status-online").removeClass("messenger-user-active");
    $("#status-away").removeClass("messenger-user-active");
    $("#status-busy").removeClass("messenger-user-active");
    $("#status-offline").removeClass("messenger-user-active");
    $(this).addClass("messenger-user-active");

    if ($("#status-online").hasClass("messenger-user-active")) {
        $(".conv-img").addClass("online");
    } else if ($("#status-away").hasClass("messenger-user-active")) {
        $(".conv-img").addClass("away");
    } else if ($("#status-busy").hasClass("messenger-user-active")) {
        $(".conv-img").addClass("busy");
    } else if ($("#status-offline").hasClass("messenger-user-active")) {
        $(".conv-img").addClass("offline");
    } else {
        $(".conv-img").removeClass();
    };

    $("#status-options").removeClass("messenger-user-active");
});

function newMessage() {
    message = $(".message-input input").val();
    if ($.trim(message) == '') {
        return false;
    }
    $('<li class="message-reply"><p>' + message + '</p></li>').appendTo($('.messages ul'));
    $('.message-input input').val(null);
    $('.contact.active .preview').html('<span>You: </span>' + message);
    $(".messages").animate({
        scrollTop: $(document).height()
    }, "fast");
};

$('#send-message').click(function () {
    newMessage();
});

$(window).on('keydown', function (e) {
    if (e.which == 13) {
        newMessage();
        return false;
    }
});

// Enable tooltip
$(function () {
    $('[data-toggle="tooltip"]').tooltip();
});