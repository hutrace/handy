package org.hutrace.handy.utils.code;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public final class ChaoticMix {
	private StringBuilder string;
	private int encryptStrLength;
	private StringBuilder encryptStr;
	private StringBuilder splitInvertStr;
	private boolean isWaString;
	private String random10;
	private String verifyChar;
	private int oneMixLimit;
	private int multiMixLimit;
	private int ten = 21;
	private int minMixSplit = 5;
	private int maxMixSplit = 50;
	private int cutoff = 100;
	private String minus = "-";
	private char[] lowerChar26 = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z'};
	private char[] capitalChar26 = new char[]{'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

	public static ChaoticMix build(String str) {
		return new ChaoticMix(str);
	}

	public static ChaoticMix build(String str, int length) {
		return new ChaoticMix(str, length);
	}

	public ChaoticMix(String str) {
		this.splitInvertStr = new StringBuilder();
		this.string = new StringBuilder();
		this.string.append(str);
		this.isWaString(str);
		this.init();
	}

	private ChaoticMix(String str, int length) {
		this.ten = length;
		this.splitInvertStr = new StringBuilder();
		this.string = new StringBuilder();
		this.string.append(str);
		this.isWaString(str);
		this.init();
	}

	private void init() {
		this.encryptStr = new StringBuilder();
		if (!this.isWaString) {
			this.random10 = randomNumber(this.ten);
			this.verifyChar = this.random10.split("")[randomNextInt(this.ten)];
			this.encrypt(this.string.toString());
		} else {
			this.splitInvertStr = this.restoreMix(this.string.substring(
					this.ten, this.string.length()));
			this.encryptStrLength = this.splitInvertStr.length();
			int splitLength = this.splitInvertStr.length() < 100 ? 5 : 50;
			this.encryptStr = TextCoding.splitInvert(this.splitInvertStr,
					splitLength);
			this.decode();
		}

	}

	private void isWaString(String str) {
		if (str.length() < this.ten) {
			this.isWaString = false;
		} else {
			String randomEncrypt = str.substring(0, this.ten);
			if (!randomEncrypt.matches("[0-9]+")) {
				this.isWaString = false;
			} else {
				this.isWaString = this.checkIs(this.string.substring(this.ten,
						this.string.length()));
				this.isWaString = true;
				this.random10 = randomEncrypt;
			}
		}
	}

	private boolean checkIs(String arg) {
		this.oneMixLimit = arg.length() < this.cutoff
				? this.minMixSplit
				: this.maxMixSplit;
		this.multiMixLimit = this.oneMixLimit + 1;
		int argL = arg.length();
		if (argL < this.multiMixLimit) {
			return arg.substring(arg.length() - 1).matches("[0-9]+");
		} else {
			int in = (int) Math.ceil((double) arg.length() * TextCoding.one
					/ (double) this.multiMixLimit);
			String check = "";

			for (int i = 0; i < in; ++i) {
				String cacha;
				if (i == in - 1) {
					cacha = arg.substring(arg.length() - 1);
					if (!check.equals(cacha)) {
						return false;
					}
				} else if ("".equals(check)) {
					check = arg.substring((i + 1) * this.multiMixLimit - 1,
							(i + 1) * this.multiMixLimit);
					if (!check.matches("[0-9]+")) {
						return false;
					}
				} else {
					cacha = arg.substring((i + 1) * this.multiMixLimit - 1,
							(i + 1) * this.multiMixLimit);
					if (!check.equals(cacha)) {
						return false;
					}
				}
			}

			return true;
		}
	}

	public void append(String arg) {
		this.string.append(arg);
		this.encrypt(arg);
	}

	public void append(int arg) {
		this.string.append(arg);
		this.encrypt(String.valueOf(arg));
	}

	public void append(char arg) {
		this.string.append(arg);
		this.encrypt(String.valueOf(arg));
	}

	public void append(boolean arg) {
		this.string.append(arg);
		this.encrypt(String.valueOf(arg));
	}

	public void append(float arg) {
		this.string.append(arg);
		this.encrypt(String.valueOf(arg));
	}

	public void append(long arg) {
		this.string.append(arg);
		this.encrypt(String.valueOf(arg));
	}

	public void append(byte arg) {
		this.string.append(arg);
		this.encrypt(String.valueOf(arg));
	}

	public static String randomNumber(int arg) {
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < arg; ++i) {
			Random ran = new Random();
			stringBuilder.append(ran.nextInt(9) + 1);
		}

		return stringBuilder.toString();
	}

	public static int randomNextInt(int arg) {
		return (new Random()).nextInt(arg);
	}

	public String wyString() {
		return this.toString();
	}

	public String toString() {
		this.oneMixLimit = this.encryptStrLength < this.cutoff
				? this.minMixSplit
				: this.maxMixSplit;
		this.multiMixLimit = this.oneMixLimit + 1;
		return (new StringBuilder(this.random10)).append(
				this.multiMix(this.splitInvertStr)).toString();
	}

	private void encrypt(String str) {
		this.encryptStr.append(TextCoding.mixEncrypt(this.strToByteStr(str)));
		this.encryptStrLength = this.encryptStr.length();
		int splitLength = this.encryptStr.length() < 100 ? 5 : 50;
		this.splitInvertStr = TextCoding.splitInvert(this.encryptStr,
				splitLength);
	}

	private void decode() {
		this.string = new StringBuilder();
		this.string.append(this.byteStrToStr(TextCoding
				.mixDecode(this.encryptStr.toString())));
	}

	public String string() {
		return this.string.toString();
	}

	public int length() {
		return this.string.length();
	}

	private StringBuilder multiMix(StringBuilder byteStr) {
		return byteStr;
	}

	private StringBuilder restoreMix(String str) {
		return new StringBuilder(str);
	}

	public String strToByteStr(String s) {
		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		StringBuilder strb = new StringBuilder();
		byte[] var8 = b;
		int var7 = b.length;

		for (int var6 = 0; var6 < var7; ++var6) {
			byte c = var8[var6];
			String bys = String.valueOf(c);
			if (bys.substring(0, 1).equals(this.minus)) {
				strb.append(this.lowerChar26[randomNextInt(26)]);
				strb.append(bys.substring(1, bys.length()));
			} else {
				strb.append(c);
			}

			strb.append(this.capitalChar26[randomNextInt(26)]);
		}

		return strb.toString();
	}

	public String byteStrToStr(String s) {
		String[] sarr = s.split("[A-Z]+");
		byte[] b = new byte[sarr.length];
		String lowerString26 = String.valueOf(this.lowerChar26);
		int a = 0;
		if (sarr[0].equals("")) {
			a = 1;
		}

		for (int i = a; i < sarr.length; ++i) {
			String cacha = sarr[i];
			b[i] = lowerString26.indexOf(cacha.substring(0, 1)) > -1
					? Byte.parseByte(this.minus
							+ cacha.substring(1, cacha.length()))
					: Byte.parseByte(cacha);
		}

		return new String(b, StandardCharsets.UTF_8);
	}

	public String verifyChar() {
		return this.verifyChar;
	}
}