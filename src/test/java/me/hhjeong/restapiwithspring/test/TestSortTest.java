package me.hhjeong.restapiwithspring.test;

import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class TestSortTest {

	@Test
	@DisplayName("test1")
	void test1() {
		System.out.println("1");
	}

	@Test
	@DisplayName("test2")
	@QuickTag
	void test2() {
		System.out.println("2");
	}

	@Test
	@DisplayName("test3")
	void test3() {
		System.out.println("3");
	}
}
