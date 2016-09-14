# React Native Background Timer
Emit event periodically (even when app is in the background).

## Installation
- `npm i react-native-background-timer --save`
- `react-native link`

## Usage
You can use the `setInterval` and `setTimeout` functions.
This API is identical to that of `react-native` and can be used to quickly replace existing timers
with background timers.

```javascript
import BackgroundTimer from 'react-native-background-timer';
```

```javascript
// Start a timer that runs continuous after X milliseconds
const intervalId = BackgroundTimer.setInterval(() => {
	// this will be executed every 200 ms
	// even when app is the the background
	console.log('tic');
}, 200);

// Cancel the timer when you are done with it
BackgroundTimer.clearInterval(intervalId);
```

```javascript
// Start a timer that runs once after X milliseconds
const timeoutId = BackgroundTimer.setTimeout(() => {
	// this will be executed once after 10 seconds
	// even when app is the the background
  	console.log('tac');
}, 10000);

// Cancel the timeout if necessary
BackgroundTimer.clearTimeout(timeoutId);
```

### Obsolete
Obsolete usage which doesn't allows to use multiple background timers.

```js
import {
  DeviceEventEmitter,
  NativeAppEventEmitter,
  Platform,
} from 'react-native';

import BackgroundTimer from 'react-native-background-timer';
```

```js
const EventEmitter = Platform.select({
  ios: () => NativeAppEventEmitter,
  android: () => DeviceEventEmitter,
})();
```

```js
// start a global timer
BackgroundTimer.start(5000); // delay in milliseconds
```
```js
// listen for event
EventEmitter.addListener('backgroundTimer', () => {
	// this will be executed every 5 seconds
	// even when app is the the background
	console.log('toe');
});
```
```js
// stop the timer
BackgroundTimer.stop();
```
