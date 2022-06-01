package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.entity.Member;
import study.datajpa.domain.dto.MemberDto;
import study.datajpa.domain.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(value = false)
//@ExtendWith(SpringExtension.class)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    
    @PersistenceContext EntityManager em;

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
        findMemberOptional.ifPresent(member -> assertThat(member1).isEqualTo(member));
    }

    @Test
    void TestPaging(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));
        memberRepository.save(new Member("member6",10));
        memberRepository.save(new Member("member7",10));
        memberRepository.save(new Member("member8",10));
        memberRepository.save(new Member("member9",10));
        memberRepository.save(new Member("member10",10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> memberPage = memberRepository.findByAge(age, pageRequest);

        assertThat(memberPage.getContent().size()).isEqualTo(3);
        assertThat(memberPage.getTotalElements()).isEqualTo(10);
        assertThat(memberPage.getNumber()).isEqualTo(0);
        assertThat(memberPage.getTotalPages()).isEqualTo(4);
        assertThat(memberPage.isFirst()).isTrue();
        assertThat(memberPage.hasNext()).isTrue();
    }

    @Test
    void TestSlice(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));
        memberRepository.save(new Member("member6",10));
        memberRepository.save(new Member("member7",10));
        memberRepository.save(new Member("member8",10));
        memberRepository.save(new Member("member9",10));
        memberRepository.save(new Member("member10",10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Slice<Member> memberPage = memberRepository.findMemberByAge(age, pageRequest);
        Slice<MemberDto> memberDtoSlice = memberPage.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));

        assertThat(memberPage.getContent().size()).isEqualTo(3);
//        assertThat(memberPage.getTotalElements()).isEqualTo(10);
        assertThat(memberPage.getNumber()).isEqualTo(0);
//        assertThat(memberPage.getTotalPages()).isEqualTo(4);
        assertThat(memberPage.isFirst()).isTrue();
        assertThat(memberPage.hasNext()).isTrue();
    }

    @Test
    void bulkUpdateTest(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",11));
        memberRepository.save(new Member("member3",12));
        memberRepository.save(new Member("member4",13));
        memberRepository.save(new Member("member5",14));
        memberRepository.save(new Member("member6",15));
        memberRepository.save(new Member("member7",16));
        memberRepository.save(new Member("member8",23));
        memberRepository.save(new Member("member9",21));
        memberRepository.save(new Member("member10",20));

        int resultCount = memberRepository.bulkAgePlus(20);

        assertThat(resultCount).isEqualTo(3);
    }
    
    @Test
    void findMemberLazyTest(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        
        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",10,teamB);
        memberRepository.save(member1); 
        memberRepository.save(member2);
        
        em.flush();
        em.clear();
        
        List<Member> memberList = memberRepository.findAll();
        
        for (Member member : memberList){
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    void findMemberFetchTest(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",10,teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> memberList = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : memberList){
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    void queryHintTest(){
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    void queryLock(){
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        List<Member> findMemberList = memberRepository.findLockByUsername(member1.getUsername());
    }

    @Test
    void callTest(){
        List<Member> result = memberRepository.findMemberCustom();
    }
}