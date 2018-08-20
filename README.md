# React Native Background Timer
Emit event periodically (even when app is in the background).

## Installation

:warning: If you use [create-react-native-app](https://github.com/react-community/create-react-native-app) you must [eject](https://github.com/react-community/create-react-native-app/blob/master/EJECTING.md) it before running `react-native link`.

- `yarn add react-native-background-timer`
- `react-native link`

### Installation using CocoaPods on iOS
- `yarn add react-native-background-timer`
- add the following to your Podfile: `pod 'react-native-background-timer', :path => '../node_modules/react-native-background-timer'`

## Usage Crossplatform
To use the same code both on Android and iOS use runBackgroundTimer() and stopBackgroundTimer(). There can be used only one background timer to keep code consistent.

```javascript
BackgroundTimer.runBackgroundTimer(() => { 
//code that will be called every 3 seconds 
}, 
3000);
//rest of code will be performing for iOS on background too

BackgroundTimer.stopBackgroundTimer(); //after this call all code on background stop run.
```
> Android didn't tested as well.

## Usage iOS
After iOS update logic of background task little bit changed. So we can't use as it was. 
You have to use only start() and stop() without parameters. And all code that is performing will continue performing on background including all setTimeout() timers.

Example:
```javascript
BackgroundTimer.start();
// Do whatever you want incuding setTimeout;
BackgroundTimer.stop();
```

> If you call stop() on background no new tasks will be started!
> Don't call .start() twice, as it stop performing previous background task and starts new. 
> If it will be called on backgound no tasks will run.

## Usage Android
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
BackgroundTimer.start(5000); // delay in milliseconds only for Android
```
```js
// listen for event
EventEmitter.addListener('backgroundTimer', () => {
	// this will be executed once after 5 seconds
	console.log('toe');
});
```
```js
// stop the timer
BackgroundTimer.stop();
```
