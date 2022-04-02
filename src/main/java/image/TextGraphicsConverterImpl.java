package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    private int maxWidth = -1;
    private int maxHeight = -1;
    private double maxRatio = -1;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));//скачали картинку с инета

        double ratio = (double) (img.getWidth() / img.getHeight()); //деление ширины на высоту

        if (maxRatio != -1 && maxRatio < ratio) throw new BadImageSizeException(ratio, maxRatio);
        int newWidth = 0;
        int newHeight = 0;
        if (maxWidth != -1 && maxWidth < img.getWidth()) {
            //если ширина задана и ширина изображения больше
            double ratioWidth = (double) ((img.getWidth() * 100) / maxWidth) / 100;
            newWidth = (int) Math.round(img.getWidth() / ratioWidth);
            newHeight = (int) Math.round(img.getHeight() / ratioWidth);
            if (maxHeight != -1 && maxHeight < newHeight) {
                //если высота задана и высота изображения после формулы осталась больше
                double ratioHeight = (double) ((newHeight * 100) / maxHeight) / 100;
                newWidth = (int) Math.round(newWidth / ratioHeight);
                newHeight = (int) Math.round(newHeight / ratioHeight);
            }
        } else if (maxHeight != -1 && maxHeight < img.getHeight()) {
            //иначе если ширина не задана, а задана высота и высота изображения больше
            double ratioHeight = (double) ((img.getHeight() * 100) / maxHeight) / 100;
            newWidth = (int) Math.round(img.getWidth() / ratioHeight);
            newHeight = (int) Math.round(img.getHeight() / ratioHeight);
        } else {
            //иначе если не задана ширина и высота
            if (maxWidth == -1) {
                //если не задана ширина
                newWidth = img.getWidth();
                if (maxHeight == -1) {
                    //и не задана высота
                    newHeight = img.getHeight();
                }
            } else if (maxHeight == -1) {
                //иначе если не задана только высота
                newHeight = img.getHeight();
            }
        }
        // Теперь нам надо попросить картинку изменить свои размеры на новые.
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        // Теперь сделаем её чёрно-белой. Для этого поступим так:
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        // Попросим у этой картинки инструмент для рисования на ней:
        Graphics2D graphics = bwImg.createGraphics();
        // А этому инструменту скажем, чтобы он скопировал содержимое из нашей суженной картинки:
        graphics.drawImage(scaledImage, 0, 0, null);

        WritableRaster bwRaster = bwImg.getRaster();
        //создали хранилище символов
        StringBuffer charImg = new StringBuffer();

        //создали схему подбора символов
        TextColorSchema schema = new TextColorSchemaImpl();

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                charImg.append(c);//записываем символ c
                charImg.append(c);//повторяем
            }
            charImg.append("\n");
        }

        // Осталось собрать все символы в один большой текст
        // Для того, чтобы изображение не было слишком узким, рекомендую
        // каждый пиксель превращать в два повторяющихся символа, полученных
        // от схемы.

        return String.valueOf(charImg);// Возвращаем собранный текст.
    }

    @Override
    public void setMaxWidth(int width) {//поменять макс высоту
        maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {//поменять макс ширину
        maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {//поменять макс сотношение
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {//поменять цвет текстовой схемы

    }
}
