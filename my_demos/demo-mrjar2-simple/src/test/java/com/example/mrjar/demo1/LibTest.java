/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.example.mrjar.demo1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Lilianne_Blaze
 */
public class LibTest {

    public LibTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }

    /**
     * Test of getMyPid method, of class Lib.
     */
    @org.junit.jupiter.api.Test
    public void testGetMyPid() {
        System.out.println("getMyPid");
        long expResult = 0L;
        long result = Lib.getMyPid();
        System.out.println("getMyPid = " + result);
    }

    /**
     * Test of getMyPidOld method, of class Lib.
     */
    @org.junit.jupiter.api.Test
    public void testGetMyPidOld() {
        System.out.println("getMyPidOld");
        long expResult = 0L;
        long result = Lib.getMyPidOld();
        System.out.println("getMyPidOld = " + result);
    }

    /**
     * Test of getMyPidNew method, of class Lib.
     */
    @org.junit.jupiter.api.Test
    public void testGetMyPidNew() {
        System.out.println("getMyPidNew");
        long expResult = 0L;
        long result = Lib.getMyPidNew();
        System.out.println("getMyPidNew = " + result);
    }

}
