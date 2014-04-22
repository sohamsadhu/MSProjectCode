package com.Soham.MSProject.Testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class ExperimentTest 
{
  private static com.Soham.MSProject.SimulationAlgorithm.Experiment experiment;
  
  @BeforeClass
  public static void initialize()
  {
    experiment = new com.Soham.MSProject.SimulationAlgorithm.Experiment();
  }
  
  @Test
  public void testCollisionSHA3()
  {
    assertEquals("com.Soham.MSProject.SimulationAlgorithm.HillClimbing", 
        experiment.getCollision("Hill Climbing").getClass().getName());
    assertEquals("com.Soham.MSProject.SimulationAlgorithm.RandomSelection", 
        experiment.getCollision("Random Search").getClass().getName());
    assertEquals("com.Soham.MSProject.SimulationAlgorithm.SimulatedAnnealing", 
        experiment.getCollision("Simulated Annealing").getClass().getName());
    assertEquals("com.Soham.MSProject.SimulationAlgorithm.TabooSearch", 
        experiment.getCollision("Taboo Search").getClass().getName());
    assertEquals("com.Soham.MSProject.SimulationAlgorithm.RandomSelection", 
        experiment.getCollision("something that is random").getClass().getName());
    assertEquals("com.Soham.MSProject.SHA3.BLAKE", 
        experiment.getSHA3("BLAKE").getClass().getName());
    assertEquals("com.Soham.MSProject.SHA3.Keccak", 
        experiment.getSHA3("Keccak").getClass().getName());
    assertEquals("com.Soham.MSProject.SHA3.Groestl", 
        experiment.getSHA3("Groestl").getClass().getName());
    assertEquals("com.Soham.MSProject.SHA3.Keccak", 
        experiment.getSHA3("something random").getClass().getName());
    assertArrayEquals(new String[]{"54686520717569636b2062726f776e20666f78206a756d7073206f7665"
        + "7220746865206c617a7920646f67", "54686520717569636b2062726f776e20666f78206a756d70732"
        + "06f76657220746865206c617a7920646f66"}, experiment.getMessages("End", 1));
  }
}