package org.hutrace.handy.utils.code;

public class Tracy62 {
	private boolean isTracy62;
	private long number;
	private String tracy62;
	private int system;
	private static final char[] SUPPORT_CHAR;

	static {
		SUPPORT_CHAR = new char[]{'Q', 'W', '0', 'm', 'n', 'E', 'b', 'R', 'v',
				'1', 'T', 'Y', 'c', 'x', 'z', 'U', '2', 'I', 'O', 'P', 'l',
				'k', 'j', '3', 'h', 'A', 'S', 'D', '4', 'g', 'F', 'G', 'f',
				'd', 'H', 'J', 's', 'K', 'L', 'a', '8', 'q', 'M', 'p', 'Z',
				'o', 'w', 'X', 'e', 'i', 'N', '9', 'u', 'r', '6', 'B', 'y',
				't', '5', 'V', '7', 'C'};
	}

	public static Tracy62 build(final long number) {
		final Tracy62 t = new Tracy62(number, null);
		t.analysis();
		return t;
	}

	public static Tracy62 build(final String tracy62) {
		final Tracy62 t = new Tracy62(0L, tracy62);
		t.analysis();
		return t;
	}

	private Tracy62(final long number, final String tracy62) {
		this.system = 62;
		if (tracy62 == null) {
			this.number = number;
			this.isTracy62 = false;
		} else {
			this.tracy62 = tracy62;
			this.isTracy62 = true;
		}
	}

	@Override
	public String toString() {
		return this.tracy62;
	}

	public long toLong() {
		return this.number;
	}

	public void analysis() {
		if (this.isTracy62) {
			this.restore();
		} else {
			this.transform();
		}
	}

	public void restore() {
		final char[] arr = this.tracy62.toCharArray();
		for (int i = 0; i < arr.length; ++i) {
			try {
				int index = 0;
				for (int xx = 0; xx < Tracy62.SUPPORT_CHAR.length; ++xx) {
					if (Tracy62.SUPPORT_CHAR[xx] == arr[arr.length - i - 1]) {
						index = xx;
					}
				}
				this.number += (long) Math.pow(this.system, i) * index;
			} catch (Exception ex) {
			}
		}
	}

	public void transform() {
		int digitIndex = 0;
		final char[] outDigits = new char[this.system + 1];
		for (digitIndex = 0; digitIndex <= this.system && this.number != 0L; ++digitIndex) {
			outDigits[outDigits.length - digitIndex - 1] = Tracy62.SUPPORT_CHAR[(int) (this.number % this.system)];
			this.number /= this.system;
		}
		this.tracy62 = new String(outDigits, outDigits.length - digitIndex, digitIndex);
	}
}
