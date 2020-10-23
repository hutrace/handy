package org.hutrace.handy.utils.code;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class TextCoding {
	public static final String COMMA = ",";
	public static final String DOT = ".";
	public static String mixRuleOriginal = "a0bcdef8ghijk5lmnop2qrstuvwx4yzABCD6EFGHIJK9LMNOP7QRSTU1VWX3YZ";
	public static String lowerA = "a";
	public static final String EMPTY_STR = "";
	public static final String NULL_STR = "null";
	public static final String UNDEFINED_STR = "undefined";
	public static final String SPACE_CHAR = " ";
	public static double one = 1.0D;
	private static String mixStringReg = "([0-9]|[A-Za-z])+";
	private static String mixRuleNew = "QW0mnEbRv1TYcxzU2IOPlkj3hASD4gFGfdHJsKLa8qMpZowXeiN9ur6Byt5V7C";
	private static int changeRule = 1;
	private static String repeatRule32 = "JGa5cR8Vs9Ll2fM4n6rW0p1Ty7YaF3uB";
	static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	private static String[] doubleDigitToOneStringRule = "qRgr7it2D*TsvkezNKJHLwamnfjyoCdMAPlhO6EbX^u;QF:85p3@#Y‰S~W/]U4x}√°!$%B(Z&)’″[_1c-○V0{G9,<±I?+÷∞=`>×."
			.split("");
	private static char[] randomArr = new char[]{'0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9'};
	private static char[] alphabet = new char[]{'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z'};

	public static TextCoding newInstance() {
		return new TextCoding();
	}

	public static String byteStrToStr(String s) {
		String[] sarr = s.split(",");
		byte[] b = new byte[sarr.length];

		for (int i = 0; i < sarr.length; ++i) {
			b[i] = Byte.parseByte(sarr[i]);
		}

		return new String(b);
	}

	public static String byteStrToStr(String s, String delimiter) {
		String[] sarr = s.split(delimiter);
		byte[] b = new byte[sarr.length];

		for (int i = 0; i < sarr.length; ++i) {
			b[i] = Byte.parseByte(sarr[i]);
		}

		return new String(b);
	}

	public static String strToByteStr(String s) {
		byte[] b = s.getBytes();
		StringBuilder strb = new StringBuilder();
		byte[] var6 = b;
		int var5 = b.length;

		for (int var4 = 0; var4 < var5; ++var4) {
			byte c = var6[var4];
			strb.append(c);
			strb.append(",");
		}

		return strb.substring(0, strb.length() - 1);
	}

	public static String strToByteStr(String s, String delimiter) {
		byte[] b = s.getBytes();
		StringBuilder strb = new StringBuilder();
		byte[] var7 = b;
		int var6 = b.length;

		for (int var5 = 0; var5 < var6; ++var5) {
			byte c = var7[var5];
			strb.append(c);
			strb.append(delimiter);
		}

		return strb.substring(0, strb.length() - 1);
	}

	public static String invert(String o) {
		String[] arr = o.split("");
		StringBuilder strb = new StringBuilder();

		for (int i = arr.length - 1; i >= 0; --i) {
			strb.append(arr[i]);
		}

		return strb.toString();
	}

	public static String mixEncrypt(String originalStr) {
		char[] letter52 = mixRuleOriginal.toCharArray();
		char[] strArr = originalStr.toCharArray();
		StringBuffer strb = new StringBuffer();

		for (int i = 0; i < strArr.length; ++i) {
			String cache = String.valueOf(strArr[i]);
			if (cache.equals(lowerA)) {
				strb.append(",");
			} else {
				int index = mixRuleOriginal.indexOf(cache);
				if (index == -1) {
					strb.append(cache);
				} else {
					strb.append(letter52[index - 1]);
				}
			}
		}

		return strb.toString();
	}

	public static String mixDecode(String str) {
		char[] letter52 = mixRuleOriginal.toCharArray();
		char[] strArr = str.toCharArray();
		StringBuffer strb = new StringBuffer();

		for (int i = 0; i < strArr.length; ++i) {
			String cache = String.valueOf(strArr[i]);
			if (",".equals(cache)) {
				strb.append(lowerA);
			} else {
				int index = mixRuleOriginal.indexOf(cache);
				if (index == -1) {
					strb.append(cache);
				} else {
					strb.append(letter52[index + 1]);
				}
			}
		}

		return strb.toString();
	}

	public static StringBuilder splitInvert(StringBuilder arg0, int arg1) {
		int in = (int) Math.ceil((double) arg0.length() * one / (double) arg1);
		int last = in - 1;
		StringBuilder resultStrb = new StringBuilder();

		for (int i = 0; i < in; ++i) {
			if (i == last) {
				resultStrb.append(invert(arg0.substring(i * arg1)));
			} else {
				resultStrb.append(invert(arg0.substring(i * arg1, (i + 1)
						* arg1)));
			}
		}

		return resultStrb;
	}

	public static StringBuilder splitInvert(String arg0, int arg1) {
		int in = (int) Math.ceil((double) arg0.length() * one / (double) arg1);
		int last = in - 1;
		StringBuilder resultStrb = new StringBuilder();

		for (int i = 0; i < in; ++i) {
			if (i == last) {
				resultStrb.append(invert(arg0.substring(i * arg1)));
			} else {
				resultStrb.append(invert(arg0.substring(i * arg1, (i + 1)
						* arg1)));
			}
		}

		return resultStrb;
	}

	public static String mix(String o) {
		Pattern p = Pattern.compile(mixStringReg);
		String originalStr = o.toString();
		if (!p.matcher(originalStr).matches()) {
			throw new RuntimeException("Parameter error: parameter range is [a-zA-Z0-9]");
		} else {
			StringBuffer strb = new StringBuffer();
			String[] arr = originalStr.split("");
			String[] mixRuleNewArr = mixRuleNew.split("");
			String[] var9 = arr;
			int var8 = arr.length;

			for (int var7 = 0; var7 < var8; ++var7) {
				String string = var9[var7];
				strb.append(mixRuleNewArr[mixRuleOriginal.indexOf(string)]);
			}

			for (int i = 0; i < 3; ++i) {
				arr = strb.toString().split("");
				strb = new StringBuffer();
				String[] var10 = arr;
				int var13 = arr.length;

				for (var8 = 0; var8 < var13; ++var8) {
					String string = var10[var8];
					strb.append(mixRuleNewArr[mixRuleOriginal.indexOf(string)]);
				}
			}

			return strb.toString();
		}
	}

	public static String deRegleToLengt(String s, int len) {
		int length = s.length();
		if (length >= len) {
			return s.substring(0, len);
		} else {
			StringBuffer strb = new StringBuffer(s);
			String[] arr = s.split("");
			boolean flag = changeRule != 1;
			String[] var9 = arr;
			int var8 = arr.length;

			for (int var7 = 0; var7 < var8; ++var7) {
				String str = var9[var7];
				if (flag) {
					flag = false;
					strb.append(str);
					if (strb.length() == len) {
						break;
					}
				} else {
					flag = true;
				}
			}

			return strb.toString();
		}
	}

	public static String randomToLength(String s, int len) {
		int length = s.length();
		if (length >= len) {
			return s.substring(0, len);
		} else {
			StringBuffer strb = new StringBuffer(s);
			Random random = new Random();
			int last = 0;

			do {
				int next;
				do {
					next = random.nextInt(10);
				} while (last == next);

				last = next;
				strb.append(next);
			} while (strb.length() != len);

			return strb.toString();
		}
	}

	public static String disposeofContinuousReprat(String str) {
		String[] arr = str.split("");
		String[] ruleArr32 = repeatRule32.split("");
		String temp = null;
		StringBuffer strb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			String s = arr[i];
			if (s.equals(temp)) {
				strb.append(ruleArr32[i]);
			} else {
				temp = s;
				strb.append(s);
			}
		}
		return strb.toString();
	}

	public static String createSign() {
		String s = createNumberString(24);
		String str32 = disposeofContinuousReprat(deRegleToLengt(mix(s), 32));
		for (int i = 0; i < 6; ++i) {
			str32 = disposeofContinuousReprat(disrupt(str32));
		}
		return MD5.upperCase(str32);
	}

	public static String createCapitalRule32() {
		return MD5.upperCase(randomToLength(mix(String.valueOf(System.currentTimeMillis())), 32));
	}

	public static String createLowerRule32() {
		return MD5.lowerCase(randomToLength(mix(String.valueOf(System.currentTimeMillis())), 32));
	}

	public static String createRule12() {
		Random random = new Random();
		String timestamp = String.valueOf(System.currentTimeMillis());
		StringBuilder strb = new StringBuilder(timestamp);
		for (int i = 0; i < 3; ++i) {
			strb = splitInvert(strb, random.nextInt(10) + 1);
		}
		String random24 = randomToLength(strb.toString(), 24);
		return numeralToRuleLengthDivideTwo(random24);
	}

	public static String createNumberString(int length) {
		return randomToLength(String.valueOf(System.currentTimeMillis()),
				length);
	}

	public static String createCode64() {
		StringBuffer result = new StringBuffer();
		result.append(System.currentTimeMillis());
		char[] arrs = new char[51];

		for (int i = 0; i < arrs.length; ++i) {
			Double random = Math.random() * 10.0D;
			arrs[i] = randomArr[random.intValue()];
			if (i != 0 && arrs[i] == arrs[i - 1]) {
				--i;
			} else {
				result.append(randomArr[random.intValue()]);
			}
		}

		return changeCode(result.toString());
	}

	public static String changeCode(String c) {
		StringBuffer r = new StringBuffer();
		char[] arr = c.toCharArray();

		for (int i = 0; i < arr.length; ++i) {
			if (i % 2 == 0) {
				r.append(arr[i]);
			} else {
				int a = Integer.parseInt(String.valueOf(arr[i]));
				Double random = a == 9
						? Math.random() * 2.0D
						: Math.random() * 1.0D;
				r.append(alphabet[a * (random.intValue() + 1)]);
			}
		}

		return r.toString();
	}

	public static String numeralToRuleLengthDivideTwo(String numeralString) {
		if (numeralString.length() % 2 != 0) {
			throw new RuntimeException("参数错误,参数长度必须为双数位");
		} else if (!numeralString.matches("[0-9]+")) {
			throw new RuntimeException("参数错误,参数必须为纯数字字符串");
		} else {
			int len2 = numeralString.length() / 2;
			StringBuilder strb = new StringBuilder();

			for (int i = 0; i < len2; ++i) {
				strb.append(deRegleDoubleDigitToOneString(numeralString
						.substring(i * 2, i * 2 + 2)));
			}

			return strb.toString();
		}
	}

	private static String deRegleDoubleDigitToOneString(String doubleDigit) {
		return deRegleDoubleDigitToOneString(Integer.parseInt(doubleDigit));
	}

	public static String deRegleDoubleDigitToOneString(int doubleDigit) {
		return doubleDigitToOneStringRule[doubleDigit];
	}

	public static String disrupt(String s) {
		char[] arr = s.toCharArray();
		StringBuilder odd = new StringBuilder();
		StringBuilder even = new StringBuilder();

		for (int i = 0; i < arr.length; ++i) {
			if (i % 2 == 0) {
				odd.append(arr[i]);
			} else {
				even.append(arr[i]);
			}
		}

		StringBuilder strb = new StringBuilder();
		strb.append(invert(even.toString()));
		strb.append(invert(odd.toString()));
		return strb.toString();
	}

	public static String passwordEncrypt(String pwd, String salt) {
		return MD5.upperCase(strSplice(pwd, salt));
	}

	public static String strSplice(String... str) {
		StringBuilder strb = new StringBuilder();

		for (int i = 0; i < str.length; ++i) {
			strb.append(str[i]);
		}

		return strb.toString();
	}

	public static String strSplice(Object... str) {
		StringBuilder strb = new StringBuilder();

		for (int i = 0; i < str.length; ++i) {
			strb.append(str[i]);
		}

		return strb.toString();
	}
	
	/**
	 * <p>生成12位随机字符串
	 * <p>通过{@link TextCoding#createNumberString(int)}生成30位字符.
	 * <p>再将字符分成两个15位的long数字进行{@link Tracy62}转码, 最后将转码后的字符拼接返回
	 * @return 12随机字符
	 */
	public static String nonceStr() {
		String str = TextCoding.createNumberString(30);
		String res = Tracy62.build(Long.parseLong(str.substring(0, 15))).toString() 
				+ Tracy62.build(Long.parseLong(str.substring(15))).toString();
		if(res.length() == 11) {
			return res + ChaoticMix.randomNumber(1);
		}
		return res;
	}
	
	/**
	 * <p>生成26位长度的订单号
	 * <p>使用{@link SimpleDateFormat}, 格式'yyyyMMddHHmmssSSS'将当前时间转换后再拼接9位长度的随机数字
	 * @return 订单号
	 */
	public static String outTradeNo() {
		return outTradeNo(26);
	}
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	/**
	 * <p>生成len长度的订单号
	 * <p>使用{@link SimpleDateFormat}, 格式'yyyyMMddHHmmssSSS'将当前时间转换后再拼接的随机数字
	 * @return 订单号
	 */
	public static String outTradeNo(int len) {
		Date date = new Date();
		String str;
		synchronized(sdf) {
			str = sdf.format(date);
		}
		return str + ChaoticMix.randomNumber(len - 17);
	}
	
	/**
	 * <p>生成26位长度的订单号
	 * <p>使用{@link TextCoding#createNumberString(int)}生成26位字符
	 * @return
	 */
	public static String outRefundNo() {
		return TextCoding.createNumberString(26);
	}
	
}
