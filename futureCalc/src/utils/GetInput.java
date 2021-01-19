package utils;

/**
 * 
 * @author Muguozheng
 * @date 2018年10月27日
 * @version
 * @description 记录输入信息
 */
public class GetInput {
	public static StringBuffer getInput(double[][] array) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		for (int i = 0; i < array.length; i++) {
			sb.append("{" + array[i][0]);
			for (int j = 1; j < array[i].length; j++) {
				sb.append("," + array[i][j]);
			}
			if (array.length > 1 && i < array.length - 1) {
				sb.append("},");
			} else {
				sb.append("}");
			}
		}
		sb.append("}");
		return sb;
	}

	public static StringBuffer getInput(int[][] array) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		for (int i = 0; i < array.length; i++) {
			sb.append("{" + array[i][0]);
			for (int j = 1; j < array[i].length; j++) {
				sb.append("," + array[i][j]);
			}
			if (array.length > 1 && i < array.length - 1) {
				sb.append("},");
			} else {
				sb.append("}");
			}
		}
		sb.append("}");
		return sb;
	}

	public static StringBuffer getInput(String[][] array) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		for (int i = 0; i < array.length; i++) {
			sb.append("{" + array[i][0]);
			for (int j = 1; j < array[i].length; j++) {
				sb.append("," + array[i][j]);
			}
			if (array.length > 1 && i < array.length - 1) {
				sb.append("},");
			} else {
				sb.append("}");
			}
		}
		sb.append("}");
		return sb;
	}

	public static StringBuffer getInput(double[] array) {
		StringBuffer sb = new StringBuffer();
		sb.append("{" + array[0]);
		for (int i = 1; i < array.length; i++) {
			sb.append("," + array[i]);
		}
		sb.append("}");
		return sb;
	}

	public static StringBuffer getInput(int[] array) {
		StringBuffer sb = new StringBuffer();
		sb.append("{" + array[0]);
		for (int i = 1; i < array.length; i++) {
			sb.append("," + array[i]);
		}
		sb.append("}");
		return sb;
	}
}
