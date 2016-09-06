'use strict';
import {
	NativeModules,
	DeviceEventEmitter
} from 'react-native';
const RNBackgroundTimer = NativeModules.BackgroundTimer || NativeModules.RNBackgroundTimer;

let uniqueId = 0;
const callbacks = {};

const BackgroundTimer = {

	// Original API
	start(delay) {
		return RNBackgroundTimer.start(delay);
	},

	stop() {
		return RNBackgroundTimer.stop();
	},

	// New API, allowing for multiple timers
	setTimeout(callback, timeout) {
		const timeoutId = ++uniqueId;
		callbacks[timeoutId] = {
			callback: callback,
			interval: false,
			timeout: timeout
		};
		RNBackgroundTimer.setTimeout(timeoutId, timeout);
		return timeoutId;
	},

	clearTimeout(timeoutId) {
		if (callbacks[timeoutId]) {
			delete callbacks[timeoutId];
			//RNBackgroundTimer.clearTimeout(timeoutId);
		}
	},

	setInterval(callback, timeout) {
		const intervalId = ++uniqueId;
		callbacks[intervalId] = {
			callback: callback,
			interval: true,
			timeout: timeout
		};
		RNBackgroundTimer.setTimeout(intervalId, timeout);
		return intervalId;
	},

	clearInterval(intervalId) {
		if (callbacks[intervalId]) {
			delete callbacks[intervalId];
			//RNBackgroundTimer.clearTimeout(intervalId);
		}
	}
};

DeviceEventEmitter.addListener('backgroundTimer.timeout', (id) => {
	if (callbacks[id]) {
		const callback = callbacks[id].callback;
		if (!callbacks[id].interval) {
			delete callbacks[id];
		}
		else {
			RNBackgroundTimer.setTimeout(id, callbacks[id].timeout);
		}
		callback();
	}
});

module.exports = BackgroundTimer;
