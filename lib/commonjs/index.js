"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getColorFromImage = getColorFromImage;
var _reactNative = require("react-native");
const LINKING_ERROR = `The package 'point-picker' doesn't seem to be linked. Make sure: \n\n` + _reactNative.Platform.select({
  ios: "- You have run 'pod install'\n",
  default: ''
}) + '- You rebuilt the app after installing the package\n' + '- You are not using Expo Go\n';
const PointPicker = _reactNative.NativeModules.PointPicker ? _reactNative.NativeModules.PointPicker : new Proxy({}, {
  get() {
    throw new Error(LINKING_ERROR);
  }
});
function getColorFromImage(imagePath, x, y) {
  return PointPicker.getColorFromImage(imagePath, x, y);
}
//# sourceMappingURL=index.js.map