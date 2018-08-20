module.exports = {
    "extends": "airbnb",
    "rules": {
        "class-methods-use-this": ["error", { "exceptMethods": ["start", "stop"] }]
    }
};
