package picture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Cluster {
  int name;
  int countOfPixels;
  int redColor;
  int greenColor;
  int blueColor;
  int redColors;
  int greenColors;
  int blueColors;

  public Cluster(int name, int rgb) {
    int green = rgb >> 8 & 0x000000FF;
    int blue = rgb >> 0 & 0x000000FF;
    int red = rgb >> 16 & 0x000000FF;
    redColor = red;
    greenColor = green;
    blueColor = blue;
    this.name = name;
    additionOfPixel(rgb);
  }

  int getName() {
    return name;
  }

  int getRGB() {
    int red = redColors / countOfPixels;
    int blue = blueColors / countOfPixels;
    int green = greenColors / countOfPixels;
    return 0xff000000 | red << 16 | green << 8 | blue;
  }

  public void reset() {
    redColor = 0;
    blueColor = 0;
    greenColor = 0;
    redColors = 0;
    blueColors = 0;
    greenColors = 0;
    countOfPixels = 0;
  }

  void deletionOfPixel(int rgb) {
    int green = rgb >> 8 & 0x000000FF;
    int blue = rgb >> 0 & 0x000000FF;
    int red = rgb >> 16 & 0x000000FF;
    redColors -= red;
    greenColors -= green;
    blueColors -= blue;
    countOfPixels--;
    redColor = redColors / countOfPixels;
    blueColor = blueColors / countOfPixels;
    greenColor = greenColors / countOfPixels;
  }

  void additionOfPixel(int colour) {
    int red = colour >> 16 & 0x000000FF;
    int green = colour >> 8 & 0x000000FF;
    int blue = colour >> 0 & 0x000000FF;
    redColors += red;
    greenColors += green;
    blueColors += blue;
    countOfPixels++;
    redColor = redColors / countOfPixels;
    greenColor = greenColors / countOfPixels;
    blueColor = blueColors / countOfPixels;
  }

  int separation(int colour) {
    int red = colour >> 16 & 0x000000FF;
    int green = colour >> 8 & 0x000000FF;
    int blue = colour >> 0 & 0x000000FF;
    int redx = Math.abs(redColor - red);
    int bluex = Math.abs(blueColor - blue);
    int greenx = Math.abs(greenColor - green);
    return (redx + bluex + greenx) / 3;
  }
}
