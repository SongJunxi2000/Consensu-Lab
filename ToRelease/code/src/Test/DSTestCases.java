package Test;

import org.junit.Test;
import static org.junit.Assert.*;

import Main.*;
import Simulator.Simulation_engine;

public class DSTestCases {
    @Test
    public void testDrop1() {
        Simulation_engine engine = null;
        try {
            engine = new Simulation_engine(30, 10, 0, 11,
                    PlayerF.DS, AdversaryF.DS_DROP, "1",
                    new int[] { -1, 0, 0, -1, -1, 1, -1, 1, -1, 1, 0, -1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, -1, 0, 0, -1, 0,
                            -1, 0, -1 });
        } catch (Exception e) {
            System.out.println("server error, please contact admins");
        }
        assertTrue(engine.check_output());
    }

    @Test
    public void testDrop2() {
        Simulation_engine engine = null;
        try {
            engine = new Simulation_engine(30, 10, 0, 11,
                    PlayerF.DS, AdversaryF.DS_DROP, "1",
                    new int[] { 0, 0, -1, 0, -1, 0, 0, 0, 1, -1, 0, -1, 1, 1, -1, 0, 0, -1, 1, 0, 0, 1, 0, 0, 1, 1, -1,
                            -1, -1, -1 });
        } catch (Exception e) {
            System.out.println("server error, please contact admins");
        }
        assertTrue(engine.check_output());
    }

    @Test
    public void testRand() {
        Simulation_engine engine = null;
        try {
            engine = new Simulation_engine(30, 10, 0, 11,
                    PlayerF.DS, AdversaryF.DS_RANDOM_MESSAGE, "1",
                    new int[] { -1, 0, 1, 0, 0, -1, 0, 0, 0, -1, 1, 0, 0, 1, 1, 0, -1, -1, 1, 0, -1, -1, 0, 0, 0, 1, -1,
                            -1, 0, -1 });
        } catch (Exception e) {
            System.out.println("server error, please contact admins");
        }
        assertTrue(engine.check_output());
    }

    @Test
    public void testRand0() {
        Simulation_engine engine = null;
        try {
            engine = new Simulation_engine(30, 10, 0, 11,
                    PlayerF.DS, AdversaryF.DS_RANDOM_MESSAGE, "1",
                    new int[] { 0, 1, 0, -1, 1, -1, 0, 1, 1, 1, 0, 0, -1, 1, 1, 0, 1, 0, 1, 0, -1, 0, -1, 0, -1, 1, -1,
                            -1, -1, -1 });
        } catch (Exception e) {
            System.out.println("server error, please contact admins");
        }
        assertTrue(engine.check_output());
    }

}
