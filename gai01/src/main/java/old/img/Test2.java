package old.img;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class Test2 {

    /**
     * 二维码大小
     */
    private static final int QRCODE_SIZE = 412;

    public static void main(String[] args) throws Exception {

        FileInputStream fis = new FileInputStream("data/test.png");
        BufferedImage read = ImageIO.read(fis);
        String[] contents = { "二维码", "文本或图片", "合并后仍然可以用","你可以试一下哟~" };

        Integer[] perContentMaxLength = { 7, 5 };

        mergeImage(read, contents, perContentMaxLength);
        FileOutputStream fos = new FileOutputStream("data/ceshi_new.png");
        ImageIO.write(read, "PNG", fos);
        fos.close();
        System.out.println("--------OK--------");
    }

    /**
     * 在给定的图片或者二维码上添加LOGO，以二维码为例 此处LOGO内容是将文本内容添加到二维码上
     * LOGO中的文字最后一行与底部挨得近，这是double转int时损失精度导致的
     *
     * @param source
     *            原二维码
     * @param contents
     *            在二维码上添加文本内容，每个元素在LOGO上占一行
     * @param perContentMaxLength
     *            指定每个元素最长的文本字段，超出的将被舍去
     * @throws Exception
     */
    private static void mergeImage(BufferedImage source, String[] contents,
                                   Integer[] perContentMaxLength) throws Exception {

        if (null == contents || contents.length < 1) {
            return;
        }

        // LOGO的长度和宽度
        int width = 100;
        int height = 100;
        // 创建原图的几何图形对象
        Graphics2D graph = source.createGraphics();
        // 计算除去LOGO外其他部分的长和宽的一半
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;

        // 创建LOGO图像对象
        BufferedImage tag = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        // 获取LOGO的几何图形对象
        Graphics2D g2 = (Graphics2D) tag.getGraphics();
        // 设置背景色
        g2.setBackground(Color.WHITE);
        // 设置画笔,设置Paint属性
        g2.setPaint(Color.BLACK);
        g2.clearRect(0, 0, width, height);
        Font font = new Font("微软雅黑", Font.BOLD, 12);
        g2.setFont(font);

        if (null != contents && contents.length > 0) {
            // 获取操作LOGO的几何图形的操作文本的对象
            FontRenderContext context = g2.getFontRenderContext();
            // 将LOGO的高平分为字符串数组长度份
            double y2 = height / contents.length;
            for (int i = 0; i < contents.length; i++) {
                String content = contents[i];

                if (null != perContentMaxLength
                        && i < perContentMaxLength.length) {
                    if (content.length() > perContentMaxLength[i]) {
                        content = content.substring(0, perContentMaxLength[i]);
                    }
                }

                // 通过设置字符串获取到字符串在LOGO图像域中的信息
                Rectangle2D bounds = font.getStringBounds(content, context);
                // 获取LOGO几何图形中的当前字符串所占的宽度bounds.getWidth()，并计算字符串像素X的起始位置，这里除以2是为了字符串居中显示
                double x2 = (width - bounds.getWidth()) / 2;
                // 获取LOGO几何图形中的当前字符串所在的高度，y2是平均分的字符串数组的高度
                double y3 = (y2 + bounds.getHeight()) / 2;
                // 计算字符串像素y的起始位置
                double y4 = y2 * i + y3;

                g2.drawString(content, (int) x2, (int) y4);
            }
        }

        // 将LOGO修改后的图形整合到原图上
        graph.drawImage(tag, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
        g2.dispose();
    }
}
