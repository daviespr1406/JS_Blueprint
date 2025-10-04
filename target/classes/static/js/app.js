
var app = (function () {


    var backend = apimock;

    var _author = '';
    var _blueprintsSummary = []; // [{name, points}]
    var $tbody = $("#blueprintsTable tbody");
    var $selectedAuthor = $("#selectedAuthor");
    var $totalPoints = $("#totalPoints");
    var $message = $("#message");
    var canvas = document.getElementById("bpCanvas");
    var ctx = canvas.getContext("2d");
    var $selectedBlueprint = $("#selectedBlueprint");

    function normalizeAuthor(a) {
        if (!a) return a;
        return a.toString().toLowerCase().replace(/\s+/g, '');
    }

    function changeAuthor(name) {
        _author = name || '';
        $selectedAuthor.text(_author ? ("Author: " + _author) : '');
    }

    function clearCanvas() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
    }

    function drawPoints(points) {
        clearCanvas();
        if (!points || points.length === 0) {
            return;
        }

        var minX = Number.POSITIVE_INFINITY, minY = Number.POSITIVE_INFINITY;
        var maxX = Number.NEGATIVE_INFINITY, maxY = Number.NEGATIVE_INFINITY;
        points.forEach(function (p) {
            if (p.x < minX) minX = p.x;
            if (p.y < minY) minY = p.y;
            if (p.x > maxX) maxX = p.x;
            if (p.y > maxY) maxY = p.y;
        });

        var bpWidth = maxX - minX || 1;
        var bpHeight = maxY - minY || 1;

        var margin = 20;
        var availW = canvas.width - margin * 2;
        var availH = canvas.height - margin * 2;

        var scale = Math.min(availW / bpWidth, availH / bpHeight);

        var offsetX = (canvas.width - (bpWidth * scale)) / 2;
        var offsetY = (canvas.height - (bpHeight * scale)) / 2;

        ctx.beginPath();
        for (var i = 0; i < points.length; i++) {
            var p = points[i];
            var x = offsetX + (p.x - minX) * scale;
            var y = offsetY + (p.y - minY) * scale;
            if (i === 0) ctx.moveTo(x, y);
            else ctx.lineTo(x, y);
        }
        ctx.lineWidth = 2;
        ctx.strokeStyle = "#2c3e50";
        ctx.stroke();

        // Dibujar puntos como pequeños círculos
        for (var j = 0; j < points.length; j++) {
            var pp = points[j];
            var cx = offsetX + (pp.x - minX) * scale;
            var cy = offsetY + (pp.y - minY) * scale;
            ctx.beginPath();
            ctx.arc(cx, cy, 3, 0, 2 * Math.PI);
            ctx.fillStyle = "#e74c3c";
            ctx.fill();
        }
    }

    function drawBlueprint(authorParam, bpname) {
        if (!authorParam || !bpname) {
            $message.text("Author or blueprint name missing.");
            return;
        }
        $message.text("");
        var key = (backend === apimock) ? normalizeAuthor(authorParam) : authorParam;

        backend.getBlueprintsByNameAndAuthor(key, bpname, function (bp) {
            if (!bp) {
                $message.text("Blueprint not found: " + bpname + " (author: " + authorParam + ")");
                return;
            }
            $selectedBlueprint.text("Drawing: " + bp.name + " (author: " + bp.author + ")");

            drawPoints(bp.points || []);
        });
    }

    function render(list) {
        $tbody.empty();
        list.forEach(function (bp) {
            var $tr = $("<tr>");
            $tr.append($("<td>").text(bp.name));
            $tr.append($("<td>").text(bp.points));
            var $openTd = $("<td>");
            var $btn = $("<button>")
                .addClass("btn btn-xs btn-success open-btn")
                .text("Open")
                .attr("data-name", bp.name)
                .attr("data-author", _author);
            $openTd.append($btn);
            $tr.append($openTd);
            $tbody.append($tr);
        });

        var total = list.reduce(function (acc, bp) { return acc + bp.points; }, 0);
        $totalPoints.text(total);
    }

    function updateBlueprints(authorName) {
        if (!authorName) {
            $message.text("Please enter an author name.");
            return;
        }
        $message.text("");
        changeAuthor(authorName);

        var key = (backend === apimock) ? normalizeAuthor(authorName) : authorName;

        backend.getBlueprintsByAuthor(key, function (data) {
            if (!data || !Array.isArray(data) || data.length === 0) {
                $message.text("No blueprints found for '" + authorName + "'.");
                _blueprintsSummary = [];
                render(_blueprintsSummary);
                clearCanvas();
                $selectedBlueprint.text("No blueprint selected");
                return;
            }

            _blueprintsSummary = data.map(function (bp) {
                return {
                    name: bp.name,
                    points: (bp.points || []).length
                };
            });

            render(_blueprintsSummary);
            $message.text("");
            clearCanvas();
            $selectedBlueprint.text("No blueprint selected");
        });
    }

    function setupEventDelegation() {
        // Botón Get blueprints
        $("#getBlueprints").off("click").on("click", function () {
            var author = $("#author").val().trim();
            if (!author) {
                $message.text("Please enter an author name.");
                return;
            }
            updateBlueprints(author);
        });

        // Delegación para botones Open en la tabla
        $tbody.off("click", ".open-btn").on("click", ".open-btn", function () {
            var bpname = $(this).attr("data-name");
            var author = $(this).attr("data-author");
            drawBlueprint(author, bpname);
        });
    }

    return {
        updateBlueprints: updateBlueprints,
        drawBlueprint: drawBlueprint,
        // utilidades para debugging / tests
        setBackend: function (b) { backend = b; },
        getBackendName: function () { return (backend === apimock) ? "apimock" : "apiclient"; }
    };

})();

$(function () {
    if (typeof app !== "undefined" && typeof app.setBackend === "function") {
    }

    $("#getBlueprints").off("click").on("click", function () {
        var author = $("#author").val().trim();
        if (!author) {
            $("#message").text("Please enter an author name.");
            return;
        }
        app.updateBlueprints(author);
    });

    $("#blueprintsTable tbody").off("click", ".open-btn").on("click", ".open-btn", function () {
        var bpname = $(this).attr("data-name");
        var author = $(this).attr("data-author");
        app.drawBlueprint(author, bpname);
    });
});
