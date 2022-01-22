package picture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that encapsulates and provides a simplified interface for manipulating an image. The
 * internal representation of the image is based on the RGB direct colour model.
 */
public class Picture {

  /**
   * The internal image representation of this picture.
   */
  private final BufferedImage image;

  /**
   * Construct a new (blank) Picture object with the specified width and height.
   */
  public Picture(int width, int height) {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  /**
   * Construct a new Picture from the image data in the specified file.
   */
  public Picture(String filepath) {
    try {
      image = ImageIO.read(new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Test if the specified point lies within the boundaries of this picture.
   *
   * @param x the x co-ordinate of the point
   * @param y the y co-ordinate of the point
   * @return <tt>true</tt> if the point lies within the boundaries of the picture, <tt>false</tt>
   * otherwise.
   */
  public boolean contains(int x, int y) {
    return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
  }

  /**
   * Returns true if this Picture is graphically identical to the other one.
   *
   * @param other The other picture to compare to.
   * @return true iff this Picture is graphically identical to other.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (!(other instanceof Picture)) {
      return false;
    }

    Picture otherPic = (Picture) other;

    if (image == null || otherPic.image == null) {
      return image == otherPic.image;
    }
    if (image.getWidth() != otherPic.image.getWidth()
            || image.getHeight() != otherPic.image.getHeight()) {
      return false;
    }

    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        if (image.getRGB(i, j) != otherPic.image.getRGB(i, j)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Return the height of the <tt>Picture</tt>.
   *
   * @return the height of this <tt>Picture</tt>.
   */
  public int getHeight() {
    return image.getHeight();
  }

  /**
   * Return the colour components (red, green, then blue) of the pixel-value located at (x,y).
   *
   * @param x x-coordinate of the pixel value to return
   * @param y y-coordinate of the pixel value to return
   * @return the RGB components of the pixel-value located at (x,y).
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *                                        the boundaries of this picture.
   */
  public Color getPixel(int x, int y) {
    int rgb = image.getRGB(x, y);
    return new Color((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff);
  }

  /**
   * Return the width of the <tt>Picture</tt>.
   *
   * @return the width of this <tt>Picture</tt>.
   */
  public int getWidth() {
    return image.getWidth();
  }

  @Override
  public int hashCode() {
    if (image == null) {
      return -1;
    }
    int hashCode = 0;
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        hashCode = 31 * hashCode + image.getRGB(i, j);
      }
    }
    return hashCode;
  }

  public void saveAs(String filepath) {
    try {
      ImageIO.write(image, "png", new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Update the pixel-value at the specified location.
   *
   * @param x   the x-coordinate of the pixel to be updated
   * @param y   the y-coordinate of the pixel to be updated
   * @param rgb the RGB components of the updated pixel-value
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *                                        the boundaries of this picture.
   */
  public void setPixel(int x, int y, Color rgb) {

    image.setRGB(
            x,
            y,
            0xff000000
                    | (((0xff & rgb.getRed()) << 16)
                    | ((0xff & rgb.getGreen()) << 8)
                    | (0xff & rgb.getBlue())));
  }

  /**
   * Returns a String representation of the RGB components of the picture.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        Color rgb = getPixel(x, y);
        sb.append("(");
        sb.append(rgb.getRed());
        sb.append(",");
        sb.append(rgb.getGreen());
        sb.append(",");
        sb.append(rgb.getBlue());
        sb.append(")");
      }
      sb.append("\n");
    }
    sb.append("\n");
    return sb.toString();
  }

  public void invert() {
    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        Color rgb = getPixel(x, y);
        int newRed = 255 - rgb.getRed();
        int newBlue = 255 - rgb.getBlue();
        int newGreen = 255 - rgb.getGreen();
        Color newColor = new Color(newRed, newGreen, newBlue);
        setPixel(x, y, newColor);
      }
    }
  }

  public void grayScale() {
    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        Color rgb = getPixel(x, y);
        int avg = (rgb.getRed() + rgb.getBlue() + rgb.getGreen()) / 3;
        Color newColor = new Color(avg, avg, avg);
        setPixel(x, y, newColor);
      }
    }
  }

  public Picture rotate(int angle) {
    Picture semiRotated = this;
    while (angle > 0) {
      semiRotated = semiRotated.helperRotate();
      angle -= 90;
    }
    return semiRotated;
  }

  private Picture helperRotate() {
    Picture newPic = new Picture (getHeight(), getWidth());
    for (int y = 0; y < getHeight(); y++) {
      List <Color> lst = new ArrayList<>();
      for (int x = 0; x < getWidth(); x++) {
        lst.add(getPixel(x, y));
      }
      for (int yy = 0; yy < getWidth(); yy++) {
        newPic.setPixel(getHeight() - y - 1, yy, lst.get(yy));
      }
    }
    return newPic;
  }

  public void makeDark(int magnitude) {
    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        Color rgb = getPixel(x, y);
        setPixel(x, y, new Color(rgb.getRed() / magnitude, rgb.getGreen() / magnitude, rgb.getBlue() / magnitude));
      }
    }
  }

  public Picture flip(String directionOfReflection) {
    Picture newPic = new Picture(getWidth(), getHeight());
    if (directionOfReflection.equals("V")) {
      for (int y = 0; y < getHeight(); y++) {
        List<Color> lst = new ArrayList<>();
        for (int x = 0; x < getWidth(); x++) {
          lst.add(getPixel(x, y));
        }
        for (int xx = 0; xx < getWidth(); xx++) {
          newPic.setPixel(xx, getHeight() - y - 1, lst.get(xx));
        }
      }
    } else if (directionOfReflection.equals("H")) {
      for (int x = 0; x < getWidth(); x++) {
        List<Color> lst = new ArrayList<>();
        for (int y = 0; y < getHeight(); y++) {
          lst.add(getPixel(x, y));
        }
        for (int yy = 0; yy < getHeight(); yy++) {
          newPic.setPixel(getWidth() - x - 1, yy, lst.get(yy));
        }
      }
    }
    return newPic;
  }

  public static Picture blend(List<Picture> pictures) {
    int smallestWidth = pictures.stream().mapToInt(Picture::getWidth).min().orElse(0);
    int smallestHeight = pictures.stream().mapToInt(Picture::getHeight).min().orElse(0);
    Picture newPic = new Picture(smallestWidth, smallestHeight);
    for (int y = 0; y < newPic.getHeight(); y++) {
      for (int x = 0; x < newPic.getWidth(); x++) {
        int sumr = 0;
        int sumg = 0;
        int sumb = 0;
        for (Picture pic : pictures) {
          sumr += pic.getPixel(x, y).getRed();
          sumg += pic.getPixel(x, y).getGreen();
          sumb += pic.getPixel(x, y).getBlue();
        }
        newPic.setPixel(x, y, new Color(sumr / pictures.size(), sumg / pictures.size(), sumb / pictures.size()));
      }
    }
    return newPic;
  }

  public Picture blur() {
    Picture newPic = new Picture (getWidth(), getHeight());
    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        int ir = 0;
        int ig = 0;
        int ib = 0;
        if (x > 0 && y > 0 && y < getHeight() - 1 && x < getWidth() - 1) {
          for (int yy = y - 1; yy < y + 2; yy++) {
            for (int xx = x - 1; xx < x + 2; xx++) {
              ir += getPixel(xx, yy).getRed();
              ig += getPixel(xx, yy).getGreen();
              ib += getPixel(xx, yy).getBlue();
            }
          }
          int rAvg = ir / 9;
          int bAvg = ib / 9;
          int gAvg = ig / 9;
          newPic.setPixel(x, y, new Color(rAvg, gAvg, bAvg));
        }
        else {
          newPic.setPixel(x, y, getPixel(x, y));
        }
      }
    }
    return newPic;
  }

  public static Picture mosaic(List<Picture> pictures, int tileSize) {
    int smallestWidth = pictures.stream().mapToInt(Picture::getWidth).min().orElse(0);
    int smallestHeight = pictures.stream().mapToInt(Picture::getHeight).min().orElse(0);
    int i = 0;
    while (smallestWidth % tileSize != 0) {
      smallestWidth -= 1;
    }
    while (smallestHeight % tileSize != 0) {
      smallestHeight -= 1;
    }
    Picture newPic = new Picture(smallestWidth, smallestHeight);
    List<Integer> replicatedList = replicator(pictures.size(), (smallestWidth - 1) * (smallestHeight - 1));
    for (int ys = 0; ys < smallestHeight; ys += tileSize) {
      for (int xs = 0; xs < smallestWidth; xs += tileSize) {
        if (i < (smallestWidth - 1) * (smallestHeight - 1)) {
          for (int y = ys; y < ys + tileSize; y++) {
            for (int x = xs; x < xs + tileSize; x++) {
              newPic.setPixel(x, y, pictures.get(replicatedList.get(i)).getPixel(x, y));
            }
          }
      }
        i += 1;
    }
  }
    return newPic;
  }

  private static List<Integer> replicator(int listSize, int sWidthMultipliedBysHeight) {
    List<Integer> lst = new ArrayList<>();
    for (int i = 0; i < sWidthMultipliedBysHeight; i++) {
      for (int z = 0; z < listSize; z++) {
          lst.add(z);
        }
    }
    return lst;
  }
}
