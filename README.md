# React Native Background Timer
Emit event periodically (also when application is running in the background).

Currently for Android only.

## Instalation
- `npm install react-native-background-timer --save`
- `rnpm link`

## Usage
```js
var {DeviceEventEmitter} = React;
var BackgroundTimer = require('react-native-background-timer');
```
```js
// start timing
BackgroundTimer.start(5000); // delay in milliseconds
```
```js
// listen for event
DeviceEventEmitter.addListener('backgroundTimer', () => {
	// this will be executed every 5 seconds
	// also when application is running in the background
	console.log('tic');
});
```
```js
// you can explicitly stop timing
BackgroundTimer.stop();
```
