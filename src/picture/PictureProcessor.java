package picture;

import static picture.KmeansClustering.imageLoad;
import static picture.KmeansClustering.imageSave;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PictureProcessor {

  public static void main(String[] args) {
    switch (args[0]) {
      case "invert":
        Picture original = new Picture(args[1]);
        Picture inverted = original.invert();
        inverted.saveAs(args[2]);
        break;

      case "grayscale":
        original = new Picture(args[1]);
        Picture grayScaled = original.grayScale();
        grayScaled.saveAs(args[2]);
        break;

      case "andy warhol style":
        original = new Picture(args[1]);
        Picture andyWarholStyled = original.andyWarholStyle();
        andyWarholStyled.saveAs(args[2]);
        break;

      case "blend":
        List<Picture> lst = new ArrayList<>();
        for (int i = 1; i < args.length - 1; i++) {
          Picture picture = new Picture(args[i]);
          lst.add(picture);
        }
        Picture blended = Picture.blend(lst);
        blended.saveAs(args[args.length - 1]);
        break;

      case "blur":
        original = new Picture(args[1]);
        Picture blurred = original.blur();
        blurred.saveAs(args[2]);
        break;

      case "rotate":
        original = new Picture(args[2]);
        Picture rotated = original.rotate(Integer.valueOf(args[1]));
        rotated.saveAs(args[3]);
        break;

      case "make dark":
        original = new Picture(args[2]);
        Picture darkerVersion = original.makeDark(Integer.valueOf(args[1]));
        darkerVersion.saveAs(args[3]);
        break;

      case "flip":
        original = new Picture(args[2]);
        Picture flipped = original.flip(args[1]);
        flipped.saveAs(args[3]);
        break;

      case "mosaic":
        lst = new ArrayList<>();
        for (int i = 2; i < args.length - 1; i++) {
          Picture picture = new Picture(args[i]);
          lst.add(picture);
        }
        Picture mosaic = Picture.mosaic(lst, Integer.valueOf(args[1]));
        mosaic.saveAs(args[args.length - 1]);
        break;

      case "compress":
        KmeansClustering kmeansclustering = new KmeansClustering();
        int kvalue = 60;
        BufferedImage imageDst = kmeansclustering.evaluate(imageLoad(args[1]), kvalue);
        imageSave(args[2], imageDst);
        break;

      default: System.out.println("Arguments for the program were incorrectly typed");
    }
  }
}

