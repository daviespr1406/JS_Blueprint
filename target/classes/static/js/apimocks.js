apimock = (function () {
var mockdata = [];

mockdata["johnconnor"] = [
    {
        author: "johnconnor",
        points: [{ x: 150, y: 120 }, { x: 215, y: 115 }, { x: 200, y: 180 }, { x: 170, y: 190 }],
        name: "house"
    },
    {
        author: "johnconnor",
        points: [{ x: 340, y: 240 }, { x: 15, y: 215 }, { x: 50, y: 300 }, { x: 120, y: 260 }],
        name: "gear"
    },
    {
        author: "johnconnor",
        points: [{ x: 50, y: 50 }, { x: 100, y: 50 }, { x: 100, y: 100 }, { x: 50, y: 100 }, { x: 50, y: 50 }],
        name: "square"
    },
    {
        author: "johnconnor",
        points: [{ x: 200, y: 50 }, { x: 250, y: 100 }, { x: 200, y: 150 }, { x: 150, y: 100 }, { x: 200, y: 50 }],
        name: "diamond"
    }
];

mockdata["maryweyland"] = [
    {
        author: "maryweyland",
        points: [{ x: 140, y: 140 }, { x: 115, y: 115 }, { x: 160, y: 180 }, { x: 200, y: 200 }],
        name: "house2"
    },
    {
        author: "maryweyland",
        points: [{ x: 140, y: 140 }, { x: 115, y: 115 }, { x: 170, y: 180 }, { x: 190, y: 220 }],
        name: "gear2"
    },
    {
        author: "maryweyland",
        points: [{ x: 60, y: 60 }, { x: 120, y: 60 }, { x: 120, y: 120 }, { x: 60, y: 120 }, { x: 60, y: 60 }],
        name: "square2"
    },
    {
        author: "maryweyland",
        points: [{ x: 250, y: 80 }, { x: 300, y: 130 }, { x: 250, y: 180 }, { x: 200, y: 130 }, { x: 250, y: 80 }],
        name: "diamond2"
    }
];

mockdata["sarahreese"] = [
    {
        author: "sarahreese",
        points: [{ x: 100, y: 50 }, { x: 150, y: 100 }, { x: 200, y: 50 }, { x: 150, y: 0 }, { x: 100, y: 50 }],
        name: "star"
    },
    {
        author: "sarahreese",
        points: [{ x: 50, y: 200 }, { x: 80, y: 230 }, { x: 110, y: 200 }, { x: 80, y: 170 }, { x: 50, y: 200 }],
        name: "triangle"
    }
];

return {
    getBlueprintsByAuthor: function (authname, callback) {
        callback(
            mockdata[authname]
        );
    },

    getBlueprintsByNameAndAuthor: function (authname, bpname, callback) {
        callback(
            mockdata[authname].find(function (e) { return e.name === bpname })
        );
    }
}
})();
