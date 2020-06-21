package kr.ac.sunmoon.shopface.work.timetable;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@Alias(value = "timetable")
public class Timetable {
	@NonNull
	private int no;
	private int branchNo;
	private Date workStartTime;
	private Date workEndTime;
	private String occupName;
	private String occupColor;
	private String RegisterDate;
}
