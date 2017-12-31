const path = require('path');
const webpack = require('webpack');
module.exports = {
    context: path.resolve(__dirname, './app'),
    entry: {
        app: './index.js',
    },
    output: {
        path: path.resolve(__dirname, './app'),
        filename: '[name].bundle.js',
        library: "App"
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: [/node_modules/],
                loader: 'babel-loader',
                options: {
                    presets: ['env']
                },
            },
        ],
    },
};