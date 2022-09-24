package it.unisa.diem.coordinator.dummy;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TestLocalDateTime {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
		String nowString = now.toString();
		System.out.println("NOW BEFORE PARSING: " + nowString);
		LocalDateTime nowParsed = LocalDateTime.parse(nowString);
		System.out.println(nowParsed.toString());
	}

}
