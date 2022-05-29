package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.MemberDto;
import study.datajpa.domain.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(value = false)
//@ExtendWith(SpringExtension.class)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertEquals(findMember1,member1);
        assertEquals(findMember2,member2);

//        List<Member> findMembers = memberJpaRepository.findAll();
//        assertEquals(findMembers.size(),2);
//
//        long count = memberJpaRepository.count();
//        assertEquals(count,2);
//
//        memberJpaRepository.delete(member1);
//        memberJpaRepository.delete(member2);
//
//        long deletedCount = memberJpaRepository.count();
//        assertEquals(deletedCount,0);

        findMember1.setUsername("member change!");
    }

    @Test
    public void findByUsernameAngAgeGreaterThen(){
        Member m1 = new Member("m1", 10);
        Member m2 = new Member("m1", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> findMembers = memberRepository.findByUsernameAndAgeGreaterThan("m1", 15);

        for (Member findMember : findMembers) {
            assertEquals(findMember.getUsername(),m2.getUsername());
            assertEquals(findMember.getAge(),m2.getAge());
        }

    }

    @Test
    void TestNameQuery(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findByUsername("member1");
        assertEquals(findMembers.get(0),member1);
    }

    @Test
    void TestQuery(){
        Member member1 = new Member("member1",20);
        Member member2 = new Member("member2",10);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findUser("member1",20);
        assertEquals(findMembers.get(0),member1);
    }

    @Test
    void findUsernameListTest(){
        Member member1 = new Member("member1",20);
        Member member2 = new Member("member2",10);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> findMemberList = memberRepository.findUsernameList();
        assertEquals(findMemberList.get(0),member1.getUsername());
        assertEquals(findMemberList.get(1),member2.getUsername());
    }

    @Test
    void findMemberDtoTest(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("AAA", 10);
        member.setTeam(team);
        memberRepository.save(member);

        List<MemberDto> memberDtoList = memberRepository.findMemberDto();
        assertEquals(memberDtoList.get(0).getId(),member.getId());
        assertEquals(memberDtoList.get(0).getTeamName(),member.getTeam().getName());
        assertEquals(memberDtoList.get(0).getUsername(),member.getUsername());
    }

    @Test
    void findByNamesTest(){
        Member member1 = new Member("member1",20);
        Member member2 = new Member("member2",10);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMemberList = memberRepository.findByNames(Arrays.asList("member1","member2"));
        assertEquals(findMemberList.get(0).getUsername(),member1.getUsername());
        assertEquals(findMemberList.get(1).getUsername(),member2.getUsername());
    }

    @Test
    void returnTypeTest(){
        Member member1 = new Member("member1",20);
        Member member2 = new Member("member2",10);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> memberList = memberRepository.findListByUsername("member1");
        assertEquals(memberList.get(0),member1);

        Member findMember = memberRepository.findMemberByUsername("member2");
        assertEquals(findMember,member2);

        Optional<Member> findMemberOptional = memberRepository.findOptionalByUsername("member1");
        findMemberOptional.ifPresent(member -> assertEquals(member,member1));
    }

}