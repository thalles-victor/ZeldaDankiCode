package com.walking.world;

public class Camera {
	public static int x = 10;
	public static int y = 10;
	public static int clamp(int Current, int Minimal, int Maksimum) {
		if(Current<Minimal)
			Current=Minimal;
		if(Current>Maksimum)
			Current=Maksimum;
		
		return Current;
	}

}
