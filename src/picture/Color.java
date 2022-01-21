package picture;

/**
 * Encapsulate the colours using the RGB direct color-model. The individual red, green and blue
 * components of a colour are assigned a value ranging from 0 to 255. A component value of 0
 * signifies no contribution is made to the color.
 */
public class Color {

  private final int red;
  private final int green;
  private final int blue;

  /**
   * Constructs a new Color object with the specified intensity values for the red, green and blue
   * components.
   *
   * @param red   the intensity of the red component in this Color.
   * @param green the intensity of the green component in this Color.
   * @param blue  the intensity of the blue component in this Color.
   */
  public Color(int red, int green, int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public int getBlue() {
    return blue;
  }

  public int getGreen() {
    return green;
  }

  public int getRed() {
    return red;
  }
}
