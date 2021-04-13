package me.hhjeong.restapiwithspring.test;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class StandardTests {

	@BeforeAll
	static void initAll() {
		System.out.println("BeforeAll.");
	}

	@BeforeEach
	void init() {
		System.out.println("BeforeEach.");
	}

	@Test
	void succeedingTest() {
		System.out.println("[Test] succeedingTest.");
	}

	@Test
	@Disabled
	void failingTest() {
		System.out.println("[Test] failingTest.");
		fail("a failing test");
	}

	@Test
	@Disabled("for demonstration purposes")
	void skippedTest() {
		System.out.println("[Test] skippedTest.");
		// not executed
	}

	@Test
	void abortedTest() {
		System.out.println("[Test] abortedTest.");
		assumeTrue("abc".contains("Z"));
		fail("test should have been aborted");
	}

	@AfterEach
	void tearDown() {
		System.out.println("AfterEach.");
	}

	@AfterAll
	static void tearDownAll() {
		System.out.println("AfterAll.");
	}

}
