// Please see documentation at https://learn.microsoft.com/aspnet/core/client-side/bundling-and-minification
// for details on configuring this project to bundle and minify static web assets.

// Write your JavaScript code.
var previewFile = function(event){
        var reader = new FileReader();
        reader.onload = function (){
                    var output = document.getElementById('preview');
        output.src = reader.result;
                };
        reader.readAsDataURL(event.target.files[0]);
};


