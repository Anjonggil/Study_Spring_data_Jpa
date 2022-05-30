package study.datajpa.repository;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {
    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    void testMember(){
        Member member = new Member("memberA");
        Member saveMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(saveMember.getId());

        assertEquals(saveMember,findMember);
    }

    @Test
    void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

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
    void TestNameQuery(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> findMembers = memberJpaRepository.findByUsername("member1");
        assertEquals(findMembers.get(0),member1);
    }

    @Test
    void TestPaging(){
        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",10));
        memberJpaRepository.save(new Member("member3",10));
        memberJpaRepository.save(new Member("member4",10));
        memberJpaRepository.save(new Member("member5",10));
        memberJpaRepository.save(new Member("member6",10));
        memberJpaRepository.save(new Member("member7",10));
        memberJpaRepository.save(new Member("member8",10));
        memberJpaRepository.save(new Member("member9",10));
        memberJpaRepository.save(new Member("member10",10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> memberList = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        assertThat(memberList.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(10);
    }


    @Test
    void bulkUpdateTest(){
        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",11));
        memberJpaRepository.save(new Member("member3",12));
        memberJpaRepository.save(new Member("member4",13));
        memberJpaRepository.save(new Member("member5",14));
        memberJpaRepository.save(new Member("member6",15));
        memberJpaRepository.save(new Member("member7",16));
        memberJpaRepository.save(new Member("member8",23));
        memberJpaRepository.save(new Member("member9",21));
        memberJpaRepository.save(new Member("member10",20));

        int resultCount = memberJpaRepository.bulkAgePlus(20);

        assertThat(resultCount).isEqualTo(3);
    }
}