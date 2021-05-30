$(document).ready(function () {
    $("#btn-createPost").on("click", function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();

        // Get form
        var form = $('#create-post')[0];

        // Create an FormData object 
        var data = new FormData(form);

        // disabled the submit button
        $("#btn-createPost").prop("disabled", true);

        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/post",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function (data) {
                $("#create-post textarea").val('');
                alert("SUCCESS : " + data);
                $("#btn-createPost").prop("disabled", false);
                location.reload();
            },
            error: function (e) {
                $("#create-post textarea").val('');
                alert("ERROR : " + e.responseText);
                $("#btn-createPost").prop("disabled", false);

            }
        });

    });

    $(".btn.comment-form-btn").on('click', function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();

        // Get form
        var form = $(this).closest('form');
        var id = $(form).attr('id').split('-')[2];
        // Create an FormData object 
        var dataForm = new FormData(form[0]);

        // disabled the submit button
        $(".btn.comment-form-btn").prop("disabled", true);

        $.ajax({
            type: "POST",
            url: "/comment",
            data: dataForm,
            processData: false,
            contentType: false,
            success: function (data) {
                alert("SUCCESS !!");
                $(form).closest('ul').append(data);
                $(".form-control.comment-input").val("");
                $(".btn.comment-form-btn").prop("disabled", false);
                $(`#post_${id} .post-card-buttons.show-comments span`).text(function (index, currentContent) {
                    return parseInt(currentContent.trim()) + 1;
                });
            },
            error: function (e) {
                alert("ERROR : " + e.responseText);
                $(form).closest('.form-control.comment-input').val('');
                $(".btn.comment-form-btn").prop("disabled", false);
            }
        });
    });

    $("span.like-btn").on('click', function (event) {
        var id = $(this).parent().parent().parent().attr('id').split('_')[1];
        var span = $(this);
        $.ajax({
            url: '/react',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({
                "postId": id
            }),
            success: function (data) {
                span.find('span').text(function (index, currentContent) {
                    if (data.msg.includes("unreaction")){
                        span.find("i").css("color","#6C757D");           
                        return parseInt(currentContent.trim()) - 1;
                    }              
                    else{
                        span.find("i").css("color","dodgerblue");
                        return parseInt(currentContent.trim()) + 1;
                    }
                        
                });
                
            },
            error: function (e) {

            }
        });
    });
});