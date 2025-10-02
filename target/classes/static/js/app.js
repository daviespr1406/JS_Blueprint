var app = (function () {

    let _selectedAuthor = null;
    let _blueprintsList = [];

    function setAuthor(author) {
        _selectedAuthor = author;
        $("#selectedAuthor").text("Author: " + _selectedAuthor);
    }

    function loadBlueprints(author) {
        apimock.getBlueprintsByAuthor(author, function (blueprints) {

            if (!blueprints) {
                alert("No blueprints found for author: " + author);
                return;
            }

            setAuthor(author);

            // Transformar lista: solo nombre y cantidad de puntos
            _blueprintsList = blueprints.map(function (bp) {
                return {
                    name: bp.name,
                    points: bp.points.length
                };
            });

            // Limpiar tabla
            $("#blueprintsTable tbody").empty();

            // Renderizar en tabla
            _blueprintsList.forEach(function (bp) {
                $("#blueprintsTable tbody").append(
                    "<tr><td>" + bp.name + "</td><td>" + bp.points + "</td><td></td></tr>"
                );
            });

            // Calcular total puntos
            let totalPoints = _blueprintsList.reduce(function (sum, bp) {
                return sum + bp.points;
            }, 0);

            $("#totalPoints").text(totalPoints);
        });
    }

    return {
        loadBlueprints: loadBlueprints
    };

})();

$(document).ready(function () {
    $("#getBlueprints").click(function () {
        let author = $("#author").val();
        if (author) {
            app.loadBlueprints(author);
        } else {
            alert("Please enter an author name");
        }
    });
});
