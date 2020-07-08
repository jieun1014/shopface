package kr.ac.sunmoon.shopface.work.schedule;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Alias(value = "schedule")
@ToString
public class Schedule implements Serializable {
	private static final long serialVersionUID = 1L;
	private int no;
	private int timetableNo;
	private String name;
	private String memberId;
	private char state;
	private int branchNo;
}
