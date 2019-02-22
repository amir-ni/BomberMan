package Utilies;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GifUtility {

    static void resize(String inputAddress, String outputAddress, int newWidth, int newHeight, int framesDelay)
    {
        try {
            String[] imageAttributes = new String[]{
                    "imageLeftPosition",
                    "imageTopPosition",
                    "imageWidth",
                    "imageHeight"
            };

            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(new File(inputAddress));
            reader.setInput(imageInputStream, false);

            int noi = reader.getNumImages(true);
            BufferedImage master = null;
            BufferedImage[] s = new BufferedImage[noi];
            for (int i = 0; i < noi; i++) {
                BufferedImage image = reader.read(i);
                IIOMetadata metadata = reader.getImageMetadata(i);

                Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
                NodeList children = tree.getChildNodes();

                for (int j = 0; j < children.getLength(); j++) {
                    Node nodeItem = children.item(j);

                    if(nodeItem.getNodeName().equals("ImageDescriptor")){
                        Map<String, Integer> imageAttr = new HashMap<>();
                        for(String imageAttribute : imageAttributes){
                            NamedNodeMap attr = nodeItem.getAttributes();
                            Node attnode = attr.getNamedItem(imageAttribute);
                            imageAttr.put(imageAttribute, Integer.valueOf(attnode.getNodeValue()));
                        }
                        if(i==0){
                            master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"), BufferedImage.TYPE_INT_ARGB);
                        }
                        master.getGraphics().drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"), null);
                    }
                }
                BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

                Graphics2D bGr = bufferedImage.createGraphics();
                assert master != null;
                bGr.drawImage(master.getScaledInstance(newWidth,newHeight, Image.SCALE_SMOOTH), 0, 0, null);
                bGr.dispose();
                s[i] = bufferedImage;
            }

            ImageOutputStream output = new FileImageOutputStream(new File(outputAddress));
            GifSequenceWriter writer = new GifSequenceWriter(output, s[0].getType(), framesDelay, true);

            for(BufferedImage bi:s)
            {
                writer.writeToSequence(bi);
            }
            writer.close();
            output.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
