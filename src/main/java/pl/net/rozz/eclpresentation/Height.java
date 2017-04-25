package pl.net.rozz.eclpresentation;

import java.util.Arrays;

public enum Height {
	SMALL(0, 150), MEDIUM(150, 180), HIGH (180, Integer.MAX_VALUE);
	
	private int from;
	private int to;

	private Height(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	public static Height heightInCm(int h) {
		return Arrays.stream(values())
				.filter(height -> height.from <= h && h < height.to)
				.findAny()
				.orElseThrow(IllegalStateException::new);
	}
}
