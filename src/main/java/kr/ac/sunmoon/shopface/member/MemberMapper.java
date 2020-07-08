package kr.ac.sunmoon.shopface.member;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
	public void insert(Member member);
	public List<Member> selectAll(Member member);
	public Member select(Member member);
	public void update(Member member);
	public void delete(Member member);
	public int countMember(Member member);
}
