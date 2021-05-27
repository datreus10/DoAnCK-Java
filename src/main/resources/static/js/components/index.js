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
});