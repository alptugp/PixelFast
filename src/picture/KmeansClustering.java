package picture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class KmeansClustering {
  Cluster[] clusters;

  public KmeansClustering() { }

  public BufferedImage evaluate(BufferedImage givenImage, int kvalue) {
    int width = givenImage.getWidth();
    int height = givenImage.getHeight();

    clusters = generateClusters(kvalue, givenImage);

    int[] lt = new int[width * height];
    Arrays.fill(lt, -1);

    boolean changedClusterPixel = true;

    while (changedClusterPixel) {
      changedClusterPixel = false;
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int rgb = givenImage.getRGB(x, y);
          Cluster cluster = minimalCluster(rgb);
          if (lt[(width * y) + x] != cluster.getName()) {
            if (lt[(width * y) + x] != -1) {
              clusters[lt[(width * y) + x]].deletionOfPixel(rgb);
            }
            cluster.additionOfPixel(rgb);
            changedClusterPixel = true;
            lt[(width * y) + x] = cluster.getName();
          }
        }
      }
    }

    BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        output.setRGB(x, y, clusters[lt[(width * y) + x]].getRGB());
      }
    }
    return output;
  }

  public Cluster[] generateClusters(int kvalue, BufferedImage givenImage) {
    Cluster[] output = new Cluster[kvalue];
    int x = 0;
    int y = 0;
    int sx = givenImage.getWidth() / kvalue;
    int sy = givenImage.getHeight() / kvalue;
    for (int i = 0; i < kvalue; i++) {
      output[i] = new Cluster(i, givenImage.getRGB(x, y));
      x += sx;
      y += sy;
    }
    return output;
  }

  public Cluster minimalCluster(int rgb) {
    int minimum = Integer.MAX_VALUE;
    Cluster cluster = null;
    for (int i = 0; i < clusters.length; i++) {
      int separation = clusters[i].separation(rgb);
      if (separation < minimum) {
        minimum = separation;
        cluster = clusters[i];
      }
    }
    return cluster;
  }

  public static BufferedImage imageLoad(String filepath) {
    BufferedImage output;
    try {
      output = ImageIO.read(new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return output;
  }

  public static void imageSave(String file, BufferedImage givenImage) {
    try {
      ImageIO.write(givenImage, "png", new File(file));
    } catch (Exception error) {
      System.out.println(error + "Given image '" + file + "' saving failed.");
    }
  }
}



