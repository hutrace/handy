package org.hutrace.handy.utils.code;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.imageio.ImageIO;

public class VerifyCode {
	private int imageWidth;
	private int imageHeight;
	private int codeLength;
	private float yawpRate;
	private int fontSize;
	private boolean isDistortion;
	private String imageCode;
	private char[] captchars;

	public VerifyCode() {
		this.imageWidth = 100;
		this.imageHeight = 50;
		this.codeLength = 4;
		this.yawpRate = 0.012f;
		this.fontSize = 28;
		this.isDistortion = true;
		this.imageCode = "";
		this.captchars = new char[]{'2', '3', '4', '5', '6', '7', '8', 'a',
				'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
				'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'A', 'B', 'C', 'D',
				'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y'};
	}

	public void createImageFile(final String filePath) throws IOException {
		final OutputStream fos = new FileOutputStream(new File(filePath));
		final BufferedImage bufferedImage = this.createImage(
				this.getImageWidth(), this.getImageHeight());
		ImageIO.write(bufferedImage, "jpg", fos);
	}

	public String createImage(final OutputStream os) throws IOException {
		final BufferedImage bufferedImage = this.createImage(
				this.getImageWidth(), this.getImageHeight());
		ImageIO.write(bufferedImage, "jpg", os);
		return this.imageCode;
	}

	private BufferedImage createImage(final int imageWidth,
			final int imageHeight) {
		final BufferedImage bufferedImage = new BufferedImage(imageWidth,
				imageHeight, 13);
		final Graphics2D graphics2D = bufferedImage.createGraphics();
		final Color color = this.getRandomColor(200, 250);
		graphics2D.setColor(color);
		graphics2D.fillRect(0, 0, bufferedImage.getWidth(),
				bufferedImage.getHeight());
		graphics2D.setColor(this.getRandomColor());
		this.drawString(graphics2D, this.getCodeLength());
		this.drawThickLine(graphics2D, 0, imageWidth / 2 + 1, imageWidth,
				imageHeight / 3 + 1, 1, this.getRandomColor(100, 200));
		this.setLTAttachLine(graphics2D, imageWidth, imageHeight);
		this.setRBAttachLine(graphics2D, imageWidth, imageHeight);
		if (this.isDistortion) {
			this.shear(graphics2D, bufferedImage.getWidth(),
					bufferedImage.getHeight(), color);
		}
		this.setYawp(bufferedImage, this.getYawpRate());
		return bufferedImage;
	}

	private void drawString(final Graphics2D graphics2D, final int length) {
		final int car = this.captchars.length - 1;
		final Random random = new Random();
		String text = "";
		for (int i = 0; i < length; ++i) {
			text = String.valueOf(text)
					+ this.captchars[random.nextInt(car) + 1];
		}
		this.imageCode = text;
		int xOffset = 1 + new Random().nextInt(40);
		final int yOffset = 30;
		final char[] charArray = text.toCharArray();
		for (int j = 0; j < charArray.length; ++j) {
			graphics2D.setFont(this.getRandomFont());
			graphics2D.drawString(String.valueOf(charArray[j]), xOffset,
					yOffset);
			xOffset = xOffset + 14 + new Random().nextInt(5);
		}
	}

	@SuppressWarnings("unused")
	private void drawCnString(final Graphics2D graphics2D, final int length) {
		int xOffset = 1 + new Random().nextInt(40);
		final int yOffset = 30;
		for (int i = 0; i < length; ++i) {
			graphics2D.setFont(this.getRandomCnFont());
			graphics2D.drawString(String.valueOf(this.getRandomCnChar()),
					xOffset, yOffset);
			xOffset = xOffset + 20 + new Random().nextInt(5);
		}
	}

	private void setLTAttachLine(final Graphics2D graphics2D,
			final int imageWidth, final int imageHeight) {
		final int lineLeft = 7;
		final Random random = new Random();
		for (int i = 0; i < lineLeft; ++i) {
			final int x = random.nextInt(imageWidth - 1);
			final int y = random.nextInt(imageHeight - 1);
			final int xl = random.nextInt(6) + 1;
			final int yl = random.nextInt(12) + 1;
			graphics2D.drawLine(x, y, x + xl + 40, y + yl + 20);
		}
	}

	private void setRBAttachLine(final Graphics2D graphics2D,
			final int imageWidth, final int imageHeight) {
		final int lineRight = 8;
		final Random random = new Random();
		for (int i = 0; i < lineRight; ++i) {
			final int x = random.nextInt(imageWidth - 1);
			final int y = random.nextInt(imageHeight - 1);
			final int xl = random.nextInt(12) + 1;
			final int yl = random.nextInt(6) + 1;
			graphics2D.drawLine(x, y, x - xl + 40, y - yl);
		}
	}

	private void setYawp(final BufferedImage bufferedImage, final float yawpRate) {
		final int width = bufferedImage.getWidth();
		final int height = bufferedImage.getHeight();
		final int area = (int) (yawpRate * width * height);
		final Random random = new Random();
		for (int i = 0; i < area; ++i) {
			final int x = random.nextInt(width);
			final int y = random.nextInt(height);
			final int rgb = this.getRandomIntColor();
			bufferedImage.setRGB(x, y, rgb);
		}
	}

	private int getRandomIntColor() {
		final int[] rgb = this.getRandomRgb();
		int color = 0;
		int[] array;
		for (int length = (array = rgb).length, i = 0; i < length; ++i) {
			final int c = array[i];
			color <<= 8;
			color |= c;
		}
		return color;
	}

	private int[] getRandomRgb() {
		final int[] rgb = new int[3];
		final Random random = new Random();
		for (int i = 0; i < 3; ++i) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}

	protected char getRandomCnChar() {
		final byte[] b = {(byte) this.getRandomIntBetween(176, 215),
				(byte) this.getRandomIntBetween(161, 249)};
		try {
			final String s = new String(b, "gb2312");
			System.out.println(s);
			assert s.length() == 1;
			return s.toCharArray()[0];
		} catch (UnsupportedEncodingException uee) {
			return this.getRandomCnChar();
		}
	}

	private int getRandomIntBetween(int first, int second) {
		final Random random = new Random();
		if (second < first) {
			final int tmp = first;
			first = second;
			second = tmp;
		}
		return random.nextInt(second - first) + first;
	}

	private Font getRandomFont() {
		final Random random = new Random();
		final Font[] font = new Font[5];
		final int fontSize = this.getFontSize();
		font[0] = new Font("Algerian", 0, fontSize);
		font[1] = new Font("Antique Olive Compact", 0, fontSize);
		font[2] = new Font("Fixedsys", 0, fontSize);
		font[3] = new Font("Algerian", 0, fontSize);
		font[4] = new Font("Arial", 0, fontSize);
		return font[random.nextInt(5)];
	}

	private Font getRandomCnFont() {
		final Random random = new Random();
		final Font[] font = new Font[5];
		final int fontSize = this.getFontSize();
		font[0] = new Font("微软雅黑", 0, fontSize);
		font[1] = new Font("宋体", 0, fontSize);
		font[2] = new Font("新宋体", 0, fontSize);
		font[3] = new Font("楷体", 0, fontSize);
		font[4] = new Font("幼圆", 0, fontSize);
		return font[random.nextInt(5)];
	}

	private Color getRandomColor(int fc, int bc) {
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		final Random random = new Random();
		final int r = fc + random.nextInt(bc - fc);
		final int g = fc + random.nextInt(bc - fc);
		final int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	private Color getRandomColor() {
		final Random random = new Random();
		final Color[] color = new Color[10];
		color[0] = new Color(32, 158, 25);
		color[1] = new Color(218, 42, 19);
		color[2] = new Color(31, 75, 208);
		return color[random.nextInt(3)];
	}

	private void drawThickLine(final Graphics g, final int x1, final int y1,
			final int x2, final int y2, final int thickness, final Color c) {
		g.setColor(c);
		final int dX = x2 - x1;
		final int dY = y2 - y1;
		final double lineLength = Math.sqrt(dX * dX + dY * dY);
		final double scale = thickness / (2.0 * lineLength);
		double ddx = -scale * dY;
		double ddy = scale * dX;
		ddx += ((ddx > 0.0) ? 0.5 : -0.5);
		ddy += ((ddy > 0.0) ? 0.5 : -0.5);
		final int dx = (int) ddx;
		final int dy = (int) ddy;
		final int[] xPoints = new int[4];
		final int[] yPoints = new int[4];
		xPoints[0] = x1 + dx;
		yPoints[0] = y1 + dy;
		xPoints[1] = x1 - dx;
		yPoints[1] = y1 - dy;
		xPoints[2] = x2 - dx;
		yPoints[2] = y2 - dy;
		xPoints[3] = x2 + dx;
		yPoints[3] = y2 + dy;
		g.fillPolygon(xPoints, yPoints, 4);
	}

	private void shearX(final Graphics g, final int w1, final int h1,
			final Color color) {
		final Random random = new Random();
		final int period = random.nextInt(2);
		final boolean borderGap = true;
		final int frames = 1;
		final int phase = random.nextInt(2);
		for (int i = 0; i < h1; ++i) {
			final double d = (period >> 1)
					* Math.sin(i / period + 6.283185307179586 * phase / frames);
			g.copyArea(0, i, w1, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + w1, i, w1, i);
			}
		}
	}

	private void shearY(final Graphics g, final int w1, final int h1,
			final Color color) {
		final Random random = new Random();
		final int period = random.nextInt(40) + 10;
		final boolean borderGap = true;
		final int frames = 20;
		final int phase = 7;
		for (int i = 0; i < w1; ++i) {
			final double d = (period >> 1)
					* Math.sin(i / period + 6.283185307179586 * phase / frames);
			g.copyArea(i, 0, 1, h1, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + h1, i, h1);
			}
		}
	}

	private void shear(final Graphics graphics, final int width,
			final int height, final Color color) {
		this.shearX(graphics, width, height, color);
		this.shearY(graphics, width, height, color);
	}

	public int getImageWidth() {
		return this.imageWidth;
	}

	public VerifyCode width(final int width) {
		if (width > 0) {
			this.imageWidth = width;
		}
		return this;
	}

	public int getImageHeight() {
		return this.imageHeight;
	}

	public VerifyCode height(final int height) {
		if (height > 0) {
			this.imageHeight = height;
		}
		return this;
	}

	public int getCodeLength() {
		return this.codeLength;
	}

	public VerifyCode length(final int length) {
		if (length > 0) {
			this.codeLength = length;
		}
		return this;
	}

	public float getYawpRate() {
		return this.yawpRate;
	}

	public VerifyCode yawpRate(final float yawpRate) {
		this.yawpRate = yawpRate;
		return this;
	}

	public int getFontSize() {
		return this.fontSize;
	}

	public VerifyCode fontSize(final int fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public boolean isDistortion() {
		return this.isDistortion;
	}

	public VerifyCode distortion(final boolean distortion) {
		this.isDistortion = distortion;
		return this;
	}
}