package com.nhb.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author: luck_nhb
 * @createDate: 2022-01-17 14:07:14
 * @version: 1.0.0
 * @description: 图片制作水印
 */
public class ImageRemarkUtil {

    // 水印透明度
    private static float alpha = 0.5f;
    // 水印横向位置
    private static int positionWidth = 400;
    // 水印纵向位置
    private static int positionHeight = 900;
    // 水印文字字体
    private static Font font = new Font("宋体", Font.BOLD, 99);
    // 水印文字颜色
    private static Color color = Color.RED;

    /**
     *
     * @param alpha
     *            水印透明度
     * @param positionWidth
     *            水印横向位置
     * @param positionHeight
     *            水印纵向位置
     * @param font
     *            水印文字字体
     * @param color
     *            水印文字颜色
     */
    public static void setImageMarkOptions(float alpha, int positionWidth,
                                           int positionHeight, Font font, Color color) {
        if (alpha != 0.0f)
            ImageRemarkUtil.alpha = alpha;
        if (positionWidth != 0)
            ImageRemarkUtil.positionWidth = positionWidth;
        if (positionHeight != 0)
            ImageRemarkUtil.positionHeight = positionHeight;
        if (font != null)
            ImageRemarkUtil.font = font;
        if (color != null)
            ImageRemarkUtil.color = color;
    }

    /**
     * 给图片添加水印图片
     *
     * @param iconPath
     *            水印图片路径
     * @param srcImgPath
     *            源图片路径
     * @param targetPath
     *            目标图片路径
     */
    public static void markImageByIcon(String iconPath, String srcImgPath,
                                       String targetPath) {
        markImageByIcon(iconPath, srcImgPath, targetPath, null);
    }

    /**
     * 给图片添加水印图片、可设置水印图片旋转角度
     *
     * @param iconPath
     *            水印图片路径
     * @param srcImgPath
     *            源图片路径
     * @param targetPath
     *            目标图片路径
     * @param degree
     *            水印图片旋转角度
     */
    public static void markImageByIcon(String iconPath, String srcImgPath,
                                       String targetPath, Integer degree) {
        OutputStream os = null;
        try {

            Image srcImg = ImageIO.read(new File(srcImgPath));

            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 1、得到画笔对象
            Graphics2D g = buffImg.createGraphics();

            // 2、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g.drawImage(
                    srcImg.getScaledInstance(srcImg.getWidth(null),
                            srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
                    null);
            // 3、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }

            // 4、水印图片的路径 水印图片一般为gif或者png的，这样可设置透明度
            ImageIcon imgIcon = new ImageIcon(iconPath);

            // 5、得到Image对象。
            Image img = imgIcon.getImage();

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));

            // 6、水印图片的位置
            g.drawImage(img, positionWidth, positionHeight, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            // 7、释放资源
            g.dispose();

            // 8、生成图片
            os = new FileOutputStream(targetPath);
            ImageIO.write(buffImg, "JPG", os);

            System.out.println("图片完成添加水印图片");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给图片添加水印文字
     *
     * @param logoText
     *            水印文字
     * @param srcImgPath
     *            源图片路径
     * @param targetPath
     *            目标图片路径
     */
    public static void markImageByText(String logoText, String srcImgPath,
                                       String targetPath) {
        markImageByText(logoText, srcImgPath, targetPath, null);
    }

    /**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     *
     * @param logoText
     * @param srcImgPath
     * @param targetPath
     * @param degree
     */
    public static void markImageByText(String logoText, String srcImgPath,
                                       String targetPath, Integer degree) {

        InputStream is = null;
        OutputStream os = null;
        try {
            // 1、源图片
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(
                    srcImg.getScaledInstance(srcImg.getWidth(null),
                            srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
                    null);
            // 4、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、设置水印文字Font
            g.setFont(font);
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            g.drawString(logoText, positionWidth, positionHeight+600);
            g.drawString(logoText, positionWidth, positionHeight-200);
            g.drawString(logoText, -200, positionHeight+1500);
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            os = new FileOutputStream(targetPath);
            ImageIO.write(buffImg, "JPG", os);

            System.out.println("图片完成添加水印文字");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String srcImgPath = "F:\\luck_nhb\\个人信息\\test.jpg";
        String logoText = "应 聘 专 用   复 印 无 效";

        String targetPath = "F:\\luck_nhb\\个人信息\\水印毕业证.jpg";


        System.out.println("给图片添加水印文字开始...");
        // 给图片添加水印文字,水印文字旋转-45
        markImageByText(logoText, srcImgPath, targetPath, -45);
        System.out.println("给图片添加水印文字结束...");

    }

}
