# React Native Background Timer
Emit event periodically (also when application is running in the background).

## Installation
- `npm install react-native-background-timer --save`
- `react-native link`

## Usage
```js
var {DeviceEventEmitter} = React;
var BackgroundTimer = require('react-native-background-timer');
```
```js
// start a global timer
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
// stop the timer
BackgroundTimer.stop();
```

## setInterval, setTimeout

Alternatively, you can use the `setInterval` and `setTimeout` functions.
This API is identical to that of `react-native` and can be used to quickly replace existing timers
with background timers.

```javascript
var BackgroundTimer = require('react-native-background-timer');
```

```javascript
// Start a timer that runs continuous after X msec
const intervalId = BackgroundTimer.setInterval(() => {
	// this will be executed every 200 ms
	// even when app is in the background
	console.log('tac');
}, 200);

// Cancel the timer when you are done with it
BackgroundTimer.clearInterval(intervalId);
```

```javascript
// Start a timer that runs once after X msec
const timeoutId = BackgroundTimer.setTimeout(() => {
	// this will be executed once after 10 seconds
	// even when app goed into the background
  	console.log('toe');
}, 10000);

// Cancel the timeout if necessary
BackgroundTimer.clearTimeout(timeoutId);
```