# point-picker

This React Native package allows users to select an image from their device and automatically extract the prominent color from the image. Whether you want to dynamically theme your app or simply explore the colors within an image, this tool provides an easy and efficient way to do so

## Installation

```sh
npm install point-picker
```

## Usage

```js
import { getColorFromImage } from 'point-picker';

// ...

const colors = await getColorFromImage(uri, 3, 2);
console.log('Selected Image COLOR:', colors);
```
