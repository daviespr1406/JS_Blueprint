
var apiclient = (function () {

    var baseUrl = window.location.origin;

    function getBlueprintsByAuthor(author, callback) {
        if (!author) {
            callback([]);
            return;
        }
        var url = baseUrl + "/blueprints/" + encodeURIComponent(author);
        $.getJSON(url)
            .done(function (data) {
                callback(data);
            })
            .fail(function (jqxhr, status, err) {
                console.error("apiclient.getBlueprintsByAuthor error:", status, err);
                callback(null);
            });
    }

    function getBlueprintsByNameAndAuthor(author, bpname, callback) {
        if (!author || !bpname) {
            callback(null);
            return;
        }
        var url = baseUrl + "/blueprints/" + encodeURIComponent(author) + "/" + encodeURIComponent(bpname);
        $.getJSON(url)
            .done(function (data) {
                callback(data);
            })
            .fail(function (jqxhr, status, err) {
                console.error("apiclient.getBlueprintsByNameAndAuthor error:", status, err);
                callback(null);
            });
    }

    return {
        getBlueprintsByAuthor: getBlueprintsByAuthor,
        getBlueprintsByNameAndAuthor: getBlueprintsByNameAndAuthor
    };

})();
