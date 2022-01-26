package picture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.imageio.ImageIO;

public class Picture {

  private final BufferedImage image;

  public Picture(int width, int height) {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  public Picture(String filepath) {
    try {
      image = ImageIO.read(new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean contains(int x, int y) {
    return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
  }

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

  public int getHeight() {
    return image.getHeight();
  }

  /**
   * Returns the colour components (red, green, then blue) of the pixel-value located at (x,y).
   *
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *                                        the boundaries of this picture.
   */
  public Color getPixel(int x, int y) {
    int rgb = image.getRGB(x, y);
    return new Color((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff);
  }

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
   * Updates the pixel-value at the specified location.
   *
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

  private Picture forEachPixel(Function<Color, Color> transform) {
    Picture transformed = new Picture(getHeight(), getWidth());
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        transformed.setPixel(x, y, transform.apply(getPixel(x, y)));
      }
    }
    return transformed;
  }

  public Picture invert() {
    return forEachPixel(c -> new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue()));
  }

  public Picture andyWarholStyle() {
    Picture newPic = new Picture(getWidth() * 2, getHeight() * 2);
    int i = 2;
    for (int yy = 0; yy < (getHeight() * 2) - 2; yy += getHeight()) {
      for (int xx = 0; xx < (getWidth() * 2) - 2; xx += getWidth()) {
        for (int x = 0; x < getWidth(); x++) {
          for (int y = 0; y < getHeight(); y++) {
            Color rgb = getPixel(x, y);
            newPic.setPixel(xx + x, yy + y, new Color(rgb.getRed() * i,
                    rgb.getGreen() * i * 4 / 3,
                    rgb.getBlue() * i * 5 / 3));
          }
        }
        i = i * 3;
      }
    }
    return newPic;
  }

  public Picture grayScale() {
    return forEachPixel(c -> colorMakerGrayScale(c));
  }

  private Color colorMakerGrayScale(Color c) {
    int avg = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
    return new Color(avg, avg, avg);
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
    Picture newPic = new Picture(getHeight(), getWidth());
    for (int y = 0; y < getHeight(); y++) {
      List<Color> lst = new ArrayList<>();
      for (int x = 0; x < getWidth(); x++) {
        lst.add(getPixel(x, y));
      }
      for (int yy = 0; yy < getWidth(); yy++) {
        newPic.setPixel(getHeight() - y - 1, yy, lst.get(yy));
      }
    }
    return newPic;
  }

  public Picture makeDark(int magnitude) {
    return forEachPixel(c -> new Color(c.getRed() / magnitude,
            c.getGreen() / magnitude,
            c.getBlue() / magnitude));
  }

  public Picture flip(String directionOfReflection) {
    Picture newPic = new Picture(getWidth(), getHeight());
    switch (directionOfReflection) {
      case "V":
        for (int y = 0; y < getHeight(); y++) {
          List<Color> lst = new ArrayList<>();
          for (int x = 0; x < getWidth(); x++) {
            lst.add(getPixel(x, y));
          }
          for (int xx = 0; xx < getWidth(); xx++) {
            newPic.setPixel(xx, getHeight() - y - 1, lst.get(xx));
          }
        }
        break;

      case "H":
        for (int x = 0; x < getWidth(); x++) {
          List<Color> lst = new ArrayList<>();
          for (int y = 0; y < getHeight(); y++) {
            lst.add(getPixel(x, y));
          }
          for (int yy = 0; yy < getHeight(); yy++) {
            newPic.setPixel(getWidth() - x - 1, yy, lst.get(yy));
          }
        }
        break;

      default: System.out.println("Please type" + " V or H for direction of reflection");
    }
    return newPic;
  }

  public static Picture blend(List<Picture> pictures) {
    int smallestWidth = pictures.stream().mapToInt(Picture::getWidth).min().orElse(0);
    int smallestHeight = pictures.stream().mapToInt(Picture::getHeight).min().orElse(0);
    Picture newPic = new Picture(smallestWidth, smallestHeight);
    for (int y = 0; y < newPic.getHeight(); y++) {
      for (int x = 0; x < newPic.getWidth(); x++) {
        int finalY = y;
        int finalX = x;
        int avgRed = pictures.stream()
                .reduce(0, (prevSum, nextElement) -> prevSum + nextElement
                                .getPixel(finalX, finalY).getRed(),
                        Integer::sum) / pictures.size();
        int avgGreen = pictures.stream()
                .reduce(0, (prevSum, nextElement) -> prevSum + nextElement
                                .getPixel(finalX, finalY).getGreen(),
                        Integer::sum) / pictures.size();
        int avgBlue = pictures.stream()
                .reduce(0, (prevSum, nextElement) -> prevSum + nextElement
                                .getPixel(finalX, finalY).getBlue(),
                        Integer::sum) / pictures.size();
        newPic.setPixel(x, y, new Color(avgRed, avgGreen, avgBlue));
      }
    }
    return newPic;
  }

  public Picture blur() {
    Picture newPic = new Picture(getWidth(), getHeight());
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
          int redAvg = ir / 9;
          int blueAvg = ib / 9;
          int greenAvg = ig / 9;
          newPic.setPixel(x, y, new Color(redAvg, greenAvg, blueAvg));
        } else {
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
    int startIndexOfRow = 0;
    while (smallestWidth % tileSize != 0) {
      smallestWidth -= 1;
    }
    while (smallestHeight % tileSize != 0) {
      smallestHeight -= 1;
    }
    Picture newPic = new Picture(smallestWidth, smallestHeight);
    for (int ys = 0; ys < smallestHeight; ys += tileSize) {
      for (int xs = 0; xs < smallestWidth; xs += tileSize) {
        if (xs == 0) {
          if (i == startIndexOfRow) {
            i = (i + 1) % pictures.size();
          }
          startIndexOfRow = i;
        }
        if (i < (smallestWidth - 1) * (smallestHeight - 1)) {
          for (int y = ys; y < ys + tileSize; y++) {
            for (int x = xs; x < xs + tileSize; x++) {
              newPic.setPixel(x, y, pictures.get(i).getPixel(x, y));
            }
          }
        }
        i = (i + 1) % pictures.size();
      }
    }
    return newPic;
  }

  private static List<Integer> listIndicesReplicator(int listSize,
                                                     int smallestWidthMultipliedBySmallestHeight) {
    List<Integer> lst = new ArrayList<>();
    for (int i = 0; i < smallestWidthMultipliedBySmallestHeight; i++) {
      for (int z = 0; z < listSize; z++) {
        lst.add(z);
      }
    }
    return lst;
  }
}