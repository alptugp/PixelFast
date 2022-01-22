package picture;

import java.util.List;
import java.util.ArrayList;

public class PictureProcessor {

  public static void main(String[] args) {
    if (args[0].equals("invert")) {
      Picture inverted = new Picture(args[1]);
      inverted.invert();
      inverted.saveAs(args[2]);
    }
    else if (args[0].equals("grayscale")) {
      Picture original = new Picture(args[1]);
      original.grayScale();
      original.saveAs(args[2]);
    }
    else if (args[0].equals("blend")) {
      List<Picture> lst = new ArrayList<>();
      for (int i = 1; i < args.length - 1; i++) {
        Picture picture = new Picture(args[i]);
        lst.add(picture);
      }
      Picture blended = Picture.blend(lst);
      blended.saveAs(args[args.length - 1]);
    }
    else if (args[0].equals("blur")) {
      Picture original = new Picture(args[1]);
      Picture blurred = original.blur();
      blurred.saveAs(args[2]);
    }
    else if (args[0].equals("rotate")) {
      Picture original = new Picture(args[2]);
      Picture rotated = original.rotate(Integer.valueOf(args[1]));
      rotated.saveAs(args[3]);
    }

    else if (args[0].equals("makeDark")) {
      Picture original = new Picture(args[2]);
      original.makeDark(Integer.valueOf(args[1]));
      original.saveAs(args[3]);
    }

    else if (args[0].equals("flip")) {
      Picture original = new Picture(args[2]);
      Picture flipped = original.flip(args[1]);
      flipped.saveAs(args[3]);
    }
    else if (args[0].equals("mosaic")) {
      List<Picture> lst = new ArrayList<>();
      for (int i = 2; i < args.length - 1; i++) {
        Picture picture = new Picture(args[i]);
        lst.add(picture);
      }
      Picture mosaic = Picture.mosaic(lst, Integer.valueOf(args[1]));
      mosaic.saveAs(args[args.length - 1]);
    }
  }
}
