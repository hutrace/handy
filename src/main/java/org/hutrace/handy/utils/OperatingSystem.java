package org.hutrace.handy.utils;

/**
 * <p>获取操作系统、判断操作系统等操作
 * @author hu trace
 * @since 1.8
 * @version 1.0
 * @time 2019年3月22日
 */
public class OperatingSystem {
	private static String OS = System.getProperty("os.name").toLowerCase();

	private static OperatingSystem _instance = new OperatingSystem();

	private OperatingSystemEnum platform;

	private OperatingSystem(){}

	public static boolean isLinux() {
		return OS.indexOf("linux") >= 0;
	}

	public static boolean isMacOS() {
		return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") < 0;
	}

	public static boolean isMacOSX() {
		return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;
	}

	public static boolean isWindows() {
		return OS.indexOf("windows") >= 0;
	}

	public static boolean isOS2() {
		return OS.indexOf("os/2") >= 0;
	}

	public static boolean isSolaris() {
		return OS.indexOf("solaris") >= 0;
	}

	public static boolean isSunOS() {
		return OS.indexOf("sunos") >= 0;
	}

	public static boolean isMPEiX() {
		return OS.indexOf("mpe/ix") >= 0;
	}

	public static boolean isHPUX() {
		return OS.indexOf("hp-ux") >= 0;
	}

	public static boolean isAix() {
		return OS.indexOf("aix") >= 0;
	}

	public static boolean isOS390() {
		return OS.indexOf("os/390") >= 0;
	}

	public static boolean isFreeBSD() {
		return OS.indexOf("freebsd") >= 0;
	}

	public static boolean isIrix() {
		return OS.indexOf("irix") >= 0;
	}

	public static boolean isDigitalUnix() {
		return OS.indexOf("digital") >= 0 && OS.indexOf("unix") > 0;
	}

	public static boolean isNetWare() {
		return OS.indexOf("netware") >= 0;
	}

	public static boolean isOSF1() {
		return OS.indexOf("osf1") >= 0;
	}

	public static boolean isOpenVMS() {
		return OS.indexOf("openvms") >= 0;
	}

	/**
	 * <p>获取操作系统名字
	 * @return 操作系统名
	 */
	public static OperatingSystemEnum getOSname() {
		if (isAix()) {
			_instance.platform = OperatingSystemEnum.AIX;
		} else if (isDigitalUnix()) {
			_instance.platform = OperatingSystemEnum.Digital_Unix;
		} else if (isFreeBSD()) {
			_instance.platform = OperatingSystemEnum.FreeBSD;
		} else if (isHPUX()) {
			_instance.platform = OperatingSystemEnum.HP_UX;
		} else if (isIrix()) {
			_instance.platform = OperatingSystemEnum.Irix;
		} else if (isLinux()) {
			_instance.platform = OperatingSystemEnum.Linux;
		} else if (isMacOS()) {
			_instance.platform = OperatingSystemEnum.Mac_OS;
		} else if (isMacOSX()) {
			_instance.platform = OperatingSystemEnum.Mac_OS_X;
		} else if (isMPEiX()) {
			_instance.platform = OperatingSystemEnum.MPEiX;
		} else if (isNetWare()) {
			_instance.platform = OperatingSystemEnum.NetWare_411;
		} else if (isOpenVMS()) {
			_instance.platform = OperatingSystemEnum.OpenVMS;
		} else if (isOS2()) {
			_instance.platform = OperatingSystemEnum.OS2;
		} else if (isOS390()) {
			_instance.platform = OperatingSystemEnum.OS390;
		} else if (isOSF1()) {
			_instance.platform = OperatingSystemEnum.OSF1;
		} else if (isSolaris()) {
			_instance.platform = OperatingSystemEnum.Solaris;
		} else if (isSunOS()) {
			_instance.platform = OperatingSystemEnum.SunOS;
		} else if (isWindows()) {
			_instance.platform = OperatingSystemEnum.Windows;
		} else {
			_instance.platform = OperatingSystemEnum.Others;
		}
		return _instance.platform;
	}
}
